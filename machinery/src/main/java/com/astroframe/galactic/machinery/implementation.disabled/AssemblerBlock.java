package com.astroframe.galactic.machinery.implementation;

import com.astroframe.galactic.machinery.api.MachineBlock;
import com.astroframe.galactic.machinery.api.MachineBlockEntity;
import com.astroframe.galactic.machinery.blockentity.AssemblerBlockEntity;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
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
    
    private final ResourceLocation blockId;
    
    /**
     * Creates a new Assembler block with explicit ID.
     *
     * @param properties The block properties
     * @param blockId The explicit block ID as ResourceLocation
     */
    public AssemblerBlock(Properties properties, ResourceLocation blockId) {
        // Break the circular dependency by using a Supplier that's evaluated later
        // Rather than directly referencing MachineryBlockEntities.ASSEMBLER.get() which causes issues
        super(properties, MachineryBlockEntities::getAssemblerType);
        this.blockId = blockId;
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
    
    /**
     * Gets the block ID.
     * This is needed in NeoForge 1.21.5 to prevent null ID issues.
     *
     * @return The block ID
     */
    public ResourceLocation getBlockId() {
        return blockId;
    }
}