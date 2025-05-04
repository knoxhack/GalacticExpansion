/**
 * Enhanced Build Monitor for Galactic Expansion Mod
 * Provides real-time WebSocket-based monitoring with improved UI feedback
 */
class BuildMonitor {
  constructor() {
    // Initialize variables
    this.socket = null;
    this.reconnectInterval = 1500; // Start with 1.5s
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 10;
    this.connected = false;
    this.moduleElements = {};
    this.lastStatus = {};
    
    // Initialize the module DOM elements references
    this.initModuleElements();
    
    // Connect to WebSocket server
    this.connect();
    
    // Setup event handlers for UI elements
    this.setupUIEventHandlers();
  }
  
  /**
   * Cache all module DOM elements for faster access
   */
  initModuleElements() {
    const modules = ['core', 'power', 'machinery', 'biotech', 'energy', 
                     'construction', 'space', 'utilities', 'vehicles', 
                     'weaponry', 'robotics'];
    
    modules.forEach(module => {
      this.moduleElements[module] = {
        container: document.getElementById(`module-${module}`),
        progressBar: document.querySelector(`#module-${module} .module-progress-bar`),
        indicator: document.querySelector(`#module-${module} .module-indicator`),
        task: document.querySelector(`#module-${module} .module-task`)
      };
    });
  }
  
  /**
   * Connect to the WebSocket server
   */
  connect() {
    try {
      console.log("Connecting to WebSocket...");
      
      // Use the correct protocol based on page protocol
      const protocol = window.location.protocol === "https:" ? "wss:" : "ws:";
      const wsUrl = `${protocol}//${window.location.host}/ws`;
      
      this.socket = new WebSocket(wsUrl);
      this.updateConnectionStatus('connecting', 'Connecting to build server...');
      
      // Setup WebSocket event handlers
      this.setupSocketHandlers();
    } catch (error) {
      console.error("Error connecting to WebSocket:", error);
      this.updateConnectionStatus('error', `Connection error: ${error.message}`);
      this.handleConnectionFailure();
    }
  }
  
  /**
   * Setup WebSocket event handlers
   */
  setupSocketHandlers() {
    this.socket.addEventListener('open', () => {
      console.log("WebSocket connection established");
      this.connected = true;
      this.reconnectAttempts = 0;
      this.reconnectInterval = 1500;
      this.updateConnectionStatus('connected', 'Connected to build server');
      
      // Request initial data
      this.sendMessage({ type: 'requestStatus' });
      this.sendMessage({ type: 'requestOutput' });
      this.sendMessage({ type: 'requestNotifications' });
      this.sendMessage({ type: 'requestShortCommits', limit: 10 });
    });
    
    this.socket.addEventListener('message', (event) => {
      try {
        const message = JSON.parse(event.data);
        console.log("Received message type:", message.type);
        this.handleMessage(message);
      } catch (error) {
        console.error("Error handling message:", error, event.data);
      }
    });
    
    this.socket.addEventListener('close', (event) => {
      console.log("WebSocket connection closed:", event.code, event.reason);
      this.connected = false;
      this.updateConnectionStatus('disconnected', 'Disconnected from build server');
      this.handleConnectionFailure();
    });
    
    this.socket.addEventListener('error', (error) => {
      console.error("WebSocket error:", error);
      this.updateConnectionStatus('error', 'Connection error');
    });
  }
  
