const express = require('express');
const http = require('http');
const path = require('path');
const fs = require('fs');
const { WebSocketServer } = require('ws');
const { exec } = require('child_process');
const { promisify } = require('util');
const execAsync = promisify(exec);

// Initialize express app
const app = express();
const server = http.createServer(app);
const wss = new WebSocketServer({ server, path: '/ws' });

// Serve static files from the public directory
app.use(express.static(path.join(__dirname, 'public')));

// Store build status information
let buildStatus = {
  status: 'idle',
  progress: 0,
  tasks: {},
  modules: {
    core: { status: 'pending' },
    energy: { status: 'pending' },
    machinery: { status: 'pending' },
    example: { status: 'pending' }
  },
  lastUpdate: new Date().toISOString(),
  buildOutput: []
};

// Handle WebSocket connections
wss.on('connection', (ws) => {
  console.log('Client connected');
  
  // Send current build status to client
  const sendStatus = () => {
    if (ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({ type: 'status', data: buildStatus }));
    }
  };
  
  // Send initial status
  sendStatus();
  
  // Handle messages from clients
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);
      
      if (data.type === 'startBuild') {
        startBuild(data.gradleCommand || 'clean build');
      } else if (data.type === 'stopBuild') {
        stopBuild();
      } else if (data.type === 'requestStatus') {
        sendStatus();
      }
    } catch (err) {
      console.error('Error handling message:', err);
    }
  });
  
  // Handle client disconnection
  ws.on('close', () => {
    console.log('Client disconnected');
  });
});

// Start Gradle build
async function startBuild(gradleCommand) {
  // Reset build status
  buildStatus = {
    status: 'building',
    progress: 0,
    startTime: new Date().toISOString(),
    tasks: {},
    modules: {
      core: { status: 'pending' },
      energy: { status: 'pending' },
      machinery: { status: 'pending' },
      example: { status: 'pending' }
    },
    lastUpdate: new Date().toISOString(),
    buildOutput: []
  };
  
  // Broadcast status update
  broadcastStatus();
  
  try {
    // Execute Gradle build with output stream
    const gradleProcess = exec(`cd .. && export JAVA_HOME=/nix/store/sziqmjk1i28cxcr5x29jbz3dzhiz1pii-openjdk-headless-21+35 && ./gradlew ${gradleCommand} --console=plain`);
    
    // Process stdout in real-time
    gradleProcess.stdout.on('data', (data) => {
      processGradleOutput(data.toString());
    });
    
    // Process stderr in real-time
    gradleProcess.stderr.on('data', (data) => {
      const output = data.toString();
      buildStatus.buildOutput.push({ type: 'error', message: output });
      broadcastStatus();
    });
    
    // Handle build completion
    gradleProcess.on('close', (code) => {
      buildStatus.status = code === 0 ? 'success' : 'failed';
      buildStatus.exitCode = code;
      buildStatus.endTime = new Date().toISOString();
      buildStatus.progress = 100;
      buildStatus.lastUpdate = new Date().toISOString();
      broadcastStatus();
    });
  } catch (error) {
    buildStatus.status = 'failed';
    buildStatus.error = error.message;
    buildStatus.endTime = new Date().toISOString();
    buildStatus.lastUpdate = new Date().toISOString();
    broadcastStatus();
  }
}

