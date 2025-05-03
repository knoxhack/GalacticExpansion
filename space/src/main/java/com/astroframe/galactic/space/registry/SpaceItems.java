package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.core.items.CoreItems;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.item.ModularRocketItem;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import com.astroframe.galactic.space.items.RocketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all items for the Space module.
 */
public class SpaceItems {
    
    // Create a deferred register for items
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticSpace.MOD_ID);
    
    /**
     * Utility method to create Item.Properties objects with stack size of 64
     * This is a workaround for NeoForge 1.21.5 "Item id not set" errors
     */
    private static Item.Properties createProperties() {
        return new Item.Properties().stacksTo(64);
    }
    
    /**
     * Utility method for items that should stack to 1
     */
    private static Item.Properties createSingleItemProperties() {
        return new Item.Properties().stacksTo(1);
    }
    
    // Define basic items - using the utility method to ensure stacksTo(64) is properly set
    public static final Supplier<Item> ROCKET_PART = ITEMS.register("rocket_part", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> MOON_DUST = ITEMS.register("moon_dust", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> LUNAR_DUST = ITEMS.register("lunar_dust", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> MOON_ROCK = ITEMS.register("moon_rock", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> MARS_ROCK = ITEMS.register("mars_rock", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> STELLAR_FRAGMENT = ITEMS.register("stellar_fragment", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> SPACE_HELMET = ITEMS.register("space_helmet", 
            () -> new Item(createProperties()));
    
    public static final Supplier<Item> OXYGEN_TANK = ITEMS.register("oxygen_tank", 
            () -> new Item(createProperties()));
    
    // Space suit items
    // In NeoForge 1.21.5, use EquipmentSlot directly and explicit stacksTo(1)
    public static final Supplier<Item> SPACE_SUIT_HELMET = ITEMS.register("space_suit_helmet",
            () -> new SpaceSuitItem(EquipmentSlot.HEAD, createSingleItemProperties()));
    
    public static final Supplier<Item> SPACE_SUIT_CHESTPLATE = ITEMS.register("space_suit_chestplate",
            () -> new SpaceSuitItem(EquipmentSlot.CHEST, createSingleItemProperties()));
    
    public static final Supplier<Item> SPACE_SUIT_LEGGINGS = ITEMS.register("space_suit_leggings",
            () -> new SpaceSuitItem(EquipmentSlot.LEGS, createSingleItemProperties()));
    
    public static final Supplier<Item> SPACE_SUIT_BOOTS = ITEMS.register("space_suit_boots",
            () -> new SpaceSuitItem(EquipmentSlot.FEET, createSingleItemProperties()));
    
    // Rocket items - ensuring each one has explicit stacksTo(1)
    public static final Supplier<Item> TIER_1_ROCKET = ITEMS.register("tier_1_rocket",
            () -> new RocketItem(1, createSingleItemProperties()));
    
    public static final Supplier<Item> TIER_2_ROCKET = ITEMS.register("tier_2_rocket",
            () -> new RocketItem(2, createSingleItemProperties()));
    
    public static final Supplier<Item> TIER_3_ROCKET = ITEMS.register("tier_3_rocket",
            () -> new RocketItem(3, createSingleItemProperties()));
    
    public static final Supplier<Item> MODULAR_ROCKET = ITEMS.register("modular_rocket",
            () -> new ModularRocketItem(createSingleItemProperties()));
    
    /**
     * Initialize and register all items.
     * @param modEventBus The mod event bus to register items on
     */
    public static void initialize(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
        
        // Validate that items are properly initialized
        if (ROCKET_PART == null || MOON_DUST == null || SPACE_SUIT_HELMET == null) {
            throw new IllegalStateException("Space items failed to initialize properly");
        }
    }
}