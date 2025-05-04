/**
 * Enhanced Build Monitor for Galactic Expansion Mod
 * Provides real-time WebSocket-based monitoring with improved UI feedback
 */

class BuildMonitor {
  constructor() {
    this.socket = null;
    this.reconnectTimer = null;
    this.reconnectAttempts = 0;
    this.maxReconnectAttempts = 10;
    this.reconnectDelay = 2000; // Start with 2 seconds
    this.maxReconnectDelay = 30000; // Max 30 seconds
    this.connectionStatusElement = document.getElementById('connectionStatus');
    this.moduleElements = {};
    this.notificationsContainer = document.getElementById('notificationsContainer');
    this.isConnected = false;
    this.pendingMessages = [];

    // Initialize module elements cache
    this.initModuleElements();

    // Set up WebSocket connection
    this.connect();
  }

  /**
   * Cache all module DOM elements for faster access
   */
  initModuleElements() {
    const moduleNames = [
      'core', 'power', 'machinery', 'biotech', 'energy', 'construction',
      'space', 'utilities', 'vehicles', 'weaponry', 'robotics'
    ];

    moduleNames.forEach(moduleName => {
      const moduleElement = document.getElementById(`module-${moduleName}`);
      if (moduleElement) {
        this.moduleElements[moduleName] = {
          container: moduleElement,
          progressBar: moduleElement.querySelector('.module-progress-bar'),
          indicator: moduleElement.querySelector('.module-indicator'),
          nameElement: moduleElement.querySelector('.module-name'),
          taskElement: moduleElement.querySelector('.module-task')
        };
      }
    });
  }

  /**
   * Connect to the WebSocket server
   */
  connect() {
    const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
    const wsUrl = `${protocol}//${window.location.host}/ws`;

    try {
      // Update UI to show connecting status
      this.updateConnectionStatus('connecting', 'Connecting to build server...');
      this.showNotification('Connection', 'Connecting to build server...', 'info', 5000);

      // Create new WebSocket connection
      this.socket = new WebSocket(wsUrl);
      this.setupSocketHandlers();
    } catch (error) {
      console.error('Failed to create WebSocket connection:', error);
      this.handleConnectionFailure();
    }
  }

  /**
   * Setup WebSocket event handlers
   */
  setupSocketHandlers() {
    // Connection opened
    this.socket.addEventListener('open', () => {
      console.log('WebSocket connection established');
      this.isConnected = true;
      this.reconnectAttempts = 0;
      this.reconnectDelay = 2000;
      
      // Update UI
      this.updateConnectionStatus('connected', 'Connected to build server');
      this.showNotification('Connected', 'Build server connection established', 'success', 3000);
      
      // Request initial data
      this.socket.send(JSON.stringify({ type: 'requestStatus' }));
      this.socket.send(JSON.stringify({ type: 'requestShortCommits', limit: 10 }));
      
      // Process any pending messages
      if (this.pendingMessages.length > 0) {
        console.log(`Processing ${this.pendingMessages.length} pending messages`);
        this.pendingMessages.forEach(msg => this.socket.send(msg));
        this.pendingMessages = [];
      }
    });

    // Message received
    this.socket.addEventListener('message', (event) => {
      try {
        const message = JSON.parse(event.data);
        this.handleMessage(message);
      } catch (error) {
        console.error('Error parsing WebSocket message:', error);
      }
    });

    // Connection closed
    this.socket.addEventListener('close', (event) => {
      this.isConnected = false;
      console.log(`WebSocket connection closed. Code: ${event.code}, Reason: ${event.reason}`);
      this.updateConnectionStatus('disconnected', 'Disconnected from build server');
      this.handleConnectionFailure();
    });

    // Connection error
    this.socket.addEventListener('error', (error) => {
      console.error('WebSocket error:', error);
      this.updateConnectionStatus('error', 'Connection error');
      
      // Don't need to call handleConnectionFailure() here as close event will fire
    });
  }

  /**
   * Handle connection failures with exponential backoff
   */
  handleConnectionFailure() {
    if (this.reconnectTimer) {
      clearTimeout(this.reconnectTimer);
    }

    if (this.reconnectAttempts < this.maxReconnectAttempts) {
      this.reconnectAttempts++;
      const delay = Math.min(this.reconnectDelay * Math.pow(1.5, this.reconnectAttempts - 1), this.maxReconnectDelay);
      
      console.log(`Reconnect attempt ${this.reconnectAttempts} in ${Math.round(delay / 1000)} seconds`);
      this.updateConnectionStatus('reconnecting', `Reconnecting (${this.reconnectAttempts}/${this.maxReconnectAttempts})...`);
      
      this.reconnectTimer = setTimeout(() => {
        this.connect();
      }, delay);
    } else {
      this.updateConnectionStatus('failed', 'Connection failed. Please reload the page.');
      this.showNotification(
        'Connection Failed', 
        'Could not connect to build server. Please reload the page.', 
        'error',
        0 // No auto-hide
      );
    }
  }

