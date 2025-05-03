package com.astroframe.galactic.core.items;

import com.astroframe.galactic.core.registry.CoreRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Core module.
 */
public class CoreItems {

    // Create a single Item.Properties instance with stacksTo(64) set
    private static final Item.Properties DEFAULT_PROPS = new Item.Properties().stacksTo(64);
    
    // Core Items
    public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
            "circuit_board", 
            () -> new Item(DEFAULT_PROPS)
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_CIRCUIT = CoreRegistry.ITEMS.register(
            "advanced_circuit", 
            () -> new Item(DEFAULT_PROPS)
    );
    
    public static final DeferredHolder<Item, Item> QUANTUM_PROCESSOR = CoreRegistry.ITEMS.register(
            "quantum_processor", 
            () -> new Item(DEFAULT_PROPS)
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Nothing needed here, registration happens through the static initializers
    }
}