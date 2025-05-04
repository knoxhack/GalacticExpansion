# Contributing to Galactic Expansion

Thank you for your interest in contributing to Galactic Expansion! This document provides guidelines and instructions for contributing to the project.

## Code of Conduct

By participating in this project, you agree to maintain a respectful and inclusive environment. Please be kind and considerate when interacting with other contributors and users.

## How to Contribute

### Reporting Bugs

If you find a bug, please report it by creating an issue on our GitHub repository. Please include:

1. A clear and descriptive title
2. Detailed steps to reproduce the bug
3. Expected behavior vs. actual behavior
4. Your Minecraft and mod version information
5. Crash logs or screenshots (if applicable)

### Suggesting Features

We welcome feature suggestions! To suggest a feature:

1. Check if the feature has already been suggested
2. Create a new issue with a clear description of the feature
3. Explain why this feature would be beneficial
4. Include mock-ups or diagrams if possible (for visual features)

### Pull Requests

We actively welcome pull requests! Here's how to submit one:

1. Fork the repository
2. Create a new branch for your feature or fix
3. Make your changes, following the coding standards below
4. Write tests for your changes if applicable
5. Update documentation to reflect your changes
6. Submit a pull request with a clear description of the changes

## Development Setup

1. Clone the repository
2. Import the project as a Gradle project in your IDE
3. Run `./gradlew build` to build all modules
4. Set up a test environment with `./gradlew runClient`

## Coding Standards

### Java Conventions

- Follow standard Java naming conventions
- Use 4 spaces for indentation (not tabs)
- Keep lines under 120 characters when possible
- Document public methods with JavaDoc

### Minecraft-specific Guidelines

- Register items, blocks, etc. in appropriate registry events
- Follow NeoForge's recommended patterns for registration
- Use capability system appropriately for complex functionality
- Keep performance in mind - space is vast, but computing resources aren't!

### Commit Guidelines

- Use clear, descriptive commit messages
- Begin with a verb in imperative mood (Add, Fix, Update, etc.)
- Reference issue numbers when applicable

## Module Structure

When contributing to a specific module, please respect its intended purpose:

- **Core**: Common functionality used by all modules
- **Power**: Energy generation and transfer
- **Machinery**: Processing and automation
- **Space**: Space travel and planetary systems
- **Biotech**: Organic systems and life support

If your contribution spans multiple modules, consider how the modules should interact via well-defined APIs.

## Testing

Please test your changes thoroughly before submitting:

1. Test single-player functionality
2. If possible, test in a multiplayer environment
3. Verify compatibility with existing features
4. Check for performance impacts

## Documentation

Update documentation when adding or changing features:

- Update README.md for major feature changes
- Add JavaDoc comments to new methods and classes
- Update in-game documentation if applicable

## Community

Join our community to discuss development:

- Discord server: [discord.example.com/galactic-expansion](https://discord.example.com/galactic-expansion)
- Development forum: [forum.example.com](https://forum.example.com)

## License

By contributing to Galactic Expansion, you agree that your contributions will be licensed under the project's MIT License.

Thank you for helping make Galactic Expansion the best space mod for Minecraft!