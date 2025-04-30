// Elements
const buildStatusText = document.getElementById('buildStatusText');
const buildTime = document.getElementById('buildTime');
const progressBar = document.getElementById('progressBar');
const progressText = document.getElementById('progressText');
const outputText = document.getElementById('outputText');
const taskContainer = document.getElementById('taskContainer');
const startBuildBtn = document.getElementById('startBuild');
const stopBuildBtn = document.getElementById('stopBuild');
const buildCommandSelect = document.getElementById('buildCommand');
const clearOutputBtn = document.getElementById('clearOutput');
const autoScrollCheckbox = document.getElementById('autoScroll');

// Establish WebSocket connection
const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
const wsUrl = `${protocol}//${window.location.host}/ws`;
let socket;
let reconnectInterval;
let buildStartTime;
let buildEndTime;
let timerInterval;

// Connect to WebSocket
function connectWebSocket() {
    console.log('Connecting to WebSocket...');
    socket = new WebSocket(wsUrl);
    
    socket.onopen = () => {
        console.log('WebSocket connection established');
        if (reconnectInterval) {
            clearInterval(reconnectInterval);
            reconnectInterval = null;
        }
        // Request initial status
        socket.send(JSON.stringify({ type: 'requestStatus' }));
    };
    
    socket.onmessage = (event) => {
        try {
            const message = JSON.parse(event.data);
            
            if (message.type === 'status') {
                updateBuildStatus(message.data);
            }
        } catch (error) {
            console.error('Error parsing WebSocket message:', error);
        }
    };
    
    socket.onclose = () => {
        console.log('WebSocket connection closed');
        // Try to reconnect
        if (!reconnectInterval) {
            reconnectInterval = setInterval(() => {
                if (!socket || socket.readyState === WebSocket.CLOSED) {
                    connectWebSocket();
                }
            }, 3000);
        }
    };
    
    socket.onerror = (error) => {
        console.error('WebSocket error:', error);
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
    if (status.status === 'building') {
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = false;
    } else {
        startBuildBtn.disabled = false;
        stopBuildBtn.disabled = true;
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

// Initialize WebSocket connection
connectWebSocket();

// Handle page unload
window.addEventListener('beforeunload', () => {
    if (socket) {
        socket.close();
    }
});