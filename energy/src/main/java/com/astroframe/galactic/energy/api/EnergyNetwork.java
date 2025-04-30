package com.astroframe.galactic.energy.api;

import com.astroframe.galactic.energy.api.energynetwork.WorldChunk;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;

import java.util.List;
import java.util.Optional;

/**
 * Interface for energy networks.
 * Represents a network of connected energy handlers that can transfer energy between them.
 */
public interface EnergyNetwork {
    
    /**
     * Gets the type of energy used by this network.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Add an energy storage to the network at the given position.
     * 
     * @param position The position
     * @param storage The energy storage
     */
    void addStorage(WorldPosition position, EnergyStorage storage);
    
    /**
     * Remove an energy storage from the network at the given position.
     * 
     * @param position The position
     */
    void removeStorage(WorldPosition position);
    
    /**
     * Get the energy storage at the given position.
     * 
     * @param position The position
     * @return The energy storage, or null if none exists
     */
    EnergyStorage getStorage(WorldPosition position);
    
    /**
     * Check if an energy storage exists at the given position.
     * 
     * @param position The position
     * @return True if an energy storage exists
     */
    boolean hasStorage(WorldPosition position);
    
    /**
     * Process energy transfers for one tick.
     * This should be called once per game tick.
     */
    void tick();
}