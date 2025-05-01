package com.astroframe.galactic.core.api.energy;

/**
 * Interface for objects that can handle energy (transfer, storage, etc.)
 * This is the central interface for energy-related functionality in the Galactic Expansion mod.
 * All energy-related operations should use this interface for interoperability.
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
    
    /**
     * Energy unit enum for energy measurement.
     * This is a temporary definition that will be replaced by the one from the energy module in the future.
     */
    enum EnergyUnit {
        /** Standard energy unit */
        GALACTIC_ENERGY_UNIT(1.0),
        
        /** Forge Energy unit (compatible with other mods) */
        FORGE_ENERGY(1.0),
        
        /** Thermal unit used for heat-based operations */
        THERMAL_UNIT(0.5),
        
        /** Kinetic unit for movement-based energy */
        KINETIC_UNIT(2.0),
        
        /** Steam unit for steam-powered machines */
        STEAM_UNIT(0.2);
        
        private final double conversionRate;
        
        EnergyUnit(double conversionRate) {
            this.conversionRate = conversionRate;
        }
        
        /**
         * Converts energy between units
         * 
         * @param amount The amount to convert
         * @param targetUnit The target unit
         * @return The converted amount
         */
        public int convertTo(int amount, EnergyUnit targetUnit) {
            return (int) (amount * (this.conversionRate / targetUnit.conversionRate));
        }
        
        /**
         * Gets the ID of this energy unit.
         * Can be used for serialization and identification purposes.
         * 
         * @return The string ID of the energy unit
         */
        public String getId() {
            return this.name().toLowerCase();
        }
    }
}