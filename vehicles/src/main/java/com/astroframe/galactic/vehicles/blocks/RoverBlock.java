package com.astroframe.galactic.vehicles.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;

/**
 * A space rover block.
 * This is a placeholder implementation to be expanded with proper vehicle mechanics.
 */
public class RoverBlock extends HorizontalDirectionalBlock {
    // Use the FACING property directly from the parent class
    
    public static final MapCodec<RoverBlock> CODEC = simpleCodec(
        props -> new RoverBlock());
    
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    
    public RoverBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(4.0f, 4.0f)
                .requiresCorrectToolForDrops());
    }
    
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
    
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
}