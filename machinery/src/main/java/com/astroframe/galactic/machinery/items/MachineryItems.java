package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.registry.MachineryRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Machinery module.
 */
public class MachineryItems {

    // Machinery Items
    public static final DeferredHolder<Item, Item> CRUSHER = MachineryRegistry.ITEMS.register(
            "crusher", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> SMELTER = MachineryRegistry.ITEMS.register(
            "smelter", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> EXTRACTOR = MachineryRegistry.ITEMS.register(
            "extractor", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> CENTRIFUGE = MachineryRegistry.ITEMS.register(
            "centrifuge", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> ASSEMBLER = MachineryRegistry.ITEMS.register(
            "assembler", 
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