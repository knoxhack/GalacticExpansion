package com.astroframe.galactic.machinery.blocks;

import com.astroframe.galactic.machinery.GalacticMachinery;
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
 * For compatibility with NeoForge 1.21.5, we're using basic Block implementations until
 * the custom implementations are fixed.
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

    // TEMPORARY: Using simple blocks for NeoForge 1.21.5 compatibility
    // More complex custom implementations have been moved to the .disabled directories and will be
    // reimplemented later when the compatibility issues are resolved.
    
    public static final Supplier<Block> ASSEMBLER = BLOCKS.register(
        "assembler_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating simple assembler block for galacticmachinery:assembler_block");
            return new SimpleMachineBlock(createStandardProperties(), "assembler");
        }
    );

    public static final Supplier<Block> CRUSHER = BLOCKS.register(
        "crusher_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating simple crusher block for galacticmachinery:crusher_block");
            return new SimpleMachineBlock(createStandardProperties(), "crusher");
        }
    );

    public static final Supplier<Block> CENTRIFUGE = BLOCKS.register(
        "centrifuge_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating simple centrifuge block for galacticmachinery:centrifuge_block");
            return new SimpleMachineBlock(createStandardProperties(), "centrifuge");
        }
    );

    public static final Supplier<Block> SMELTER = BLOCKS.register(
        "smelter_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating simple smelter block for galacticmachinery:smelter_block");
            return new SimpleMachineBlock(createStandardProperties(), "smelter");
        }
    );

    public static final Supplier<Block> EXTRACTOR = BLOCKS.register(
        "extractor_block", 
        () -> {
            GalacticMachinery.LOGGER.debug("Creating simple extractor block for galacticmachinery:extractor_block");
            return new SimpleMachineBlock(createStandardProperties(), "extractor");
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
        GalacticMachinery.LOGGER.info("Registering machinery blocks (basic placeholders only)");
        BLOCKS.register(eventBus);
    }
}