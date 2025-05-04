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
        
        try {
            // Add debug logging to track registration process
            GalacticMachinery.LOGGER.debug("Beginning machinery block items registration");
            
            // Register block items for each machinery block with proper logging
            GalacticMachinery.LOGGER.debug("Registering assembler block item");
            BLOCK_ITEMS.registerItem("assembler_block", props -> 
                new BlockItem(MachineryBlocks.ASSEMBLER.get(), props));
            
            GalacticMachinery.LOGGER.debug("Registering crusher block item");
            BLOCK_ITEMS.registerItem("crusher_block", props -> 
                new BlockItem(MachineryBlocks.CRUSHER.get(), props));
            
            GalacticMachinery.LOGGER.debug("Registering centrifuge block item");
            BLOCK_ITEMS.registerItem("centrifuge_block", props -> 
                new BlockItem(MachineryBlocks.CENTRIFUGE.get(), props));
            
            GalacticMachinery.LOGGER.debug("Registering smelter block item");
            BLOCK_ITEMS.registerItem("smelter_block", props -> 
                new BlockItem(MachineryBlocks.SMELTER.get(), props));
            
            GalacticMachinery.LOGGER.debug("Registering extractor block item");
            BLOCK_ITEMS.registerItem("extractor_block", props -> 
                new BlockItem(MachineryBlocks.EXTRACTOR.get(), props));
            
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