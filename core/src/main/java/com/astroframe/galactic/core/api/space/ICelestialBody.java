package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Interface for celestial bodies like planets, moons, and asteroids.
 * This is a central concept for the Space Frontier module.
 */
public interface ICelestialBody {
    
    /**
     * Gets the unique identifier for this celestial body.
     * 
     * @return The ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the display name of this celestial body.
     * 
     * @return The display name
     */
    String getDisplayName();
    
    /**
     * Gets the type of this celestial body.
     * 
     * @return The body type
     */
    CelestialBodyType getBodyType();
    
    /**
     * Gets the parent celestial body, if any.
     * 
     * @return The parent body, or null if this is a star or other root body
     */
    ICelestialBody getParent();
    
    /**
     * Gets the children celestial bodies (e.g., moons of a planet).
     * 
     * @return A set of child bodies
     */
    Set<ICelestialBody> getChildren();
    
    /**
     * Gets the orbital properties of this body.
     * 
     * @return The orbital properties
     */
    OrbitalProperties getOrbitalProperties();
    
    /**
     * Gets the physical properties of this body.
     * 
     * @return The physical properties
     */
    PhysicalProperties getPhysicalProperties();
    
    /**
     * Gets the atmospheric properties of this body.
     * 
     * @return The atmospheric properties
     */
    AtmosphericProperties getAtmosphericProperties();
    
    /**
     * Gets the surface gravity relative to Earth (1.0 = Earth gravity).
     * 
     * @return The surface gravity
     */
    float getSurfaceGravity();
    
    /**
     * Gets the dimension/level associated with this celestial body.
     * 
     * @return The dimension, or null if not applicable
     */
    Level getDimension();
    
    /**
     * Gets the dimension ID associated with this celestial body.
     * 
     * @return The dimension ID, or null if not applicable
     */
    ResourceLocation getDimensionId();
    
    /**
     * Gets all available landing sites on this celestial body.
     * 
     * @return A list of landing sites
     */
    List<LandingSite> getLandingSites();
    
    /**
     * Gets the resources that can be found on this celestial body.
     * 
     * @return A map of resource IDs to abundance levels (0.0 to 1.0)
     */
    Map<ResourceLocation, Float> getResources();
    
    /**
     * Gets the temperature range on this celestial body in degrees Celsius.
     * 
     * @return An array of [min, max] temperatures
     */
    float[] getTemperatureRange();
    
    /**
     * Gets the survival difficulty level for this celestial body.
     * 
     * @return A value from 0.0 (easy) to 10.0 (extreme)
     */
    float getSurvivalDifficulty();
    
    /**
     * Gets the orbital position of this body at the given time.
     * 
     * @param timeInTicks The game time in ticks
     * @return The orbital position as [x, y, z] coordinates
     */
    double[] getOrbitalPosition(long timeInTicks);
    
    /**
     * Gets the distance to another celestial body in space units.
     * 
     * @param other The other celestial body
     * @param timeInTicks The game time in ticks
     * @return The distance
     */
    double getDistanceTo(ICelestialBody other, long timeInTicks);
    
    /**
     * Checks if this celestial body is within the habitable zone of its parent star.
     * 
     * @return Whether this body is habitable
     */
    boolean isHabitable();
    
    /**
     * Gets the travel time to this celestial body from another body in ticks.
     * 
     * @param from The starting celestial body
     * @param rocketTier The tier of the rocket (higher = faster)
     * @return The travel time in ticks
     */
    long getTravelTimeFrom(ICelestialBody from, int rocketTier);
    
    /**
     * Classes representing different properties of celestial bodies.
     */
    class OrbitalProperties {
        private double semiMajorAxis; // Distance from parent in space units
        private double eccentricity; // Orbital eccentricity (0 = circular)
        private double inclination; // Orbital inclination in degrees
        private double longitudeOfAscendingNode; // In degrees
        private double argumentOfPeriapsis; // In degrees
        private double meanAnomaly; // In degrees
        private double orbitalPeriod; // In MC days
        
        // Getters and setters
    }
    
    class PhysicalProperties {
        private double radius; // In space units
        private double mass; // In Earth masses
        private double density; // In g/cm³
        private float albedo; // Reflectivity (0.0 to 1.0)
        private int rotationPeriod; // In MC ticks
        private boolean tidallyLocked; // Whether rotation matches orbital period
        
        // Getters and setters
    }
    
    class AtmosphericProperties {
        private boolean hasAtmosphere;
        private float pressure; // In Earth atmospheres
        private Map<String, Float> composition; // Gas name to percentage
        private boolean breathable;
        private float toxicity; // 0.0 to 1.0
        private boolean corrosive;
        
        // Getters and setters
    }
    
    class LandingSite {
        private String name;
        private int x, y, z; // Coordinates
        private Set<String> features; // Special features of this site
        private float difficulty; // Landing difficulty (0.0 to 1.0)
        
        // Getters and setters
    }
    
    /**
     * Types of celestial bodies.
     */
    enum CelestialBodyType {
        STAR,
        PLANET,
        DWARF_PLANET,
        MOON,
        ASTEROID,
        COMET,
        SPACE_STATION,
        BLACK_HOLE,
        NEBULA,
        ARTIFICIAL
    }
}