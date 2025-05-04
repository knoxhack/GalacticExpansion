package com.astroframe.galactic.energy.blocks;

import com.astroframe.galactic.energy.GalacticEnergy;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

/**
 * Registry handler for all Energy module blocks.
 */
public class EnergyBlocks {

    // Block registry
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, GalacticEnergy.MODID);

    // Energy Transformer - Converts between energy types
    public static final Supplier<Block> ENERGY_TRANSFORMER_BLOCK = BLOCKS.register("energy_transformer_block", 
            DisabledEnergyBlock::new);
    
    // Energy Emitter - Projects energy across distance
    public static final Supplier<Block> ENERGY_EMITTER_BLOCK = BLOCKS.register("energy_emitter_block", 
            DisabledEnergyBlock::new);

    /**
     * Register all blocks in this class.
     * @param event The register event
     */
    public static void register(RegisterEvent event) {
        BLOCKS.register(event);
    }
}