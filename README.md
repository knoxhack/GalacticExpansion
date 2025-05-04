# Galactic Expansion

A cutting-edge Minecraft mod for NeoForge 1.21.5 that revolutionizes space exploration with advanced technology, machinery, and interplanetary travel. Featuring a highly modular design with specialized components, Galactic Expansion introduces new gameplay mechanics while maintaining excellent performance and compatibility.

![Galactic Expansion Banner](attached_assets/20250429_1105_Galactic%20Expansion%20Banner_simple_compose_01jt115j8vfkj9ydj0phb768jv.png)

## Features

- **Advanced Space Exploration**: Travel to planets, moons, and deep space using multi-tiered rocket systems
- **Modular Architecture**: Clean separation between systems with specialized components and interfaces
- **High-Tech Machinery**: Automated processing, assembly, and manufacturing systems
- **Specialized Space Equipment**: Space suits, oxygen systems, and radiation protection gear
- **Energy Production & Distribution**: Comprehensive power generation and transfer network
- **Biotech Systems**: Sustainable food production and life support systems
- **Weapons & Defense Systems**: Technology to protect your space colonies and bases
- **Futuristic Construction**: Specialized building blocks and materials for space bases
- **Robotics & Automation**: Programmable robots to automate base operations
- **Resource Management**: Mine, process, and refine space resources
- **Vehicle Systems**: Specialized space and planetary exploration vehicles

## Modules

### Core

The foundational module that contains common functionality and APIs used by all other modules.

- Base registry system for items, blocks, and mechanics
- Advanced player data attachment system for tracking space-related player stats
- Tag framework for flexible object categorization
- Cross-module communication system 
- Enhanced resource management system
- Comprehensive utility classes for developers
- Machine base template with standardized textures

### Power

Energy generation, storage, and transfer systems that power all mod machinery.

- Multiple power generation methods (solar, nuclear, fusion)
- Energy storage blocks with tiered capacities
- Advanced power transfer networks
- Energy transformation and conversion systems
- Wireless power transmission for space applications
- Power monitoring and management interfaces

### Machinery

Advanced processing machines and automation systems.

- Assembler machine for component fabrication
- Processing machines for refining materials
- Tiered machine system with efficiency upgrades
- Advanced machine interfaces with status displays
- Automation connectivity between machines
- Configurable energy usage and optimization

### Biotech

Biological systems for sustainable space living.

- Bioreactor for organic material processing
- Sustainable food generation systems
- Atmosphere regulators and processors
- Advanced crop growing systems for space
- Genetic modification for space-adapted crops
- Medical facilities for treating space-related conditions

### Space

Primary module for space exploration and celestial body management.

- Multi-tiered rocket system for planetary travel
- Space station construction framework
- Oxygen generation and management systems
- Space suit equipment with life support
- Planetary surface analysis tools
- Gravity manipulation devices
- Specialized space resources and materials

### Construction

Building materials and systems designed for space environments.

- Pressure-sealed construction blocks
- Radiation shielding materials
- Specialized space base components
- Quick-deploy habitat systems
- Airlock and atmospheric separation systems
- Structural integrity enhancement blocks

### Utilities

General purpose tools and equipment for space exploration.

- Scanning and surveying equipment
- Communication networks for distant bases
- Navigation systems for space travel
- Remote monitoring and control systems
- Resource detection and analysis tools
- Emergency response and backup systems

### Vehicles

Specialized transportation systems for space and planetary surfaces.

- Lunar rovers and Mars exploration vehicles
- Atmospheric skimmers for gas collection
- Cargo transportation systems
- Mining vehicles for resource collection
- Passenger transport for colonies
- Modular vehicle construction system

### Weaponry

Defense systems for protection against hostile environments and entities.

- Energy-based weapon systems
- Defense shield generators
- Automated defense turrets
- Personal protection equipment
- Base defense networks
- Anti-meteorite systems

### Robotics

Programmable automation systems for base operations.

- Automated mining robots
- Maintenance drones
- Programmable constructors
- Resource collection robots
- Research and exploration bots
- Specialized manufacturing assistants

## Installation

### Requirements

- Minecraft 1.21.5
- NeoForge 21.5.62 or later
- Java 21 or later

### Installation Steps

