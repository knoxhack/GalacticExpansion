package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface for celestial bodies such as planets, moons, asteroids, etc.
 */
public interface ICelestialBody {
    
    /**
     * Gets the unique identifier for this celestial body.
     * 
     * @return The resource location ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the display name of this celestial body.
     * 
     * @return The display name
     */
    String getName();
    
    /**
     * Gets the distance from the celestial body to Earth in astronomical units.
     * 
     * @return The distance in AU
     */
    float getDistance();
    
    /**
     * Gets the required rocket tier to reach this celestial body.
     * 
     * @return The minimum rocket tier required (1-5)
     */
    int getRequiredTier();
    
    /**
     * Gets the gravity strength of this celestial body relative to Earth.
     * 
     * @return The gravity multiplier (Earth = 1.0)
     */
    float getGravity();
    
    /**
     * Checks if this celestial body has an atmosphere.
     * 
     * @return true if the body has an atmosphere
     */
    boolean hasAtmosphere();
    
    /**
     * Gets the atmospheric pressure relative to Earth.
     * 
     * @return The atmospheric pressure (Earth = 1.0)
     */
    float getAtmosphericPressure();
    
    /**
     * Gets the average temperature on the surface in Celsius.
     * 
     * @return The temperature in degrees Celsius
     */
    float getTemperature();
    
    /**
     * Checks if this celestial body has a breathable atmosphere.
     * 
     * @return true if the atmosphere is breathable
     */
    default boolean hasBreathableAtmosphere() {
        return hasAtmosphere() && getAtmosphericPressure() >= 0.7f && getAtmosphericPressure() <= 1.3f;
    }
    
    /**
     * Gets the required rocket tier as an alternative method name.
     * 
     * @return The required rocket tier (1-5)
     */
    default int getRocketTierRequired() {
        return getRequiredTier();
    }
    
    /**
     * Gets the distance from Earth (home) as an alternative method name.
     * 
     * @return The distance in AU
     */
    default float getDistanceFromHome() {
        return getDistance();
    }
    
    /**
     * Checks if this celestial body has unique resources that cannot be found elsewhere.
     * 
     * @return true if it has unique resources
     */
    default boolean hasUniqueResources() {
        return true; // Default implementation assumes all celestial bodies have unique resources
    }
}