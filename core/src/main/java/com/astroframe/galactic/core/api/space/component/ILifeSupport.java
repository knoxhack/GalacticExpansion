package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.LifeSupportType;

/**
 * Interface for rocket life support systems.
 */
public interface ILifeSupport extends IRocketComponent {
    
    /**
     * Gets the maximum crew capacity this life support system can handle.
     * @return The maximum crew capacity
     */
    int getMaxCrewCapacity();
    
    /**
     * Gets the oxygen efficiency of this life support system (0.0-1.0).
     * @return The oxygen efficiency
     */
    float getOxygenEfficiency();
    
    /**
     * Gets the water recycling rate of this life support system (0.0-1.0).
     * @return The water recycling rate
     */
    float getWaterRecyclingRate();
    
    /**
     * Checks if this system has advanced medical facilities.
     * @return True if has advanced medical
     */
    boolean hasAdvancedMedical();
    
    /**
     * Checks if this system has radiation scrubbers.
     * @return True if has radiation scrubbers
     */
    boolean hasRadiationScrubbers();
    
    /**
     * Gets the type of this life support system.
     * @return The life support type
     */
    LifeSupportType getLifeSupportType();
}