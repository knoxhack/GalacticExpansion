package com.astroframe.galactic.weaponry.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * A storage block for weapons in the Galactic mod.
 * This is a placeholder implementation that will be enhanced with
 * actual weapon storage functionality in the future.
 */
public class ArmoryBlock extends Block {
    
    public static final MapCodec<ArmoryBlock> CODEC = simpleCodec(
        props -> new ArmoryBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for ArmoryBlock.
     */
    public ArmoryBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(5.0F, 7.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }
}