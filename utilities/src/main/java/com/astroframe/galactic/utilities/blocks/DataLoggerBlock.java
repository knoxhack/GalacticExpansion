package com.astroframe.galactic.utilities.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

/**
 * A specialized utility block for logging data in the Galactic mod.
 * This is a placeholder implementation that will be enhanced with
 * actual data recording functionality in the future.
 */
public class DataLoggerBlock extends Block {
    
    public static final MapCodec<DataLoggerBlock> CODEC = simpleCodec(
        props -> new DataLoggerBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for DataLoggerBlock.
     */
    public DataLoggerBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .strength(2.5F, 6.0F)
                .requiresCorrectToolForDrops()
                .lightLevel(state -> 5)  // Emits a small amount of light
                .sound(SoundType.METAL));
    }
    
    /**
     * Called randomly when the block is in the world.
     * This is a placeholder that will be replaced with
     * actual data logging functionality.
     */
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        // Just a placeholder for now - will be implemented later
    }
}