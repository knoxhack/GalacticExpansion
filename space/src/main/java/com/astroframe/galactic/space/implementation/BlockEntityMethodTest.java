package com.astroframe.galactic.space.implementation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Simple test class to check what BlockEntity methods are available
 * in NeoForge 1.21.5
 */
public class BlockEntityMethodTest extends BlockEntity {
    
    public BlockEntityMethodTest(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }
    
    // Check loadAdditional method
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        // This is the method we should be implementing
    }
    
    // Check saveAdditional method
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // This is the method we should be implementing
    }
}