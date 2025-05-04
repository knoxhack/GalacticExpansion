package com.astroframe.galactic.energy.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

/**
 * Base class for Energy blocks with functionality disabled.
 * This is used as a placeholder until full functionality is implemented.
 */
public class DisabledEnergyBlock extends Block {
    
    public DisabledEnergyBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_BLUE)
                .strength(3.5F, 6.0F)
                .sound(SoundType.METAL)
                .requiresCorrectToolForDrops()
        );
    }
}