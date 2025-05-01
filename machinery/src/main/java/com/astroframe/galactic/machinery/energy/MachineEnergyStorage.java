package com.astroframe.galactic.machinery.energy;

import com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit;

/**
 * Energy storage implementation for machines.
 */
public interface MachineEnergyStorage {
    
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
    
    /**
     * Energy type enum for machinery module.
     */
    enum EnergyType {
        ELECTRICAL("electrical"),
        MECHANICAL("mechanical"),
        THERMAL("thermal"),
        QUANTUM("quantum");
        
        private final String id;
        
        EnergyType(String id) {
            this.id = id;
        }
        
        /**
         * Gets the ID of this energy type.
         * 
         * @return The ID
         */
        public String getId() {
            return id;
        }
    }
}