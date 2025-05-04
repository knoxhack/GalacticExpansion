package com.astroframe.galactic.machinery.blocks;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.custom.AssemblerBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all blocks in the Machinery module.
 */
public class MachineryBlocks {

    // Deferred Register for blocks - use simple create method instead of createBlocks
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticMachinery.MOD_ID);
    
    /**
     * Create standard machinery block properties
     * @return Properties for machinery blocks
     */
    private static BlockBehaviour.Properties createStandardProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F) 
            .sound(SoundType.METAL);
    }

    // Create blocks using DeferredBlock pattern which ensures proper registry ID
    public static final DeferredBlock<Block> ASSEMBLER = BLOCKS.registerBlock(
        "assembler_block", 
        () -> new AssemblerBlock(createStandardProperties())
    );

    public static final DeferredBlock<Block> CRUSHER = BLOCKS.registerBlock(
        "crusher_block", 
        () -> new Block(createStandardProperties())
    );

    public static final DeferredBlock<Block> CENTRIFUGE = BLOCKS.registerBlock(
        "centrifuge_block", 
        () -> new Block(createStandardProperties())
    );

    public static final DeferredBlock<Block> SMELTER = BLOCKS.registerBlock(
        "smelter_block", 
        () -> new Block(createStandardProperties())
    );

    public static final DeferredBlock<Block> EXTRACTOR = BLOCKS.registerBlock(
        "extractor_block", 
        () -> new Block(createStandardProperties())
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