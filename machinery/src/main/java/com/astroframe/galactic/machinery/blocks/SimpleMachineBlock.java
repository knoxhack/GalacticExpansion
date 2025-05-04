package com.astroframe.galactic.machinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A simplified implementation for machine blocks that avoids the ResourceLocation dependency
 * which was causing NeoForge 1.21.5 compatibility issues.
 */
public class SimpleMachineBlock extends Block {
    private final String machineType;
    
    /**
     * Constructs a new simple machine block.
     * This is a basic implementation that avoids compatibility issues.
     *
     * @param properties Block properties
     * @param machineType Type of the machine (e.g., "assembler", "crusher")
     */
    public SimpleMachineBlock(Properties properties, String machineType) {
        super(properties);
        this.machineType = machineType;
    }
    
    /**
     * Gets the type of machine.
     *
     * @return Machine type string
     */
    public String getMachineType() {
        return machineType;
    }
    
    @Override
    public String toString() {
        return "SimpleMachineBlock(" + machineType + ")";
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Default block shape
        return super.getShape(state, level, pos, context);
    }
}