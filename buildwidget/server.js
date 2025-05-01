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
const wss = new WebSocketServer({ 
  server, 
  path: '/ws',
  clientTracking: true 
});

// Serve static files from the public directory
app.use(express.static(path.join(__dirname, 'public')));

// Store build status information
let buildStatus = {
  status: 'idle',
  progress: 0,
  tasks: {},
  modules: {
    core: { status: 'pending', lastBuild: null },
    power: { status: 'pending', lastBuild: null },
    machinery: { status: 'pending', lastBuild: null },
    biotech: { status: 'pending', lastBuild: null },
    energy: { status: 'pending', lastBuild: null },
    construction: { status: 'pending', lastBuild: null },
    space: { status: 'pending', lastBuild: null },
    utilities: { status: 'pending', lastBuild: null },
    vehicles: { status: 'pending', lastBuild: null },
    weaponry: { status: 'pending', lastBuild: null },
    robotics: { status: 'pending', lastBuild: null }
  },
  lastUpdate: new Date().toISOString(),
  buildOutput: []
};

// Build metrics storage
const buildHistory = [];
const buildMetrics = {
  totalBuilds: 0,
  successfulBuilds: 0,
  averageBuildTime: 0,
  lastSuccessful: null,
  currentBuild: null
};

// Store connected clients with their handlers
const clients = new Map();

// Handle WebSocket connections
wss.on('connection', (ws) => {
  console.log('Client connected');
  
  // Send current build status to client
  const sendStatus = () => {
    if (ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({ type: 'status', data: buildStatus }));
    }
  };
  
  // Send build output to client
  const sendBuildOutput = (output) => {
    if (ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({ type: 'buildOutput', data: output }));
    }
  };
  
  // Send task updates to client
  const sendTaskUpdates = (tasks) => {
    if (ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({ type: 'tasks', data: tasks }));
    }
  };
  
  // Send error message to client
  const sendError = (errorMessage) => {
    if (ws.readyState === ws.OPEN) {
      ws.send(JSON.stringify({ type: 'error', data: errorMessage }));
    }
  };
  
  // Send initial status
  sendStatus();
  
  // Handle messages from clients
  ws.on('message', (message) => {
    try {
      const data = JSON.parse(message);
      
      if (data.type === 'startBuild') {
        startBuild(data.gradleCommand || 'clean build')
          .catch(error => sendError(`Build failed: ${error.message}`));
      } else if (data.type === 'stopBuild') {
        stopBuild();
      } else if (data.type === 'requestStatus') {
        sendStatus();
      } else if (data.type === 'requestOutput') {
        sendBuildOutput(buildStatus.buildOutput);
      } else if (data.type === 'requestTasks') {
        sendTaskUpdates(buildStatus.tasks);
      }
    } catch (err) {
      console.error('Error handling message:', err);
      sendError(`Error processing request: ${err.message}`);
    }
  });
  
  // Handle client disconnection
  ws.on('close', () => {
    console.log('Client disconnected');
    clients.delete(ws);
  });
  
  // Store client functions for broadcasting
  clients.set(ws, {
    sendStatus,
    sendBuildOutput,
    sendTaskUpdates,
    sendError
  });
});

