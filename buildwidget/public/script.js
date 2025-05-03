// Elements
const buildStatusText = document.getElementById('buildStatusText');
const buildTime = document.getElementById('buildTime');
const progressBar = document.getElementById('progressBar');
const progressText = document.getElementById('progressText');
const outputText = document.getElementById('outputText');
const taskContainer = document.getElementById('taskContainer');
const startBuildBtn = document.getElementById('startBuild');
const stopBuildBtn = document.getElementById('stopBuild');
const createCheckpointBtn = document.getElementById('createCheckpoint');
const createReleaseBtn = document.getElementById('createRelease');
const buildCommandSelect = document.getElementById('buildCommand');
const clearOutputBtn = document.getElementById('clearOutput');
const autoScrollCheckbox = document.getElementById('autoScroll');

// Version information elements
const currentVersionElement = document.getElementById('currentVersion');
const lastReleaseDateElement = document.getElementById('lastReleaseDate');

// Establish WebSocket connection
const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
const host = window.location.host;  // Use the same host, proxy will handle redirecting
const wsUrl = `${protocol}//${host}/ws`;
let socket;
let reconnectInterval;
let buildStartTime;
let buildEndTime;
let timerInterval;

// WebSocket states for proper state checking
const WebSocket = window.WebSocket;

// Connect to WebSocket with improved reconnection logic
function connectWebSocket() {
    console.log('Connecting to WebSocket...');
    
    // Update connection status indicator
    const statusIndicator = document.getElementById('connectionStatus');
    if (statusIndicator) {
        statusIndicator.className = 'connection-status connecting';
        statusIndicator.title = 'Connecting to build server...';
    }
    
    // Show connection status in UI with close button
    const connectionNotice = document.createElement('div');
    connectionNotice.className = 'connection-notice';
    
    // Create notification content
    const noticeContent = document.createElement('span');
    noticeContent.textContent = 'Connecting to build server...';
    connectionNotice.appendChild(noticeContent);
    
    // Create close button
    const closeButton = document.createElement('button');
    closeButton.className = 'notice-close';
    closeButton.innerHTML = '&times;';
    closeButton.addEventListener('click', () => {
        connectionNotice.remove();
    });
    connectionNotice.appendChild(closeButton);
    
    document.body.appendChild(connectionNotice);
    
    // Auto-hide after 5 seconds
    setTimeout(() => {
        connectionNotice.style.opacity = 0;
        setTimeout(() => connectionNotice.remove(), 1000);
    }, 5000);
    
    // Create new socket
    socket = new WebSocket(wsUrl);
    
    // Set connection timeout
    const connectionTimeout = setTimeout(() => {
        if (socket.readyState !== WebSocket.OPEN) {
            socket.close();
            console.error('WebSocket connection timed out');
            
            // Update status indicator
            if (statusIndicator) {
                statusIndicator.className = 'connection-status disconnected';
                statusIndicator.title = 'Connection timed out. Reconnecting...';
            }
            
            // Schedule reconnection
            if (!reconnectInterval) {
                reconnectAttempt = 1;
                attemptReconnection();
            }
        }
    }, 10000);
    
    // Handle open event
    socket.addEventListener('open', () => {
        console.log('WebSocket connection established');
        clearTimeout(connectionTimeout);
        
        // Reset reconnection attempts
        if (reconnectInterval) {
            clearInterval(reconnectInterval);
            reconnectInterval = null;
        }
        
        // Update status indicator
        if (statusIndicator) {
            statusIndicator.className = 'connection-status connected';
            statusIndicator.title = 'Connected to build server';
        }
        
        // Request initial status update and short commits
        socket.send(JSON.stringify({ type: 'requestShortCommits', limit: 10 }));
        socket.send(JSON.stringify({ type: 'requestStatus' }));
    });
    
    // Handle message event
    socket.addEventListener('message', (event) => {
        try {
            // Handle different data formats that might come through
            let message;
            
            if (typeof event.data === 'string') {
                message = JSON.parse(event.data);
            } else if (event.data instanceof Blob) {
                // For blob data, we'd need to read it as text first
                const reader = new FileReader();
                reader.onload = function() {
                    try {
                        const jsonData = JSON.parse(reader.result);
                        handleMessageData(jsonData);
                    } catch (e) {
                        console.error('Error parsing Blob message:', e);
                    }
                };
                reader.readAsText(event.data);
                return; // Early return, as we're handling async
            } else {
                console.error('Unknown message format received:', typeof event.data);
                return;
            }
            
            // Process the parsed message
            handleMessageData(message);
        } catch (error) {
            console.error('Error parsing WebSocket message:', error);
        }
    });
    
    // Helper function to process message data
    function handleMessageData(message) {
        if (!message || !message.type) {
            console.error('Invalid message format:', message);
            return;
        }
        
        console.log('Received message type:', message.type);
        
        try {
            // Ensure we have data
            const data = message.data || {}; 
            
            switch (message.type) {
                case 'status':
                    updateBuildStatus(data);
                    // Also update module status if available
                    if (data && data.modules) {
                        updateModuleStatus(data.modules);
                    }
                    break;
                case 'buildOutput':
                    updateBuildOutput(data);
                    break;
                case 'tasks':
                    updateTaskList(data);
                    break;
                case 'metrics':
                    updateBuildMetrics(data);
                    break;
                case 'notifications':
                    // Handle array of notifications
                    if (Array.isArray(data)) {
                        data.forEach(notification => {
                            showNotification(
                                notification.title || 'Notification', 
                                notification.message || '', 
                                notification.type || 'info'
                            );
                        });
                    } else if (data && typeof data === 'object') {
                        showNotification(
                            data.title || 'Notification', 
                            data.message || '', 
                            data.type || 'info'
                        );
                    }
                    break;
                case 'checkpointStatus':
                    updateCheckpointStatus(data);
                    break;
                case 'moduleStatus':
                    updateModuleStatus(data);
                    break;
                case 'shortCommits':
                    updateShortCommits(data);
                    break;
                case 'versionHistory':
                case 'versionInfo':
                    updateVersionInfo(data);
                    break;
                case 'error':
                    showNotification('Error', data.message || String(data), 'error');
                    break;
                default:
                    console.warn('Unknown message type:', message.type);
            }
        } catch (error) {
            console.error('Error handling message data:', error, message);
            // Try to show an error notification if possible
            try {
                showNotification('Error', 'Failed to process server message', 'error');
            } catch (e) {
                // Last resort: log to console
                console.error('Critical error:', e);
            }
        }
    }
    
    // Handle error event
    socket.addEventListener('error', (error) => {
        console.error('WebSocket error:', error);
        
        // Update status indicator
        if (statusIndicator) {
            statusIndicator.className = 'connection-status error';
            statusIndicator.title = 'Connection error. Reconnecting...';
        }
    });
    
    // Handle close event
    socket.addEventListener('close', (event) => {
        console.log('WebSocket connection closed:', event.code, event.reason);
        
        // Update status indicator
        if (statusIndicator) {
            statusIndicator.className = 'connection-status disconnected';
            statusIndicator.title = 'Disconnected. Reconnecting...';
        }
        
        // Clear the build time interval
        if (timerInterval) {
            clearInterval(timerInterval);
        }
        
        // Attempt reconnection
        if (!reconnectInterval) {
            reconnectAttempt = 1;
            attemptReconnection();
        }
    });
}

