# NeoForge 1.21.5 Compatibility Updates

## Overview

This document summarizes the changes made to adapt the Galactic Expansion mod to NeoForge 1.21.5 from older Forge versions. The update involved addressing API changes and handling new patterns for registry access, tag manipulation, and resource handling.

## Critical Mod ID Changes

NeoForge 1.21.5 has stricter requirements for mod IDs. All mod IDs must:
- Match exactly between `@Mod` annotation and configuration files
- Avoid using underscores (use "galacticspace" instead of "galactic_space")
- Be consistent across all references in code and configuration files

```java
// Old code
@Mod("galactic_space")
public class SpaceModule {
    public static final String MODID = "galactic_space";
}

// New code for NeoForge 1.21.5
@Mod("galacticspace")
public class SpaceModule {
    public static final String MODID = "galacticspace";
}
```

## Event Bus Registration Issues

In NeoForge 1.21.5, the event bus system is more strict about registering classes as event handlers:

```java
// This causes an error in NeoForge 1.21.5 if the class has no @SubscribeEvent methods
NeoForge.EVENT_BUS.register(this);

// Only register classes that actually have methods with @SubscribeEvent annotations
@SubscribeEvent
public void onSomeEvent(SomeEvent event) {
    // Event handling code
}
```

Always make sure that any class you register to the event bus has at least one method annotated with `@SubscribeEvent`.

## Item Registration Issues

In NeoForge 1.21.5, Item registration requires properties to be explicitly set to avoid the error "Item id not set":

```java
// This causes "Item id not set" error in NeoForge 1.21.5
public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
    "circuit_board", 
    () -> new Item(new Item.Properties())
);

// This works in NeoForge 1.21.5
public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
    "circuit_board", 
    () -> new Item(new Item.Properties().stacksTo(64))
);
```

Always call at least one method on the Item.Properties object when registering items to ensure proper initialization.

## Key Changes

### Registry Access

- Replaced `ForgeRegistries` with `BuiltInRegistries` for accessing vanilla registries
- Updated registry lookup patterns to use `.registry().getOptional()` for NeoForge 1.21.5
- Added proper handling of `Holder<T>` wrapper objects returned by registry operations

```java
// Old code (pre-NeoForge 1.21.5)
Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:diamond"));

// New code (NeoForge 1.21.5)
Item item = BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft:diamond"));
// or
Optional<Item> item = BuiltInRegistries.ITEM.getOptional(new ResourceLocation("minecraft:diamond"));
```

### ResourceLocation Handling

- Replaced direct `ResourceLocation` constructor calls with `ResourceLocation.parse(String)`
- Updated ResourceKey creation to follow the proper pattern for NeoForge 1.21.5

```java
// Old code
ResourceLocation location = new ResourceLocation("namespace", "path");

// New code
ResourceLocation location = ResourceLocation.parse("namespace:path");
```

### Tag/NBT Operations

- Updated `CompoundTag` and `ListTag` methods to handle `Optional` returns in NeoForge 1.21.5
- Implemented proper type checking and casting for NBT tag operations
- Added fallback mechanisms when direct tag operations fail

```java
// Old code
CompoundTag tag = itemStack.getTag();

// New code (with fallback)
CompoundTag tag = null;
if (itemStack.hasTag()) {
    tag = itemStack.getTag();
}
```

### BlockEntityType Registration

- Updated BlockEntityType instantiation with proper type parameters
- Added required boolean parameter in BlockEntityType.Builder
- Fixed factory reference handling for block entity registration

```java
// Old code
BlockEntityType<MyBlockEntity> type = BlockEntityType.Builder.of(
    MyBlockEntity::new, 
    myBlock
).build(null);

// New code
BlockEntityType<MyBlockEntity> type = BlockEntityType.Builder.of(
    MyBlockEntity::new, 
    myBlock
).build(true);
```

### Enchantment Handling

- Updated enchantment registry access using `BuiltInRegistries.ENCHANTMENT`
- Implemented reflection-based fallback for cross-version compatibility
- Added proper error handling for enchantment operations

### Package References

- Updated package references from `net.minecraftforge` to `net.neoforged.neoforge`
- Fixed import statements across all files to match NeoForge 1.21.5 package structure

## Affected Modules

- Core module: API interfaces and common utility classes updated
- Space module: Implementation classes updated to handle registry and tag changes
- All modules: Registry access patterns standardized

## Testing

The project now successfully builds with NeoForge 1.21.5. All modules compile without errors after the compatibility updates.

## Future Work

- Continue testing with in-game verification
- Verify all dynamic registry operations work correctly at runtime
- Monitor for any additional API changes in future NeoForge versions