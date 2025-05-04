package com.astroframe.galactic.machinery.blockentity;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.Set;

/**
 * Registry for all block entities in the Machinery module.
 */
public class MachineryBlockEntities {
    
    // Deferred Register for block entities
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GalacticMachinery.MOD_ID);
    
    // Store the instance once created to avoid duplicate creation
    private static BlockEntityType<AssemblerBlockEntity> ASSEMBLER_TYPE = null;
    
    // Block entity types
    public static final Supplier<BlockEntityType<AssemblerBlockEntity>> ASSEMBLER = 
        BLOCK_ENTITIES.register(
            "assembler_block", 
            () -> {
                // Log a clear message about block entity creation
                GalacticMachinery.LOGGER.debug("Creating assembler block entity type");
                
                // Define a factory function for creating AssemblerBlockEntity instances
                BiFunction<BlockPos, BlockState, AssemblerBlockEntity> factory = 
                    (pos, state) -> new AssemblerBlockEntity(pos, state);
                
                try {
                    // Create a set of valid blocks for this entity type
                    // We need to ensure MachineryBlocks is initialized before using it
                    Block assemblerBlock = MachineryBlocks.ASSEMBLER.get();
                    String blockId = GalacticMachinery.MOD_ID + ":assembler_block";
                    GalacticMachinery.LOGGER.debug("Using assembler block for block entity with ID: " + blockId);
                    
                    Set<Block> validBlocks = Set.of(assemblerBlock);
                    
                    // Create block entity type with explicit factory and type parameter
                    // In NeoForge 1.21.5, use direct constructor with boolean false for dataSaver
                    ASSEMBLER_TYPE = new BlockEntityType<>(
                        factory::apply,
                        Set.of(assemblerBlock),
                        false
                    );
                } catch (Exception e) {
                    GalacticMachinery.LOGGER.error("Error creating assembler block entity", e);
                    // Create a fallback version with an empty set of blocks
                    ASSEMBLER_TYPE = new BlockEntityType<>(
                        factory::apply, 
                        Set.of(), 
                        false
                    );
                }
                
                return ASSEMBLER_TYPE;
            }
        );
    
    /**
     * Gets the assembler block entity type.
     * This method breaks the circular dependency with AssemblerBlock.
     * 
     * @return The block entity type for assemblers
     */
    public static BlockEntityType<AssemblerBlockEntity> getAssemblerType() {
        // If already created, return the instance
        if (ASSEMBLER_TYPE != null) {
            return ASSEMBLER_TYPE;
        }
        
        // Otherwise get from the supplier if registered
        try {
            return ASSEMBLER.get();
        } catch (Exception e) {
            // During initial registration, we may not have the type yet.
            // In that case, create a temporary type that will be replaced later.
            GalacticMachinery.LOGGER.debug("Creating temporary assembler block entity type");
            BiFunction<BlockPos, BlockState, AssemblerBlockEntity> factory = 
                (pos, state) -> new AssemblerBlockEntity(pos, state);
            
            // Use an empty set here since this is just a temporary registration
            ASSEMBLER_TYPE = new BlockEntityType<>(
                factory::apply, 
                Set.of(), // Empty set since this is temporary
                false
            );
            
            return ASSEMBLER_TYPE;
        }
    }
    
    /**
     * Initializes the block entities registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery block entities");
        BLOCK_ENTITIES.register(eventBus);
    }
}
