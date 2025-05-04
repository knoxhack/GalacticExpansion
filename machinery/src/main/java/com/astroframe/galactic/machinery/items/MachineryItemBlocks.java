package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;

/**
 * Registry class for block items in the Machinery module.
 */
public class MachineryItemBlocks {

    // Create a DeferredRegister for items
    private static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(ForgeRegistries.ITEMS, GalacticMachinery.MOD_ID);

    /**
     * Initializes block items for the machinery module.
     * Called during registry phase.
     */
    public static void init() {
        GalacticMachinery.LOGGER.info("Initializing machinery block items");

        try {
            // Register block items with proper item properties
            // Assembler block item
            BLOCK_ITEMS.register("assembler", 
                () -> new BlockItem(MachineryBlocks.ASSEMBLER.get(), new Item.Properties()));

            // Crusher block item
            BLOCK_ITEMS.register("crusher_block",
                () -> new BlockItem(MachineryBlocks.CRUSHER.get(), new Item.Properties()));

            // Centrifuge block item
            BLOCK_ITEMS.register("centrifuge_block",
                    () -> new BlockItem(MachineryBlocks.CENTRIFUGE.get(), new Item.Properties()));

            // Smelter block item
            BLOCK_ITEMS.register("smelter_block",
                    () -> new BlockItem(MachineryBlocks.SMELTER.get(), new Item.Properties()));

            // Extractor block item
            BLOCK_ITEMS.register("extractor_block",
                    () -> new BlockItem(MachineryBlocks.EXTRACTOR.get(), new Item.Properties()));


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