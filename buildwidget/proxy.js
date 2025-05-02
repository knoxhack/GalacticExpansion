/**
 * Proxy server for Build Status Widget
 * Routes traffic from public port 5000 to internal port 5001
 */

const express = require('express');
const { createProxyMiddleware } = require('http-proxy-middleware');
const path = require('path');

// Create Express server
const app = express();

// Serve static files directly
app.use(express.static(path.join(__dirname, 'public')));

// Proxy all requests to the internal server
app.use('/', createProxyMiddleware({
  target: 'http://localhost:5004', // Updated to match server.js port
  ws: true,  // Enable WebSocket proxying
  changeOrigin: true,
  pathRewrite: {
    '^/api': '/api',  // Keep API routes as-is
    '^/ws': '/ws'     // Keep WebSocket route as-is
  }
}));

// Start the proxy server
const PORT = process.env.PORT || 5000;
app.listen(PORT, '0.0.0.0', () => {
  console.log(`Proxy server running on port ${PORT}, forwarding to internal port 5004`);
});