// Process Gradle output and extract build status information
function processGradleOutput(output) {
  // Add to build output
  buildStatus.buildOutput.push({ type: 'info', message: output });
  
  // Update last update timestamp
  buildStatus.lastUpdate = new Date().toISOString();
  
  // Parse modules and tasks
  const taskMatch = output.match(/> Task :(\w+):(\w+)/);
  if (taskMatch && taskMatch.length >= 3) {
    const module = taskMatch[1];
    const task = taskMatch[2];
    
    // Update module status
    if (buildStatus.modules[module]) {
      buildStatus.modules[module].status = 'building';
      buildStatus.modules[module].currentTask = task;
      buildStatus.modules[module].lastUpdate = new Date().toISOString();
    }
    
    // Track task
    const taskKey = `${module}:${task}`;
    buildStatus.tasks[taskKey] = {
      status: 'running',
      module: module,
      task: task,
      startTime: new Date().toISOString()
    };
  }
  
  // Check for task completion
  const taskCompleteMatch = output.match(/> Task :(\w+):(\w+) (UP-TO-DATE|FROM-CACHE|SKIPPED|FAILED|NO-SOURCE)/);
  if (taskCompleteMatch && taskCompleteMatch.length >= 4) {
    const module = taskCompleteMatch[1];
    const task = taskCompleteMatch[2];
    const status = taskCompleteMatch[3].toLowerCase();
    
    // Update task status
    const taskKey = `${module}:${task}`;
    if (buildStatus.tasks[taskKey]) {
      buildStatus.tasks[taskKey].status = status === 'failed' ? 'failed' : 'success';
      buildStatus.tasks[taskKey].endTime = new Date().toISOString();
    }
    
    // Check if module is complete
    if (task === 'build' || task === 'assemble') {
      if (buildStatus.modules[module]) {
        buildStatus.modules[module].status = status === 'failed' ? 'failed' : 'success';
      }
    }
  }
  
  // Look for build summary to update progress
  if (output.includes('actionable tasks:')) {
    const progressMatch = output.match(/(\d+) actionable tasks: (\d+) executed/);
    if (progressMatch && progressMatch.length >= 3) {
      const total = parseInt(progressMatch[1], 10);
      const executed = parseInt(progressMatch[2], 10);
      buildStatus.progress = Math.floor((executed / total) * 100);
    }
  }
  
  // Check if any module failed
  if (output.includes('BUILD FAILED')) {
    buildStatus.status = 'failed';
  }
  
  // Check for build success
  if (output.includes('BUILD SUCCESSFUL')) {
    buildStatus.status = 'success';
    buildStatus.progress = 100;
  }
  
  // Broadcast updated status
  broadcastStatus();
}

// Stop ongoing build
function stopBuild() {
  // This implementation varies by platform, here's a basic version
  exec('pkill -f gradlew');
  
  buildStatus.status = 'stopped';
  buildStatus.endTime = new Date().toISOString();
  buildStatus.lastUpdate = new Date().toISOString();
  
  broadcastStatus();
}

// Broadcast build status to all connected clients
function broadcastStatus() {
  wss.clients.forEach((client) => {
    if (client.readyState === client.OPEN) {
      client.send(JSON.stringify({ type: 'status', data: buildStatus }));
    }
  });
}

// API endpoint to get current build status
app.get('/api/status', (req, res) => {
  res.json(buildStatus);
});

// API endpoint to trigger a new build
app.post('/api/build/start', express.json(), (req, res) => {
  const gradleCommand = req.body.gradleCommand || 'clean build';
  startBuild(gradleCommand);
  res.json({ success: true, message: 'Build started', command: gradleCommand });
});

// API endpoint to stop current build
app.post('/api/build/stop', (req, res) => {
  stopBuild();
  res.json({ success: true, message: 'Build stopped' });
});

// Serve the main HTML page
app.get('/', (req, res) => {
  res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

// Monitor build status in real-time
function startMonitoringBuildStatus() {
  // Poll Gradle's build status every 2 seconds
  setInterval(async () => {
    try {
      // Check if any build processes are running using a more reliable method
      const { stdout } = await execAsync('pgrep java || echo ""');
      
      if (stdout && stdout.trim() !== '') {
        // Gradle is running, update status if not already building
        if (buildStatus.status !== 'building') {
          buildStatus.status = 'building';
          buildStatus.startTime = new Date().toISOString();
          buildStatus.lastUpdate = new Date().toISOString();
          buildStatus.progress = Math.floor(Math.random() * 50); // Approximate progress
          broadcastStatus();
        }
        
        // Increment progress if building
        if (buildStatus.progress < 90) {
          buildStatus.progress += 1;
          buildStatus.lastUpdate = new Date().toISOString();
          broadcastStatus();
        }
      } else {
        // No Gradle process running
        if (buildStatus.status === 'building') {
          // Build just finished
          const buildSuccessful = await checkBuildSuccess();
          buildStatus.status = buildSuccessful ? 'success' : 'failed';
          buildStatus.endTime = new Date().toISOString();
          buildStatus.progress = 100;
          buildStatus.lastUpdate = new Date().toISOString();
          broadcastStatus();
        }
      }
    } catch (error) {
      console.error('Error monitoring build status:', error);
    }
  }, 2000);
}

// Check if the build was successful
async function checkBuildSuccess() {
  try {
    // Check for build failures in recent logs
    const { stdout } = await execAsync('grep -i "BUILD FAILED" $(find ../ -name "*.log" -ctime -1 2>/dev/null) 2>/dev/null || echo "No failures found"');
    return !stdout.includes('BUILD FAILED');
  } catch (error) {
    // If grep fails, assume build succeeded
    return true;
  }
}

// Start the server
const PORT = process.env.PORT || 5000;
server.listen(PORT, '0.0.0.0', () => {
  console.log(`Build status server running on port ${PORT}`);
  
  // Start monitoring build status
  startMonitoringBuildStatus();
});