package com.astroframe.galactic.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.execution.TaskExecutionListener
import org.gradle.api.tasks.TaskState
import org.gradle.api.Task
import org.gradle.BuildResult

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.MediaType
import groovy.json.JsonOutput

/**
 * Simple class to store task execution data
 */
class TaskData {
    String taskPath
    String module
    String taskName
    boolean finished = false
    boolean success = false
    String status = 'running'
    String startTime
    String endTime
    
    TaskData(String taskPath) {
        this.taskPath = taskPath
        this.startTime = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        
        String[] pathParts = taskPath.split(':')
        if (pathParts.length >= 2) {
            this.module = pathParts[1]
            if (pathParts.length >= 3) {
                this.taskName = pathParts[2]
            }
        }
    }
    
    void finish(TaskState state) {
        this.finished = true
        this.success = !state.failure
        
        if (state.failure) {
            this.status = 'failed'
        } else if (state.skipped) {
            this.status = 'skipped'
        } else if (state.upToDate) {
            this.status = 'up-to-date'
        } else if (state.noSource) {
            this.status = 'no-source'
        } else {
            this.status = 'success'
        }
        
        this.endTime = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    }
}

/**
 * Simple plugin to report build status to a web server
 */
class BuildStatusPlugin implements Plugin<Project> {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8")
    private final OkHttpClient client = new OkHttpClient()
    private final Map<String, TaskData> tasks = [:]
    private final Map<String, Map> modules = [:]
    private final List<Map> buildOutput = []
    private String buildStatus = 'running'
    private String buildStartTime
    private String buildEndTime
    
    @Override
    void apply(Project project) {
        // Only apply to root project
        if (project != project.rootProject) {
            return
        }
        
        // Check if we're running in CI - disable if so
        String ciEnv = System.getenv("CI")
        boolean isCI = ciEnv != null && (ciEnv.equalsIgnoreCase("true") || ciEnv.equals("1"))
        
        if (isCI) {
            project.logger.lifecycle("BuildStatusPlugin disabled in CI environment")
            return
        }
        
        // Create extension
        project.extensions.create('buildStatusWidget', BuildStatusWidgetExtension)
        
        // Set build start time
        buildStartTime = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        
        // Initialize on project evaluation
        project.afterEvaluate {
            // Initialize module status
            project.subprojects.each { subproject ->
                modules[subproject.name] = [status: 'pending', currentTask: '']
            }
            
            // Send initial status
            sendStatusUpdate(project)
            
            // Add task listener
            project.gradle.addListener(new TaskExecutionListener() {
                @Override
                void beforeExecute(Task task) {
                    handleTaskStart(project, task)
                }
                
                @Override
                void afterExecute(Task task, TaskState state) {
                    handleTaskComplete(project, task, state)
                }
            })
            
            // Add build finished listener
            project.gradle.addBuildListener(new org.gradle.BuildAdapter() {
                @Override
                void buildFinished(BuildResult result) {
                    buildStatus = result.failure ? 'failed' : 'success'
                    buildEndTime = new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                    sendStatusUpdate(project)
                }
            })
        }
    }
    
    private void handleTaskStart(Project project, Task task) {
        // Skip extension task to avoid circular dependency
        if (task.name == 'buildStatusWidgetExtension') return
        
        String taskPath = task.path
        TaskData taskData = new TaskData(taskPath)
        tasks[taskPath] = taskData
        
        // Update module status
        if (taskData.module && modules.containsKey(taskData.module)) {
            modules[taskData.module].status = 'building'
            modules[taskData.module].currentTask = taskData.taskName ?: ''
        }
        
        // Log event
        String event = "Task ${taskPath} started"
        buildOutput.add([type: 'info', message: event])
        
        // Send status update
        sendStatusUpdate(project)
    }
    
    private void handleTaskComplete(Project project, Task task, TaskState state) {
        // Skip extension task
        if (task.name == 'buildStatusWidgetExtension') return
        
        String taskPath = task.path
        TaskData taskData = tasks[taskPath]
        
        if (taskData) {
            // Update task data
            taskData.finish(state)
            
            // Update module status for important tasks
            if (taskData.module && taskData.taskName && 
                (taskData.taskName == 'build' || taskData.taskName == 'assemble')) {
                if (modules.containsKey(taskData.module)) {
                    modules[taskData.module].status = state.failure ? 'failed' : 'success'
                }
            }
            
            // Log event
            String event = "Task ${taskPath} ${taskData.status}"
            buildOutput.add([type: state.failure ? 'error' : 'info', message: event])
            
            // Send status update
            sendStatusUpdate(project)
        }
    }
    
    private void sendStatusUpdate(Project project) {
        try {
            def extension = project.extensions.findByType(BuildStatusWidgetExtension)
            if (!extension) {
                return
            }
            
            String serverUrl = extension.serverUrl ?: 'http://localhost:5000'
            String apiEndpoint = "${serverUrl}/api/status"
            
            // Calculate progress
            int totalTasks = tasks.size()
            int completedTasks = tasks.values().count { it.finished }
            int progress = totalTasks > 0 ? (int)((completedTasks / totalTasks) * 100) : 0
            
            // Build status object
            def status = [
                status: buildStatus,
                progress: progress,
                modules: modules,
                tasks: tasks.collectEntries { key, value ->
                    [(key): [
                        status: value.status,
                        module: value.module ?: '',
                        task: value.taskName ?: '',
                        startTime: value.startTime,
                        endTime: value.endTime
                    ]]
                },
                buildOutput: buildOutput,
                startTime: buildStartTime,
                endTime: buildEndTime,
                lastUpdate: new Date().format("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            ]
            
            // Send update
            String json = JsonOutput.toJson(status)
            RequestBody body = RequestBody.create(json, JSON)
            
            Request request = new Request.Builder()
                .url(apiEndpoint)
                .post(body)
                .build()
                
            client.newCall(request).execute().close()
        } catch (Exception e) {
            // Don't fail the build
            project.logger.warn("Failed to send build status update: ${e.message}")
        }
    }
}

/**
 * Extension for build status widget configuration
 */
class BuildStatusWidgetExtension {
    String serverUrl = 'http://localhost:5000'
}