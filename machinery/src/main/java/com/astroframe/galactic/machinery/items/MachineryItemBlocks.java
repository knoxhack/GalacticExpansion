
package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

/**
 * Registry for block items in the Machinery module.
 */
public class MachineryItemBlocks {

    // Deferred Register for block items
    private static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);
        
    // Block items for all machinery blocks
    public static final DeferredHolder<Item, Item> ASSEMBLER_ITEM = BLOCK_ITEMS.register(
        "assembler", 
        () -> new BlockItem(MachineryBlocks.ASSEMBLER.get(), new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> CRUSHER_ITEM = BLOCK_ITEMS.register(
        "crusher", 
        () -> new BlockItem(MachineryBlocks.CRUSHER.get(), new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> CENTRIFUGE_ITEM = BLOCK_ITEMS.register(
        "centrifuge", 
        () -> new BlockItem(MachineryBlocks.CENTRIFUGE.get(), new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> SMELTER_ITEM = BLOCK_ITEMS.register(
        "smelter", 
        () -> new BlockItem(MachineryBlocks.SMELTER.get(), new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> EXTRACTOR_ITEM = BLOCK_ITEMS.register(
        "extractor", 
        () -> new BlockItem(MachineryBlocks.EXTRACTOR.get(), new Item.Properties())
    );

    /**
     * Initializes the block items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        try {
            GalacticMachinery.LOGGER.info("Registering machinery block items");
            GalacticMachinery.LOGGER.debug("Machinery block items registered successfully");
        } catch (Exception e) {
            // Log any errors that occur during registration
            GalacticMachinery.LOGGER.error("Error registering machinery block items", e);
            throw e;
        }

        // Register the block items deferred register
        BLOCK_ITEMS.register(GalacticMachinery.MOD_EVENT_BUS);
    }
}
