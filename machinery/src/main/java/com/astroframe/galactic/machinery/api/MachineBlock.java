package com.astroframe.galactic.machinery.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Base class for all machine blocks in the mod.
 * This provides common functionality for machines.
 */
public abstract class MachineBlock extends net.minecraft.world.level.block.Block implements EntityBlock {
    
    private final Supplier<BlockEntityType<?>> blockEntityTypeSupplier;
    
    /**
     * Creates a new MachineBlock with the specified properties.
     *
     * @param properties The block properties
     * @param blockEntityTypeSupplier Supplier for the BlockEntityType
     */
    public MachineBlock(Properties properties, Supplier<BlockEntityType<?>> blockEntityTypeSupplier) {
        super(properties);
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }
    
    /**
     * Creates a new MachineBlock with the specified properties.
     *
     * @param properties The block properties
     */
    public MachineBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.blockEntityTypeSupplier = () -> null;
    }
    
    /**
     * Helper method to create a ticker for machine block entities.
     * This handles the casting required.
     *
     * @param <T> The BlockEntity type
     * @param level The level
     * @param state The block state
     * @param blockEntityType The block entity type
     * @return The ticker or null if the types don't match
     */
    @Nullable
    protected <T extends BlockEntity> BlockEntityTicker<T> createMachineTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        if (level.isClientSide()) {
            return null;
        }
        
        return (world, pos, blockState, blockEntity) -> {
            if (blockEntity instanceof MachineBlockEntity machineEntity) {
                machineEntity.serverTick(world, pos, blockState);
            }
        };
    }
}