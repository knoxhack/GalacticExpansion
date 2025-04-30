package com.astroframe.galactic.energy.api;

/**
 * Interface for energy storage devices.
 * This defines the basic operations that all energy storage devices should support.
 */
public interface EnergyStorage {
    
    /**
     * Add energy to the storage.
     * 
     * @param amount The amount of energy to add
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) added
     */
    int receiveEnergy(int amount, boolean simulate);
    
    /**
     * Remove energy from the storage.
     * 
     * @param amount The maximum amount of energy to remove
     * @param simulate If true, the operation will only be simulated
     * @return The amount of energy that was (or would have been) removed
     */
    int extractEnergy(int amount, boolean simulate);
    
    /**
     * Get the amount of energy currently stored.
     * 
     * @return The stored energy
     */
    int getEnergy();
    
    /**
     * Get the maximum amount of energy that can be stored.
     * 
     * @return The maximum energy capacity
     */
    int getMaxEnergy();
    
    /**
     * Check if this storage can have energy extracted.
     * 
     * @return true if energy can be extracted, false otherwise
     */
    boolean canExtract();
    
    /**
     * Check if this storage can receive energy.
     * 
     * @return true if energy can be received, false otherwise
     */
    boolean canReceive();
    
    /**
     * Get the type of energy this storage can handle.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Get the fill level as a percentage (0.0 to 1.0).
     * 
     * @return The fill level
     */
    default float getFillLevel() {
        int maxEnergy = getMaxEnergy();
        return maxEnergy > 0 ? (float) getEnergy() / maxEnergy : 0;
    }
}