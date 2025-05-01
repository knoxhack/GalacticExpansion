package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket fuel tank components.
 */
public interface IFuelTank extends IRocketComponent {
    
    /**
     * Gets the maximum fuel capacity of this tank.
     * @return The fuel capacity
     */
    int getCapacity();
    
    /**
     * Gets the structural integrity of this fuel tank.
     * Higher values provide better protection against damage.
     * @return The structural integrity
     */
    float getStructuralIntegrity();
    
    /**
     * Gets the type of fuel this tank can store.
     * @return The fuel type
     */
    FuelType getFuelType();
    
    /**
     * Gets the flow rate of this fuel tank.
     * Determines how quickly fuel can be transferred to engines.
     * @return The flow rate
     */
    int getFlowRate();
    
    /**
     * Checks if this fuel tank has cryogenic storage capabilities.
     * Required for certain fuel types.
     * @return true if cryogenic storage is available
     */
    boolean hasCryogenicStorage();
    
    /**
     * Gets the leak resistance of this fuel tank.
     * Higher values reduce the chance of fuel leakage during damage.
     * @return The leak resistance
     */
    float getLeakResistance();
    
    /**
     * Enum representing the different types of fuels.
     */
    enum FuelType {
        CHEMICAL,       // Standard rocket fuel
        HYDROGEN,       // More efficient, requires special handling
        NUCLEAR,        // High energy density, hazardous
        ANTIMATTER,     // Extremely powerful, highly volatile
        EXOTIC          // For experimental drives and interstellar travel
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.FUEL_TANK;
    }
}