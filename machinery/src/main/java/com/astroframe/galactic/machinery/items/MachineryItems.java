package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.registry.MachineryRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all items in the Machinery module.
 */
public class MachineryItems {

    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(GalacticMachinery.MOD_ID);
    
    // Machinery Items - using the DeferredRegister.Items.registerItem to properly set IDs
    public static final DeferredHolder<Item, Item> CRUSHER = ITEMS_HELPER.registerItem(
            "crusher", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> SMELTER = ITEMS_HELPER.registerItem(
            "smelter", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> EXTRACTOR = ITEMS_HELPER.registerItem(
            "extractor", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> CENTRIFUGE = ITEMS_HELPER.registerItem(
            "centrifuge", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> ASSEMBLER = ITEMS_HELPER.registerItem(
            "assembler", 
            properties -> new Item(properties.stacksTo(64))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Class is initialized and items are registered when the class is loaded
        // Validate that important items are registered correctly
        if (CRUSHER == null || SMELTER == null || ASSEMBLER == null) {
            throw new IllegalStateException("Machinery items failed to initialize properly");
        }
        
        // Register our Items DeferredRegister to the event bus
        ITEMS_HELPER.register(GalacticMachinery.MOD_EVENT_BUS);
    }
}