// Attempt reconnection with exponential backoff
let reconnectAttempt = 1;
function attemptReconnection() {
    // Calculate delay with exponential backoff (1.5s, 3s, 6s, 12s, 15s max)
    const delay = Math.min(Math.pow(1.5, reconnectAttempt) * 1000, 15000);
    
    console.log(`Reconnecting (attempt ${reconnectAttempt}) in ${delay/1000}s`);
    
    // Schedule reconnection
    setTimeout(() => {
        if (socket.readyState === WebSocket.CLOSED) {
            connectWebSocket();
            reconnectAttempt++;
        }
    }, delay);
}

// Initialize connection
connectWebSocket();

// Update build status
function updateBuildStatus(status) {
    // Update status text
    buildStatusText.textContent = capitalizeFirstLetter(status.status);
    
    // Update status class
    buildStatusText.className = '';
    buildStatusText.classList.add(`status-${status.status.toLowerCase()}`);
    
    // Update progress bar
    if (status.progress !== undefined) {
        // Ensure progress is a value between 0 and 1
        const normalizedProgress = Math.min(Math.max(status.progress, 0), 1);
        const progressPercent = Math.round(normalizedProgress * 100);
        
        // Apply to UI
        progressBar.style.width = `${progressPercent}%`;
        progressText.textContent = `${progressPercent}%`;
        
        // Update progress bar color based on status
        progressBar.className = 'progress-bar';
        if (status.status === 'running') {
            progressBar.classList.add('progress-running');
        } else if (status.status === 'success') {
            progressBar.classList.add('progress-success');
        } else if (status.status === 'failed') {
            progressBar.classList.add('progress-failed');
        }
    } else {
        // If progress is undefined, set a default based on status
        if (status.status === 'success' || status.status === 'failed') {
            progressBar.style.width = '100%';
            progressText.textContent = '100%';
        } else {
            progressBar.style.width = '0%';
            progressText.textContent = '0%';
        }
    }
    
    // Update build time
    if (status.startTime) {
        buildStartTime = new Date(status.startTime);
        
        if (status.status === 'running') {
            // Start timer for running build
            if (!timerInterval) {
                updateBuildTimeDisplay();
                timerInterval = setInterval(updateBuildTimeDisplay, 1000);
            }
        } else {
            // Clear timer for completed build
            if (timerInterval) {
                clearInterval(timerInterval);
                timerInterval = null;
            }
            
            // Set end time for completed build
            if (status.endTime) {
                buildEndTime = new Date(status.endTime);
                const duration = Math.round((buildEndTime - buildStartTime) / 1000);
                const minutes = Math.floor(duration / 60);
                const seconds = duration % 60;
                buildTime.textContent = `Duration: ${padZero(minutes)}:${padZero(seconds)}`;
            }
        }
    }
    
    // Enable/disable buttons based on build status
    if (status.status === 'running') {
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = false;
        createReleaseBtn.disabled = true;
    } else {
        startBuildBtn.disabled = false;
        stopBuildBtn.disabled = true;
        
        // Only enable release button if build was successful
        createReleaseBtn.disabled = !(status.status === 'success');
    }
}