  /**
   * Handle connection failures with exponential backoff
   */
  handleConnectionFailure() {
    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      const delay = Math.min(30000, this.reconnectInterval * Math.pow(1.5, this.reconnectAttempts - 1));
      
      console.log(`Reconnecting (attempt ${this.reconnectAttempts}) in ${(delay/1000).toFixed(1)}s`);
      this.updateConnectionStatus('reconnecting', `Reconnecting (attempt ${this.reconnectAttempts})...`);
      
      setTimeout(() => this.connect(), delay);
    } else {
      this.updateConnectionStatus('failed', 'Connection failed after maximum attempts');
      console.error("Maximum reconnection attempts reached");
    }
  }
  
  /**
   * Process an incoming WebSocket message
   * @param {Object} message - The parsed message
   */
  handleMessage(message) {
    switch (message.type) {
      case 'status':
        this.updateBuildStatus(message.data);
        this.updateBuildTime(message.data);
        this.updateModuleStatus(message.data.modules);
        break;
        
      case 'buildOutput':
        this.updateBuildOutput(message.data);
        break;
        
      case 'notifications':
        this.handleNotifications(message.data);
        break;
        
      case 'tasks':
        // Handle task updates (to be implemented)
        break;
        
      case 'error':
        console.error("Server error:", message.data);
        this.showNotification("Server Error", message.data, 'error');
        break;
        
      case 'shortCommits':
        // Handle short commit history (to be implemented)
        break;
        
      case 'metrics':
        // Handle build metrics (to be implemented)
        break;
        
      default:
        console.warn("Unknown message type:", message.type);
    }
  }
  
  /**
   * Update the connection status indicator
   * @param {string} status - Status type: connected, connecting, disconnected, error, failed
   * @param {string} tooltip - Tooltip text
   */
  updateConnectionStatus(status, tooltip) {
    const statusElement = document.getElementById('connectionStatus');
    if (!statusElement) return;
    
    // Remove all previous classes
    statusElement.classList.remove('connected', 'connecting', 'disconnected', 'error', 'reconnecting', 'failed');
    
    // Add the current status class
    statusElement.classList.add(status);
    
    // Update tooltip
    statusElement.title = tooltip || '';
  }
  
  /**
   * Update the build status display
   * @param {Object} data - Build status data
   */
  updateBuildStatus(data) {
    const statusTextElement = document.getElementById('buildStatusText');
    const progressBarElement = document.getElementById('progressBar');
    const progressTextElement = document.getElementById('progressText');
    
    if (statusTextElement) {
      let statusText = 'Unknown';
      let statusClass = '';
      
      switch (data.status) {
        case 'building':
          statusText = 'Building';
          statusClass = 'building';
          break;
        case 'success':
          statusText = 'Success';
          statusClass = 'success';
          break;
        case 'failed':
          statusText = 'Failed';
          statusClass = 'failed';
          break;
        case 'canceled':
          statusText = 'Canceled';
          statusClass = 'canceled';
          break;
        case 'idle':
          statusText = 'Idle';
          statusClass = 'idle';
          break;
      }
      
      statusTextElement.textContent = statusText;
      
      // Remove all status classes and add the current one
      statusTextElement.classList.remove('building', 'success', 'failed', 'canceled', 'idle');
      if (statusClass) {
        statusTextElement.classList.add(statusClass);
      }
    }
    
    // Update progress bar
    if (progressBarElement && progressTextElement) {
      const progress = data.progress || 0;
      progressBarElement.style.width = `${progress}%`;
      progressTextElement.textContent = `${progress}%`;
      
      // Update progress bar class based on status
      progressBarElement.classList.remove('building', 'success', 'failed');
      if (data.status === 'building') {
        progressBarElement.classList.add('building');
      } else if (data.status === 'success') {
        progressBarElement.classList.add('success');
      } else if (data.status === 'failed') {
        progressBarElement.classList.add('failed');
      }
    }
    
    // Enable/disable UI buttons based on build status
    const startBuildBtn = document.getElementById('startBuild');
    const stopBuildBtn = document.getElementById('stopBuild');
    const createReleaseBtn = document.getElementById('createRelease');
    
    if (startBuildBtn && stopBuildBtn) {
      if (data.status === 'building') {
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = false;
      } else {
        startBuildBtn.disabled = false;
        stopBuildBtn.disabled = true;
      }
    }
    
    if (createReleaseBtn) {
      createReleaseBtn.disabled = data.status !== 'success';
    }
  }
  
  /**
   * Update the build time display
   * @param {Object} data - Build status data
   */
  updateBuildTime(data) {
    const buildTimeElement = document.getElementById('buildTime');
    if (!buildTimeElement) return;
    
    let timeText = '';
    
    if (data.startTime) {
      if (data.status === 'building') {
        // Calculate elapsed time for in-progress builds
        const startTime = new Date(data.startTime);
        const now = new Date();
        const elapsed = now - startTime;
        timeText = `Elapsed: ${formatDuration(elapsed)}`;
        
        // Update continuously while building
        if (!this.buildTimeInterval) {
          this.buildTimeInterval = setInterval(() => {
            if (this.lastStatus.status === 'building') {
              const startTime = new Date(this.lastStatus.startTime);
              const now = new Date();
              const elapsed = now - startTime;
              buildTimeElement.textContent = `Elapsed: ${formatDuration(elapsed)}`;
            } else {
              clearInterval(this.buildTimeInterval);
              this.buildTimeInterval = null;
            }
          }, 1000);
        }
      } else if (data.endTime && data.duration) {
        // Display final build time for completed builds
        timeText = `Duration: ${formatDuration(data.duration)}`;
        
        // Clear the interval if it exists
        if (this.buildTimeInterval) {
          clearInterval(this.buildTimeInterval);
          this.buildTimeInterval = null;
        }
      }
    }
    
    buildTimeElement.textContent = timeText;
    this.lastStatus = data;
  }
  
  /**
   * Update module status display
   * @param {Object} modules - Module status data
   */
  updateModuleStatus(modules) {
    for (const [moduleName, moduleData] of Object.entries(modules)) {
      const elements = this.moduleElements[moduleName];
      if (!elements) continue;
      
      // Store for logging
      console.log(`Updating module ${moduleName} with status:`, moduleData);
      
      // Update status classes
      elements.container.classList.remove('building', 'success', 'failed', 'pending');
      elements.progressBar.classList.remove('building', 'success', 'failed', 'pending');
      elements.indicator.classList.remove('building', 'success', 'failed', 'pending');
      
      // Add appropriate class based on status
      elements.container.classList.add(moduleData.status);
      elements.progressBar.classList.add(moduleData.status);
      elements.indicator.classList.add(moduleData.status);
      
      // Reset pulse animation and apply it again for new updates
      elements.container.classList.remove('pulse');
      void elements.container.offsetWidth; // Force reflow to restart animation
      elements.container.classList.add('pulse');
      
      // Update task info if available
      if (elements.task && moduleData.lastTask) {
        elements.task.textContent = moduleData.lastTask;
      } else if (elements.task) {
        elements.task.textContent = '';
      }
      
      // Update progress bar width based on status
      if (elements.progressBar) {
        let width = '0%';
        
        if (moduleData.status === 'success') {
          width = '100%';
        } else if (moduleData.status === 'building') {
          width = '60%'; // Indicate in-progress
        } else if (moduleData.status === 'failed') {
          width = '30%'; // Indicate partial progress before failure
        }
        
        elements.progressBar.style.width = width;
      }
    }
  }
  
  /**
   * Update build output display
   * @param {string|Object|Array} output - Build output data
   */
  updateBuildOutput(output) {
    const outputElement = document.getElementById('outputText');
    if (!outputElement) return;
    
    const filterSelect = document.getElementById('filterOutput');
    const currentFilter = filterSelect ? filterSelect.value : 'all';
    const autoScroll = document.getElementById('autoScroll');
    const shouldAutoScroll = autoScroll ? autoScroll.checked : true;
    
    if (Array.isArray(output)) {
      // Handle array of output lines
      output.forEach(line => {
        this.appendOutputLine(line, outputElement, currentFilter);
      });
    } else if (typeof output === 'object') {
      // Handle single output object
      this.appendOutputLine(output, outputElement, currentFilter);
    } else if (typeof output === 'string') {
      // Handle plain string
      this.appendOutputLine({ type: 'info', message: output }, outputElement, currentFilter);
    }
    
    // Apply auto-scrolling if enabled
    if (shouldAutoScroll) {
      outputElement.scrollTop = outputElement.scrollHeight;
    }
  }
  
  /**
   * Append a single line to the output display
   * @param {string|Object} line - Line to append
   * @param {HTMLElement} outputElement - Output element
   * @param {string} filter - Current filter
   */
  appendOutputLine(line, outputElement, filter) {
    // Normalize line to object format if it's a string
    const outputLine = typeof line === 'string' ? { type: 'info', message: line } : line;
    
    // Apply filter
    if (filter !== 'all' && outputLine.type !== filter) {
      return;
    }
    
    // Create line element
    const lineElement = document.createElement('div');
    lineElement.className = `output-line ${outputLine.type || 'info'}`;
    
    // Add timestamp
    const timestamp = document.createElement('span');
    timestamp.className = 'timestamp';
    timestamp.textContent = new Date().toLocaleTimeString();
    lineElement.appendChild(timestamp);
    
    // Add type badge
    const badge = document.createElement('span');
    badge.className = `badge ${outputLine.type || 'info'}`;
    badge.textContent = outputLine.type?.toUpperCase() || 'INFO';
    lineElement.appendChild(badge);
    
    // Add message
    const message = document.createElement('span');
    message.className = 'message';
    message.textContent = outputLine.message || '';
    lineElement.appendChild(message);
    
    outputElement.appendChild(lineElement);
  }
  
  /**
   * Handle notification messages
   * @param {Object|Array} notifications - Notifications data
   */
  handleNotifications(notifications) {
    if (!notifications) return;
    
    if (Array.isArray(notifications)) {
      notifications.forEach(notification => {
        this.showNotification(
          notification.title || getDefaultTitle(notification.type),
          notification.message,
          notification.type || 'info',
          notification.timeout || 5000
        );
      });
    } else {
      this.showNotification(
        notifications.title || getDefaultTitle(notifications.type),
        notifications.message,
        notifications.type || 'info',
        notifications.timeout || 5000
      );
    }
  }
  
  /**
   * Display a notification
   * @param {string} title - Notification title
   * @param {string} message - Notification message
   * @param {string} type - Notification type: info, success, warning, error
   * @param {number} timeout - Auto-dismiss timeout in ms, 0 for no auto-dismiss
   */
  showNotification(title, message, type = 'info', timeout = 5000) {
    const container = document.getElementById('notificationsContainer');
    if (!container) return;
    
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.innerHTML = `
      <div class="notification-title">${title}</div>
      <div class="notification-message">${message}</div>
      <button class="notification-close">&times;</button>
    `;
    
    // Add close button handler
    const closeBtn = notification.querySelector('.notification-close');
    closeBtn.addEventListener('click', () => {
      notification.classList.add('hiding');
      setTimeout(() => {
        try {
          container.removeChild(notification);
        } catch (e) {
          // Notification may have been removed already
        }
      }, 300);
    });
    
    // Add the notification to the container
    container.appendChild(notification);
    
    // Set auto-dismiss timeout if specified
    if (timeout > 0) {
      setTimeout(() => {
        notification.classList.add('hiding');
        setTimeout(() => {
          try {
            container.removeChild(notification);
          } catch (e) {
            // Notification may have been removed already
          }
        }, 300);
      }, timeout);
    }
  }
  
  /**
   * Setup UI event handlers
   */
  setupUIEventHandlers() {
    // Start build button
    const startBuildBtn = document.getElementById('startBuild');
    if (startBuildBtn) {
      startBuildBtn.addEventListener('click', () => {
        const buildCommand = document.getElementById('buildCommand');
        const command = buildCommand ? buildCommand.value : 'build';
        
        this.sendMessage({
          type: 'startBuild',
          gradleCommand: command
        });
      });
    }
    
    // Stop build button
    const stopBuildBtn = document.getElementById('stopBuild');
    if (stopBuildBtn) {
      stopBuildBtn.addEventListener('click', () => {
        this.sendMessage({ type: 'stopBuild' });
      });
    }
    
    // Filter output
    const filterSelect = document.getElementById('filterOutput');
    if (filterSelect) {
      filterSelect.addEventListener('change', () => {
        // Request full output and let the filter apply on update
        this.sendMessage({ type: 'requestOutput' });
      });
    }
    
    // Clear output button
    const clearOutputBtn = document.getElementById('clearOutput');
    if (clearOutputBtn) {
      clearOutputBtn.addEventListener('click', () => {
        const outputElement = document.getElementById('outputText');
        if (outputElement) {
          outputElement.innerHTML = '';
        }
      });
    }
    
    // Create checkpoint button
    const createCheckpointBtn = document.getElementById('createCheckpointBtn');
    if (createCheckpointBtn) {
      createCheckpointBtn.addEventListener('click', () => {
        // Implement checkpoint creation
        const checkpointName = prompt('Enter a name for this checkpoint:', 
                                      `Checkpoint ${new Date().toLocaleString()}`);
        if (checkpointName) {
          const description = prompt('Enter a description (optional):');
          this.sendMessage({
            type: 'createCheckpoint',
            name: checkpointName,
            description: description || ''
          });
        }
      });
    }
    
    // Create release button
    const createReleaseBtn = document.getElementById('createRelease');
    if (createReleaseBtn) {
      createReleaseBtn.addEventListener('click', () => {
        if (confirm('Create a new GitHub release with all module JARs?')) {
          this.sendMessage({ type: 'createRelease' });
        }
      });
    }
  }
  
  /**
   * Send a message to the server
   * @param {Object} message - Message to send
   */
  sendMessage(message) {
    if (this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(JSON.stringify(message));
    } else {
      console.warn('Cannot send message, socket not connected');
      this.showNotification('Connection Error', 'Not connected to server', 'error');
    }
  }
}

/**
 * Format a duration in milliseconds to a human-readable string
 * @param {number} ms - Duration in milliseconds
 * @returns {string} Formatted duration string
 */
function formatDuration(ms) {
  if (!ms || isNaN(ms)) return '0s';
  
  const seconds = Math.floor(ms / 1000) % 60;
  const minutes = Math.floor(ms / (1000 * 60)) % 60;
  const hours = Math.floor(ms / (1000 * 60 * 60));
  
  let result = '';
  if (hours > 0) result += `${hours}h `;
  if (minutes > 0 || hours > 0) result += `${minutes}m `;
  result += `${seconds}s`;
  
  return result.trim();
}

/**
 * Get a default title for a notification type
 * @param {string} type - Notification type
 * @returns {string} Default title
 */
function getDefaultTitle(type) {
  switch (type) {
    case 'success': return 'Success';
    case 'error': return 'Error';
    case 'warning': return 'Warning';
    case 'info':
    default: return 'Information';
  }
}

// Initialize the monitor when the DOM is fully loaded
document.addEventListener('DOMContentLoaded', () => {
  window.buildMonitor = new BuildMonitor();
});