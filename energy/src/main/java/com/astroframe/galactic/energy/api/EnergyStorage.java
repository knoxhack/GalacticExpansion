package com.astroframe.galactic.energy.api;

/**
 * Interface for energy storage capabilities.
 * This provides methods for storing, extracting, and querying energy.
 */
public interface EnergyStorage {
    
    /**
     * Get the current amount of energy stored.
     * 
     * @return The stored energy amount
     */
    int getEnergy();
    
    /**
     * Get the maximum amount of energy that can be stored.
     * 
     * @return The maximum energy capacity
     */
    int getMaxEnergy();
    
    /**
     * Get the energy type this storage can handle.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Add energy to the storage.
     * 
     * @param amount The amount of energy to add
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) added
     */
    int receiveEnergy(int amount, boolean simulate);
    
    /**
     * Extract energy from the storage.
     * 
     * @param amount The maximum amount of energy to extract
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int amount, boolean simulate);
    
    /**
     * Check if energy can be extracted.
     * 
     * @return true if energy can be extracted, false otherwise
     */
    boolean canExtract();
    
    /**
     * Check if energy can be received.
     * 
     * @return true if energy can be received, false otherwise
     */
    boolean canReceive();
    
    /**
     * Get the current energy fill level as a percentage (0-1).
     * 
     * @return The fill level
     */
    default float getFillLevel() {
        return (float) getEnergy() / getMaxEnergy();
    }
    
    /**
     * Check if the storage is empty.
     * 
     * @return true if the storage is empty, false otherwise
     */
    default boolean isEmpty() {
        return getEnergy() <= 0;
    }
    
    /**
     * Check if the storage is full.
     * 
     * @return true if the storage is full, false otherwise
     */
    default boolean isFull() {
        return getEnergy() >= getMaxEnergy();
    }
}