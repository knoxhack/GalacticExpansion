package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyNetwork;
import com.astroframe.galactic.energy.api.energynetwork.WorldPosition;

/**
 * Wrapper class for EnergyNetwork.
 * This provides compatibility for code that depends on the old API.
 */
public class NetworkWrapper {
    private final EnergyNetwork network;
    private final Level level;
    
    /**
     * Constructor for NetworkWrapper.
     * 
     * @param network The energy network
     * @param level The Minecraft level
     */
    public NetworkWrapper(EnergyNetwork network, Level level) {
        this.network = network;
        this.level = level;
    }
    
    /**
     * Gets the energy network.
     * 
     * @return The energy network
     */
    public EnergyNetwork getNetwork() {
        return network;
    }
    
    /**
     * Gets the energy type for this network.
     * 
     * @return The energy type
     */
    public com.astroframe.galactic.energy.api.EnergyType getEnergyType() {
        return network.getEnergyType();
    }
    
    /**
     * Gets the Minecraft level.
     * 
     * @return The level
     */
    public Level getLevel() {
        return level;
    }
    
    /**
     * Adds a storage to the network at the specified position.
     * 
     * @param pos The position
     * @param storage The energy storage
     */
    public void addStorage(BlockPos pos, EnergyStorage storage) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        // Convert to the new API's storage type
        com.astroframe.galactic.energy.api.EnergyStorage newStorage = 
            new com.astroframe.galactic.energy.api.EnergyStorageAdapter(storage);
        network.addStorage(position, newStorage);
    }
    
    /**
     * Removes a storage from the network at the specified position.
     * 
     * @param pos The position
     */
    public void removeStorage(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        network.removeStorage(position);
    }
    
    /**
     * Checks if a storage exists at the specified position.
     * 
     * @param pos The position
     * @return True if a storage exists
     */
    public boolean hasStorage(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        return network.hasStorage(position);
    }
    
    /**
     * Gets the storage at the specified position.
     * 
     * @param pos The position
     * @return The energy storage, or null if none exists
     */
    public EnergyStorage getStorage(BlockPos pos) {
        WorldPosition position = BlockPosAdapter.toWorldPosition(pos, level);
        com.astroframe.galactic.energy.api.EnergyStorage storage = network.getStorage(position);
        if (storage == null) {
            return null;
        }
        if (storage instanceof com.astroframe.galactic.energy.api.EnergyStorageAdapter) {
            return ((com.astroframe.galactic.energy.api.EnergyStorageAdapter) storage).getOriginal();
        }
        // Create a wrapper for the new storage
        return new EnergyStorageAdapter(storage);
    }
}