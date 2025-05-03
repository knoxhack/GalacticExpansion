package com.astroframe.galactic.core.items;

import com.astroframe.galactic.core.registry.CoreRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Core module.
 */
public class CoreItems {

    // Create a single Item.Properties instance with stacksTo(64) set
    // This is essential for NeoForge 1.21.5 to avoid "Item id not set" errors
    private static final Item.Properties DEFAULT_PROPS = new Item.Properties().stacksTo(64);
    
    // Core Items - explicitly setting stacksTo(64) for each item to ensure it's properly set
    public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
            "circuit_board", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_CIRCUIT = CoreRegistry.ITEMS.register(
            "advanced_circuit", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> QUANTUM_PROCESSOR = CoreRegistry.ITEMS.register(
            "quantum_processor", 
            () -> new Item(new Item.Properties().stacksTo(64))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Class is initialized and items are registered when the class is loaded
        // This comment helps ensure the class gets properly loaded
        if (CIRCUIT_BOARD == null || ADVANCED_CIRCUIT == null || QUANTUM_PROCESSOR == null) {
            throw new IllegalStateException("Core items failed to initialize properly");
        }
    }
}