package com.astroframe.galactic.power.items;

import com.astroframe.galactic.power.registry.PowerRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Power module.
 */
public class PowerItems {

    // Power Items
    public static final DeferredHolder<Item, Item> BASIC_GENERATOR = PowerRegistry.ITEMS.register(
            "basic_generator", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_GENERATOR = PowerRegistry.ITEMS.register(
            "advanced_generator", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> SOLAR_PANEL = PowerRegistry.ITEMS.register(
            "solar_panel", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> FUSION_REACTOR = PowerRegistry.ITEMS.register(
            "fusion_reactor", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Nothing needed here, registration happens through the static initializers
    }
}