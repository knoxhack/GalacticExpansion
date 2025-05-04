# Changelog

All notable changes to the Galactic Expansion mod will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2025-05-04

### Added

#### Core Module
- Core API interfaces for cross-module compatibility
- Player data attachment system
- Registry helpers for blocks, items, and other components
- Common utility classes
- Machine base template with standardized textures
- Tag management system

#### Power Module
- Basic energy infrastructure (sources, storage, transfer)
- Energy interface for machine compatibility
- Energy monitoring components

#### Machinery Module
- Base machine framework with tier system
- Assembler machine with full implementation
- Standardized machine registration system
- Core machine interfaces (Machine, EnergyConsumer)
- Block and item registration for machinery components

#### Biotech Module
- Initial bioreactor implementation
- Basic organic processing capabilities
- Food production systems foundation

#### Space Module
- Space suit components (helmet, chestplate, leggings, boots)
- Oxygen management system
- Initial rocket implementations (Tier 1)
- Moon and Mars resources

### Changed
- Updated all components for NeoForge 1.21.5 compatibility
- Improved texture resolution and visual consistency
- Standardized module API interfaces

### Fixed
- Block entity registration for NeoForge 1.21.5
- Attachment system compatibility with newer versions
- Tag handling in newer NeoForge versions
- Holder references in ItemStack handling

## [0.0.1] - 2025-04-28

### Added
- Initial project structure
- Basic module framework
- Preliminary NeoForge 1.21.5 support