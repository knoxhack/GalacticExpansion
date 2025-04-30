package com.astroframe.galactic.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.Task
import org.gradle.api.invocation.Gradle
import org.gradle.api.ProjectEvaluationListener
import org.gradle.api.ProjectState

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.RequestBody
import okhttp3.MediaType
import okhttp3.HttpUrl
import org.gradle.BuildResult

import groovy.json.JsonOutput

/**
 * Gradle plugin that reports build status in real-time to a WebSocket server.
 * This enables real-time build monitoring through a web interface.
 */
class BuildStatusPlugin implements Plugin<Project> {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8")
    private OkHttpClient client = new OkHttpClient()
    private List<String> buildEvents = []
    private Map<String, TaskExecutionData> taskExecutions = [:]
    private Map<String, Object> buildStatus = [
        status: 'running',
        modules: [:],
        tasks: [:],
        buildOutput: [],
        progress: 0,
        startTime: new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ]
    
    @Override
    void apply(Project project) {
        // Only apply to root project
        if (project != project.rootProject) {
            return
        }

        // Create extension to configure the plugin
        project.extensions.create('buildStatusWidget', BuildStatusWidgetExtension)
        
        // Wait for the project to be evaluated
        project.afterEvaluate {
            // Initialize modules based on subprojects
            project.subprojects.each { subproject ->
                buildStatus.modules[subproject.name] = [status: 'pending']
            }
            
            // Create task execution listener as a service
            def buildStatusListener = new BuildStatusTaskListener(project, buildStatus, taskExecutions, buildEvents)
            
            // Add a task to send build status updates
            project.tasks.register('sendBuildStatusUpdate') {
                doLast {
                    // Send final update
                    sendBuildStatusUpdate(project)
                }
            }
            
            // Configure all tasks to track execution
            project.allprojects { p ->
                p.tasks.configureEach { task ->
                    task.doFirst {
                        buildStatusListener.beforeTask(task)
                    }
                    task.doLast {
                        buildStatusListener.afterTask(task, task.state)
                    }
                    // Make tasks finalize with the update task
                    task.finalizedBy(project.tasks.named('sendBuildStatusUpdate'))
                }
            }
            
            // Add a gradle property to track build status
            project.gradle.ext.buildStatus = buildStatus
        }
    }
    
    private void sendBuildStatusUpdate(Project project) {
        try {
            def extension = project.extensions.findByType(BuildStatusWidgetExtension)
            if (!extension) {
                return // Extension not configured
            }
            
            String serverUrl = extension.serverUrl ?: 'http://localhost:5000'
            String apiEndpoint = "${serverUrl}/api/status"
            
            // Calculate progress based on completed tasks
            int totalTasks = taskExecutions.size()
            int completedTasks = taskExecutions.values().count { it.finished }
            if (totalTasks > 0) {
                buildStatus.progress = (int)((completedTasks / totalTasks) * 100)
            }
            
            // Update timestamp
            buildStatus.lastUpdate = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            
            // Send update via HTTP POST
            String json = JsonOutput.toJson(buildStatus)
            RequestBody body = RequestBody.create(json, JSON)
            
            Request request = new Request.Builder()
                .url(apiEndpoint)
                .post(body)
                .build()
                
            client.newCall(request).execute().close()
        } catch (Exception e) {
            // Silently fail, don't disrupt the build
            project.logger.warn("Failed to send build status update: ${e.message}")
        }
    }
    
    private static class TaskExecutionData {
        String taskPath
        boolean finished = false
        boolean success = false
        boolean skipped = false
        boolean upToDate = false
        boolean noSource = false
        long startTime = System.currentTimeMillis()
        long endTime = 0
        
        TaskExecutionData(String taskPath) {
            this.taskPath = taskPath
        }
    }
}

class BuildStatusWidgetExtension {
    String serverUrl = 'http://localhost:5000'
}

/**
 * Helper class to track task execution for build status
 */
class BuildStatusTaskListener {
    private final Project project
    private final Map<String, Object> buildStatus
    private final Map<String, TaskExecutionData> taskExecutions
    private final List<String> buildEvents
    
    BuildStatusTaskListener(Project project, Map<String, Object> buildStatus, 
                          Map<String, TaskExecutionData> taskExecutions, 
                          List<String> buildEvents) {
        this.project = project
        this.buildStatus = buildStatus
        this.taskExecutions = taskExecutions
        this.buildEvents = buildEvents
    }
    
    void beforeTask(Task task) {
        // Record task start time
        String taskPath = task.path
        TaskExecutionData executionData = new TaskExecutionData(taskPath)
        taskExecutions[taskPath] = executionData
        
        // Extract module from task path (format :moduleName:taskName)
        String[] pathParts = taskPath.split(':')
        if (pathParts.length >= 2) {
            String moduleName = pathParts[1]
            
            // Update module status
            if (buildStatus.modules.containsKey(moduleName)) {
                buildStatus.modules[moduleName].status = 'building'
                buildStatus.modules[moduleName].currentTask = pathParts.length >= 3 ? pathParts[2] : ''
            }
        }
        
        // Add to build events
        String event = "Task ${taskPath} started"
        buildEvents.add(event)
        buildStatus.buildOutput.add([type: 'info', message: event])
        
        // Update task status
        buildStatus.tasks[taskPath] = [
            status: 'running',
            module: pathParts.length >= 2 ? pathParts[1] : '',
            task: pathParts.length >= 3 ? pathParts[2] : '',
            startTime: new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        ]
    }
    
    void afterTask(Task task, TaskState state) {
        String taskPath = task.path
        TaskExecutionData executionData = taskExecutions[taskPath]
        
        if (executionData) {
            executionData.finished = true
            executionData.success = !state.failure
            executionData.skipped = state.skipped
            executionData.upToDate = state.upToDate
            executionData.noSource = state.noSource
            executionData.endTime = System.currentTimeMillis()
            
            // Add to build events
            String status = state.failure ? "failed" : 
                          (state.skipped ? "skipped" : 
                          (state.upToDate ? "up-to-date" : 
                          (state.noSource ? "no-source" : "succeeded")))
            String event = "Task ${taskPath} ${status}"
            buildEvents.add(event)
            buildStatus.buildOutput.add([type: state.failure ? 'error' : 'info', message: event])
            
            // Extract module from task path
            String[] pathParts = taskPath.split(':')
            if (pathParts.length >= 2) {
                String moduleName = pathParts[1]
                
                // Update module status for key tasks
                if (pathParts.length >= 3 && 
                    (pathParts[2] == 'build' || pathParts[2] == 'assemble')) {
                    if (buildStatus.modules.containsKey(moduleName)) {
                        buildStatus.modules[moduleName].status = state.failure ? 'failed' : 'success'
                    }
                }
            }
            
            // Update task status
            if (buildStatus.tasks.containsKey(taskPath)) {
                buildStatus.tasks[taskPath].status = state.failure ? 'failed' : 
                                                   (state.skipped ? 'skipped' : 
                                                   (state.upToDate ? 'up-to-date' : 
                                                   (state.noSource ? 'no-source' : 'success')))
                buildStatus.tasks[taskPath].endTime = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            }
        }
    }
}