// Update build output
function updateBuildOutput(output) {
    if (!output) return;
    
    console.log('Processing build output:', typeof output, Array.isArray(output));
    
    // Handle array of outputs
    if (Array.isArray(output)) {
        output.forEach(line => processOutputLine(line));
        return;
    }
    
    // Handle object with type and message
    if (typeof output === 'object' && output.type && output.message) {
        processOutputLine(output);
        return;
    }
    
    // Handle string output (legacy format)
    processOutputLine(output);
}

// Process a single line of output
function processOutputLine(line) {
    let outputType = 'info';
    let outputText = '';
    
    // Create span with appropriate class based on output type
    const outputLine = document.createElement('span');
    
    // Handle object format
    if (typeof line === 'object' && line.type && line.message) {
        outputType = line.type;
        outputText = line.message;
    } else {
        outputText = String(line);
        
        // Determine type based on content
        if (outputText.includes('ERROR') || outputText.includes('FAILED')) {
            outputType = 'error';
        } else if (outputText.includes('SUCCESS') || outputText.toLowerCase().includes('build successful')) {
            outputType = 'success';
        } else if (outputText.includes('Task :')) {
            outputType = 'task';
        }
    }
    
    // Set class and text
    outputLine.className = `output-${outputType}`;
    outputLine.textContent = outputText;
    
    // Get the container
    const outputContainer = document.getElementById('outputText');
    if (!outputContainer) {
        console.error('Output container not found!');
        return;
    }
    
    // Append to output container
    outputContainer.appendChild(outputLine);
    outputContainer.appendChild(document.createElement('br'));
    
    // Auto-scroll if enabled
    if (autoScrollCheckbox && autoScrollCheckbox.checked) {
        outputContainer.parentElement.scrollTop = outputContainer.parentElement.scrollHeight;
    }
}

