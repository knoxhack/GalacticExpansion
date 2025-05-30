package com.astroframe.galactic.core.items;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.registry.CoreRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all items in the Core module.
 */
public class CoreItems {

    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(GalacticCore.MOD_ID);
    
    // Base crafting materials
    public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = ITEMS_HELPER.registerItem(
            "circuit_board", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_CIRCUIT = ITEMS_HELPER.registerItem(
            "advanced_circuit", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> QUANTUM_PROCESSOR = ITEMS_HELPER.registerItem(
            "quantum_processor", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    // Additional crafting materials
    public static final DeferredHolder<Item, Item> NANO_WIRING = ITEMS_HELPER.registerItem(
            "nano_wiring", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> GALACTIC_ALLOY = ITEMS_HELPER.registerItem(
            "galactic_alloy", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    // Tools
    public static final DeferredHolder<Item, Item> PORTABLE_SCANNER = ITEMS_HELPER.registerItem(
            "portable_scanner", 
            properties -> new Item(properties.stacksTo(1))
    );
    
    public static final DeferredHolder<Item, Item> ENGINEERS_TOOLKIT = ITEMS_HELPER.registerItem(
            "engineers_toolkit", 
            properties -> new Item(properties.stacksTo(1))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Class is initialized and items are registered when the class is loaded
        // Validate important items are registered correctly
        if (CIRCUIT_BOARD == null || ADVANCED_CIRCUIT == null || QUANTUM_PROCESSOR == null) {
            throw new IllegalStateException("Core items failed to initialize properly");
        }
        
        // Register our Items DeferredRegister to the EVENT BUS, not to another DeferredRegister
        // Both DeferredRegister.Items and regular DeferredRegister should be registered to the event bus
        ITEMS_HELPER.register(GalacticCore.MOD_EVENT_BUS);
    }
}