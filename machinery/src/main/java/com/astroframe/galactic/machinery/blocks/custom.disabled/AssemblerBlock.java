package com.astroframe.galactic.machinery.blocks.custom;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blockentity.AssemblerBlockEntity;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

/**
 * The assembler block for creating modular machines.
 */
public class AssemblerBlock extends Block implements EntityBlock {

    // State properties
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    /**
     * Creates a new AssemblerBlock with the given properties.
     *
     * @param properties The block properties
     */
    public AssemblerBlock(Properties properties) {
        // In NeoForge 1.21.5, we need to make sure the properties are fully initialized before passing to super
        super(properties);
        
        // Register default state for powered property
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(POWERED, Boolean.FALSE));
    }

    /**
     * Create the block state definition with our properties
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
    
    /**
     * Get the state for when the block is placed
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(POWERED, false);
    }
    
    /**
     * Create a block entity for this block
     */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AssemblerBlockEntity(pos, state);
    }
    
    /**
     * Get the ticker for the block entity
     */
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        // As we're temporarily disabling block entities, this needs to be safer
        // and avoid potential null pointer exceptions
        if (blockEntityType == MachineryBlockEntities.getAssemblerType() && MachineryBlockEntities.getAssemblerType() != null) {
            return (lvl, pos, blockState, blockEntity) -> {
                if (blockEntity instanceof AssemblerBlockEntity assemblerEntity) {
                    assemblerEntity.serverTick(lvl, pos, blockState);
                }
            };
        }
        return null;
    }
}