// Update task list
function updateTaskList(tasks) {
    if (!tasks || !taskContainer) return;
    
    // Clear existing tasks
    taskContainer.innerHTML = '';
    
    // Add new tasks
    tasks.slice(0, 10).forEach(task => {
        const taskElement = document.createElement('div');
        taskElement.className = `task-item status-${task.status.toLowerCase()}`;
        
        // Create task content
        const taskTime = new Date(task.timestamp);
        const timeAgo = getRelativeTime(taskTime);
        
        taskElement.innerHTML = `
            <div class="task-name">${task.name}</div>
            <div class="task-info">
                <span class="task-status">${capitalizeFirstLetter(task.status)}</span>
                <span class="task-time">${timeAgo}</span>
            </div>
        `;
        
        // Add to container
        taskContainer.appendChild(taskElement);
    });
    
    // If no tasks, show message
    if (tasks.length === 0) {
        const emptyMessage = document.createElement('div');
        emptyMessage.className = 'empty-message';
        emptyMessage.textContent = 'No recent tasks';
        taskContainer.appendChild(emptyMessage);
    }
}

// Update build time display
function updateBuildTimeDisplay() {
    if (!buildStartTime) return;
    
    const now = new Date();
    const duration = now - buildStartTime;
    const seconds = Math.floor((duration / 1000) % 60);
    const minutes = Math.floor((duration / (1000 * 60)) % 60);
    const hours = Math.floor(duration / (1000 * 60 * 60));
    
    if (hours > 0) {
        buildTime.textContent = `Duration: ${hours}:${padZero(minutes)}:${padZero(seconds)}`;
    } else {
        buildTime.textContent = `Duration: ${padZero(minutes)}:${padZero(seconds)}`;
    }
}

// Pad numbers with leading zero
function padZero(num) {
    return num.toString().padStart(2, '0');
}

// Capitalize first letter of a string
function capitalizeFirstLetter(string) {
    if (!string) return '';
    return string.charAt(0).toUpperCase() + string.slice(1);
}

// Update build metrics display
function updateBuildMetrics(metrics) {
    console.log('Updating build metrics:', metrics);
    
    // Update existing metrics cards if they exist
    if (metrics && metrics.builds) {
        // Update Build Count
        const buildCountElement = document.getElementById('buildCount');
        if (buildCountElement) {
            buildCountElement.textContent = metrics.builds.total || 0;
        }
        
        // Update Success Rate
        const successRateElement = document.getElementById('successRate');
        if (successRateElement) {
            const rate = metrics.builds.successRate || 0;
            successRateElement.textContent = `${Math.round(rate)}%`;
        }
        
        // Update Average Build Time
        const avgBuildTimeElement = document.getElementById('averageBuildTime');
        if (avgBuildTimeElement && metrics.performance) {
            const avgTime = metrics.performance.avgBuildTime || 0;
            avgBuildTimeElement.textContent = formatDuration(avgTime);
        }
        
        // Update Last Successful Build
        const lastSuccessfulElement = document.getElementById('lastSuccessful');
        if (lastSuccessfulElement) {
            if (metrics.lastSuccessful) {
                lastSuccessfulElement.textContent = getRelativeTime(new Date(metrics.lastSuccessful));
            } else {
                lastSuccessfulElement.textContent = "None";
            }
        }
    } else {
        console.warn('Metrics data is incomplete or missing', metrics);
    }
}

