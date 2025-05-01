package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.EngineType;

/**
 * Interface for rocket engines.
 */
public interface IRocketEngine extends IRocketComponent {
    
    /**
     * Gets the thrust power of this engine.
     * @return The thrust power
     */
    int getThrust();
    
    /**
     * Gets the fuel efficiency of this engine (0.0-1.0).
     * @return The fuel efficiency
     */
    float getEfficiency();
    
    /**
     * Gets the type of this engine.
     * @return The engine type
     */
    EngineType getEngineType();
    
    /**
     * Gets the heat capacity of this engine.
     * @return The heat capacity
     */
    int getHeatCapacity();
    
    /**
     * Checks if this engine can operate underwater.
     * @return True if can operate underwater
     */
    boolean canOperateUnderwater();
    
    /**
     * Checks if this engine can operate in atmosphere.
     * @return True if can operate in atmosphere
     */
    boolean canOperateInAtmosphere();
    
    /**
     * Checks if this engine can operate in space.
     * @return True if can operate in space
     */
    boolean canOperateInSpace();
}