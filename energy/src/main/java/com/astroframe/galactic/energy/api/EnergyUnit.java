package com.astroframe.galactic.energy.api;

/**
 * Enumeration of energy units used in the mod.
 * This allows for conversion between different energy systems.
 */
public enum EnergyUnit {
    /**
     * The standard energy unit used in the Galactic Expansion mod.
     */
    GALACTIC_ENERGY_UNIT(1.0),
    
    /**
     * Forge Energy unit (compatible with other mods).
     */
    FORGE_ENERGY(1.0),
    
    /**
     * Thermal unit (used for heat-based machines).
     */
    THERMAL_UNIT(0.5),
    
    /**
     * Kinetic unit (used for machines that generate energy from movement).
     */
    KINETIC_UNIT(2.0),
    
    /**
     * Steam unit (used for steam-based generators).
     */
    STEAM_UNIT(0.2);
    
    private final double conversionRate;
    
    EnergyUnit(double conversionRate) {
        this.conversionRate = conversionRate;
    }
    
    /**
     * Convert an amount of energy from this unit to another unit.
     * 
     * @param amount The amount of energy in this unit
     * @param targetUnit The target unit to convert to
     * @return The equivalent amount in the target unit
     */
    public int convertTo(int amount, EnergyUnit targetUnit) {
        return (int) (amount * (this.conversionRate / targetUnit.conversionRate));
    }
}