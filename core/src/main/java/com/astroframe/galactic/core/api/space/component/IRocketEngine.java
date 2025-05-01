package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket engine components.
 */
public interface IRocketEngine extends IRocketComponent {
    
    /**
     * Gets the thrust power of this engine.
     * Higher values provide more acceleration.
     * @return The thrust power
     */
    int getThrust();
    
    /**
     * Gets the fuel efficiency of this engine.
     * Higher values mean less fuel consumption.
     * @return The fuel efficiency
     */
    float getEfficiency();
    
    /**
     * Gets the engine type.
     * @return The engine type
     */
    EngineType getEngineType();
    
    /**
     * Gets the maximum heat level this engine can handle.
     * @return The maximum heat capacity
     */
    int getHeatCapacity();
    
    /**
     * Checks if this engine can operate in water.
     * @return true if the engine works underwater
     */
    boolean canOperateUnderwater();
    
    /**
     * Checks if this engine can operate in atmosphere.
     * @return true if the engine works in atmosphere
     */
    boolean canOperateInAtmosphere();
    
    /**
     * Checks if this engine can operate in space.
     * @return true if the engine works in space
     */
    boolean canOperateInSpace();
    
    /**
     * Enum representing the different types of rocket engines.
     */
    enum EngineType {
        CHEMICAL,
        ION,
        PLASMA,
        FUSION,
        ANTIMATTER
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.ENGINE;
    }
}