package com.astroframe.galactic.weaponry.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * A specialized block for weapon crafting and modification.
 * This is a placeholder implementation that will be enhanced with
 * actual weapon crafting functionality in the future.
 */
public class WeaponStationBlock extends Block {
    
    public static final MapCodec<WeaponStationBlock> CODEC = simpleCodec(
        props -> new WeaponStationBlock());
    
    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }
    
    /**
     * Constructor for WeaponStationBlock.
     */
    public WeaponStationBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(4.0F, 6.0F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }
}