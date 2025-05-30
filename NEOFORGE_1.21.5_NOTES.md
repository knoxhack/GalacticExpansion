# NeoForge 1.21.5 Migration Notes

This document contains important notes about changes and requirements when migrating to NeoForge 1.21.5.

## Item Registration Changes

### Item ID Requirements

- In NeoForge 1.21.5, item IDs must be set in the `Item.Properties` constructor.
- Failing to set an item ID results in a "Item id not set" error during mod loading.

### Using DeferredRegister.Items

The proper way to register items in NeoForge 1.21.5:

```java
// Create the specialized Items DeferredRegister
private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(MOD_ID);

// Use registerItem which properly sets the item ID in Item.Properties
public static final DeferredHolder<Item, Item> MY_ITEM = ITEMS_HELPER.registerItem(
    "my_item", 
    properties -> new Item(properties.stacksTo(64))
);

// IMPORTANT: Register the Items helper to the EVENT BUS, not to another DeferredRegister
public static void init() {
    ITEMS_HELPER.register(MOD_EVENT_BUS); 
}
```

### Stack Size Requirements

- Always explicitly set the stack size using `properties.stacksTo(64)` for standard items
- For single stack items use `properties.stacksTo(1)`

## Event Bus Changes

### Static Event Bus Access

In NeoForge 1.21.5, it's important to provide static access to the mod's event bus for registration:

```java
@Mod(MyMod.MOD_ID)
public class MyMod {
    
    /** The mod ID for this module */
    public static final String MOD_ID = "mymod";
    
    /** The mod's event bus - static access for NeoForge 1.21.5 registration */
    public static IEventBus MOD_EVENT_BUS;
    
    /** Singleton instance of the mod */
    public static MyMod INSTANCE;
    
    /**
     * Constructs a new instance of the mod.
     */
    public MyMod(IEventBus modEventBus) {
        INSTANCE = this;
        MOD_EVENT_BUS = modEventBus; // Store static reference for module registration
        
        // Initialize registries
        // ...
    }
}
```

### Event Handler Registration

There are two distinct event buses in NeoForge 1.21.5, and it's critical to register handlers to the correct one:

#### Mod Event Bus

- Register to `MOD_EVENT_BUS` for mod lifecycle events
- Handles events that implement `IModBusEvent` interface
- Examples: `FMLCommonSetupEvent`, `RegisterEvent`, `BuildCreativeModeTabContentsEvent`
- Used for mod initialization, registration, and setup

```java
// Register to mod event bus
GalacticCore.MOD_EVENT_BUS.register(this);

// Example mod event handler
@SubscribeEvent
public void setup(FMLCommonSetupEvent event) {
    // Mod initialization code
}
```

#### Game Event Bus

- Register to `NeoForge.EVENT_BUS` for game events
- Handles events that do not implement `IModBusEvent`
- Examples: `PlayerEvent.PlayerLoggedInEvent`, `LivingDamageEvent`, `ServerTickEvent`
- Used for gameplay interactions and responding to game state changes

```java
// Register to game event bus
NeoForge.EVENT_BUS.register(this);

// Example game event handler
@SubscribeEvent
public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
    // Player login handling code
}
```

#### Best Practices

- Only register classes with `@SubscribeEvent` methods to event buses
- All DeferredRegister objects should be registered directly to the mod event bus
- Never register a DeferredRegister to another DeferredRegister - always register to the event bus
- Using the wrong event bus will cause a `java.lang.IllegalArgumentException` at runtime

## Package Changes

- Many packages have moved from `net.minecraftforge` to `net.neoforged.neoforge`
- Event bus package has moved to `net.neoforged.bus.api`
- `net.minecraft.resources.ResourceLocation` constructors are deprecated, use `ResourceLocation.parse()` instead
- Sound Event handling uses the `Holder.direct` pattern
- Registry access should use `BuiltInRegistries` or `Registries` instead of `ForgeRegistries`

## Optional Handling

- Many methods now return `Optional<T>` wrappers instead of possibly null values
- Special care needs to be taken when working with Tag methods
- Properly casting Tag objects to specific types (StringTag, CompoundTag, etc.) is required

## Block Entity Changes

- Updated Provider interfaces in BlockEntity classes
- BlockEntityType.Builder syntax has changed for proper block entity registration

## Utility Methods for Cross-Version Compatibility

Recommended helper methods to create:
- TagHelper - for handling NBT data across versions
- ResourceLocationHelper - for creating resource locations
- ItemStackHelper - for working with item stacks and tags

## Creative Tabs

- Creative tab definition and registration has changed
- Use `ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.parse(modId + ":tab_id"))`

## Defensive Coding

- Add comprehensive error handling with try-catch blocks for API operations
- Make enum types public and move them to separate files when accessed across packages
- Add utility methods to enums to enhance compatibility
- Add validation in initialization methods to catch problems early