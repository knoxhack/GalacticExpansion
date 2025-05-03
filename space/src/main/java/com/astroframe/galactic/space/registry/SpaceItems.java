package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.item.ModularRocketItem;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import com.astroframe.galactic.space.items.RocketItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all items for the Space module.
 */
public class SpaceItems {
    
    // Create a deferred register for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(net.minecraft.core.registries.Registries.ITEM, GalacticSpace.MOD_ID);
    
    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(GalacticSpace.MOD_ID);
    
    // Define basic items - using registerItem to ensure item IDs are properly set
    public static final DeferredHolder<Item, Item> ROCKET_PART = ITEMS_HELPER.registerItem(
            "rocket_part", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> MOON_DUST = ITEMS_HELPER.registerItem(
            "moon_dust", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> LUNAR_DUST = ITEMS_HELPER.registerItem(
            "lunar_dust", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> MOON_ROCK = ITEMS_HELPER.registerItem(
            "moon_rock", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> MARS_ROCK = ITEMS_HELPER.registerItem(
            "mars_rock", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> STELLAR_FRAGMENT = ITEMS_HELPER.registerItem(
            "stellar_fragment", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> SPACE_HELMET = ITEMS_HELPER.registerItem(
            "space_helmet", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> OXYGEN_TANK = ITEMS_HELPER.registerItem(
            "oxygen_tank", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    // Space suit items - using registerItem to ensure proper ID setting
    public static final DeferredHolder<Item, Item> SPACE_SUIT_HELMET = ITEMS_HELPER.registerItem(
            "space_suit_helmet",
            properties -> new SpaceSuitItem(EquipmentSlot.HEAD, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> SPACE_SUIT_CHESTPLATE = ITEMS_HELPER.registerItem(
            "space_suit_chestplate",
            properties -> new SpaceSuitItem(EquipmentSlot.CHEST, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> SPACE_SUIT_LEGGINGS = ITEMS_HELPER.registerItem(
            "space_suit_leggings",
            properties -> new SpaceSuitItem(EquipmentSlot.LEGS, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> SPACE_SUIT_BOOTS = ITEMS_HELPER.registerItem(
            "space_suit_boots",
            properties -> new SpaceSuitItem(EquipmentSlot.FEET, properties.stacksTo(1))
    );
    
    // Rocket items - ensuring proper ID setting with registerItem
    public static final DeferredHolder<Item, Item> TIER_1_ROCKET = ITEMS_HELPER.registerItem(
            "tier_1_rocket",
            properties -> new RocketItem(1, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> TIER_2_ROCKET = ITEMS_HELPER.registerItem(
            "tier_2_rocket",
            properties -> new RocketItem(2, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> TIER_3_ROCKET = ITEMS_HELPER.registerItem(
            "tier_3_rocket",
            properties -> new RocketItem(3, properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> MODULAR_ROCKET = ITEMS_HELPER.registerItem(
            "modular_rocket",
            properties -> new ModularRocketItem(properties.stacksTo(1))
    );
    
    /**
     * Initialize and register all items.
     * @param modEventBus The mod event bus to register items on
     */
    public static void initialize(IEventBus modEventBus) {
        // Register our Items DeferredRegister to the normal ITEMS DeferredRegister
        ITEMS_HELPER.register(ITEMS);
        
        // Register items to the event bus
        ITEMS.register(modEventBus);
        
        // Validate that items are properly initialized
        if (ROCKET_PART == null || MOON_DUST == null || SPACE_SUIT_HELMET == null) {
            throw new IllegalStateException("Space items failed to initialize properly");
        }
    }
}