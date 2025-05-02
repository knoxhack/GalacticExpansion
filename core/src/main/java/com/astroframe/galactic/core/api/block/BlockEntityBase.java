package com.astroframe.galactic.core.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Base class for all block entities in the mod.
 * Handles NeoForge 1.21.5-specific implementations for serialization/deserialization.
 */
public abstract class BlockEntityBase extends BlockEntity {
    
    /**
     * Constructor for BlockEntityBase.
     * 
     * @param type The block entity type
     * @param pos The block position
     * @param state The block state
     */
    public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    /**
     * Handle loading in NeoForge 1.21.5.
     * This method properly implements loading with the Provider parameter.
     */
    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        // Call custom load implementation that's version-agnostic
        this.loadData(tag);
    }
    
    /**
     * Handle saving in NeoForge 1.21.5.
     * This method properly implements saving with the Provider parameter.
     */
    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        // Call custom save implementation that's version-agnostic
        this.saveData(tag);
    }
    
    /**
     * Custom method for loading entity data.
     * Implement this in subclasses instead of loadAdditional to avoid version compatibility issues.
     * 
     * @param tag The tag to load data from
     */
    protected abstract void loadData(CompoundTag tag);
    
    /**
     * Custom method for saving entity data.
     * Implement this in subclasses instead of saveAdditional to avoid version compatibility issues.
     * 
     * @param tag The tag to save data to
     */
    protected abstract void saveData(CompoundTag tag);
    
    /**
     * Helper method for creating an update packet for this block entity.
     * 
     * @return The update packet
     */
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    /**
     * Helper method for getting the update tag for this block entity.
     * 
     * @return The update tag
     */
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveData(tag);
        return tag;
    }
}