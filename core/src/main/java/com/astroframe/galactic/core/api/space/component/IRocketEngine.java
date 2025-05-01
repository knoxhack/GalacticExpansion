package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket engines.
 */
public interface IRocketEngine extends IRocketComponent {
    
    /**
     * Gets the thrust of this engine in newtons.
     * @return The thrust
     */
    int getThrust();
    
    /**
     * Gets the fuel consumption rate in units per second.
     * @return The fuel consumption rate
     */
    int getFuelConsumptionRate();
    
    /**
     * Gets the efficiency of this engine (0.1-2.0).
     * Higher values mean more thrust per unit of fuel.
     * @return The efficiency
     */
    float getEfficiency();
    
    /**
     * Gets the type of fuel this engine uses.
     * @return The fuel type
     */
    FuelType getFuelType();
    
    /**
     * Determines if this engine can operate in atmosphere.
     * @return true if the engine can operate in atmosphere
     */
    boolean canOperateInAtmosphere();
    
    /**
     * Determines if this engine can operate in space.
     * @return true if the engine can operate in space
     */
    boolean canOperateInSpace();
    
    /**
     * Enum representing the types of fuel a rocket engine can use.
     */
    enum FuelType {
        /**
         * Chemical fuel - common and less efficient.
         */
        CHEMICAL,
        
        /**
         * Plasma fuel - more advanced and efficient.
         */
        PLASMA,
        
        /**
         * Antimatter fuel - extremely advanced and efficient.
         */
        ANTIMATTER
    }
}