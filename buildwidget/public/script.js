// Elements
const buildStatusText = document.getElementById('buildStatusText');
const buildTime = document.getElementById('buildTime');
const progressBar = document.getElementById('progressBar');
const progressText = document.getElementById('progressText');
const outputText = document.getElementById('outputText');
const taskContainer = document.getElementById('taskContainer');
const startBuildBtn = document.getElementById('startBuild');
const stopBuildBtn = document.getElementById('stopBuild');
const createReleaseBtn = document.getElementById('createRelease');
const buildCommandSelect = document.getElementById('buildCommand');
const clearOutputBtn = document.getElementById('clearOutput');
const autoScrollCheckbox = document.getElementById('autoScroll');

// Establish WebSocket connection
const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
const host = window.location.host.replace('5000', '5001');  // Use port 5001 instead
const wsUrl = `${protocol}//${host}/ws`;
let socket;
let reconnectInterval;
let buildStartTime;
let buildEndTime;
let timerInterval;

// Connect to WebSocket with improved reconnection logic
function connectWebSocket() {
    console.log('Connecting to WebSocket...');
    
    // Update connection status indicator
    const statusIndicator = document.getElementById('connectionStatus');
    if (statusIndicator) {
        statusIndicator.className = 'connection-status connecting';
        statusIndicator.title = 'Connecting to build server...';
    }
    
    // Show connection status in UI
    const connectionNotice = document.createElement('div');
    connectionNotice.className = 'connection-notice';
    connectionNotice.textContent = 'Connecting to build server...';
    document.body.appendChild(connectionNotice);
    
    // Create new socket
    socket = new WebSocket(wsUrl);
    
    // Set connection timeout
    const connectionTimeout = setTimeout(() => {
        if (socket.readyState !== WebSocket.OPEN) {
            socket.close();
            connectionNotice.textContent = 'Connection timed out. Retrying...';
            connectionNotice.className = 'connection-notice error';
        }
    }, 5000);
    
    socket.onopen = () => {
        console.log('WebSocket connection established');
        // Clear timeout and reconnect interval
        clearTimeout(connectionTimeout);
        if (reconnectInterval) {
            clearInterval(reconnectInterval);
            reconnectInterval = null;
        }
        
        // Update UI
        connectionNotice.textContent = 'Connected to build server';
        connectionNotice.className = 'connection-notice success';
        setTimeout(() => {
            connectionNotice.style.opacity = 0;
            setTimeout(() => connectionNotice.remove(), 1000);
        }, 2000);
        
        // Add or update connection status indicator
        const statusIndicator = document.getElementById('connectionStatus') || document.createElement('div');
        statusIndicator.id = 'connectionStatus';
        statusIndicator.className = 'connection-status connected';
        statusIndicator.title = 'Connected to build server';
        
        if (!document.getElementById('connectionStatus')) {
            // Make sure we have a navbar element
            let navbar = document.querySelector('.navbar');
            if (!navbar) {
                navbar = document.createElement('div');
                navbar.className = 'navbar';
                document.querySelector('.container').prepend(navbar);
            }
            navbar.appendChild(statusIndicator);
        }
        
        // Enable build controls
        startBuildBtn.disabled = false;
        
        // Request initial status
        socket.send(JSON.stringify({ type: 'requestStatus' }));
    };
    
    socket.onmessage = (event) => {
        try {
            const message = JSON.parse(event.data);
            
            switch(message.type) {
                case 'status':
                    updateBuildStatus(message.data);
                    break;
                case 'buildOutput':
                    updateBuildOutput(message.data);
                    break;
                case 'tasks':
                    updateTaskList(message.data);
                    break;
                case 'error':
                    console.error('Build error:', message.data);
                    // Display error in the UI with timestamp
                    const errorSpan = document.createElement('span');
                    errorSpan.className = 'output-error';
                    const timestamp = new Date().toLocaleTimeString();
                    errorSpan.textContent = `[${timestamp}] ERROR: ${message.data}`;
                    outputText.appendChild(errorSpan);
                    outputText.appendChild(document.createElement('br'));
                    
                    // Auto-scroll if enabled
                    if (autoScrollCheckbox.checked) {
                        outputText.scrollTop = outputText.scrollHeight;
                    }
                    break;
                case 'metrics':
                    updateBuildMetrics(message.data);
                    break;
                default:
                    console.warn('Unknown message type:', message.type);
            }
        } catch (error) {
            console.error('Error parsing WebSocket message:', error);
        }
    };
    
    socket.onclose = (event) => {
        console.log(`WebSocket connection closed: ${event.code} ${event.reason}`);
        clearTimeout(connectionTimeout);
        
        // Update UI
        connectionNotice.textContent = 'Disconnected from build server. Reconnecting...';
        connectionNotice.className = 'connection-notice error';
        
        // Add status indicator to the navbar
        const statusIndicator = document.getElementById('connectionStatus') || document.createElement('div');
        statusIndicator.id = 'connectionStatus';
        statusIndicator.className = 'connection-status disconnected';
        statusIndicator.title = 'Disconnected from build server';
        
        if (!document.getElementById('connectionStatus')) {
            document.querySelector('.navbar').appendChild(statusIndicator);
        }
        
        // Disable build controls
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = true;
        
        // Try to reconnect with exponential backoff
        if (!reconnectInterval) {
            let retryCount = 0;
            const maxRetries = 10;
            
            reconnectInterval = setInterval(() => {
                if (!socket || socket.readyState === WebSocket.CLOSED) {
                    retryCount++;
                    if (retryCount > maxRetries) {
                        // Reset to base interval after max retries
                        retryCount = 0;
                        connectionNotice.textContent = 'Connection failed. Still trying...';
                    }
                    const backoffTime = Math.min(30, Math.pow(1.5, retryCount));
                    console.log(`Reconnecting (attempt ${retryCount}) in ${backoffTime.toFixed(1)}s`);
                    
                    // Update the status indicator with retry info
                    statusIndicator.title = `Reconnecting (attempt ${retryCount})...`;
                    
                    connectWebSocket();
                }
            }, 3000);
        }
    };
    
    socket.onerror = (error) => {
        console.error('WebSocket error:', error);
        connectionNotice.textContent = 'Connection error. Retrying...';
        connectionNotice.className = 'connection-notice error';
        
        // Update status indicator
        const statusIndicator = document.getElementById('connectionStatus');
        if (statusIndicator) {
            statusIndicator.className = 'connection-status disconnected';
            statusIndicator.title = 'Connection error. Retrying...';
        }
    };
}

