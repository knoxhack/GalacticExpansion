package com.astroframe.galactic.utilities.registry;

import com.astroframe.galactic.utilities.GalacticUtilities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all items in the Utilities module.
 */
public class UtilitiesItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM, GalacticUtilities.MOD_ID);

    // Register block items
    public static final Supplier<Item> SIMPLE_UTILITY_BLOCK_ITEM = ITEMS.register(
            "simple_utility_block", 
            () -> new BlockItem(UtilitiesBlocks.SIMPLE_UTILITY_BLOCK.get(), new Item.Properties()));
    
    public static final Supplier<Item> DATA_LOGGER_ITEM = ITEMS.register(
            "data_logger", 
            () -> new BlockItem(UtilitiesBlocks.DATA_LOGGER.get(), new Item.Properties()));
    
    // Register regular items
    public static final Supplier<Item> HAND_SCANNER = ITEMS.register(
            "hand_scanner", 
            () -> new Item(new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> CIRCUIT_BOARD = ITEMS.register(
            "circuit_board", 
            () -> new Item(new Item.Properties()));
    
    /**
     * Register all items with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}