package com.astroframe.galactic.core.api.energy;

/**
 * Interface for handling energy storage and transfer.
 */
public interface IEnergyHandler {
    
    /**
     * Enum representing different energy units.
     */
    enum EnergyUnit {
        RF("Redstone Flux", "RF"),
        EU("Energy Units", "EU"),
        FE("Forge Energy", "FE"),
        J("Joules", "J"),
        GJ("Galactic Joules", "GJ");
        
        private final String displayName;
        private final String symbol;
        
        EnergyUnit(String displayName, String symbol) {
            this.displayName = displayName;
            this.symbol = symbol;
        }
        
        /**
         * Gets the display name of this energy unit.
         * @return The display name
         */
        public String getDisplayName() {
            return displayName;
        }
        
        /**
         * Gets the symbol of this energy unit.
         * @return The symbol
         */
        public String getSymbol() {
            return symbol;
        }
    }
    
    /**
     * Gets the energy stored.
     * @return The energy stored
     */
    int getEnergyStored();
    
    /**
     * Gets the maximum energy capacity.
     * @return The maximum energy capacity
     */
    int getMaxEnergyCapacity();
    
    /**
     * Gets the energy unit used by this handler.
     * @return The energy unit
     */
    EnergyUnit getEnergyUnit();
    
    /**
     * Receives energy.
     * @param maxReceive The maximum amount to receive
     * @param simulate If true, the transfer will only be simulated
     * @return The amount of energy that was (or would have been, if simulated) received
     */
    int receiveEnergy(int maxReceive, boolean simulate);
    
    /**
     * Extracts energy.
     * @param maxExtract The maximum amount to extract
     * @param simulate If true, the extraction will only be simulated
     * @return The amount of energy that was (or would have been, if simulated) extracted
     */
    int extractEnergy(int maxExtract, boolean simulate);
    
    /**
     * Checks if energy can be extracted.
     * @return True if energy can be extracted, false otherwise
     */
    boolean canExtract();
    
    /**
     * Checks if energy can be received.
     * @return True if energy can be received, false otherwise
     */
    boolean canReceive();
}