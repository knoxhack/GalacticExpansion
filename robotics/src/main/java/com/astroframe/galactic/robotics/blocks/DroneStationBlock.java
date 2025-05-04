package com.astroframe.galactic.robotics.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * A specialized block for drone programming and deployment.
 * This is a placeholder implementation that will be enhanced with
 * actual drone functionality in the future.
 */
public class DroneStationBlock extends Block {
    
    public static final MapCodec<DroneStationBlock> CODEC = simpleCodec(
        props -> new DroneStationBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for DroneStationBlock.
     */
    public DroneStationBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(3.5F, 5.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }
}