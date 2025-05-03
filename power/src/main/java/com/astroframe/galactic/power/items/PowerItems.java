package com.astroframe.galactic.power.items;

import com.astroframe.galactic.power.GalacticPower;
import com.astroframe.galactic.power.registry.PowerRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all items in the Power module.
 */
public class PowerItems {

    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(GalacticPower.MOD_ID);
    
    // Power Items - using the DeferredRegister.Items.registerItem to properly set IDs
    public static final DeferredHolder<Item, Item> BASIC_GENERATOR = ITEMS_HELPER.registerItem(
            "basic_generator", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_GENERATOR = ITEMS_HELPER.registerItem(
            "advanced_generator", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> SOLAR_PANEL = ITEMS_HELPER.registerItem(
            "solar_panel", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> FUSION_REACTOR = ITEMS_HELPER.registerItem(
            "fusion_reactor", 
            properties -> new Item(properties.stacksTo(64))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Register our Items DeferredRegister to the normal ITEMS DeferredRegister
        ITEMS_HELPER.register(PowerRegistry.ITEMS);
        
        // Validate that important items are registered correctly
        if (BASIC_GENERATOR == null || ADVANCED_GENERATOR == null || SOLAR_PANEL == null) {
            throw new IllegalStateException("Power items failed to initialize properly");
        }
    }
}