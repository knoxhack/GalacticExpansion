# Galactic Expansion Pre-built JARs

This directory contains pre-built JAR files for the Galactic Expansion mod:

- `galacticcore-1.0.0.jar` - The core API module
- `galacticexpansion-energy-1.0.0.jar` - The energy systems module 
- `galacticexpansion-machinery-1.0.0.jar` - The machinery module
- `galacticexpansion-example-1.0.0.jar` - Example mod implementation

## Installation

To install the mod, place the JAR files in your Minecraft mods folder. The core module is required
for all other modules to function.

## Building from Source

If you prefer to build the mod from source, use the Gradle build system:

```bash
./gradlew build
```

This will generate JAR files for all modules in their respective `build/libs` directories.