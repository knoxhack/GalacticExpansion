package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket fuel tank components.
 * Fuel tanks store the propellant needed for rocket engines to function.
 */
public interface IFuelTank extends IRocketComponent {
    
    /**
     * Gets the maximum fuel capacity of this tank.
     * @return The maximum fuel capacity in fuel units
     */
    int getMaxFuelCapacity();
    
    /**
     * Gets the fuel type stored in this tank.
     * @return The fuel type
     */
    FuelType getFuelType();
    
    /**
     * Gets the pressure rating of this tank.
     * Higher values allow for more compressed/volatile fuel storage.
     * @return The pressure rating
     */
    float getPressureRating();
    
    /**
     * Gets the insulation level of this tank.
     * Higher values reduce boil-off for cryogenic fuels.
     * @return The insulation level (1-10)
     */
    int getInsulationLevel();
    
    /**
     * Gets the structural integrity of this tank.
     * Higher values make the tank more resistant to damage.
     * @return The structural integrity (1-10)
     */
    int getStructuralIntegrity();
    
    /**
     * Enum representing the different types of rocket fuel.
     */
    enum FuelType {
        SOLID_FUEL("Solid Fuel"),           // Basic early-game fuel
        LIQUID_KEROSENE("Kerosene"),        // Standard chemical rocket fuel
        LIQUID_HYDROGEN("Liquid Hydrogen"), // More efficient but requires cryogenic storage
        XENON("Xenon Gas"),                 // For ion engines
        NUCLEAR("Nuclear"),                 // For nuclear thermal engines
        PLASMA("Plasma"),                   // For plasma engines
        ANTIMATTER("Antimatter");           // End-game fuel type
        
        private final String displayName;
        
        FuelType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.FUEL_TANK;
    }
}