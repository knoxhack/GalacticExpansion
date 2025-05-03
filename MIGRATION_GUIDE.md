# NeoForge 1.21.5 Migration Guide

This guide provides steps for migrating Minecraft mods to NeoForge 1.21.5 from older Forge versions. 

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

1. **Event Subscription**:
   - Review `@SubscribeEvent` annotations
   - Ensure events are registered to the right event bus

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