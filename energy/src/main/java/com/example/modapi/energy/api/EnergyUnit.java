package com.example.modapi.energy.api;

/**
 * Enum representing different energy units that can be used in the mod.
 * Allows for conversion between different energy systems.
 */
public enum EnergyUnit {
    /**
     * The standard Forge Energy unit (FE).
     */
    FORGE_ENERGY("Forge Energy", "FE", 1.0),
    
    /**
     * Energy unit compatible with the old Redstone Flux system.
     */
    REDSTONE_FLUX("Redstone Flux", "RF", 1.0),
    
    /**
     * Energy unit for mod-specific higher-tier energy.
     */
    MODAPI_ENERGY("ModAPI Energy", "ME", 10.0);
    
    private final String name;
    private final String abbreviation;
    private final double conversionRateToFE;
    
    /**
     * Constructor for EnergyUnit.
     * 
     * @param name The full name of the energy unit
     * @param abbreviation The short abbreviation for display
     * @param conversionRateToFE The conversion rate to Forge Energy
     */
    EnergyUnit(String name, String abbreviation, double conversionRateToFE) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.conversionRateToFE = conversionRateToFE;
    }
    
    /**
     * Gets the full name of the energy unit.
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the abbreviation of the energy unit.
     * 
     * @return The abbreviation
     */
    public String getAbbreviation() {
        return abbreviation;
    }
    
    /**
     * Gets the conversion rate to Forge Energy.
     * 
     * @return The conversion rate
     */
    public double getConversionRateToFE() {
        return conversionRateToFE;
    }
    
    /**
     * Converts an amount of energy from this unit to Forge Energy.
     * 
     * @param amount The amount to convert
     * @return The equivalent amount in Forge Energy
     */
    public int toForgeEnergy(int amount) {
        return (int) (amount * conversionRateToFE);
    }
    
    /**
     * Converts an amount of energy from Forge Energy to this unit.
     * 
     * @param feAmount The amount of Forge Energy
     * @return The equivalent amount in this unit
     */
    public int fromForgeEnergy(int feAmount) {
        return (int) (feAmount / conversionRateToFE);
    }
    
    /**
     * Converts an amount of energy from this unit to another unit.
     * 
     * @param amount The amount to convert
     * @param targetUnit The target energy unit
     * @return The equivalent amount in the target unit
     */
    public int convertTo(int amount, EnergyUnit targetUnit) {
        // Convert through Forge Energy as the common denominator
        int feAmount = toForgeEnergy(amount);
        return targetUnit.fromForgeEnergy(feAmount);
    }
}
