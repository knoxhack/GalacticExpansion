package com.astroframe.galactic.machinery.blockentity;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.Set;

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
            "assembler_block", 
            () -> {
                // Log a clear message about block entity creation
                GalacticMachinery.LOGGER.debug("Creating assembler block entity type");
                
                // Define a factory function for creating AssemblerBlockEntity instances
                BiFunction<BlockPos, BlockState, AssemblerBlockEntity> factory = 
                    (pos, state) -> new AssemblerBlockEntity(pos, state);
                
                // Create a set of valid blocks for this entity type
                // We need to ensure MachineryBlocks is initialized before using it
                Block assemblerBlock = MachineryBlocks.ASSEMBLER.get();
                Set<Block> validBlocks = Set.of(assemblerBlock);
                
                // Create block entity type with explicit factory and type parameter
                // In NeoForge 1.21.5, the third parameter is a boolean for dataSaver (not null)
                return BlockEntityType.Builder.of(
                    factory::apply, 
                    assemblerBlock
                ).build(null); // Using builder pattern which handles dataSaver correctly
            }
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
