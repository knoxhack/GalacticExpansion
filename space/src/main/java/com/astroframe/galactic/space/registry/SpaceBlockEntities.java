package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.Set;

/**
 * Registry for all block entities in the Space module.
 */
public class SpaceBlockEntities {
    
    // Create a deferred register for block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticSpace.MOD_ID);
    
    // Register the rocket assembly table block entity
    // In NeoForge 1.21.5, BlockEntityType.Builder.of() should be used instead of builder()
    public static final Supplier<BlockEntityType<RocketAssemblyTableBlockEntity>> ROCKET_ASSEMBLY_TABLE = 
            BLOCK_ENTITIES.register("rocket_assembly_table", 
                    () -> {
                        // In NeoForge 1.21.5, we need to create it directly without a builder
                        // Create a set of valid blocks for this entity type
                        Set<Block> validBlocks = Set.of(SpaceBlocks.ROCKET_ASSEMBLY_TABLE.get());
                        // Create the block entity type directly
                        return new BlockEntityType<>(
                            RocketAssemblyTableBlockEntity::new, 
                            validBlocks,
                            null
                        );
                    });
    
    /**
     * Register the block entity registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}