# Galactic Expansion Pre-built JARs

This directory contains pre-built JAR files for the Galactic Expansion mod:

- `galacticcore-1.0.0.jar` - The core API module
- `galacticexpansion-energy-1.0.0.jar` - The energy systems module 
- `galacticexpansion-machinery-1.0.0.jar` - The machinery module
- `galacticexpansion-example-1.0.0.jar` - Example mod implementation
- `galacticexpansion-all-1.0.0.jar` - All modules combined into a single JAR

## Installation

To install the mod, place the JAR files in your Minecraft mods folder. The core module is required
for all other modules to function. Alternatively, you can use the combined JAR file
`galacticexpansion-all-1.0.0.jar` which includes all modules.

## Testing

You can test the combined JAR file by running:

```bash
java -jar galacticexpansion-all-1.0.0.jar
```

This will run the example mod's main method, demonstrating energy transfer between machines.

## Module Dependencies

The dependency structure is as follows:

```
core <-- energy <-- machinery <-- example
```

Each module depends on all modules to its left in the diagram.

## Building from Source

If you prefer to build the mod from source, use the Gradle build system:

```bash
./gradlew build
```

This will generate JAR files for all modules in their respective `build/libs` directories.