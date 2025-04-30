package com.example.modapi.energy.api;

/**
 * Interface for handling energy storage and transfer.
 * Defines methods for adding, removing, and querying energy.
 */
public interface IEnergyHandler {
    
    /**
     * Add energy to the storage.
     * 
     * @param maxReceive Maximum amount to receive
     * @param simulate If true, the transfer is simulated and no energy is actually received
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Remove energy from the storage.
     * 
     * @param maxExtract Maximum amount to extract
     * @param simulate If true, the transfer is simulated and no energy is actually extracted
     * @return Amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int maxExtract, boolean simulate);
    
    /**
     * Get the amount of energy currently stored.
     * 
     * @return The amount of energy stored
     */
    int getEnergyStored();
    
    /**
     * Get the maximum amount of energy that can be stored.
     * 
     * @return The maximum energy capacity
     */
    int getMaxEnergyStored();
    
    /**
     * Whether this energy handler can have energy extracted from it.
     * 
     * @return True if energy can be extracted
     */
    boolean canExtract();
    
    /**
     * Whether this energy handler can receive energy.
     * 
     * @return True if energy can be received
     */
    boolean canReceive();
    
    /**
     * Get the energy unit used by this handler.
     * 
     * @return The energy unit
     */
    EnergyUnit getEnergyUnit();
}
