package com.astroframe.galactic.utilities.registry;

import com.astroframe.galactic.utilities.GalacticUtilities;
import com.astroframe.galactic.utilities.blocks.DataLoggerBlock;
import com.astroframe.galactic.utilities.blocks.SimpleUtilityBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry class for all blocks in the Utilities module.
 */
public class UtilitiesBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK, GalacticUtilities.MOD_ID);

    // Register the simple utility block
    public static final DeferredRegister.BlockHelper<Block> SIMPLE_UTILITY_BLOCK = BLOCKS.registerSimpleBlock(
            "simple_utility_block", SimpleUtilityBlock::new);
    
    // Register the data logger block
    public static final DeferredRegister.BlockHelper<Block> DATA_LOGGER = BLOCKS.registerSimpleBlock(
            "data_logger", DataLoggerBlock::new);
    
    /**
     * Register all blocks with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}