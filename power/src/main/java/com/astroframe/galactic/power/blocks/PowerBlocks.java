package com.astroframe.galactic.power.blocks;

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
 * For compatibility with NeoForge 1.21.5, we're using basic Block implementations until
 * the custom implementations are fixed.
 */
public class PowerBlocks {

    // Deferred Register for blocks
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticPower.MOD_ID);
    
    /**
     * Create standard power block properties
     * @return Properties for power blocks
     */
    private static BlockBehaviour.Properties createStandardProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.METAL)
            .requiresCorrectToolForDrops()
            .strength(4.5F) 
            .sound(SoundType.METAL);
    }

    // TEMPORARY: Using basic blocks for NeoForge 1.21.5 compatibility
    // Custom implementations will be added later when the compatibility issues are resolved.
    
    public static final Supplier<Block> BASIC_GENERATOR_BLOCK = BLOCKS.register(
        "basic_generator_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating basic generator block with ID: galacticpower:basic_generator_block");
            ResourceLocation id = ResourceLocation.parse(GalacticPower.MOD_ID + ":" + "basic_generator_block");
            return new Block(createStandardProperties()) {
                @Override
                public String toString() {
                    return "BasicGeneratorBlock(placeholder):" + id;
                }
            };
        }
    );

    public static final Supplier<Block> ADVANCED_GENERATOR_BLOCK = BLOCKS.register(
        "advanced_generator_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating advanced generator block with ID: galacticpower:advanced_generator_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> SOLAR_PANEL_BLOCK = BLOCKS.register(
        "solar_panel_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating solar panel block with ID: galacticpower:solar_panel_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> FUSION_REACTOR_BLOCK = BLOCKS.register(
        "fusion_reactor_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating fusion reactor block with ID: galacticpower:fusion_reactor_block");
            return new Block(createStandardProperties());
        }
    );

    public static final Supplier<Block> ENERGY_STORAGE_BLOCK = BLOCKS.register(
        "energy_storage_block", 
        () -> {
            GalacticPower.LOGGER.debug("Creating energy storage block with ID: galacticpower:energy_storage_block");
            return new Block(createStandardProperties());
        }
    );

    /**
     * Initializes the blocks registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticPower.LOGGER.info("Registering power blocks (basic placeholders only)");
        BLOCKS.register(eventBus);
    }
}