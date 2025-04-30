// Simple script to start the build status widget server
const { spawn } = require('child_process');
const path = require('path');

// Start the server
console.log('Starting build status widget server...');
const server = spawn('node', [path.join(__dirname, 'server.js')], {
  stdio: 'inherit'
});

server.on('close', (code) => {
  console.log(`Server process exited with code ${code}`);
});

// Handle termination signals
process.on('SIGINT', () => {
  console.log('Stopping server...');
  server.kill('SIGINT');
  process.exit(0);
});

process.on('SIGTERM', () => {
  console.log('Stopping server...');
  server.kill('SIGTERM');
  process.exit(0);
});