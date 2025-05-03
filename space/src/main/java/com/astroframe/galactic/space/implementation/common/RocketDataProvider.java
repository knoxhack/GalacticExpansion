package com.astroframe.galactic.space.implementation.common;

import com.astroframe.galactic.core.api.space.IRocket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Interface for blocks/items that can provide rocket data.
 * This abstraction helps avoid circular dependencies between components.
 */
public interface RocketDataProvider {
    
    /**
     * Gets the current rocket data as a CompoundTag.
     * 
     * @return The rocket data tag
     */
    CompoundTag getRocketDataTag();
    
    /**
     * Sets the rocket data from a CompoundTag.
     * 
     * @param tag The rocket data tag
     */
    void setRocketDataTag(CompoundTag tag);
    
    /**
     * Gets the constructed rocket instance.
     * 
     * @return The rocket instance or null if not available
     */
    IRocket getRocket();
    
    /**
     * Gets all component item stacks.
     * 
     * @return List of component item stacks
     */
    List<ItemStack> getComponentStacks();
    
    /**
     * Checks if this provider has a complete rocket.
     * 
     * @return true if rocket is complete, false otherwise
     */
    boolean hasCompleteRocket();
}