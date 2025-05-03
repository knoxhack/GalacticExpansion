package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for block items in the Machinery module.
 * These are items that place blocks.
 */
public class MachineryItemBlocks {

    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(GalacticMachinery.MOD_ID);
    
    /**
     * Initializes the block items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        GalacticMachinery.LOGGER.info("Registering machinery block items");
        
        // Register block items for each machinery block
        BLOCK_ITEMS.registerItem("assembler", props -> 
            new BlockItem(MachineryBlocks.ASSEMBLER.get(), props));
        
        BLOCK_ITEMS.registerItem("crusher", props -> 
            new BlockItem(MachineryBlocks.CRUSHER.get(), props));
        
        BLOCK_ITEMS.registerItem("centrifuge", props -> 
            new BlockItem(MachineryBlocks.CENTRIFUGE.get(), props));
        
        BLOCK_ITEMS.registerItem("smelter", props -> 
            new BlockItem(MachineryBlocks.SMELTER.get(), props));
        
        BLOCK_ITEMS.registerItem("extractor", props -> 
            new BlockItem(MachineryBlocks.EXTRACTOR.get(), props));
        
        // Register the block items deferred register
        BLOCK_ITEMS.register(GalacticMachinery.MOD_EVENT_BUS);
    }
}