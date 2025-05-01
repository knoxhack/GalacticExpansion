package com.astroframe.galactic.core.api.energy;

import net.minecraft.core.Direction;

import java.util.Set;

/**
 * Interface for blocks or entities that can be linked to an energy network.
 * This allows for automatic detection and connection of energy-capable blocks.
 */
public interface IEnergyLinkable {
    
    /**
     * Determines if this block can connect to energy networks through the given face.
     * 
     * @param direction The direction to check
     * @return Whether a connection is possible through this face
     */
    boolean canConnectEnergy(Direction direction);
    
    /**
     * Gets the energy handler for the specified face.
     * 
     * @param direction The direction to get the handler for
     * @return The energy handler, or null if not available for this face
     */
    IEnergyHandler getEnergyHandler(Direction direction);
    
    /**
     * Gets the available connection sides for this energy linkable.
     * 
     * @return A set of directions where energy connections are possible
     */
    Set<Direction> getConnectableSides();
    
    /**
     * Called when this block is connected to an energy network.
     * 
     * @param networkId The unique identifier of the energy network
     * @param direction The direction through which the connection was established
     */
    default void onConnectedToNetwork(String networkId, Direction direction) {
        // Default empty implementation
    }
    
    /**
     * Called when this block is disconnected from an energy network.
     * 
     * @param networkId The unique identifier of the energy network
     * @param direction The direction through which the connection was established
     */
    default void onDisconnectedFromNetwork(String networkId, Direction direction) {
        // Default empty implementation
    }
}