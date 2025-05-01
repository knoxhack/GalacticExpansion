package com.astroframe.galactic.core.api.energy;

import com.astroframe.galactic.energy.api.EnergyUnit;

/**
 * Interface for blocks or entities that can handle energy.
 * This is the base interface for all energy-related operations.
 */
public interface IEnergyHandler {
    
    /**
     * Gets the amount of energy stored.
     * 
     * @return The amount of energy stored
     */
    int getEnergyStored();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return The maximum amount of energy that can be stored
     */
    int getMaxEnergyStored();
    
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
     * Returns whether energy can be extracted from this handler.
     * 
     * @return Whether energy can be extracted
     */
    boolean canExtract();
    
    /**
     * Returns whether energy can be received by this handler.
     * 
     * @return Whether energy can be received
     */
    boolean canReceive();
    
    /**
     * Gets the energy unit used by this handler.
     * 
     * @return The energy unit
     */
    EnergyUnit getEnergyUnit();
    
    // Using EnergyUnit from energy module
}