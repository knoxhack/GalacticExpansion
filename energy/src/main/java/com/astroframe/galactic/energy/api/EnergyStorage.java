package com.astroframe.galactic.energy.api;

/**
 * Interface for energy storage.
 * Represents a container for energy that can receive and extract energy.
 * Extends IEnergyHandler to provide a consistent API for all energy handlers.
 */
public interface EnergyStorage extends IEnergyHandler {
    
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
     * Implementation for IEnergyHandler compatibility.
     * 
     * @return Stored energy
     */
    @Override
    default int getEnergyStored() {
        return getEnergy();
    }
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * Implementation for IEnergyHandler compatibility.
     * 
     * @return Maximum energy
     */
    @Override
    default int getMaxEnergyStored() {
        return getMaxEnergy();
    }
    
    /**
     * Gets the type of energy stored.
     * 
     * @return The energy type
     */
    EnergyType getEnergyType();
    
    /**
     * Get the energy unit for this storage.
     * 
     * @return The energy unit
     */
    @Override
    default EnergyUnit getEnergyUnit() {
        return EnergyUnit.GALACTIC_ENERGY_UNIT;
    }
}