package com.astroframe.galactic.buildstatus

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.TaskExecutionException
import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import java.net.HttpURLConnection
import java.net.URL

/**
 * Plugin for updating the build status widget
 */
class BuildStatusPlugin implements Plugin<Project> {
    void apply(Project project) {
        // Create the extension for configuration
        def extension = project.extensions.create('buildStatusWidget', BuildStatusWidgetExtension)
        
        // Register a listener for task execution
        project.gradle.taskGraph.afterTask { task ->
            // Send status update for each executed task
            if (task.state.executed) {
                def moduleStatus = getModuleFromTask(task.path)
                if (moduleStatus) {
                    updateBuildStatus(extension.serverUrl, task.path, moduleStatus, task.state.failure != null)
                }
            }
        }
        
        // Register build status listener
        project.gradle.buildFinished { result ->
            def status = result.failure ? "failed" : "success"
            println "Build finished with status: ${status}"
            
            try {
                // Send final build status
                sendBuildComplete(extension.serverUrl, status)
            } catch (Exception e) {
                println "Failed to send build complete status: ${e.message}"
            }
        }
    }
    
    /**
     * Send a module status update to the build status server
     */
    void updateBuildStatus(String serverUrl, String taskPath, String module, boolean failed) {
        try {
            // Don't send updates for certain tasks
            if (taskPath.contains("buildSrc") || taskPath.contains(":help")) {
                return
            }
            
            def status = failed ? "failed" : "success"
            def url = "${serverUrl}/api/module-update"
            
            // Print debug
            println "Sending module status ${module}:${status} to ${url}"
            
            def conn = new URL(url).openConnection() as HttpURLConnection
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.setDoOutput(true)
            
            def payload = [
                module: module,
                status: status,
                task: taskPath
            ]
            
            def json = new JsonBuilder(payload).toString()
            conn.outputStream.withWriter { writer ->
                writer << json
            }
            
            def responseCode = conn.responseCode
            if (responseCode != 200) {
                println "Failed to update build status: HTTP ${responseCode}"
            }
            
            conn.disconnect()
        } catch (Exception e) {
            println "Failed to send build status update: ${e.message}"
        }
    }
    
    /**
     * Send build complete status to the build status server
     */
    void sendBuildComplete(String serverUrl, String status) {
        try {
            def url = "${serverUrl}/api/build-complete"
            
            def conn = new URL(url).openConnection() as HttpURLConnection
            conn.setRequestMethod("POST")
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            conn.setDoOutput(true)
            
            def payload = [
                status: status
            ]
            
            def json = new JsonBuilder(payload).toString()
            conn.outputStream.withWriter { writer ->
                writer << json
            }
            
            def responseCode = conn.responseCode
            if (responseCode != 200) {
                println "Failed to send build complete status: HTTP ${responseCode}"
            }
            
            conn.disconnect()
        } catch (Exception e) {
            println "Failed to send build complete status: ${e.message}"
        }
    }
    
    /**
     * Extract module name from a task path
     */
    String getModuleFromTask(String taskPath) {
        // Parse the task path to get the module
        if (taskPath.startsWith(":")) {
            // Root project task - not associated with a specific module
            if (taskPath.count(":") > 1) {
                // Format: ":module:task"
                def parts = taskPath.split(":")
                if (parts.length >= 2) {
                    return parts[1]
                }
            }
        }
        return null
    }
}

/**
 * Extension for configuring the build status widget
 */
class BuildStatusWidgetExtension {
    String serverUrl = 'http://localhost:5000'
}