  /**
   * Process an incoming WebSocket message
   * @param {Object} message - The parsed message
   */
  handleMessage(message) {
    if (!message || !message.type) {
      console.error('Invalid message format:', message);
      return;
    }

    const data = message.data || {};
    
    console.log('Received message type:', message.type);
    
    try {
      switch (message.type) {
        case 'status':
          this.updateBuildStatus(data);
          if (data.modules) {
            this.updateModuleStatus(data.modules);
          }
          break;
          
        case 'buildOutput':
          this.updateBuildOutput(data);
          break;
          
        case 'notifications':
          this.handleNotifications(data);
          break;
          
        case 'moduleStatus':
          this.updateModuleStatus(data);
          break;
          
        case 'error':
          this.showNotification('Error', data.message || String(data), 'error');
          break;
          
        default:
          // Forward other messages to the main handler
          if (window.handleMessageData) {
            window.handleMessageData(message);
          } else {
            console.warn('Unknown message type:', message.type);
          }
      }
    } catch (error) {
      console.error('Error handling message:', error);
    }
  }

  /**
   * Update the connection status indicator
   * @param {string} status - Status type: connected, connecting, disconnected, error, failed
   * @param {string} tooltip - Tooltip text
   */
  updateConnectionStatus(status, tooltip) {
    if (this.connectionStatusElement) {
      this.connectionStatusElement.className = `connection-status ${status}`;
      this.connectionStatusElement.title = tooltip || '';
    }
  }

  /**
   * Update the build status display
   * @param {Object} data - Build status data
   */
  updateBuildStatus(data) {
    const buildStatusText = document.getElementById('buildStatusText');
    const progressBar = document.getElementById('progressBar');
    const progressText = document.getElementById('progressText');
    
    if (!data) return;
    
    // Update build status text
    if (buildStatusText) {
      let statusDisplay = data.status || 'Unknown';
      // Capitalize first letter
      statusDisplay = statusDisplay.charAt(0).toUpperCase() + statusDisplay.slice(1);
      buildStatusText.textContent = statusDisplay;
      
      // Add class for styling
      buildStatusText.className = data.status || '';
    }
    
    // Update progress bar
    if (progressBar && progressText) {
      const progress = data.progress || 0;
      const progressPercentage = Math.round(progress * 100);
      
      progressBar.style.width = `${progressPercentage}%`;
      progressText.textContent = `${progressPercentage}%`;
      
      // Add class for status-based styling
      progressBar.className = 'progress-bar';
      if (data.status) {
        progressBar.classList.add(data.status);
      }
    }
    
    // Update build time display
    this.updateBuildTime(data);
    
    // Enable/disable buttons based on status
    const startBuildBtn = document.getElementById('startBuild');
    const stopBuildBtn = document.getElementById('stopBuild');
    const createReleaseBtn = document.getElementById('createRelease');
    
    if (startBuildBtn && stopBuildBtn && createReleaseBtn) {
      if (data.status === 'building' || data.status === 'starting') {
        startBuildBtn.disabled = true;
        stopBuildBtn.disabled = false;
        createReleaseBtn.disabled = true;
      } else {
        startBuildBtn.disabled = false;
        stopBuildBtn.disabled = true;
        createReleaseBtn.disabled = data.status !== 'success';
      }
    }
  }

  /**
   * Update the build time display
   * @param {Object} data - Build status data
   */
  updateBuildTime(data) {
    const buildTimeElement = document.getElementById('buildTime');
    if (!buildTimeElement) return;
    
    if (data.status === 'building' || data.status === 'starting') {
      // Show elapsed time
      if (data.startTime) {
        const startTime = new Date(data.startTime);
        const now = new Date();
        const elapsed = now - startTime;
        buildTimeElement.textContent = formatDuration(elapsed);
        
        // Start timer to update elapsed time
        if (!this.buildTimeInterval) {
          this.buildTimeInterval = setInterval(() => {
            const now = new Date();
            const elapsed = now - startTime;
            buildTimeElement.textContent = formatDuration(elapsed);
          }, 1000);
        }
      }
    } else {
      // Stop timer if running
      if (this.buildTimeInterval) {
        clearInterval(this.buildTimeInterval);
        this.buildTimeInterval = null;
      }
      
      // Show duration if available
      if (data.duration) {
        buildTimeElement.textContent = formatDuration(data.duration);
      } else if (data.startTime && data.endTime) {
        const startTime = new Date(data.startTime);
        const endTime = new Date(data.endTime);
        const duration = endTime - startTime;
        buildTimeElement.textContent = formatDuration(duration);
      } else {
        buildTimeElement.textContent = '';
      }
    }
  }

