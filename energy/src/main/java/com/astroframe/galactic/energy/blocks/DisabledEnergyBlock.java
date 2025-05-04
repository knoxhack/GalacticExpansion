package com.astroframe.galactic.energy.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A placeholder implementation for energy blocks with disabled functionality.
 * Used in place of the actual functional blocks until NeoForge compatibility issues are resolved.
 */
public class DisabledEnergyBlock extends Block {
    private final String energyType;
    private final ResourceLocation energyId;
    
    /**
     * Constructs a new disabled energy block.
     * This is a placeholder for energy blocks whose functional implementations have been disabled.
     *
     * @param properties Block properties
     * @param energyType Type of the energy block (e.g., "transformer", "emitter")
     * @param energyId The resource location ID for the block
     */
    public DisabledEnergyBlock(Properties properties, String energyType, ResourceLocation energyId) {
        super(properties);
        this.energyType = energyType;
        this.energyId = energyId;
    }
    
    /**
     * Gets the type of energy block.
     *
     * @return Energy type string
     */
    public String getEnergyType() {
        return energyType;
    }
    
    /**
     * Gets the energy block ID.
     *
     * @return Resource location
     */
    public ResourceLocation getEnergyId() {
        return energyId;
    }
    
    @Override
    public String toString() {
        return "DisabledEnergyBlock(" + energyType + "):" + energyId;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Default block shape
        return super.getShape(state, level, pos, context);
    }
}