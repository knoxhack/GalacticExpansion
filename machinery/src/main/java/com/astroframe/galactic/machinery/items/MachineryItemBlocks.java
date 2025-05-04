package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all block items in the Machinery module.
 * These are items that represent placeable blocks.
 */
public class MachineryItemBlocks {

    // Deferred Register for block items
    private static final DeferredRegister.Items BLOCK_ITEMS = 
        DeferredRegister.createItems(GalacticMachinery.MOD_ID);

    // Machinery Block Items - corresponding to blocks from MachineryBlocks
    public static final DeferredHolder<Item, Item> ASSEMBLER_ITEM = BLOCK_ITEMS.registerItem(
        "assembler",
        properties -> new BlockItem(MachineryBlocks.ASSEMBLER.get(), properties)
    );

    public static final DeferredHolder<Item, Item> CRUSHER_ITEM = BLOCK_ITEMS.registerItem(
        "crusher",
        properties -> new BlockItem(MachineryBlocks.CRUSHER.get(), properties)
    );

    public static final DeferredHolder<Item, Item> CENTRIFUGE_ITEM = BLOCK_ITEMS.registerItem(
        "centrifuge",
        properties -> new BlockItem(MachineryBlocks.CENTRIFUGE.get(), properties)
    );

    public static final DeferredHolder<Item, Item> SMELTER_ITEM = BLOCK_ITEMS.registerItem(
        "smelter",
        properties -> new BlockItem(MachineryBlocks.SMELTER.get(), properties)
    );

    public static final DeferredHolder<Item, Item> EXTRACTOR_ITEM = BLOCK_ITEMS.registerItem(
        "extractor",
        properties -> new BlockItem(MachineryBlocks.EXTRACTOR.get(), properties)
    );

    /**
     * Initializes the block items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        GalacticMachinery.LOGGER.info("Registering machinery block items");

        // Register the block items deferred register
        BLOCK_ITEMS.register(GalacticMachinery.MOD_EVENT_BUS);
    }
}