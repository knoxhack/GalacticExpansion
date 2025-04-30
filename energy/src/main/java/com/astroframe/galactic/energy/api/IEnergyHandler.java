package com.astroframe.galactic.energy.api;

/**
 * Interface for blocks or entities that can handle energy.
 * Represents anything that can store, provide, or consume energy.
 * This extends EnergyStorage to provide a common interface for all energy-related components.
 */
public interface IEnergyHandler extends EnergyStorage {
    /**
     * Gets the amount of energy currently stored.
     * This is an alias for getEnergy() for compatibility with external API.
     * 
     * @return Stored energy
     */
    default int getEnergyStored() {
        return getEnergy();
    }
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * This is an alias for getMaxEnergy() for compatibility with external API.
     * 
     * @return Maximum energy
     */
    default int getMaxEnergyStored() {
        return getMaxEnergy();
    }
}