// Update UI with build status
function updateBuildStatus(status) {
    // Update main status
    buildStatusText.textContent = capitalizeFirstLetter(status.status);
    
    // Apply status-specific styles
    buildStatusText.className = ''; // Reset classes
    buildStatusText.classList.add(`status-${status.status}`);
    
    // Update progress
    progressBar.style.width = `${status.progress}%`;
    progressText.textContent = `${status.progress}%`;
    
    // Update build timing information
    if (status.startTime) {
        buildStartTime = new Date(status.startTime);
    }
    
    if (status.endTime) {
        buildEndTime = new Date(status.endTime);
        clearInterval(timerInterval);
        updateBuildTimeDisplay();
    } else if (status.status === 'building' && buildStartTime) {
        // Start timer
        if (!timerInterval) {
            timerInterval = setInterval(updateBuildTimeDisplay, 1000);
        }
    }
    
    // Update module status
    for (const [moduleName, moduleData] of Object.entries(status.modules)) {
        const moduleCard = document.getElementById(`module-${moduleName}`);
        if (moduleCard) {
            const indicator = moduleCard.querySelector('.module-indicator');
            const taskElement = moduleCard.querySelector('.module-task');
            
            // Reset classes
            indicator.className = 'module-indicator';
            indicator.classList.add(moduleData.status);
            
            // Update current task
            if (moduleData.currentTask) {
                taskElement.textContent = moduleData.currentTask;
            } else {
                taskElement.textContent = '';
            }
        }
    }
    
    // Update task list
    updateTaskList(status.tasks);
    
    // Update output
    if (status.buildOutput && status.buildOutput.length > 0) {
        updateBuildOutput(status.buildOutput);
    }
    
    // Update buttons
    if (status.status === 'building' || status.releaseInProgress) {
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = status.status !== 'building'; // Fixed logic here
        createReleaseBtn.disabled = true;
        
        // Update button text if release is in progress
        if (status.releaseInProgress) {
            createReleaseBtn.textContent = 'Creating Release...';
        }
    } else {
        startBuildBtn.disabled = false;
        stopBuildBtn.disabled = true;
        
        // Enable the GitHub Release button if build was successful and release is ready
        if ((status.status === 'success' || status.releaseReady) && !status.releaseCreated) {
            createReleaseBtn.disabled = false;
            createReleaseBtn.textContent = 'Create GitHub Release';
        } else {
            createReleaseBtn.disabled = true;
            
            // Change button text if release was created
            if (status.releaseCreated) {
                const versionText = status.releaseVersion ? ` (v${status.releaseVersion})` : '';
                createReleaseBtn.textContent = `Release Created${versionText}`;
            }
        }
    }
    
    // Update version info section if it exists and we have version data
    if (status.releaseVersion && status.releaseCreated) {
        let versionInfoSection = document.getElementById('versionInfoSection');
        if (!versionInfoSection) {
            versionInfoSection = document.createElement('div');
            versionInfoSection.id = 'versionInfoSection';
            versionInfoSection.className = 'version-info-section';
            
            const versionHeader = document.createElement('h4');
            versionHeader.textContent = 'Latest Release';
            versionInfoSection.appendChild(versionHeader);
            
            const versionContent = document.createElement('div');
            versionContent.id = 'versionContent';
            versionContent.className = 'version-content';
            versionInfoSection.appendChild(versionContent);
            
            // Find a good place to insert it
            const buildStatusSection = document.querySelector('.build-status');
            buildStatusSection.parentNode.insertBefore(versionInfoSection, buildStatusSection.nextSibling);
        }
        
        // Update version info content
        const versionContent = document.getElementById('versionContent');
        if (versionContent) {
            const releaseTime = status.releaseTime ? new Date(status.releaseTime) : new Date();
            const timeAgo = getRelativeTime(releaseTime);
            
            versionContent.innerHTML = `
                <div class="version-details">
                    <p class="version-number">Version: <strong>${status.releaseVersion}</strong></p>
                    <p class="version-date">Released: ${timeAgo}</p>
                    <p class="version-modules">All module JARs are included in this release</p>
                </div>
            `;
        }
    }
}

