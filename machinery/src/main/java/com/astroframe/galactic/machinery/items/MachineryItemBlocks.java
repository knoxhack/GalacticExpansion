package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all block items in the Machinery module.
 * These are items that represent placeable blocks.
 */
public class MachineryItemBlocks {

    // Deferred Register for block items
    public static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);
    
    // Use DeferredItem for safer registration and access
    public static final DeferredItem<BlockItem> ASSEMBLER_ITEM = BLOCK_ITEMS.registerItem(
        "assembler_block",
        () -> new BlockItem(MachineryBlocks.ASSEMBLER.get(), new Item.Properties())
    );

    public static final DeferredItem<BlockItem> CRUSHER_ITEM = BLOCK_ITEMS.registerItem(
        "crusher_block",
        () -> new BlockItem(MachineryBlocks.CRUSHER.get(), new Item.Properties())
    );

    public static final DeferredItem<BlockItem> CENTRIFUGE_ITEM = BLOCK_ITEMS.registerItem(
        "centrifuge_block",
        () -> new BlockItem(MachineryBlocks.CENTRIFUGE.get(), new Item.Properties())
    );

    public static final DeferredItem<BlockItem> SMELTER_ITEM = BLOCK_ITEMS.registerItem(
        "smelter_block",
        () -> new BlockItem(MachineryBlocks.SMELTER.get(), new Item.Properties())
    );

    public static final DeferredItem<BlockItem> EXTRACTOR_ITEM = BLOCK_ITEMS.registerItem(
        "extractor_block",
        () -> new BlockItem(MachineryBlocks.EXTRACTOR.get(), new Item.Properties())
    );

    /**
     * Initializes the block items registry.
     * Called during the module's registry phase.
     * 
     * @param eventBus The event bus to register with
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery block items");
        // This must be called after MachineryBlocks.init in the registry setup
        BLOCK_ITEMS.register(eventBus);
    }
}