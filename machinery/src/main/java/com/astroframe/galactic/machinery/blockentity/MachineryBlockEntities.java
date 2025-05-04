package com.astroframe.galactic.machinery.blockentity;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all block entities in the Machinery module.
 */
public class MachineryBlockEntities {
    
    // Deferred Register for block entities
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticMachinery.MOD_ID);
    
    // Block entity types
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AssemblerBlockEntity>> ASSEMBLER = 
        BLOCK_ENTITIES.register(
            "assembler", 
            () -> BlockEntityType.builder(
                AssemblerBlockEntity::new, 
                MachineryBlocks.ASSEMBLER.get()
            ).build(null)
        );
    
    /**
     * Initializes the block entities registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery block entities");
        BLOCK_ENTITIES.register(eventBus);
    }
}