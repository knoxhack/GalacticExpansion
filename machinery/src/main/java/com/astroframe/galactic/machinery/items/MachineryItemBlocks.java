package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all block items in the Machinery module.
 * These are items that represent placeable blocks.
 */
public class MachineryItemBlocks {

    // Deferred Register for block items - switching to create for consistency
    private static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);

    // Machinery Block Items - corresponding to blocks from MachineryBlocks
    // Update block item registration to match block registration IDs
    public static final Supplier<Item> ASSEMBLER_ITEM = BLOCK_ITEMS.register(
        "assembler_block",
        () -> new BlockItem(MachineryBlocks.ASSEMBLER.get(), new Item.Properties())
    );

    public static final Supplier<Item> CRUSHER_ITEM = BLOCK_ITEMS.register(
        "crusher_block",
        () -> new BlockItem(MachineryBlocks.CRUSHER.get(), new Item.Properties())
    );

    public static final Supplier<Item> CENTRIFUGE_ITEM = BLOCK_ITEMS.register(
        "centrifuge_block",
        () -> new BlockItem(MachineryBlocks.CENTRIFUGE.get(), new Item.Properties())
    );

    public static final Supplier<Item> SMELTER_ITEM = BLOCK_ITEMS.register(
        "smelter_block",
        () -> new BlockItem(MachineryBlocks.SMELTER.get(), new Item.Properties())
    );

    public static final Supplier<Item> EXTRACTOR_ITEM = BLOCK_ITEMS.register(
        "extractor_block",
        () -> new BlockItem(MachineryBlocks.EXTRACTOR.get(), new Item.Properties())
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