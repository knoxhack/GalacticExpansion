package com.astroframe.galactic.energy.api;

/**
 * Interface for energy handling components.
 * Defines methods for energy storage, transfer, and query.
 */
public interface IEnergyHandler {
    
    /**
     * Add energy to the handler.
     * 
     * @param maxReceive Maximum amount of energy to receive
     * @param simulate If true, the transfer will only be simulated
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Remove energy from the handler.
     * 
     * @param maxExtract Maximum amount of energy to extract
     * @param simulate If true, the extraction will only be simulated
     * @return Amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int maxExtract, boolean simulate);
    
    /**
     * Get the amount of energy currently stored.
     * 
     * @return Stored energy
     */
    int getEnergyStored();
    
    /**
     * Get the maximum amount of energy that can be stored.
     * 
     * @return Maximum energy capacity
     */
    int getMaxEnergyStored();
    
    /**
     * Returns whether this storage can have energy extracted.
     * 
     * @return True if extraction is allowed
     */
    boolean canExtract();
    
    /**
     * Returns whether this storage can receive energy.
     * 
     * @return True if receiving is allowed
     */
    boolean canReceive();
    
    /**
     * Get the energy type this handler uses.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
}