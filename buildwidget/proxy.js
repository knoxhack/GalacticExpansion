/**
 * Proxy server for Build Status Widget
 * Routes traffic from public port 5000 to internal port 5004
 */

const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');
const http = require('http');

// Create Express server
const app = express();
const server = http.createServer(app);

// Serve static files directly
app.use(express.static(path.join(__dirname, 'public')));

// Configure proxy with improved WebSocket handling
const wsProxy = createProxyMiddleware({
  target: 'http://localhost:5004',
  ws: true,  // Enable WebSocket proxying
  changeOrigin: true,
  logLevel: 'debug', // Add debug logging to help troubleshoot
  pathRewrite: {
    '^/api': '/api',  // Keep API routes as-is
    '^/ws': '/ws'     // Keep WebSocket route as-is
  },
  onError: (err, req, res) => {
    console.error('Proxy error:', err);
    if (res && !res.headersSent) {
      res.writeHead(500, { 'Content-Type': 'application/json' });
      res.end(JSON.stringify({ error: 'Proxy error', message: err.message }));
    }
  }
});

// Apply the proxy middleware
app.use('/', wsProxy);

// Mount the WebSocket proxy at the server level for better handling
server.on('upgrade', wsProxy.upgrade);

// Start the proxy server
const PORT = process.env.PORT || 5000;
server.listen(PORT, '0.0.0.0', () => {
  console.log(`Proxy server running on port ${PORT}, forwarding to internal port 5004`);
  console.log(`WebSocket proxy enabled and listening for connections`);
});