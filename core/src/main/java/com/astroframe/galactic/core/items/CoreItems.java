package com.astroframe.galactic.core.items;

import com.astroframe.galactic.core.registry.CoreRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Core module.
 */
public class CoreItems {

    // Core Items
    public static final DeferredHolder<Item, Item> CIRCUIT_BOARD = CoreRegistry.ITEMS.register(
            "circuit_board", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> ADVANCED_CIRCUIT = CoreRegistry.ITEMS.register(
            "advanced_circuit", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> QUANTUM_PROCESSOR = CoreRegistry.ITEMS.register(
            "quantum_processor", 
            () -> new Item(new Item.Properties())
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Nothing needed here, registration happens through the static initializers
    }
}