// Update build output
function updateBuildOutput(output) {
    // Clear if too many lines to prevent performance issues
    if (outputText.childNodes.length > 1000) {
        outputText.innerHTML = '';
    }
    
    // Add new output
    output.forEach(line => {
        const span = document.createElement('span');
        span.className = `output-${line.type}`;
        span.textContent = line.message;
        outputText.appendChild(span);
        
        // Add line break after each message
        outputText.appendChild(document.createElement('br'));
    });
    
    // Auto-scroll to bottom
    if (autoScrollCheckbox.checked) {
        outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
    }
}

// Update task list
function updateTaskList(tasks) {
    // Clear task container
    taskContainer.innerHTML = '';
    
    // Sort tasks by start time (most recent first)
    const sortedTasks = Object.entries(tasks)
        .map(([key, task]) => ({ key, ...task }))
        .sort((a, b) => new Date(b.startTime) - new Date(a.startTime))
        .slice(0, 10); // Show only the 10 most recent tasks
    
    // Add task items
    sortedTasks.forEach(task => {
        const taskItem = document.createElement('div');
        taskItem.className = 'task-item';
        
        const taskInfo = document.createElement('div');
        taskInfo.className = 'task-info';
        taskInfo.innerHTML = `
            <span class="task-module">${task.module}</span>:
            <span class="task-name">${task.task}</span>
        `;
        
        const taskStatus = document.createElement('div');
        taskStatus.className = `task-status status-${task.status}`;
        taskStatus.textContent = capitalizeFirstLetter(task.status);
        
        taskItem.appendChild(taskInfo);
        taskItem.appendChild(taskStatus);
        taskContainer.appendChild(taskItem);
    });
}

