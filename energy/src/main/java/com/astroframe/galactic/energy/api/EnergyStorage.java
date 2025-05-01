package com.astroframe.galactic.energy.api;

/**
 * Interface for energy storage capabilities.
 * This defines how energy can be stored, extracted, and received.
 */
public interface EnergyStorage {
    
    /**
     * Adds energy to storage.
     * 
     * @param amount The maximum amount of energy to receive
     * @param simulate If true, the transfer will only be simulated
     * @return The amount of energy that was (or would have been, if simulated) received
     */
    int receiveEnergy(int amount, boolean simulate);
    
    /**
     * Removes energy from storage.
     * 
     * @param amount The maximum amount of energy to extract
     * @param simulate If true, the extraction will only be simulated
     * @return The amount of energy that was (or would have been, if simulated) extracted
     */
    int extractEnergy(int amount, boolean simulate);
    
    /**
     * Gets the amount of energy stored.
     * 
     * @return The amount of energy stored
     */
    int getEnergy();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return The maximum amount of energy that can be stored
     */
    int getMaxEnergy();
    
    /**
     * Returns whether energy can be extracted from this storage.
     * 
     * @return Whether energy can be extracted
     */
    boolean canExtract();
    
    /**
     * Returns whether energy can be received by this storage.
     * 
     * @return Whether energy can be received
     */
    boolean canReceive();
    
    /**
     * Gets the energy type used by this storage.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
}