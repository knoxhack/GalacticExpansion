package com.astroframe.galactic.robotics.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * A specialized block for programming and managing robots.
 * This is a placeholder implementation that will be enhanced with
 * actual robot programming functionality in the future.
 */
public class RobotStationBlock extends Block {
    
    public static final MapCodec<RobotStationBlock> CODEC = simpleCodec(
        props -> new RobotStationBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for RobotStationBlock.
     */
    public RobotStationBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(4.0F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }
}