// Update build time display
function updateBuildTimeDisplay() {
    if (!buildStartTime) return;
    
    const now = buildEndTime || new Date();
    const elapsed = now - buildStartTime;
    
    const seconds = Math.floor((elapsed / 1000) % 60);
    const minutes = Math.floor((elapsed / (1000 * 60)) % 60);
    const hours = Math.floor(elapsed / (1000 * 60 * 60));
    
    const timeString = `${padZero(hours)}:${padZero(minutes)}:${padZero(seconds)}`;
    buildTime.textContent = `Build time: ${timeString}`;
}

// Utility function to pad zero for time display
function padZero(num) {
    return num.toString().padStart(2, '0');
}

// Utility function to capitalize first letter
function capitalizeFirstLetter(string) {
    return string.charAt(0).toUpperCase() + string.slice(1);
}

// Update build metrics display
function updateBuildMetrics(metrics) {
    // Create or get metrics container
    let metricsContainer = document.getElementById('metricsContainer');
    if (!metricsContainer) {
        metricsContainer = document.createElement('div');
        metricsContainer.id = 'metricsContainer';
        metricsContainer.className = 'metrics-container';
        document.querySelector('.container').appendChild(metricsContainer);
        
        // Add heading
        const heading = document.createElement('h3');
        heading.textContent = 'Build Metrics';
        metricsContainer.appendChild(heading);
        
        // Create metrics grid
        const metricsGrid = document.createElement('div');
        metricsGrid.className = 'metrics-grid';
        metricsContainer.appendChild(metricsGrid);
        
        // Success rate metric
        const successRate = document.createElement('div');
        successRate.className = 'metric-card';
        successRate.innerHTML = `
            <h4>Success Rate</h4>
            <div id="successRateValue" class="metric-value">-</div>
            <div class="metric-chart">
                <div id="successRateBar" class="progress-bar"></div>
            </div>
        `;
        metricsGrid.appendChild(successRate);
        
        // Average build time metric
        const avgBuildTime = document.createElement('div');
        avgBuildTime.className = 'metric-card';
        avgBuildTime.innerHTML = `
            <h4>Average Build Time</h4>
            <div id="avgBuildTimeValue" class="metric-value">-</div>
            <div id="avgBuildTimeChange" class="metric-change"></div>
        `;
        metricsGrid.appendChild(avgBuildTime);
        
        // Build timeline
        const timelineCard = document.createElement('div');
        timelineCard.className = 'metric-card timeline-card';
        timelineCard.innerHTML = `
            <h4>Build Timeline</h4>
            <div id="buildTimeline" class="build-timeline"></div>
        `;
        metricsContainer.appendChild(timelineCard);
    }
    
    // Update success rate
    if (metrics.successRate !== undefined) {
        const successRateElement = document.getElementById('successRateValue');
        const successRateBar = document.getElementById('successRateBar');
        
        successRateElement.textContent = `${Math.round(metrics.successRate * 100)}%`;
        successRateBar.style.width = `${metrics.successRate * 100}%`;
        
        // Add color based on success rate
        successRateBar.className = 'progress-bar';
        if (metrics.successRate >= 0.8) {
            successRateBar.classList.add('success');
        } else if (metrics.successRate >= 0.5) {
            successRateBar.classList.add('warning');
        } else {
            successRateBar.classList.add('error');
        }
    }
    
    // Update average build time
    if (metrics.avgBuildTime !== undefined) {
        const avgBuildTimeElement = document.getElementById('avgBuildTimeValue');
        const avgBuildTimeChange = document.getElementById('avgBuildTimeChange');
        
        // Format time as mm:ss
        const minutes = Math.floor(metrics.avgBuildTime / 60000);
        const seconds = Math.floor((metrics.avgBuildTime % 60000) / 1000);
        avgBuildTimeElement.textContent = `${padZero(minutes)}:${padZero(seconds)}`;
        
        // Show trend if available
        if (metrics.buildTimeTrend !== undefined) {
            const trendPct = Math.abs(Math.round(metrics.buildTimeTrend * 100));
            const trendSymbol = metrics.buildTimeTrend < 0 ? '↓' : '↑';
            const trendClass = metrics.buildTimeTrend < 0 ? 'trend-down' : 'trend-up';
            
            avgBuildTimeChange.className = `metric-change ${trendClass}`;
            avgBuildTimeChange.textContent = `${trendSymbol} ${trendPct}%`;
        }
    }
    
    // Update build timeline
    if (metrics.recentBuilds && metrics.recentBuilds.length > 0) {
        const timelineElement = document.getElementById('buildTimeline');
        if (timelineElement) {
            timelineElement.innerHTML = ''; // Clear existing timeline
            
            // Create timeline items
            metrics.recentBuilds.forEach(build => {
                const buildItem = document.createElement('div');
                buildItem.className = `timeline-item status-${build.status}`;
                
                // Format date as relative time
                const buildDate = new Date(build.timestamp);
                const timeAgo = getRelativeTime(buildDate);
                
                buildItem.setAttribute('title', 
                    `${build.command}\n${buildDate.toLocaleString()}\nDuration: ${formatDuration(build.duration)}`);
                
                buildItem.innerHTML = `
                    <div class="timeline-marker"></div>
                    <div class="timeline-content">
                        <div class="timeline-title">${build.command}</div>
                        <div class="timeline-time">${timeAgo}</div>
                    </div>
                `;
                
                timelineElement.appendChild(buildItem);
            });
        }
    }
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
    const gradleCommand = buildCommandSelect.value;
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
});

