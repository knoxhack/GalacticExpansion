package com.astroframe.galactic.machinery.blocks;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.custom.AssemblerBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all blocks in the Machinery module.
 */
public class MachineryBlocks {

    // Deferred Register for blocks
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

    // Create blocks using standard Supplier pattern with proper debugging
    public static final Supplier<Block> ASSEMBLER = BLOCKS.register(
        "assembler_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating assembler block with ID: galacticmachinery:assembler_block");
            return new AssemblerBlock(createStandardProperties());
        }
    );

    public static final Supplier<Block> CRUSHER = BLOCKS.register(
        "crusher_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating crusher block with ID: galacticmachinery:crusher_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> CENTRIFUGE = BLOCKS.register(
        "centrifuge_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating centrifuge block with ID: galacticmachinery:centrifuge_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> SMELTER = BLOCKS.register(
        "smelter_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating smelter block with ID: galacticmachinery:smelter_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> EXTRACTOR = BLOCKS.register(
        "extractor_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating extractor block with ID: galacticmachinery:extractor_block");
            return new Block(createStandardProperties());
        }
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