// Show notification
function showNotification(title, message, type = 'info', duration = 5000) {
    // Get or create notification container
    let container = document.getElementById('notificationsContainer');
    if (!container) {
        container = document.createElement('div');
        container.id = 'notificationsContainer';
        container.className = 'notifications-container';
        document.body.appendChild(container);
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification notification-${type}`;
    
    // Add unique ID
    const notificationId = 'notification-' + Date.now();
    notification.id = notificationId;
    
    // Create notification content
    notification.innerHTML = `
        <div class="notification-title">${title}</div>
        <div class="notification-message">${message}</div>
        <button class="notification-close">&times;</button>
    `;
    
    // Add to container
    container.appendChild(notification);
    
    // Add close handler
    const closeButton = notification.querySelector('.notification-close');
    closeButton.addEventListener('click', () => {
        notification.classList.add('notification-closing');
        setTimeout(() => notification.remove(), 300);
    });
    
    // Auto-remove after duration
    if (duration > 0) {
        setTimeout(() => {
            if (document.getElementById(notificationId)) {
                notification.classList.add('notification-closing');
                setTimeout(() => notification.remove(), 300);
            }
        }, duration);
    }
    
    // Return for later reference
    return notification;
}

// Update module status display
function updateModuleStatus(modules) {
    if (!modules) return;
    
    console.log("Updating module status:", modules);
    
    Object.entries(modules).forEach(([moduleName, status]) => {
        console.log(`Updating module ${moduleName} with status:`, status);
        
        const moduleBar = document.getElementById(`module-${moduleName}`);
        if (moduleBar) {
            // Update progress bar
            const progressBar = moduleBar.querySelector('.module-progress-bar');
            if (progressBar) {
                // Update status class
                progressBar.className = 'module-progress-bar';
                const statusValue = typeof status === 'string' ? status : (status.status || 'pending');
                progressBar.classList.add(statusValue);
                
                // Update progress width
                let progressWidth = 0;
                
                if (statusValue === 'success') {
                    progressWidth = 100;
                } else if (statusValue === 'failed') {
                    progressWidth = 100;
                } else if (statusValue === 'building') {
                    // If we have progress info, use it, otherwise use 50%
                    const progress = typeof status === 'object' && status.progress ? status.progress : 0.5;
                    progressWidth = progress * 100;
                }
                
                progressBar.style.width = `${progressWidth}%`;
            }
            
            // Update indicator
            const indicator = moduleBar.querySelector('.module-indicator');
            if (indicator) {
                indicator.className = 'module-indicator';
                const statusValue = typeof status === 'string' ? status : (status.status || 'pending');
                indicator.classList.add(statusValue);
            }
            
            // Update task name
            const taskElement = moduleBar.querySelector('.module-task');
            if (taskElement) {
                const statusValue = typeof status === 'string' ? status : (status.status || 'pending');
                const taskValue = typeof status === 'object' ? (status.currentTask || status.lastTask || '') : '';
                taskElement.textContent = taskValue;
                
                // For success status, add a check mark symbol and smiley
                if (statusValue === 'success' && !taskValue) {
                    taskElement.textContent = 'Build complete';
                    moduleBar.classList.add('success-complete');
                } else if (statusValue === 'failed' && !taskValue) {
                    taskElement.textContent = 'Build failed';
                    moduleBar.classList.add('failed-complete');
                } else {
                    // Remove classes if the status changes
                    moduleBar.classList.remove('success-complete');
                    moduleBar.classList.remove('failed-complete');
                }
            }
        }
    });
}

// Helper function to get relative time
function getRelativeTime(date) {
    const now = new Date();
    const diffMs = now - date;
    const diffSec = Math.floor(diffMs / 1000);
    const diffMin = Math.floor(diffSec / 60);
    const diffHour = Math.floor(diffMin / 60);
    const diffDay = Math.floor(diffHour / 24);
    
    if (diffDay > 0) {
        return `${diffDay}d ago`;
    } else if (diffHour > 0) {
        return `${diffHour}h ago`;
    } else if (diffMin > 0) {
        return `${diffMin}m ago`;
    } else {
        return 'Just now';
    }
}

// Helper function to format duration
function formatDuration(ms) {
    const seconds = Math.floor((ms / 1000) % 60);
    const minutes = Math.floor((ms / (1000 * 60)) % 60);
    const hours = Math.floor(ms / (1000 * 60 * 60));
    
    if (hours > 0) {
        return `${hours}h ${padZero(minutes)}m ${padZero(seconds)}s`;
    } else if (minutes > 0) {
        return `${minutes}m ${padZero(seconds)}s`;
    } else {
        return `${seconds}s`;
    }
}

// Event listeners
startBuildBtn.addEventListener('click', () => {
    console.log('Start build button clicked');
    
    if (!socket || socket.readyState !== WebSocket.OPEN) {
        console.error('WebSocket not connected! Attempting to reconnect...');
        connectWebSocket();
        // Show error to user
        showNotification('Connection Error', 'WebSocket not connected. Attempting to reconnect...', 'error');
        return;
    }
    
    const gradleCommand = buildCommandSelect.value;
    console.log('Sending startBuild command with:', gradleCommand);
    
    try {
        socket.send(JSON.stringify({
            type: 'startBuild',
            gradleCommand: gradleCommand
        }));
        
        // Reset build time tracking
        buildStartTime = new Date();
        buildEndTime = null;
        clearInterval(timerInterval);
        timerInterval = setInterval(updateBuildTimeDisplay, 1000);
        
        // Clear output
        outputText.innerHTML = '';
        
        console.log('Build command sent successfully');
    } catch (error) {
        console.error('Error sending build command:', error);
        showNotification('Error', 'Failed to send build command: ' + error.message, 'error');
    }
});

stopBuildBtn.addEventListener('click', () => {
    console.log('Stop build button clicked');
    
    if (!socket || socket.readyState !== WebSocket.OPEN) {
        console.error('WebSocket not connected! Attempting to reconnect...');
        connectWebSocket();
        // Show error to user
        showNotification('Connection Error', 'WebSocket not connected. Attempting to reconnect...', 'error');
        return;
    }
    
    try {
        socket.send(JSON.stringify({ type: 'stopBuild' }));
        console.log('Stop build command sent successfully');
        
        // Provide immediate feedback
        showNotification('Build Stopping', 'Requested to stop the current build process', 'info');
    } catch (error) {
        console.error('Error sending stop command:', error);
        showNotification('Error', 'Failed to send stop command: ' + error.message, 'error');
    }
});

clearOutputBtn.addEventListener('click', () => {
    outputText.innerHTML = '';
});

// Handle output filtering
const filterSelect = document.getElementById('filterOutput');
if (filterSelect) {
    filterSelect.addEventListener('change', () => {
        // Apply filter
        const filterType = filterSelect.value;
        const buildOutputLines = outputText.querySelectorAll('span');
        
        if (filterType === 'all') {
            // Show all lines
            buildOutputLines.forEach(line => {
                line.style.display = '';
                // Also show BR elements
                const br = line.nextSibling;
                if (br && br.nodeName === 'BR') {
                    br.style.display = '';
                }
            });
        } else {
            // Filter by type
            buildOutputLines.forEach(line => {
                const lineType = line.className.replace('output-', '');
                line.style.display = (lineType === filterType) ? '' : 'none';
                
                // Also hide BR elements after filtered lines
                const br = line.nextSibling;
                if (br && br.nodeName === 'BR') {
                    br.style.display = (lineType === filterType) ? '' : 'none';
                }
            });
        }
        
        // Add notification about applied filter
        socket.send(JSON.stringify({
            type: 'customNotification',
            notification: {
                type: 'info',
                title: 'Output Filter Applied',
                message: `Showing ${filterType === 'all' ? 'all output' : `only ${filterType} messages`}`,
                timeout: 3000
            }
        }));
    });
};

// Handle checkpoint creation
createCheckpointBtn.addEventListener('click', () => {
    // Show a modal dialog for checkpoint information
    const checkpointName = prompt('Enter checkpoint name (required):', `Checkpoint ${new Date().toLocaleDateString()}`);
    
    if (!checkpointName) {
        // User canceled or didn't provide a name
        return;
    }
    
    const description = prompt('Enter checkpoint description (optional):', 'Automated checkpoint with build and release');
    
    // Disable the button while processing
    createCheckpointBtn.disabled = true;
    createCheckpointBtn.textContent = 'Processing Checkpoint...';
    
    // Add checkpoint message to output
    const checkpointMessage = document.createElement('span');
    checkpointMessage.className = 'output-info';
    checkpointMessage.textContent = `[Checkpoint] Creating checkpoint "${checkpointName}"...`;
    outputText.appendChild(checkpointMessage);
    outputText.appendChild(document.createElement('br'));
    
    // Auto-scroll to bottom
    if (autoScrollCheckbox.checked) {
        outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
    }
    
    // Call the API to create a checkpoint
    fetch('/api/checkpoint', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            name: checkpointName,
            description: description || ''
        })
    })
    .then(response => response.json())
    .then(data => {
        // Re-enable the button
        createCheckpointBtn.disabled = false;
        createCheckpointBtn.textContent = 'Create Checkpoint';
        
        if (data.success) {
            // Add success message
            const successMessage = document.createElement('span');
            successMessage.className = 'output-success';
            successMessage.textContent = `[Checkpoint] Checkpoint "${checkpointName}" created and build process started.`;
            outputText.appendChild(successMessage);
            outputText.appendChild(document.createElement('br'));
            
            // Show notification
            showNotification('Checkpoint Created', `Checkpoint "${checkpointName}" created and build process started.`, 'success');
        } else {
            // Add error message
            const errorMessage = document.createElement('span');
            errorMessage.className = 'output-error';
            errorMessage.textContent = `[Checkpoint Error] ${data.message}`;
            outputText.appendChild(errorMessage);
            outputText.appendChild(document.createElement('br'));
            
            // Show notification
            showNotification('Checkpoint Failed', data.message, 'error');
        }
        
        // Auto-scroll to bottom
        if (autoScrollCheckbox.checked) {
            outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
        }
    })
    .catch(error => {
        console.error('Error creating checkpoint:', error);
        
        // Re-enable the button
        createCheckpointBtn.disabled = false;
        createCheckpointBtn.textContent = 'Create Checkpoint';
        
        // Add error message
        const errorMessage = document.createElement('span');
        errorMessage.className = 'output-error';
        errorMessage.textContent = `[Checkpoint Error] ${error.message || 'Unknown error occurred'}`;
        outputText.appendChild(errorMessage);
        outputText.appendChild(document.createElement('br'));
        
        // Show notification
        showNotification('Checkpoint Failed', 'Error creating checkpoint', 'error');
        
        // Auto-scroll to bottom
        if (autoScrollCheckbox.checked) {
            outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
        }
    });
});

// Create GitHub release
createReleaseBtn.addEventListener('click', () => {
    console.log('Create GitHub release button clicked');
    
    if (!socket || socket.readyState !== WebSocket.OPEN) {
        console.error('WebSocket not connected! Attempting to reconnect...');
        connectWebSocket();
        // Show error to user
        showNotification('Connection Error', 'WebSocket not connected. Attempting to reconnect...', 'error');
        return;
    }
    
    if (confirm('Create a new GitHub release with all versioned module JARs?')) {
        try {
            // Show release in progress
            createReleaseBtn.disabled = true;
            createReleaseBtn.textContent = 'Creating Release...';
            
            // Add release message to output
            const releaseMessage = document.createElement('span');
            releaseMessage.className = 'output-info';
            releaseMessage.textContent = '[GitHub Release] Starting versioned release process...';
            outputText.appendChild(releaseMessage);
            outputText.appendChild(document.createElement('br'));
            
            // Auto-scroll to bottom
            if (autoScrollCheckbox && autoScrollCheckbox.checked) {
                outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
            }
            
            // Send release request
            socket.send(JSON.stringify({ type: 'createRelease' }));
            console.log('GitHub release command sent successfully');
        } catch (error) {
            console.error('Error sending GitHub release command:', error);
            
            // Re-enable the button
            createReleaseBtn.disabled = false;
            createReleaseBtn.textContent = 'Create GitHub Release';
            
            // Show error
            showNotification('Error', 'Failed to send GitHub release command: ' + error.message, 'error');
        }
    }
});

// Update version info in UI
function updateVersionInfo(versionInfo) {
    if (!versionInfo) return;
    
    // Update version number
    if (currentVersionElement && versionInfo.version) {
        currentVersionElement.textContent = versionInfo.version;
    }
    
    // Update last release date
    if (lastReleaseDateElement && versionInfo.lastReleaseDate) {
        const releaseDate = new Date(versionInfo.lastReleaseDate);
        lastReleaseDateElement.textContent = `Last Release: ${releaseDate.toLocaleDateString()}`;
    }
}

// Update checkpoint status
function updateCheckpointStatus(status) {
    if (!status) return;
    
    // Add checkpoint status message to output
    const statusMessage = document.createElement('span');
    statusMessage.className = `output-${status.success ? 'success' : 'error'}`;
    statusMessage.textContent = `[Checkpoint] ${status.message}`;
    outputText.appendChild(statusMessage);
    outputText.appendChild(document.createElement('br'));
    
    // Auto-scroll to bottom
    if (autoScrollCheckbox.checked) {
        outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
    }
    
    // Show notification
    if (status.success) {
        showNotification('Checkpoint Status', status.message, 'success');
    } else {
        showNotification('Checkpoint Status', status.message, 'error');
    }
    
    // Re-enable checkpoint button if needed
    if (createCheckpointBtn.disabled) {
        createCheckpointBtn.disabled = false;
        createCheckpointBtn.textContent = 'Create Checkpoint';
    }
}

// Update short commits
function updateShortCommits(commits) {
    console.log('Updating short commits:', commits);
    
    const commitList = document.getElementById('commitList');
    if (!commitList) return;
    
    // Clear existing content
    commitList.innerHTML = '';
    
    // Check if we have commits
    if (!commits || commits.length === 0) {
        const emptyMessage = document.createElement('div');
        emptyMessage.className = 'loading-commits';
        emptyMessage.textContent = 'No recent commits found';
        commitList.appendChild(emptyMessage);
        return;
    }
    
    // Add each commit to the list
    commits.forEach(commit => {
        const commitItem = document.createElement('div');
        commitItem.className = 'commit-item';
        
        const message = document.createElement('div');
        message.className = 'commit-message';
        message.textContent = commit.message || 'No message';
        
        const details = document.createElement('div');
        details.className = 'commit-details';
        
        // Only add author if available
        if (commit.author) {
            const author = document.createElement('span');
            author.className = 'commit-author';
            author.textContent = commit.author;
            details.appendChild(author);
        }
        
        // Add date if available
        if (commit.date) {
            const date = document.createElement('span');
            date.className = 'commit-date';
            date.textContent = getRelativeTime(new Date(commit.date));
            details.appendChild(date);
        }
        
        commitItem.appendChild(message);
        commitItem.appendChild(details);
        commitList.appendChild(commitItem);
    });
    
    // Add event listener to refresh button
    const refreshBtn = document.getElementById('refreshCommits');
    if (refreshBtn) {
        refreshBtn.addEventListener('click', () => {
            if (socket && socket.readyState === WebSocket.OPEN) {
                socket.send(JSON.stringify({
                    type: 'requestShortCommits',
                    limit: 10
                }));
                
                // Show loading indicator
                commitList.innerHTML = '<div class="loading-commits">Loading commit history...</div>';
            }
        });
    }
}