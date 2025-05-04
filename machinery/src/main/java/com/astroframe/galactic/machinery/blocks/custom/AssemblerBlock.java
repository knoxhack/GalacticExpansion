package com.astroframe.galactic.machinery.blocks.custom;

import com.astroframe.galactic.machinery.GalacticMachinery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.Nullable;

/**
 * The assembler block for creating modular machines.
 */
public class AssemblerBlock extends Block {

    // State properties
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    /**
     * Creates a new AssemblerBlock with the given properties.
     *
     * @param properties The block properties
     */
    public AssemblerBlock(Properties properties) {
        // In NeoForge 1.21.5, we need to make sure the properties are fully initialized before passing to super
        super(properties);
        
        // Register default state for powered and facing properties
        this.registerDefaultState(this.stateDefinition.any()
            .setValue(POWERED, Boolean.FALSE)
            .setValue(FACING, Direction.NORTH));
    }

    /**
     * Create the block state definition with our properties
     */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }
    
    /**
     * Get the state for when the block is placed
     */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
            .setValue(FACING, context.getHorizontalDirection().getOpposite())
            .setValue(POWERED, false);
    }
}