  /**
   * Update module status display
   * @param {Object} modules - Module status data
   */
  updateModuleStatus(modules) {
    if (!modules) return;
    
    Object.entries(modules).forEach(([moduleName, status]) => {
      const moduleElements = this.moduleElements[moduleName];
      if (!moduleElements) return;
      
      const statusValue = typeof status === 'string' ? status : (status.status || 'pending');
      
      // Update progress bar
      if (moduleElements.progressBar) {
        moduleElements.progressBar.className = 'module-progress-bar ' + statusValue;
        
        let progressWidth = 0;
        if (statusValue === 'success' || statusValue === 'failed') {
          progressWidth = 100;
        } else if (statusValue === 'building') {
          // If we have progress info, use it, otherwise use 50%
          const progress = typeof status === 'object' && status.progress ? status.progress : 0.5;
          progressWidth = progress * 100;
        }
        
        moduleElements.progressBar.style.width = `${progressWidth}%`;
      }
      
      // Update status indicator
      if (moduleElements.indicator) {
        moduleElements.indicator.className = 'module-indicator ' + statusValue;
      }
      
      // Update task display
      if (moduleElements.taskElement) {
        const taskValue = typeof status === 'object' ? (status.currentTask || status.lastTask || '') : '';
        moduleElements.taskElement.textContent = taskValue;
        
        // For success status, add a check mark
        if (statusValue === 'success' && !taskValue) {
          moduleElements.taskElement.textContent = '✓';
        }
      }
      
      // Add a visual pulse effect to the module that's being updated
      if (moduleElements.container) {
        moduleElements.container.classList.add('pulse');
        setTimeout(() => {
          moduleElements.container.classList.remove('pulse');
        }, 1000);
      }
    });
  }

  /**
   * Update build output display
   * @param {string|Object|Array} output - Build output data
   */
  updateBuildOutput(output) {
    const outputText = document.getElementById('outputText');
    if (!outputText) return;
    
    const filterSelect = document.getElementById('filterOutput');
    const currentFilter = filterSelect ? filterSelect.value : 'all';
    
    if (Array.isArray(output)) {
      output.forEach(item => {
        this.appendOutputLine(item, outputText, currentFilter);
      });
    } else {
      this.appendOutputLine(output, outputText, currentFilter);
    }
    
    // Auto-scroll if enabled
    const autoScrollCheckbox = document.getElementById('autoScroll');
    if (autoScrollCheckbox && autoScrollCheckbox.checked) {
      outputText.scrollTop = outputText.scrollHeight;
    }
  }

  /**
   * Append a single line to the output display
   * @param {string|Object} line - Line to append
   * @param {HTMLElement} outputElement - Output element
   * @param {string} filter - Current filter
   */
  appendOutputLine(line, outputElement, filter) {
    // Convert string to object if needed
    let lineObj = typeof line === 'string' 
      ? { type: 'info', message: line } 
      : line;
    
    // Default to info type if not specified
    const type = lineObj.type || 'info';
    
    // Check filter
    if (filter !== 'all' && filter !== type) return;
    
    // Create formatted line
    const lineElement = document.createElement('div');
    lineElement.className = `output-line ${type}`;
    
    // Add timestamp
    const timestamp = new Date().toLocaleTimeString();
    lineElement.innerHTML = `<span class="timestamp">[${timestamp}]</span> `;
    
    // Add type badge
    const badge = document.createElement('span');
    badge.className = `badge ${type}`;
    badge.textContent = type.toUpperCase();
    lineElement.appendChild(badge);
    
    // Add message
    const message = document.createElement('span');
    message.className = 'message';
    message.textContent = ' ' + (lineObj.message || '');
    lineElement.appendChild(message);
    
    // Add to output
    outputElement.appendChild(lineElement);
  }

