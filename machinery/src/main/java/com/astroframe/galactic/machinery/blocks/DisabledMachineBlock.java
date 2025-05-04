package com.astroframe.galactic.machinery.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A placeholder implementation for machine blocks with disabled functionality.
 * Used in place of the actual functional blocks until NeoForge compatibility issues are resolved.
 */
public class DisabledMachineBlock extends Block {
    private final String machineType;
    private final ResourceLocation machineId;
    
    /**
     * Constructs a new disabled machine block.
     * This is a placeholder for machine blocks whose functional implementations have been disabled.
     *
     * @param properties Block properties
     * @param machineType Type of the machine (e.g., "assembler", "crusher")
     * @param machineId The resource location ID for the machine
     */
    public DisabledMachineBlock(Properties properties, String machineType, ResourceLocation machineId) {
        super(properties);
        this.machineType = machineType;
        this.machineId = machineId;
    }
    
    /**
     * Gets the type of machine.
     *
     * @return Machine type string
     */
    public String getMachineType() {
        return machineType;
    }
    
    /**
     * Gets the machine ID.
     *
     * @return Machine resource location
     */
    public ResourceLocation getMachineId() {
        return machineId;
    }
    
    @Override
    public String toString() {
        return "DisabledMachineBlock(" + machineType + "):" + machineId;
    }
    
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        // Default block shape
        return super.getShape(state, level, pos, context);
    }
}