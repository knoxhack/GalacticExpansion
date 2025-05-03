package com.astroframe.galactic.machinery.implementation;

import com.astroframe.galactic.machinery.api.MachineBlock;
import com.astroframe.galactic.machinery.api.MachineBlockEntity;
import com.astroframe.galactic.machinery.blockentity.AssemblerBlockEntity;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Block implementation for the Assembler machine.
 */
public class AssemblerBlock extends MachineBlock {
    
    /**
     * Creates a new Assembler block.
     *
     * @param properties The block properties
     */
    public AssemblerBlock(Properties properties) {
        super(properties, () -> MachineryBlockEntities.ASSEMBLER.get());
    }
    
    /**
     * Creates a new block entity for this block.
     *
     * @param pos The position
     * @param state The block state
     * @return The block entity
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AssemblerBlockEntity(pos, state);
    }
    
    /**
     * Gets the render shape for this block.
     * 
     * @param state The block state
     * @return The render shape
     */
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
    
    /**
     * Creates a ticker for the block entity.
     *
     * @param level The level
     * @param state The block state
     * @param blockEntityType The block entity type
     * @return The ticker
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return createMachineTicker(level, state, blockEntityType);
    }
}