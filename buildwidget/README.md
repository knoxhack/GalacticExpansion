# Build Status Widget

A real-time build status monitoring system for the Galactic Expansion mod project. This tool provides a web interface to monitor build progress, view build logs, and track build status.

## Features

- Real-time build status monitoring
- WebSocket-based updates for instant feedback
- Build time tracking
- Task list display
- Build output log streaming
- Clean, responsive web interface

## Architecture

The build status widget consists of:

1. **Server Component**: Node.js server with Express and Socket.io
2. **Client Component**: HTML/CSS/JavaScript frontend
3. **Gradle Plugin**: Custom Gradle plugin to report build events

The Gradle plugin communicates with the Node.js server, which then updates connected clients via WebSockets.

## Getting Started

### Prerequisites

- Node.js 18 or later
- npm or yarn
- Gradle 8.12 or later

### Installation

1. Install Node.js dependencies:
   ```
   cd buildwidget
   npm install
   ```

2. Start the server:
   ```
   node server.js
   ```

3. The build status widget will be accessible at: [http://localhost:5000](http://localhost:5000)

## Usage

The build status widget starts automatically when you run Gradle builds. The Gradle plugin sends build events to the server, which then updates the web interface.

### Viewing Build Status

Open [http://localhost:5000](http://localhost:5000) in your browser to view the build status.

### Build Events

The following build events are tracked:

- Build started
- Build success
- Build failure
- Task execution
- Build progress

### Configuration

The build status widget can be configured in the root `build.gradle` file:

```groovy
buildStatusWidget {
    serverUrl = 'http://localhost:5000' // Default URL
    // Additional configuration options
}
```

## Development

### Server Component

The server component (`server.js`) is responsible for:

- Serving the web interface
- Accepting build status updates from Gradle
- Broadcasting updates to connected clients via WebSockets

### Client Component

The client component (`public/index.html`, `public/style.css`, `public/script.js`) provides:

- Visual representation of build status
- Real-time updates via WebSockets
- Build output log display
- Task list and progress display

### Gradle Plugin

The Gradle plugin (`buildSrc/src/main/groovy/com/astroframe/galactic/gradle/BuildStatusPlugin.groovy`) adds build event listeners to Gradle to report:

- Build start and completion
- Task execution
- Build progress

## Contributing

Contributions to improve the build status widget are welcome:

1. Enhance the UI
2. Add more build metrics
3. Improve WebSocket reliability
4. Add authentication for multi-user environments

## License

This component is covered by the main project license (MIT).