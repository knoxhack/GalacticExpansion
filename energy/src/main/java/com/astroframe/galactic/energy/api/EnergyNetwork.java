package com.astroframe.galactic.energy.api;

import java.util.Collection;

/**
 * Interface for energy networks.
 * An energy network connects energy sources, consumers, and storage devices.
 */
public interface EnergyNetwork {
    
    /**
     * Get the energy type this network handles.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Add a storage device to the network.
     * 
     * @param storage The storage to add
     * @return true if the storage was added, false if it was incompatible
     */
    boolean addStorage(EnergyStorage storage);
    
    /**
     * Remove a storage device from the network.
     * 
     * @param storage The storage to remove
     * @return true if the storage was removed, false if it wasn't in the network
     */
    boolean removeStorage(EnergyStorage storage);
    
    /**
     * Get all storage devices in the network.
     * 
     * @return A collection of storage devices
     */
    Collection<EnergyStorage> getStorageDevices();
    
    /**
     * Add energy to the network.
     * The energy will be distributed among storage devices.
     * 
     * @param amount The amount of energy to add
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) added
     */
    int addEnergy(int amount, boolean simulate);
    
    /**
     * Extract energy from the network.
     * The energy will be taken from storage devices.
     * 
     * @param amount The maximum amount of energy to extract
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int amount, boolean simulate);
    
    /**
     * Get the total amount of energy stored in the network.
     * 
     * @return The total stored energy
     */
    int getEnergy();
    
    /**
     * Get the maximum amount of energy that can be stored in the network.
     * 
     * @return The maximum energy capacity
     */
    int getMaxEnergy();
    
    /**
     * Process the network for one tick.
     * This should handle energy distribution between sources, consumers, and storage.
     */
    void tick();
}