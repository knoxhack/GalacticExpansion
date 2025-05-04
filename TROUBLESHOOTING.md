# Galactic Expansion: Troubleshooting Guide

This document provides solutions for common issues encountered with the Galactic Expansion mod.

## Table of Contents

1. [Installation Issues](#installation-issues)
2. [Startup Crashes](#startup-crashes)
3. [Broken Mod State Errors](#broken-mod-state-errors)
4. [Rendering Issues](#rendering-issues)
5. [Gameplay Problems](#gameplay-problems)
6. [Performance Optimization](#performance-optimization)
7. [Compatibility Issues](#compatibility-issues)

## Installation Issues

### Mod Not Appearing in Mod List

- Make sure you've placed the JAR files in the correct mods folder
- Verify you're using NeoForge 21.5.62+ with Minecraft 1.21.5
- Check the logs for loading errors (logs/latest.log)
- Ensure the Core module is installed, as it's required for all other modules

### Missing Dependencies

- The Core module is required for all other modules to function
- Some modules may have dependencies on other modules (check the documentation)
- If using only specific modules, check their individual requirements

## Startup Crashes

### Java Version Errors

- Galactic Expansion requires Java 21 or later
- To check your Java version, run `java -version` in a command prompt
- If using a launcher like MultiMC or CurseForge, ensure it's configured to use Java 21

### Memory Allocation

- Allocate at least 4GB of RAM to Minecraft
- For complex space builds, 6-8GB is recommended
- Too much RAM can also cause issues (don't exceed 50% of your total system RAM)

## Broken Mod State Errors

### Understanding the Issue

The "Cowardly refusing to send event to a broken mod state" error occurs when:
- An error happened during mod initialization
- The mod tried to register event handlers after an error occurred
- NeoForge prevents potentially corrupted mods from handling events

### Common Specific Errors

#### "Block id not set" Error

This error typically occurs during block registration, particularly with custom blocks like machines:

```
java.lang.NullPointerException: Block id not set
```

**Causes:**
- The block's registry name wasn't properly set before registration
- Incorrect DeferredRegister setup
- Block properties were misconfigured
- Registration order issues with dependencies

**Solutions:**
- Ensure blocks are registered with proper IDs in format `"modid:block_name"`
- Check that DeferredRegister for blocks is initialized with the correct mod ID
- Verify all blocks have properties properly set before registration
- Make sure registration happens during the correct event phase

#### "Trying to access unbound value: ResourceKey" Error

This error happens when code tries to access a registry object that hasn't been registered yet:

```
java.lang.NullPointerException: Trying to access unbound value: ResourceKey[minecraft:block / modid:block_name]
```

**Causes:**
- Registration order issues (items or block entities referencing blocks before they're registered)
- Missing blocks in the registry
- Incorrect registry keys or mod IDs
- Circular dependencies between registries

**Solutions:**
- Ensure proper registration order (blocks before items, before block entities)
- Verify mod IDs are consistent across all registrations
- Use supplier references for cross-registry dependencies
- Check that all referenced blocks actually exist in your code

### General Solutions

1. **Check the Full Logs**
   - Look above the "broken mod state" errors for the root cause
   - Search for "ERROR" or "FATAL" in the logs/debug.log file
   - Identify which module is causing the issue

2. **Incompatible Module Versions**
   - Ensure all Galactic Expansion module JARs are from the same version
   - Don't mix beta and release modules

3. **One by One Testing**
   - Start with only the Core module
   - Add modules one by one until you identify the problematic one
   - Report the specific module if it consistently causes issues

4. **Conflicting Mods**
   - Remove other mods and add them back one by one
   - Check for known incompatibilities in the mod's documentation
   - Some rendering enhancement mods may conflict with space visuals

5. **Clean Installation**
   - Delete the Minecraft instance and create a fresh one
   - Install NeoForge 21.5.62+
   - Add only Galactic Expansion modules

6. **Fix for Client Events**
   - If the errors relate to client-side events and textures
   - Ensure your graphics drivers are up to date
   - Try running Minecraft with default resource packs only

7. **Special Fix for Texture Atlas Issues**
   - Delete the .minecraft/assets folder to force a re-download of textures
   - Clear the texture cache in the launcher settings if available
   - Try launching with the `-Dforge.forceNoStencil=true` JVM argument

## Rendering Issues

### Missing Textures

- Check that the mod loaded successfully without errors
- Verify your graphics drivers are up to date
- Try disabling shader packs or resource packs that modify block textures

### Transparent or Glitchy Space Textures

- Update your graphics drivers
- Make sure your GPU supports OpenGL 4.5+
- Try setting Mipmap Levels to 0 in Video Settings
- Disable Antialiasing if enabled

### Space Suit Display Problems

- Ensure you're wearing a complete suit (all four pieces)
- Try cycling the helmet visuals with the assigned keybind (default: V)
- Check for conflicts with other armor rendering mods

## Gameplay Problems

### Oxygen Not Working Correctly

- Verify your space suit is complete and has charged oxygen tanks
- Check for leaks in your space station (use the Atmosphere Scanner)
- Ensure oxygen generators have power and resources

### Machines Not Processing

- Check power connections and levels
- Verify the correct items are being provided
- Some machines have tier requirements for certain recipes
- Ensure the machine has space for output items

### Space Travel Issues

- Rockets require correct fuel type and amount
- Launch pad must be constructed correctly
- Space suit is required for survival
- Some destinations have specific rocket tier requirements

## Performance Optimization

### Reducing Lag

- Reduce the number of active machines in a single area
- Consider using efficiency upgrades to reduce machine counts
- Lower render distance when in complex space stations
- Disable fancy graphics and particles
- Install performance-enhancing mods like Sodium/Rubidium

### Server Settings

For server administrators:
- Increase allocated memory for servers with many players
- Set view-distance to 8-10 for optimal performance
- Use pre-generation for planetary dimensions
- Consider setting tick-distance lower for distant chunks

## Compatibility Issues

### Known Compatible Mods

- JEI/REI for recipe viewing
- Most map mods (Journey Map, Xaero's, etc.)
- Storage mods with appropriate adapters
- Optimization mods like Sodium/Rubidium

### Known Incompatible Mods

- Some dimension mods may conflict with space dimensions
- Certain physics mods may interfere with zero-gravity features
- Older armor rendering mods may conflict with space suits

## Module-Specific Fixes

### Machinery Module Issues

If you're experiencing issues specifically with the Machinery module, try these fixes:

#### AssemblerBlock Registration Issues

The most common issue with the Machinery module is related to the AssemblerBlock registration:

```
java.lang.NullPointerException: Block id not set
```

**Fix:**
1. Make sure you're using the latest version of the module
2. If the issue persists, try this manual fix:
   - Locate the `machinery/blocks/MachineryBlocks.java` file in the source code
   - Ensure the block registration uses this format:
     ```java
     public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, GalacticMachinery.MOD_ID);
     
     public static final RegistryObject<Block> ASSEMBLER_BLOCK = BLOCKS.register("assembler_block", 
         () -> new AssemblerBlock(BlockBehaviour.Properties.of()
             .mapColor(MapColor.METAL)
             .requiresCorrectToolForDrops()
             .strength(5.0F, 6.0F)
             .sound(SoundType.METAL)));
     ```
   - Make sure the block entities are registered AFTER blocks:
     ```java
     // In MachineryBlockEntities.java
     public static final RegistryObject<BlockEntityType<AssemblerBlockEntity>> ASSEMBLER_BLOCK_ENTITY = 
         BLOCK_ENTITIES.register("assembler_block_entity", 
             () -> BlockEntityType.Builder.of(
                 AssemblerBlockEntity::new, 
                 MachineryBlocks.ASSEMBLER_BLOCK.get())
             .build(null));
     ```

3. Check registration order in your mod initialization class:
   ```java
   public class GalacticMachinery {
       public static final String MOD_ID = "galacticmachinery";
       
       public GalacticMachinery() {
           // Register blocks FIRST
           MachineryBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
           // Then register items
           MachineryItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
           // Then register block entities
           MachineryBlockEntities.BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
       }
   }
   ```

### Reporting Issues

If you discover compatibility issues or other bugs:

1. Check if the issue is already reported on our GitHub
2. Prepare a detailed report with your mod list, logs, and steps to reproduce
3. Submit the issue on our GitHub repository or Discord
4. Include crash reports if available

## Advanced Troubleshooting

For developers and advanced users:

### Debug Mode

Add the following to your JVM arguments to enable detailed mod logging:
```
-Dgalactic.debug=true
```

### Custom Logging

Enable more detailed logging by editing the config file:
```
config/galacticexpansion/logging.toml
```

Set the log level to DEBUG for more information.

### Profiling

Use the built-in profiling tool by pressing F3+L in-game when experiencing performance issues.
Export the results and include them in bug reports for performance-related issues.