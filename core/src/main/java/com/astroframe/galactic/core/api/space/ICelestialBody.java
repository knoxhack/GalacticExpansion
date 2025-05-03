package com.astroframe.galactic.core.api.space;

import com.astroframe.galactic.core.api.space.CelestialBodyType;
import com.astroframe.galactic.core.api.space.TemperatureRange;
import com.astroframe.galactic.core.api.space.RadiationLevel;
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
    
    /**
     * Gets the temperature range category of this celestial body.
     * 
     * @return The temperature range
     */
    default TemperatureRange getTemperatureRange() {
        return TemperatureRange.fromTemperature(getTemperature());
    }
    
    /**
     * Gets the radiation level category of this celestial body.
     * 
     * @return The radiation level
     */
    default RadiationLevel getRadiationLevel() {
        // Default implementation assumes no radiation
        return RadiationLevel.NONE;
    }
    
    /**
     * Checks if this celestial body is discovered by the player.
     * 
     * @return true if the celestial body is discovered
     */
    default boolean isDiscovered() {
        return true; // Default implementation assumes all celestial bodies are discovered
    }
    
    /**
     * Sets the discovered state of this celestial body.
     * 
     * @param discovered true to mark as discovered, false otherwise
     */
    default void setDiscovered(boolean discovered) {
        // Default implementation does nothing
    }
    
    /**
     * Checks if this celestial body has liquid water.
     * 
     * @return true if the celestial body has liquid water
     */
    default boolean hasLiquidWater() {
        float temp = getTemperature();
        return hasAtmosphere() && temp > 0 && temp < 100;
    }
    
    /**
     * Gets the relative size of this celestial body compared to Earth.
     * 
     * @return The size multiplier (Earth = 1.0)
     */
    default float getRelativeSize() {
        return 1.0f; // Default implementation assumes Earth-sized
    }
    
    /**
     * Gets the relative gravity of this celestial body compared to Earth.
     * 
     * @return The gravity multiplier (Earth = 1.0)
     */
    default float getRelativeGravity() {
        return getGravity(); // By default, use the gravity method
    }
    
    /**
     * Gets the description of this celestial body.
     * 
     * @return The description
     */
    default String getDescription() {
        return "A celestial body in space."; // Default generic description
    }
    
    /**
     * Gets the type of this celestial body.
     * 
     * @return The celestial body type
     */
    default CelestialBodyType getType() {
        return CelestialBodyType.PLANET; // Default to planet type
    }
    
    /**
     * Gets the parent celestial body of this celestial body (e.g., the planet a moon orbits).
     * 
     * @return The parent celestial body, or null if none
     */
    default ICelestialBody getParent() {
        return null; // Default to no parent
    }
    
    /**
     * Gets the atmospheric density relative to Earth.
     * This is an alias for getAtmosphericPressure for compatibility.
     * 
     * @return The atmospheric density (Earth = 1.0)
     */
    default float getAtmosphereDensity() {
        return hasAtmosphere() ? getAtmosphericPressure() : 0.0f;
    }
}