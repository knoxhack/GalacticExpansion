package com.astroframe.galactic.energy.api;

/**
 * Interface for blocks or entities that can handle energy.
 * Represents anything that can store, provide, or consume energy.
 */
public interface IEnergyHandler {
    
    /**
     * Adds energy to the handler.
     * 
     * @param maxReceive Maximum amount to receive
     * @param simulate If true, the addition is only simulated
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Removes energy from the handler.
     * 
     * @param maxExtract Maximum amount to extract
     * @param simulate If true, the extraction is only simulated
     * @return Amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int maxExtract, boolean simulate);
    
    /**
     * Gets the amount of energy currently stored.
     * 
     * @return Stored energy
     */
    int getEnergyStored();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return Maximum energy
     */
    int getMaxEnergyStored();
    
    /**
     * Returns whether this energy handler can receive energy.
     * 
     * @return True if this can receive energy
     */
    boolean canReceive();
    
    /**
     * Returns whether this energy handler can extract energy.
     * 
     * @return True if this can extract energy
     */
    boolean canExtract();
    
    /**
     * Gets the type of energy handled by this energy handler.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
}