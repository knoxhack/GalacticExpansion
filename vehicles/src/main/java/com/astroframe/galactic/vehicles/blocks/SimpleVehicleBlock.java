package com.astroframe.galactic.vehicles.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.core.Direction;

import java.util.function.Function;

/**
 * A simple vehicle block that faces in a direction.
 * This is a placeholder block without complex functionality.
 */
public class SimpleVehicleBlock extends HorizontalDirectionalBlock {
    // Use the FACING property directly from the parent class
    
    public static final MapCodec<SimpleVehicleBlock> CODEC = simpleCodec(
        props -> new SimpleVehicleBlock());
    
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
    
    public SimpleVehicleBlock() {
        super(Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(3.5f, 3.5f)
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