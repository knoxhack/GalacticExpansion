package com.astroframe.galactic.space.implementation.common;

import com.astroframe.galactic.core.api.space.IRocket;
import net.minecraft.core.BlockPos;

/**
 * Interface for holographic projector functionality.
 * This is used to decouple implementations and avoid circular dependencies.
 * Provides the essential methods needed by other components to interact with projectors.
 */
public interface HolographicProjectorAccess {
    /**
     * Links the projector to an assembly table.
     *
     * @param tablePos The position of the assembly table, or null to unlink
     */
    void linkToAssemblyTable(BlockPos tablePos);
    
    /**
     * Gets the linked assembly table position.
     *
     * @return The position or null if not linked
     */
    BlockPos getLinkedTablePos();
    
    /**
     * Sets the rocket data to display in the projector.
     *
     * @param rocket The rocket data
     */
    void setRocketData(IRocket rocket);
    
    /**
     * Gets whether the projector is active.
     *
     * @return The active state
     */
    boolean isActive();
    
    /**
     * Sets whether the projector is active.
     *
     * @param active The active state
     */
    void setActive(boolean active);
}