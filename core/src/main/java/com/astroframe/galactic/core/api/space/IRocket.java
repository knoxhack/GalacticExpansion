package com.astroframe.galactic.core.api.space;

/**
 * Interface for rocket objects.
 * Defines methods to access rocket properties and capabilities.
 */
public interface IRocket {
    
    /**
     * Gets the tier of this rocket.
     * Higher tier rockets can go further and carry more cargo.
     * 
     * @return The rocket tier (1-5)
     */
    int getTier();
    
    /**
     * Gets the current fuel amount.
     * 
     * @return The fuel amount in standard units
     */
    float getFuel();
    
    /**
     * Gets the maximum fuel capacity.
     * 
     * @return The fuel capacity in standard units
     */
    float getFuelCapacity();
    
    /**
     * Gets the total mass of the rocket.
     * 
     * @return The mass in kilograms
     */
    float getMass();
    
    /**
     * Gets the thrust of the rocket engines.
     * 
     * @return The thrust in kilonewtons
     */
    float getThrust();
    
    /**
     * Determines if this rocket is capable of reaching orbit.
     * This is based on the thrust-to-weight ratio and fuel.
     * 
     * @return true if the rocket can reach orbit, false otherwise
     */
    boolean canReachOrbit();
    
    /**
     * Calculates the maximum altitude this rocket can reach.
     * 
     * @return The maximum altitude in meters
     */
    float getMaxAltitude();
    
    /**
     * Calculates the time required to reach orbit.
     * 
     * @return The time to orbit in seconds, or -1 if orbit cannot be reached
     */
    float getTimeToOrbit();
}