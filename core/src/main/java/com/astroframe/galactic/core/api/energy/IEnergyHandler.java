package com.astroframe.galactic.core.api.energy;

/**
 * Interface for objects that can handle energy.
 * This is the core energy API that all energy-related modules build upon.
 */
public interface IEnergyHandler {
    
    /**
     * Energy units that can be used by energy handlers.
     */
    enum EnergyUnit {
        /**
         * Forge Energy Units, compatible with other mods.
         */
        FE,
        
        /**
         * Galactic Energy Units, internal to this mod.
         */
        GEU,
        
        /**
         * Joules, standard SI unit of energy.
         */
        JOULE,
        
        /**
         * Fuel Units, used for liquid rocket fuel.
         */
        FUEL
    }
    
    /**
     * Receives energy into the handler.
     * 
     * @param maxReceive Maximum amount to receive
     * @param simulate If true, the transfer will only be simulated
     * @return Amount of energy that was (or would have been) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Extracts energy from the handler.
     * 
     * @param maxExtract Maximum amount to extract
     * @param simulate If true, the extraction will only be simulated
     * @return Amount of energy that was (or would have been) extracted
     */
    int extractEnergy(int maxExtract, boolean simulate);
    
    /**
     * Gets the amount of energy currently stored.
     * 
     * @return The amount of energy stored
     */
    int getEnergy();
    
    /**
     * Gets the maximum amount of energy that can be stored.
     * 
     * @return The maximum energy capacity
     */
    int getMaxEnergy();
    
    /**
     * Returns whether this energy handler can extract energy.
     * 
     * @return true if this can extract energy, false otherwise
     */
    boolean canExtract();
    
    /**
     * Returns whether this energy handler can receive energy.
     * 
     * @return true if this can receive energy, false otherwise
     */
    boolean canReceive();
    
    /**
     * Gets the unit of energy used by this handler.
     * 
     * @return The energy unit
     */
    default EnergyUnit getEnergyUnit() {
        return EnergyUnit.FE;
    }
    
    /**
     * Converts the given amount of energy from the source unit to the target unit.
     * 
     * @param amount The amount of energy to convert
     * @param from The source energy unit
     * @param to The target energy unit
     * @return The converted energy amount
     */
    static int convertEnergy(int amount, EnergyUnit from, EnergyUnit to) {
        if (from == to) {
            return amount;
        }
        
        // Conversion factors may need to be adjusted based on game balance
        double converted = amount;
        
        // First convert to joules as the common unit
        switch (from) {
            case FE:
                converted = amount * 10.0; // 1 FE = 10 J
                break;
            case GEU:
                converted = amount * 100.0; // 1 GEU = 100 J
                break;
            case JOULE:
                // Already in joules
                break;
            case FUEL:
                converted = amount * 1000.0; // 1 FUEL = 1000 J
                break;
        }
        
        // Then convert from joules to the target unit
        switch (to) {
            case FE:
                converted = converted / 10.0; // 10 J = 1 FE
                break;
            case GEU:
                converted = converted / 100.0; // 100 J = 1 GEU
                break;
            case JOULE:
                // Already in joules
                break;
            case FUEL:
                converted = converted / 1000.0; // 1000 J = 1 FUEL
                break;
        }
        
        return (int) Math.round(converted);
    }
}