  /**
   * Handle notification messages
   * @param {Object|Array} notifications - Notifications data
   */
  handleNotifications(notifications) {
    if (Array.isArray(notifications)) {
      notifications.forEach(notification => {
        this.showNotification(
          notification.title || 'Notification',
          notification.message || '',
          notification.type || 'info',
          notification.timeout || 5000
        );
      });
    } else if (notifications && typeof notifications === 'object') {
      this.showNotification(
        notifications.title || 'Notification',
        notifications.message || '',
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
    if (!this.notificationsContainer) return;
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    
    // Add title
    const titleElement = document.createElement('div');
    titleElement.className = 'notification-title';
    titleElement.textContent = title;
    notification.appendChild(titleElement);
    
    // Add message
    const messageElement = document.createElement('div');
    messageElement.className = 'notification-message';
    messageElement.textContent = message;
    notification.appendChild(messageElement);
    
    // Add close button
    const closeButton = document.createElement('button');
    closeButton.className = 'notification-close';
    closeButton.innerHTML = '&times;';
    closeButton.addEventListener('click', () => {
      notification.classList.add('hiding');
      setTimeout(() => {
        if (notification.parentNode) {
          notification.parentNode.removeChild(notification);
        }
      }, 300);
    });
    notification.appendChild(closeButton);
    
    // Add to container
    this.notificationsContainer.appendChild(notification);
    
    // Auto-dismiss after timeout if specified
    if (timeout > 0) {
      setTimeout(() => {
        notification.classList.add('hiding');
        setTimeout(() => {
          if (notification.parentNode) {
            notification.parentNode.removeChild(notification);
          }
        }, 300);
      }, timeout);
    }
  }

  /**
   * Send a message to the server
   * @param {Object} message - Message to send
   */
  sendMessage(message) {
    if (!message) return;
    
    const messageStr = typeof message === 'string' 
      ? message 
      : JSON.stringify(message);
    
    if (this.isConnected && this.socket && this.socket.readyState === WebSocket.OPEN) {
      this.socket.send(messageStr);
    } else {
      // Queue message to send when connected
      this.pendingMessages.push(messageStr);
      
      // Try to reconnect if not already trying
      if (!this.isConnected && !this.reconnectTimer) {
        this.connect();
      }
    }
  }
}

/**
 * Format a duration in milliseconds to a human-readable string
 * @param {number} ms - Duration in milliseconds
 * @returns {string} Formatted duration string
 */
function formatDuration(ms) {
  const seconds = Math.floor(ms / 1000);
  const minutes = Math.floor(seconds / 60);
  const hours = Math.floor(minutes / 60);
  
  const remainingMinutes = minutes % 60;
  const remainingSeconds = seconds % 60;
  
  let result = '';
  
  if (hours > 0) {
    result += `${hours}h `;
  }
  
  if (remainingMinutes > 0 || hours > 0) {
    result += `${remainingMinutes}m `;
  }
  
  result += `${remainingSeconds}s`;
  
  return result;
}

// Initialize the build monitor when page is loaded
document.addEventListener('DOMContentLoaded', () => {
  window.buildMonitor = new BuildMonitor();
  
  // Set up event handlers for controls
  
  // Start build button
  const startBuildBtn = document.getElementById('startBuild');
  if (startBuildBtn) {
    startBuildBtn.addEventListener('click', () => {
      const buildCommandSelect = document.getElementById('buildCommand');
      const gradleCommand = buildCommandSelect ? buildCommandSelect.value : 'build';
      
      window.buildMonitor.sendMessage({
        type: 'startBuild',
        gradleCommand
      });
    });
  }
  
  // Stop build button
  const stopBuildBtn = document.getElementById('stopBuild');
  if (stopBuildBtn) {
    stopBuildBtn.addEventListener('click', () => {
      window.buildMonitor.sendMessage({
        type: 'stopBuild'
      });
    });
  }
  
  // Create checkpoint button
  const createCheckpointBtn = document.getElementById('createCheckpoint');
  if (createCheckpointBtn) {
    createCheckpointBtn.addEventListener('click', () => {
      const checkpointName = prompt('Enter a name for this checkpoint:', 
        `Checkpoint-${new Date().toISOString().slice(0, 10)}`);
      
      if (checkpointName) {
        window.buildMonitor.sendMessage({
          type: 'createCheckpoint',
          name: checkpointName,
          description: 'Manual checkpoint created from build widget'
        });
      }
    });
  }
  
  // Create release button
  const createReleaseBtn = document.getElementById('createRelease');
  if (createReleaseBtn) {
    createReleaseBtn.addEventListener('click', () => {
      if (confirm('Are you sure you want to create a GitHub release?')) {
        window.buildMonitor.sendMessage({
          type: 'createRelease'
        });
      }
    });
  }
});