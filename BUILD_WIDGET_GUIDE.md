# Build Status Widget Guide

This guide explains how to use the Build Status Widget for monitoring and managing mod build processes.

## Overview

The Build Status Widget provides real-time feedback on mod builds, including:

- Current build status for each module
- Build output logs
- Build metrics and performance
- Version information
- Checkpoint status

## Setup

The Build Status Widget consists of two components:

1. **Status Server** - Displays build status and provides the web interface
2. **Proxy Server** - Routes traffic from port 5000 to the internal widget port

### Starting the Widget

The widget can be started using the provided workflows:

1. Use the "Build Status Widget" workflow to start the main widget server
2. Use the "Build Widget Proxy" workflow to start the proxy server

Both components must be running for the widget to function correctly.

## Using the Widget

### Monitoring Builds

The widget displays real-time information about:

- Which modules are currently building
- Success or failure status
- Build durations and trends
- Recent build output

### Starting Builds

You can trigger builds directly from the widget by:

1. Clicking the "Build" button
2. Selecting which modules to build
3. Choosing build options (clean, tests, etc.)

### Viewing Build Logs

The widget captures and displays build logs in real-time:

- Output is color-coded for easier reading
- Errors and warnings are highlighted
- You can filter output by module or severity

## Troubleshooting

### Widget Connection Issues

If you see WebSocket connection errors:

1. **Check Both Services Are Running**
   - Verify both "Build Status Widget" and "Build Widget Proxy" workflows are active
   - Restart both services if needed

2. **WebSocket Timeout Errors**
   ```
   WebSocket error: {isTrusted:true}
   WebSocket connection timed out
   ```
   
   This usually indicates one of:
   - The widget server is overloaded during a large build
   - Network issues between the proxy and server
   - Memory pressure causing service instability
   
   **Solutions:**
   - Restart both services
   - Wait until any large builds complete
   - Close other resource-intensive applications

3. **Server Connection Refused**
   
   If the widget shows "Connection refused" errors:
   - Ensure the main server is running on the expected port
   - Check for port conflicts with other applications
   - Verify firewall settings aren't blocking the connection

### Build Monitoring Issues

1. **Missing Module Updates**
   
   If module updates aren't showing:
   - Check the module paths are correct in the configuration
   - Verify the module is included in the current build
   - Look for build errors specific to that module

2. **Incorrect Status Information**
   
   If status information is wrong or outdated:
   - Click the "Refresh" button to manually update status
   - Check timestamp of last updates
   - Restart the widget service if information is stale

## Advanced Usage

### Custom Build Commands

You can customize build commands through the widget:

1. Click "Custom Build"
2. Enter specific Gradle commands
3. Select target modules
4. Click "Execute"

### Checkpoint Management

Use checkpoints to mark significant development milestones:

1. Click "Create Checkpoint"
2. Enter a descriptive name and details
3. The system will capture current build state

### Build Release Process

To create a release build:

1. Ensure all tests pass
2. Click "Release Build"
3. Enter version information
4. The system will create versioned JAR files
5. Optional: Publish to distribution platforms

## Widget API Endpoints

For developers who want to interact with the widget programmatically:

- `/api/status` - Get current build status (GET)
- `/api/build` - Trigger a build (POST)
- `/api/logs` - Get build logs (GET)
- `/api/metrics` - Get build metrics (GET)
- `/api/checkpoint` - Create a checkpoint (POST)

## Configuration

The widget can be configured by editing:

```
buildwidget/config.js
```

Important settings include:

- `port`: The internal port used by the widget server
- `proxyPort`: The public-facing port (usually 5000)
- `buildCommand`: Default Gradle command for builds
- `modulePaths`: Paths to different modules
- `refreshInterval`: How often to refresh status (ms)
- `logRetention`: How long to keep build logs
- `websocketTimeout`: WebSocket connection timeout

## Notes for Developers

When extending the Build Widget:

- The widget uses a WebSocket connection for real-time updates
- Data is sent in JSON format between components
- Build progress is monitored by parsing Gradle output
- Status updates are broadcast to all connected clients
- The widget proxy handles HTTP->WebSocket protocol upgrades