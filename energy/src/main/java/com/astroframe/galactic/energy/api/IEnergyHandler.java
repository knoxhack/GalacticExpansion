package com.astroframe.galactic.energy.api;

/**
 * Interface for objects that handle energy.
 * These can be energy providers, consumers, or both.
 */
public interface IEnergyHandler {
    
    /**
     * Get the amount of energy currently stored.
     * 
     * @return The amount of energy
     */
    int getEnergyStored();
    
    /**
     * Get the maximum amount of energy that can be stored.
     * 
     * @return The maximum energy
     */
    int getMaxEnergyStored();
    
    /**
     * Add energy to the storage.
     * 
     * @param energy The amount to add
     * @param simulate If true, the addition will only be simulated
     * @return The amount of energy that was (or would have been) added
     */
    int receiveEnergy(int energy, boolean simulate);
    
    /**
     * Remove energy from the storage.
     * 
     * @param energy The amount to remove
     * @param simulate If true, the extraction will only be simulated
     * @return The amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int energy, boolean simulate);
    
    /**
     * Whether this storage can have energy extracted.
     * 
     * @return True if this can extract energy
     */
    boolean canExtract();
    
    /**
     * Whether this storage can receive energy.
     * 
     * @return True if this can receive energy
     */
    boolean canReceive();
    
    /**
     * Get the energy unit for this handler.
     * 
     * @return The energy unit
     */
    default EnergyUnit getEnergyUnit() {
        return EnergyUnit.GALACTIC_ENERGY_UNIT;
    }
}