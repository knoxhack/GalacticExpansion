package com.astroframe.galactic.energy.blocks;

import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.registry.EnergyRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry handler for all Energy module blocks.
 */
public class EnergyBlocks {

    // Block registry
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, GalacticEnergy.MOD_ID);

    // Energy Transformer - Converts between energy types
    public static final Supplier<Block> ENERGY_TRANSFORMER_BLOCK = BLOCKS.register("energy_transformer_block", 
            DisabledEnergyBlock::new);
    
    // Energy Emitter - Projects energy across distance
    public static final Supplier<Block> ENERGY_EMITTER_BLOCK = BLOCKS.register("energy_emitter_block", 
            DisabledEnergyBlock::new);

    /**
     * Initialize the block registry
     * @param eventBus The event bus to register on
     */
    public static void init(IEventBus eventBus) {
        GalacticEnergy.LOGGER.info("Registering Energy blocks");
        BLOCKS.register(eventBus);
    }
    
    /**
     * Register block items for all blocks
     */
    public static void registerBlockItems() {
        GalacticEnergy.LOGGER.info("Registering Energy block items");
        
        // Register block items
        EnergyRegistry.ITEMS.register("energy_transformer_block", 
                () -> new BlockItem(ENERGY_TRANSFORMER_BLOCK.get(), EnergyRegistry.createStandardItemProperties()));
        
        EnergyRegistry.ITEMS.register("energy_emitter_block", 
                () -> new BlockItem(ENERGY_EMITTER_BLOCK.get(), EnergyRegistry.createStandardItemProperties()));
    }
}