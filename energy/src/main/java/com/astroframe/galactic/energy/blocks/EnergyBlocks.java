package com.astroframe.galactic.energy.blocks;

import com.astroframe.galactic.energy.GalacticEnergy;
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
 * Registry for all blocks in the Energy module.
 * For compatibility with NeoForge 1.21.5, we're using disabled implementations
 * until the custom functionality is implemented.
 */
public class EnergyBlocks {

    // Deferred Register for blocks
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticEnergy.MOD_ID);
    
    /**
     * Create standard energy block properties
     * @return Properties for energy module blocks
     */
    private static BlockBehaviour.Properties createStandardProperties() {
        return BlockBehaviour.Properties.of()
            .mapColor(MapColor.COLOR_BLUE)
            .requiresCorrectToolForDrops()
            .strength(5.0F) 
            .sound(SoundType.METAL);
    }

    // TEMPORARY: Using disabled blocks for NeoForge 1.21.5 compatibility
    // Custom implementations will be added later when the compatibility issues are resolved.
    
    public static final Supplier<Block> ENERGY_TRANSFORMER = BLOCKS.register(
        "energy_transformer_block", 
        () -> {
            GalacticEnergy.LOGGER.debug("Creating energy transformer block with ID: galacticenergy:energy_transformer_block");
            ResourceLocation id = ResourceLocation.parse(GalacticEnergy.MOD_ID + ":" + "energy_transformer_block");
            return new DisabledEnergyBlock(createStandardProperties(), "transformer", id);
        }
    );
    
    public static final Supplier<Block> ENERGY_EMITTER = BLOCKS.register(
        "energy_emitter_block", 
        () -> {
            GalacticEnergy.LOGGER.debug("Creating energy emitter block with ID: galacticenergy:energy_emitter_block");
            ResourceLocation id = ResourceLocation.parse(GalacticEnergy.MOD_ID + ":" + "energy_emitter_block");
            return new DisabledEnergyBlock(createStandardProperties(), "emitter", id);
        }
    );

    /**
     * Register the block items for this module.
     * Called during the EnergyRegistry setup.
     */
    public static void registerBlockItems() {
        // Register block items
        GalacticEnergy.LOGGER.info("Registering energy block items");
    }

    /**
     * Initializes the blocks registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticEnergy.LOGGER.info("Registering energy blocks (basic placeholders only)");
        BLOCKS.register(eventBus);
    }
}