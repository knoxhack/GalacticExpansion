package com.astroframe.galactic.power.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A placeholder implementation for power blocks with disabled functionality.
 * Used in place of the actual functional blocks until NeoForge compatibility issues are resolved.
 */
public class DisabledPowerBlock extends Block {
    private final String powerType;
    private final ResourceLocation powerId;
    
    /**
     * Constructs a new disabled power block.
     * This is a placeholder for power blocks whose functional implementations have been disabled.
     *
     * @param properties Block properties
     * @param powerType Type of the power block (e.g., "generator", "battery")
     * @param powerId The resource location ID for the block
     */
    public DisabledPowerBlock(Properties properties, String powerType, ResourceLocation powerId) {
        super(properties);
        this.powerType = powerType;
        this.powerId = powerId;
    }
    
    /**
     * Gets the type of power block.
     *
     * @return Power type string
     */
    public String getPowerType() {
        return powerType;
    }
    
    /**
     * Gets the power block ID.
     *
     * @return Resource location
     */
    public ResourceLocation getPowerId() {
        return powerId;
    }
    
    @Override
    public String toString() {
        return "DisabledPowerBlock(" + powerType + "):" + powerId;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Default block shape
        return super.getShape(state, level, pos, context);
    }
}