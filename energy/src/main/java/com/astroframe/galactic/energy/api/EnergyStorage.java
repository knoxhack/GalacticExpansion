package com.astroframe.galactic.energy.api;

/**
 * Interface for energy storage.
 * Represents a container for energy that can receive and extract energy.
 */
public interface EnergyStorage {
    
    /**
     * Adds energy to the storage.
     * 
     * @param amount Maximum amount to receive
     * @param simulate If true, the addition is only simulated
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int amount, boolean simulate);
    
    /**
     * Removes energy from the storage.
     * 
     * @param amount Maximum amount to extract
     * @param simulate If true, the extraction is only simulated
     * @return Amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int amount, boolean simulate);
    
    /**
     * Gets the amount of energy currently stored.
     * 
     * @return Stored energy
     */
    int getEnergy();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return Maximum energy
     */
    int getMaxEnergy();
    
    /**
     * Gets the amount of energy currently stored.
     * Use this alias for compatibility with NeoForge energy API.
     * 
     * @return Stored energy
     */
    default int getEnergyStored() {
        return getEnergy();
    }
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * Use this alias for compatibility with NeoForge energy API.
     * 
     * @return Maximum energy
     */
    default int getMaxEnergyStored() {
        return getMaxEnergy();
    }
    
    /**
     * Returns whether this storage can receive energy.
     * 
     * @return True if this storage can receive energy
     */
    boolean canReceive();
    
    /**
     * Returns whether this storage can extract energy.
     * 
     * @return True if this storage can extract energy
     */
    boolean canExtract();
    
    /**
     * Gets the type of energy stored.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
}