// Start Gradle build
async function startBuild(gradleCommand) {
  // Reset build status
  const now = new Date();
  buildStatus = {
    status: 'building',
    progress: 0,
    startTime: now.toISOString(),
    tasks: {},
    modules: {
      core: { status: 'pending', lastUpdate: now.toISOString() },
      power: { status: 'pending', lastUpdate: now.toISOString() },
      machinery: { status: 'pending', lastUpdate: now.toISOString() },
      biotech: { status: 'pending', lastUpdate: now.toISOString() },
      construction: { status: 'pending', lastUpdate: now.toISOString() },
      robotics: { status: 'pending', lastUpdate: now.toISOString() },
      space: { status: 'pending', lastUpdate: now.toISOString() },
      utilities: { status: 'pending', lastUpdate: now.toISOString() },
      vehicles: { status: 'pending', lastUpdate: now.toISOString() },
      weaponry: { status: 'pending', lastUpdate: now.toISOString() }
    },
    lastUpdate: now.toISOString(),
    buildOutput: []
  };
  
  // Broadcast status update
  broadcastStatus();
  
  try {
    // Execute build script or Gradle command directly
    const useScript = gradleCommand === 'clean build' || gradleCommand === 'build';
    const buildCommand = useScript 
      ? `cd .. && ./scripts/build_all_modules.sh` 
      : `cd .. && export JAVA_HOME=/nix/store/sziqmjk1i28cxcr5x29jbz3dzhiz1pii-openjdk-headless-21+35 && ./gradlew ${gradleCommand} --console=plain`;
    
    console.log(`Executing build command: ${buildCommand}`);
    const gradleProcess = exec(buildCommand);
    
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
      
      // If build was successful, push to GitHub
      if (code === 0) {
        console.log('Build successful! Pushing JAR files to GitHub...');
        
        // Execute push script
        const pushProcess = exec('cd .. && ./scripts/push_jars_to_github.sh');
        
        // Process push script output
        pushProcess.stdout.on('data', (data) => {
          const output = data.toString();
          buildStatus.buildOutput.push({ 
            type: 'info', 
            message: `[GitHub Push] ${output}`
          });
          broadcastOutput([{ type: 'info', message: `[GitHub Push] ${output}` }]);
        });
        
        pushProcess.stderr.on('data', (data) => {
          const output = data.toString();
          buildStatus.buildOutput.push({ 
            type: 'error', 
            message: `[GitHub Push Error] ${output}`
          });
          broadcastOutput([{ type: 'error', message: `[GitHub Push Error] ${output}` }]);
        });
        
        pushProcess.on('close', (pushCode) => {
          if (pushCode === 0) {
            const successMessage = 'Successfully pushed JAR files to GitHub!';
            buildStatus.buildOutput.push({ 
              type: 'info', 
              message: `[GitHub Push] ${successMessage}`
            });
            broadcastOutput([{ type: 'info', message: `[GitHub Push] ${successMessage}` }]);
          } else {
            const errorMessage = `Failed to push JAR files to GitHub (exit code: ${pushCode})`;
            buildStatus.buildOutput.push({ 
              type: 'error', 
              message: `[GitHub Push Error] ${errorMessage}`
            });
            broadcastOutput([{ type: 'error', message: `[GitHub Push Error] ${errorMessage}` }]);
          }
        });
      }
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
  // Create output line
  const outputLine = { type: 'info', message: output };
  
  // Add to build output
  buildStatus.buildOutput.push(outputLine);
  
  // Update last update timestamp
  buildStatus.lastUpdate = new Date().toISOString();
  
  // Broadcast just the new output line for efficiency
  broadcastOutput([outputLine]);
  
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
  // Broadcast full status update to all connected clients
  clients.forEach((handlers, client) => {
    if (client.readyState === client.OPEN) {
      handlers.sendStatus();
    }
  });
  
  // Also send directly to all clients using WebSocketServer's client tracking
  wss.clients.forEach(client => {
    if (client.readyState === client.OPEN) {
      client.send(JSON.stringify({ type: 'status', data: buildStatus }));
    }
  });
}

// Broadcast build output only
function broadcastOutput(output) {
  clients.forEach((handlers, client) => {
    if (client.readyState === client.OPEN) {
      handlers.sendBuildOutput(output);
    }
  });
}

// Broadcast error message
function broadcastError(errorMessage) {
  clients.forEach((handlers, client) => {
    if (client.readyState === client.OPEN) {
      handlers.sendError(errorMessage);
    }
  });
}

// Broadcast build metrics to all clients
function broadcastMetrics() {
  // Calculate success rate
  const successRate = buildMetrics.totalBuilds > 0 
    ? Math.round((buildMetrics.successfulBuilds / buildMetrics.totalBuilds) * 100) 
    : 0;
  
  // Format time
  const formatTime = (seconds) => {
    if (seconds < 60) return `${Math.round(seconds)}s`;
    return `${Math.floor(seconds / 60)}m ${Math.round(seconds % 60)}s`;
  };
  
  // Format date
  const formatDate = (date) => {
    if (!date) return 'Never';
    return new Date(date).toLocaleString();
  };
  
  // Prepare metrics data
  const metricsData = {
    totalBuilds: buildMetrics.totalBuilds,
    successRate: `${successRate}%`,
    averageBuildTime: formatTime(buildMetrics.averageBuildTime || 0),
    lastSuccessful: formatDate(buildMetrics.lastSuccessful),
    buildHistory: buildHistory.slice(-10).map(build => ({
      id: build.id,
      command: build.command,
      status: build.status,
      startTime: build.startTime,
      duration: formatTime(build.duration || 0)
    }))
  };
  
  // Send to all clients - both ways to ensure delivery
  clients.forEach((handlers, client) => {
    if (client.readyState === client.OPEN) {
      handlers.sendStatus();
    }
  });
  
  // Also send to all clients using the WSS clients collection
  wss.clients.forEach(client => {
    if (client.readyState === client.OPEN) {
      client.send(JSON.stringify({ type: 'metrics', data: metricsData }));
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
          buildStatus.progress = 0; // Start at 0%
          broadcastStatus();
        }
        
        // Calculate more accurate progress based on time elapsed
        if (buildStatus.progress < 99) {
          // Get elapsed time in seconds
          const startTime = new Date(buildStatus.startTime);
          const now = new Date();
          const elapsedSeconds = Math.floor((now - startTime) / 1000);
          
          // Estimate progress using a logarithmic curve that approaches 99% 
          // but never reaches 100% until completion
          const estimatedProgress = Math.min(
            99, 
            Math.floor(30 * Math.log10(1 + elapsedSeconds / 2))
          );
          
          if (estimatedProgress > buildStatus.progress) {
            buildStatus.progress = estimatedProgress;
            buildStatus.lastUpdate = new Date().toISOString();
            broadcastStatus();
          }
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
    
    // Also check for compilation errors
    const compileErrors = await execAsync('grep -i "compilation failed" $(find ../ -name "*.log" -ctime -1 2>/dev/null) 2>/dev/null || echo "No failures found"');
    
    return !stdout.includes('BUILD FAILED') && !compileErrors.stdout.includes('compilation failed');
  } catch (error) {
    // If grep fails, assume build succeeded
    return true;
  }
}

// Start the server
const PORT = process.env.PORT || 5001;
server.listen(PORT, '0.0.0.0', () => {
  console.log(`Build status server running on port ${PORT}`);
  
  // Start monitoring build status
  startMonitoringBuildStatus();
});