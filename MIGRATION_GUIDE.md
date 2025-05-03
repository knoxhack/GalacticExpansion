# NeoForge 1.21.5 Migration Guide

This guide provides steps for migrating Minecraft mods to NeoForge 1.21.5 from older Forge versions. 

## Mod ID Consistency

One of the most critical changes in NeoForge 1.21.5 is the strict enforcement of mod ID consistency:

1. **Mod ID Format**:
   - Do not use underscores in mod IDs (use `modname` instead of `mod_name`)
   - Keep mod IDs lowercase and alphanumeric
   - Make sure the mod ID is consistent between code and configuration

2. **Update @Mod Annotation**:
   ```java
   // Change this:
   @Mod("my_mod_id")
   
   // To this:
   @Mod("mymodid")
   ```

3. **Update Configuration Files**:
   - In `mods.toml` or `neoforge.mods.toml`, ensure the `modId` value matches exactly
   - Update all dependency references to use the new format

4. **Check MODID Constants**:
   - Update any `public static final String MODID = "my_mod_id"` constants
   - Ensure all registry object registrations use the updated mod ID

## Dependencies and Environment

1. **Java Version**: Ensure you're using Java 21 for NeoForge 1.21.5 development.
   ```gradle
   // build.gradle
   java {
       toolchain.languageVersion = JavaLanguageVersion.of(21)
   }
   ```

2. **Gradle**: Update to Gradle 8.12 or newer for best compatibility.

3. **NeoForge Version**: Add the proper NeoForge dependency:
   ```gradle
   // build.gradle
   dependencies {
       minecraft 'net.neoforged:neoforge:21.5.62'
   }
   ```

4. **Mappings**: Use the official "official" mappings:
   ```gradle
   // build.gradle
   minecraft {
       mappings channel: 'official', version: '1.21.5'
   }
   ```

## Package Structure Changes

1. **Update imports**:
   - Change `net.minecraftforge` to `net.neoforged.neoforge`
   - Change `net.minecraftforge.fml` to `net.neoforged.fml`

2. **Event Bus**:
   - Use `NeoForge.EVENT_BUS` instead of `MinecraftForge.EVENT_BUS`
   - Use `FMLEvents.registryRegistration` instead of `FMLRegistryEvent`

## Registry Changes

1. **Accessing Vanilla Registries**:
   - Use `BuiltInRegistries` instead of `ForgeRegistries` for vanilla registries
   - Use `NeoForgeRegistries` for NeoForge-specific registries

2. **Registry Operations**:
   - Replace `getValue` with direct `.get()` or `.getOptional()`
   - Handle `Optional<T>` returns where appropriate
   - Use `ResourceLocation.parse()` instead of the constructor

3. **DeferredRegister**:
   - Update import to `net.neoforged.neoforge.registries.DeferredRegister`
   - Registration process remains similar

## Item Registration Changes

1. **Item Properties**:
   - In NeoForge 1.21.5, items need explicit properties set to avoid "Item id not set" errors
   - Always call at least one method on the Item.Properties object when registering items
   ```java
   // This will crash in NeoForge 1.21.5
   new Item(new Item.Properties())
   
   // This will work in NeoForge 1.21.5
   new Item(new Item.Properties().stacksTo(64))
   // or
   new Item(new Item.Properties().durability(100))
   ```

2. **Item Registry Names**:
   - Ensure registry names are consistent with mod ID format (no underscores)
   - Use the same format in both code and JSON files

## NBT and Tag Changes

1. **CompoundTag Operations**:
   - Add proper null-checking around tag operations
   - Handle `Optional<T>` returns from tag getter methods
   - Use type-specific getters instead of generic ones

2. **ItemStack Tag Handling**:
   - Check `.hasTag()` before accessing tags
   - Implement fallbacks for when tags are missing

## BlockEntity Changes

1. **BlockEntityType Registration**:
   - Update builder with boolean parameter: `.build(true)`
   - Ensure proper type parameters in the builder

2. **Provider Interfaces**:
   - Check for updated interface methods that may have changed

## Event Handling

1. **Event Bus Registration**:
   - Only register classes to the event bus if they have methods with `@SubscribeEvent` annotation
   - Remove any calls to `EVENT_BUS.register(this)` if the class has no event handler methods
   - NeoForge is more strict and will throw an exception if you register a class with no event handlers

2. **Event Names and Imports**:
   - Update from `net.minecraftforge.event` to `net.neoforged.neoforge.event`
   - Some event classes may have moved or been renamed

3. **Event Subscription**:
   - Review `@SubscribeEvent` annotations
   - Ensure events are registered to the right event bus
   - Use `NeoForge.EVENT_BUS` instead of `MinecraftForge.EVENT_BUS`

## Capability System

1. **Capability Registration**:
   - Update to use the new capability API
   - Register capabilities during the appropriate registration phase

## Testing

1. Compile your mod against NeoForge 1.21.5
2. Fix any compile-time errors
3. Run the mod in a development environment
4. Fix runtime issues
5. Verify all functionality in-game

## Common Issues

1. **Registry Access Errors**: Ensure all registry access uses the new patterns.
2. **NBT Exceptions**: Add proper null-checking for all NBT operations.
3. **Block Entity Registration Failures**: Check BlockEntityType builder usage.
4. **Missing Resources**: Verify that all resource locations are valid.
5. **Event Firing Order**: Some events might fire in a different order; adjust your code accordingly.

## Resources

- [NeoForge Documentation](https://neoforged.net/)
- [NeoForge GitHub Repository](https://github.com/neoforged/NeoForge)
- [NeoForge Community Discord](https://discord.neoforged.net/)