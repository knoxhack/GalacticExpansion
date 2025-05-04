# Galactic Expansion JAR Files

This mod is packaged in multiple formats to accommodate different installation preferences.

## JAR File Types

### Individual Module JARs
Each module is packaged as a separate JAR file, which allows you to install only the modules you want:

- `core_galacticcore-0.1.0.jar` - Core API and utilities (required by all other modules)
- `biotech_galacticbiotech-0.1.0.jar` - Biological technology
- `energy_galacticenergy-0.1.0.jar` - Energy generation and storage
- `machinery_galacticmachinery-0.1.0.jar` - Various machines and mechanisms
- `power_galacticpower-0.1.0.jar` - Power transfer and distribution
- `space_galacticspace-0.1.0.jar` - Space exploration and planets
- `robotics_galacticrobotics-0.1.0.jar` - Robots and automation
- `construction_galacticconstruction-0.1.0.jar` - Building tools and materials
- `utilities_galacticutilities-0.1.0.jar` - Miscellaneous utility items
- `vehicles_galacticvehicles-0.1.0.jar` - Various vehicles and transportation
- `weaponry_galacticweaponry-0.1.0.jar` - Weapons and defensive systems

### All-in-One JAR
For convenience, we also provide an all-in-one JAR file that includes all modules in a single file:

- `galacticexpansion_all-in-one-0.1.0.jar` - Contains all modules in one JAR

This all-in-one JAR exposes a unified modid `galacticexpansion` which replaces the individual module IDs.

#### Technical Details

The all-in-one JAR has been designed to avoid conflicts between modules by:
- Using a single unified modid `galacticexpansion` instead of individual module IDs
- Disabling individual module mods.toml files
- Creating a unified configuration at the JAR root level
- Providing proper asset paths for all modules

This approach eliminates dependency errors that would occur if modules tried to reference each other.

## Installation

### Using Individual Modules
1. Install NeoForge 1.21.5 for Minecraft 1.21
2. Place the `core_galacticcore-0.1.0.jar` in your Minecraft mods folder
3. Add any additional module JARs that you want to use

### Using All-in-One JAR
1. Install NeoForge 1.21.5 for Minecraft 1.21
2. Place the `galacticexpansion_all-in-one-0.1.0.jar` in your Minecraft mods folder
   - You don't need any of the individual module JARs when using the all-in-one JAR

## Notes
- The all-in-one JAR is the easiest way to install the complete mod
- Using individual module JARs gives you more control over which features are installed
- The core module is required regardless of which other modules you install
- All JARs are compatible with NeoForge 1.21.5 for Minecraft 1.21

## Important
- **DO NOT** mix the all-in-one JAR with individual module JARs. This will cause conflicts.
- Choose either the all-in-one JAR *OR* the individual module JARs approach, not both.
- If you were previously using individual module JARs, remove them all before installing the all-in-one JAR.