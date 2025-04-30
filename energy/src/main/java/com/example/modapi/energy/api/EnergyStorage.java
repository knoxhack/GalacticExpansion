package com.example.modapi.energy.api;

import com.astroframe.galactic.energy.api.EnergyType;

/**
 * Compatibility interface for energy storage.
 * This interface exists only for backward compatibility with existing code.
 * All new code should use com.astroframe.galactic.energy.api.EnergyStorage directly.
 * 
 * @deprecated Use {@link com.astroframe.galactic.energy.api.EnergyStorage} instead
 */
@Deprecated
public interface EnergyStorage {
    
    /**
     * Adds energy to the storage.
     * 
     * @param maxReceive Maximum amount to receive
     * @param simulate If true, the addition is only simulated
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Removes energy from the storage.
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
    default int getEnergyStored() {
        return getEnergy();
    }
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return Maximum energy
     */
    default int getMaxEnergyStored() {
        return getMaxEnergy();
    }
    
    /**
     * Gets the amount of energy currently stored.
     * This is the new method name used in Galactic Energy.
     * 
     * @return Stored energy
     */
    int getEnergy();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * This is the new method name used in Galactic Energy.
     * 
     * @return Maximum energy
     */
    int getMaxEnergy();
    
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
    
    /**
     * Simple enum for energy units.
     * This is maintained for backward compatibility.
     */
    enum EnergyUnit {
        /**
         * Standard Forge Energy unit.
         */
        FORGE_ENERGY,
        
        /**
         * RF energy unit (compatible with Forge Energy).
         */
        RF,
        
        /**
         * EU energy unit (IndustrialCraft 2).
         */
        EU,
        
        /**
         * Custom energy unit.
         */
        CUSTOM
    }
}