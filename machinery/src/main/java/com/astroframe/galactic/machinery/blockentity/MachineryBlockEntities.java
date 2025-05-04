package com.astroframe.galactic.machinery.blockentity;

import com.astroframe.galactic.machinery.GalacticMachinery;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all block entities in the Machinery module.
 * TEMPORARILY DISABLED for NeoForge 1.21.5 compatibility until issues with block ID handling are fixed.
 */
public class MachineryBlockEntities {
    
    // Deferred Register for block entities
    private static final DeferredRegister<net.minecraft.world.level.block.entity.BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticMachinery.MOD_ID);
    
    // Block entities are temporarily disabled for compatibility
    
    /**
     * Initializes the block entities registry.
     * Currently a stub until block entities can be properly implemented.
     * 
     * @param eventBus The event bus to register with
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Block entities temporarily disabled for compatibility");
        // Still register the event bus to avoid null pointer exceptions
        BLOCK_ENTITIES.register(eventBus);
    }
}
