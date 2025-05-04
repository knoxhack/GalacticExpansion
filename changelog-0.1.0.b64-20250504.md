# Galactic Expansion Mod - Changelog for v0.1.0.b64 (May 4, 2025)

## Major Fixes
- **Fixed dependency issues in all-in-one JAR**: Resolved the critical "Mod galacticspace requires galacticcore" crash by consolidating all module dependencies.
- **Unified ModID system**: Replaced individual module IDs with a single "galacticexpansion" ModID to prevent conflicts.
- **Removed problematic ModProvider classes**: Eliminated class conflicts that were causing dependency resolution failures.

## Technical Improvements
- **Enhanced packaging process**: Updated the packaging script to handle module unification more reliably.
- **Fixed JSON configuration references**: Corrected resource paths to use the unified ModID structure.
- **Added documentation**: Updated JARS_README.md with clearer installation instructions and troubleshooting information.

## Known Issues
- No new issues reported in this release.

## Notes for Modpack Creators
- This version introduces a more stable unified JAR format.
- If you were previously using individual module JARs, switch to the all-in-one version for better compatibility.
- The mod now requires only a single dependency entry in your modpack configuration.