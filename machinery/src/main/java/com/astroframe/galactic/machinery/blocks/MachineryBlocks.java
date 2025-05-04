
package com.astroframe.galactic.machinery.blocks;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.custom.AssemblerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;

/**
 * Registry class for all blocks in the Machinery module.
 */
public class MachineryBlocks {

    // Deferred Register for blocks
    private static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticMachinery.MOD_ID);

    // Machinery Blocks with deferred holders - must match resource file names
    public static final DeferredHolder<Block, Block> ASSEMBLER = BLOCKS.register(
        "assembler", 
        () -> new AssemblerBlock(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .strength(3.5f)
            .requiresCorrectToolForDrops()
            .sound(SoundType.METAL))
    );

    public static final DeferredHolder<Block, Block> CRUSHER = BLOCKS.register(
        "crusher", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL))
    );

    public static final DeferredHolder<Block, Block> CENTRIFUGE = BLOCKS.register(
        "centrifuge", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL))
    );

    public static final DeferredHolder<Block, Block> SMELTER = BLOCKS.register(
        "smelter", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL))
    );

    public static final DeferredHolder<Block, Block> EXTRACTOR = BLOCKS.register(
        "extractor", 
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F, 6.0F)
            .sound(SoundType.METAL))
    );

    /**
     * Register the block items for this module.
     * Called during the MachineryRegistry setup.
     */
    public static void registerBlockItems() {
        // Register block items
        GalacticMachinery.LOGGER.info("Registering machinery block items");
    }

    /**
     * Initializes the blocks registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery blocks");
        BLOCKS.register(eventBus);
    }
}
