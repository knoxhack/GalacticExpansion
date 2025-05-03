# GitHub Release Process Fix

## Issue
The GitHub release process was not correctly including the JAR files that had the fixes for the "Item id not set" error in NeoForge 1.21.5. This resulted in users downloading JAR files from GitHub releases that still had the error.

## Changes Made
We've made the following improvements to the GitHub release process:

1. Created an improved GitHub release script (`scripts/improved_github_release.sh`) that:
   - Uses a standardized naming convention for JAR files that matches what Minecraft expects
   - Explicitly looks for JAR files for each module in the expected locations
   - Creates properly named JAR files with the format `modulename_galacticmodulename-0.1.0.jar`
   - Creates a ZIP file containing all module JARs for easier distribution

2. Updated the build and release process in `buildwidget/build-and-release.js` to:
   - Use the improved GitHub release script for all releases
   - Better handle errors and fallbacks during the release process

## How to Test This Fix
1. Run a full build with `./gradlew clean build`
2. The build process will automatically create the JAR files in the `build/libs` directory of each module
3. Use the packaging script `./package-jars.sh` to create a ZIP file with all the module JARs
4. Test the JAR files in Minecraft with NeoForge 1.21.5

## Technical Details
The key fix was ensuring that the Item.Properties fields in all module item registration classes have stacksTo(64) set explicitly. The GitHub release script now ensures that these properly fixed JAR files are included in all GitHub releases.

## Example
```java
// Create a single Item.Properties instance with stacksTo(64) set
private static final Item.Properties DEFAULT_PROPS = new Item.Properties().stacksTo(64);

// Use it for item registration
public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
        "circuit_board", 
        () -> new Item(DEFAULT_PROPS)
);
```

This pattern is now consistently applied across all module item registry classes.