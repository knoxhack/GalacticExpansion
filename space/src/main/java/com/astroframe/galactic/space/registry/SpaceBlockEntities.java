package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all block entities in the Space module.
 * Currently disabled to avoid errors with simplified block implementations.
 */
public class SpaceBlockEntities {
    
    // Create a deferred register for block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticSpace.MOD_ID);
    
    // Block entity registration is disabled since we're using simplified blocks
    // without block entity functionality to avoid errors
    
    /**
     * Register the block entity registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}