stopBuildBtn.addEventListener('click', () => {
    socket.send(JSON.stringify({ type: 'stopBuild' }));
});

clearOutputBtn.addEventListener('click', () => {
    outputText.innerHTML = '';
});

createReleaseBtn.addEventListener('click', () => {
    if (confirm('Create a new GitHub release with all versioned module JARs?')) {
        // Show release in progress
        createReleaseBtn.disabled = true;
        createReleaseBtn.textContent = 'Creating Versioned Release...';
        
        // Add release message to output
        const releaseMessage = document.createElement('span');
        releaseMessage.className = 'output-info';
        releaseMessage.textContent = '[GitHub Release] Starting versioned release process...';
        outputText.appendChild(releaseMessage);
        outputText.appendChild(document.createElement('br'));
        
        // Auto-scroll to bottom
        if (autoScrollCheckbox.checked) {
            outputText.parentElement.scrollTop = outputText.parentElement.scrollHeight;
        }
        
        // Create version info section in UI if it doesn't exist
        let versionInfoSection = document.getElementById('versionInfoSection');
        if (!versionInfoSection) {
            versionInfoSection = document.createElement('div');
            versionInfoSection.id = 'versionInfoSection';
            versionInfoSection.className = 'version-info-section';
            
            const versionHeader = document.createElement('h4');
            versionHeader.textContent = 'Latest Release';
            versionInfoSection.appendChild(versionHeader);
            
            const versionContent = document.createElement('div');
            versionContent.id = 'versionContent';
            versionContent.className = 'version-content';
            versionContent.innerHTML = '<p>Creating new release...</p>';
            versionInfoSection.appendChild(versionContent);
            
            // Find a good place to insert it
            const buildStatusSection = document.querySelector('.build-status');
            buildStatusSection.parentNode.insertBefore(versionInfoSection, buildStatusSection.nextSibling);
        } else {
            // Update existing version info section
            const versionContent = document.getElementById('versionContent');
            if (versionContent) {
                versionContent.innerHTML = '<p>Creating new release...</p>';
            }
        }
        
        // Send release request via WebSocket
        socket.send(JSON.stringify({
            type: 'createRelease'
        }));
        
        // The server will respond through the normal WebSocket channel
        // and we'll update the UI based on that response
        
        // Note: The rest of the UI updates will be handled by the existing
        // WebSocket message handlers for status updates and output messages
        
        // We'll defer enabling the button until we get confirmation from the server
    }
});

// Initialize WebSocket connection
connectWebSocket();

// Handle page unload
window.addEventListener('beforeunload', () => {
    if (socket) {
        socket.close();
    }
});