1. Download the latest release from [our website](https://example.com/galactic-expansion) or the [GitHub releases page](https://github.com/example/galactic-expansion/releases)
2. Place the JAR file in your Minecraft mods folder
3. Ensure you have the correct version of NeoForge installed
4. Launch Minecraft and enjoy exploring space!

### Module Installation

Galactic Expansion uses a modular design - you can install just the components you want:

- **Core Module**: Always required - provides base functionality
- **Optional Modules**: Install any combination of other modules according to your preferences

Each module is available as a separate JAR file. Simply place the desired module JARs in your mods folder.

## Gameplay Guide

### Getting Started

1. Craft the Basic Circuit Fabricator (requires iron, redstone, and glass)
2. Use the fabricator to create Basic Circuits
3. Construct your first Assembler machine
4. Begin manufacturing space-grade components
5. Build your first Tier 1 Rocket to reach the Moon

### Space Travel

1. Craft a complete Space Suit (helmet, chestplate, leggings, and boots)
2. Build an Oxygen Generator to fill oxygen tanks
3. Construct a Launch Pad
4. Place your rocket on the launch pad
5. Fuel the rocket with the appropriate fuel type
6. Launch to your chosen destination

### Base Building

1. Start with the Habitat Core module
2. Add Oxygen Generators and Life Support systems
3. Expand with specialized modules for research, manufacturing, and food production
4. Connect modules with airlocks and corridors
5. Establish power generation appropriate to your location (solar works well in orbit)

## Compatibility

### Compatible Mods

- JEI/REI (item information)
- WAILA/HWYLA/WTHIT (block information)
- Most resource pack formats
- Most minimaps and world map mods
- Storage mods (with appropriate adapters)

### Known Issues

- May have rendering conflicts with certain shader packs
- Performance impact when many machines are active in a small area
- Some block placement issues in zero-gravity environments

### Troubleshooting

#### "Broken Mod State" Errors

If you encounter errors like "Cowardly refusing to send event to a broken mod state" on startup:

1. Ensure all modules are from the same version (don't mix versions)
2. Check that you're using NeoForge 21.5.62+ with Minecraft 1.21.5
3. Remove any incompatible mods that might conflict
4. Try removing optional modules one by one to identify conflicts
5. Delete the Minecraft cache folder and restart

#### Client Crashes

For client-side crashes with texture loading errors:

1. Ensure you have sufficient RAM allocated (minimum 4GB recommended)
2. Update your graphics drivers to the latest version
3. Try disabling fancy graphics settings in Minecraft options
4. Check for resource pack conflicts
5. Ensure you have Java 21 installed, not an older version

## Development

### Setup for Developers

1. Clone the repository
2. Import into your IDE as a Gradle project
3. Run `./gradlew build` to build all modules

### Building from Source

Build all modules:
```
./gradlew clean build
```

Build specific modules:
```
./gradlew :core:build
./gradlew :power:build
./gradlew :machinery:build
./gradlew :space:build
./gradlew :biotech:build
```

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

## Contributing

We welcome contributions from the community!

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

Please see our [Contribution Guidelines](CONTRIBUTING.md) for more details.

## Roadmap and Future Additions

The Galactic Expansion mod is actively under development. Here are some features planned for upcoming versions:

### Coming in v0.2.0

- **Advanced Space Station Builder**: Visual drag-and-drop interface for designing custom space stations
- **Expanded Planet System**: Additional planets, moons, and asteroid fields to explore
- **Advanced Life Support Systems**: More complex oxygen, temperature, and radiation management
- **Galactic Map Interface**: Interactive 3D map of the solar system for navigation
- **Improved Machine GUIs**: Enhanced interfaces with better visualization of processes

### Coming in v0.3.0

- **Alien Life Forms**: Encounter and study extraterrestrial organisms
- **Research System**: Conduct experiments to unlock advanced technologies
- **Improved Power Network**: Visual power flow management and optimization
- **Automation and Logistics**: Advanced item and fluid transport systems
- **Space Elevators**: Earth-to-orbit transport infrastructure

### Long-term Vision

- **Interstellar Travel**: Expand beyond our solar system
- **Alien Civilizations**: Diplomacy and trading with other intelligent species
- **Colony Management**: Advanced systems for managing growing space settlements
- **Terraforming**: Transform hostile planets into habitable environments
- **Resource Wars**: Competitive multiplayer scenarios for resource-rich areas
- **Scenario System**: Custom mission-based gameplay with objectives and rewards

### Technical Improvements

- **Performance Optimization**: Continued focus on improving performance with many machines
- **Mod Compatibility**: Enhanced integration with popular tech and space mods
- **API Expansion**: More hooks for addon developers
- **Cross-Platform Support**: Potential Fabric version if demand exists

We welcome community input on prioritizing these features. Join our Discord or GitHub discussions to share your thoughts!

## Frequently Asked Questions

**Q: Can I use this mod in my modpack?**  
A: Yes, you're welcome to include Galactic Expansion in any modpack, public or private.

**Q: Does the mod work in multiplayer?**  
A: Yes, it's fully compatible with multiplayer and includes synchronization for all space-related mechanics.

**Q: How demanding is this mod on computer resources?**  
A: The mod is optimized for performance, but space environments with many machines may impact performance on lower-end systems.

**Q: Do I need to install all modules?**  
A: No, only the Core module is required. Other modules can be installed based on your preferences.

**Q: Is there compatibility with older Minecraft/Forge versions?**  
A: Currently only NeoForge 1.21.5 is officially supported. Backports may be considered in the future.

**Q: Will features from the roadmap definitely be added?**  
A: The roadmap represents our current development plans, but specific features may change based on technical feasibility and community feedback.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- The NeoForge team for their excellent modding framework
- The Minecraft modding community for inspiration and support
- Our beta testers for their valuable feedback
- All GitHub contributors who have helped improve the mod