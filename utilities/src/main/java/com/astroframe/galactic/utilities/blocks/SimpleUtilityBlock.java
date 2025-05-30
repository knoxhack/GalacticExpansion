package com.astroframe.galactic.utilities.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * A simple utility block implementation.
 * This is a placeholder until more complex functionality is implemented.
 */
public class SimpleUtilityBlock extends Block {
    
    public static final MapCodec<SimpleUtilityBlock> CODEC = simpleCodec(
        props -> new SimpleUtilityBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for SimpleUtilityBlock.
     */
    public SimpleUtilityBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(2.0F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }
}