# Galactic Expansion

A modular Minecraft mod API for Neoforge 1.21.5, designed with a focus on extensibility and developer experience. Supports multiple modules including core, energy, and machinery systems with robust integration capabilities.

## Features

- **Modular Architecture**: Clean separation between modules through interface-based design
- **Core Registry System**: Easy registration of mod components with annotation-based system
- **Tag System**: Flexible tagging of objects for dynamic grouping and discovery
- **Energy System**: Extensible energy framework with multiple energy types
- **Machinery Framework**: Base implementations for various machine types
- **Developer API**: Comprehensive API for extending the mod

## Modules

### Core

The foundation module containing the registry system, tag framework, and utilities.

- Annotation-based registration with `@Register`
- Tag system with `@TaggedWith` for flexible object categorization
- Registry helpers for easy component registration

### Energy

Handles energy generation, storage, and transfer with support for multiple energy types.

- Defines energy types (Electrical, Steam, etc.)
- Energy storage interface and implementations
- Energy network for connecting components

### Machinery

Provides machine abstractions and implementations.

- Base machine framework
- Specialized machine types (Generator, Processor)
- Machine registry and factory methods

### Example

Demonstrates the usage of all modules together.

- Shows integration between energy and machinery
- Provides complete examples of registry usage
- Demonstrates tag system for machine discovery

## Development

### Setup

1. Clone the repository
2. Import into your IDE as a Gradle project
3. Run `./gradlew build` to build all modules

### Versioning

The project uses semantic versioning. Version management can be done with:

```
./gradlew bumpMajorVersion   # For x.0.0
./gradlew bumpMinorVersion   # For 0.x.0
./gradlew bumpPatchVersion   # For 0.0.x
./gradlew showCurrentVersion # Show current version
```

### GitHub Integration

The project includes scripts for GitHub integration:

- `./scripts/push-to-github.sh` - Push changes to GitHub
- `./scripts/github-release.sh` - Create a GitHub release

See the [scripts documentation](./scripts/README.md) for more details.

## Building

Build all modules:
```
./gradlew clean build
```

Build a specific module:
```
./gradlew :core:build
./gradlew :energy:build
./gradlew :machinery:build
./gradlew :example:build
```

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- The Neoforge team for the modding framework
- The Minecraft modding community for inspiration and support