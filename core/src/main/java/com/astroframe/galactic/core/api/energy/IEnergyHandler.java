package com.astroframe.galactic.core.api.energy;

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
    
    /**
     * Energy unit enum for standardizing energy measurements across modules.
     */
    enum EnergyUnit {
        /** Forge Energy unit (FE) */
        FORGE_ENERGY(1),
        
        /** Galactic Energy unit (GE) */
        GALACTIC_ENERGY(10),
        
        /** Redstone Flux (RF, compatible with FE) */
        REDSTONE_FLUX(1),
        
        /** Energy Units (EU, from Industrial Craft) */
        ENERGY_UNITS(4);
        
        private final int conversionRate;
        
        EnergyUnit(int conversionRate) {
            this.conversionRate = conversionRate;
        }
        
        /**
         * Converts an amount of energy from this unit to another unit.
         * 
         * @param amount The amount of energy in this unit
         * @param to The target energy unit
         * @return The amount of energy in the target unit
         */
        public int convertTo(int amount, EnergyUnit to) {
            if (to == this) {
                return amount;
            }
            
            // Convert to common base (FE) and then to target unit
            int baseAmount = amount * this.conversionRate;
            return baseAmount / to.conversionRate;
        }
    }
}