package com.astroframe.galactic.power.blocks;

import com.astroframe.galactic.machinery.blocks.DisabledMachineBlock;
import com.astroframe.galactic.power.GalacticPower;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all blocks in the Power module.
 * For compatibility with NeoForge 1.21.5, we're using disabled implementations
 * until the custom functionality is implemented.
 */
public class PowerBlocks {

    // Deferred Register for blocks
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticPower.MOD_ID);
    
    /**
     * Create standard power block properties
     * @return Properties for power module blocks
     */
    private static BlockBehaviour.Properties createStandardProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(5.0F) 
            .sound(SoundType.METAL);
    }

    // TEMPORARY: Using disabled blocks for NeoForge 1.21.5 compatibility
    // Custom implementations will be added later when the compatibility issues are resolved.
    
    public static final Supplier<Block> BASIC_GENERATOR = BLOCKS.register(
        "basic_generator_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating basic generator block with ID: galacticpower:basic_generator_block");
            ResourceLocation id = ResourceLocation.parse(GalacticPower.MOD_ID + ":" + "basic_generator_block");
            return new DisabledMachineBlock(createStandardProperties(), "generator", id);
        }
    );

    /**
     * Register the block items for this module.
     * Called during the PowerRegistry setup.
     */
    public static void registerBlockItems() {
        // Register block items
        GalacticPower.LOGGER.info("Registering power block items");
    }

    /**
     * Initializes the blocks registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticPower.LOGGER.info("Registering power blocks (basic placeholders only)");
        BLOCKS.register(eventBus);
    }
}