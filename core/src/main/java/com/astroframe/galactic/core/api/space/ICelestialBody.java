package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface for celestial bodies that can be visited by rockets.
 */
public interface ICelestialBody {
    
    /**
     * Gets the unique ID of this celestial body.
     * @return The celestial body ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the name of this celestial body.
     * @return The name
     */
    String getName();
    
    /**
     * Gets the description of this celestial body.
     * @return The description
     */
    String getDescription();
    
    /**
     * Gets the type of this celestial body.
     * @return The celestial body type
     */
    CelestialBodyType getType();
    
    /**
     * Gets the parent celestial body, or null if this is a primary body.
     * @return The parent body
     */
    ICelestialBody getParent();
    
    /**
     * Gets the distance of this celestial body from Earth (or home planet).
     * @return The distance in arbitrary units
     */
    int getDistanceFromHome();
    
    /**
     * Gets the size of this celestial body relative to Earth.
     * @return The relative size
     */
    float getRelativeSize();
    
    /**
     * Gets the gravity of this celestial body relative to Earth.
     * @return The relative gravity
     */
    float getRelativeGravity();
    
    /**
     * Gets the density of the atmosphere on this celestial body.
     * 0 means no atmosphere, 1 means Earth-like atmosphere.
     * @return The atmosphere density
     */
    float getAtmosphereDensity();
    
    /**
     * Gets the temperature range on this celestial body.
     * @return The temperature range
     */
    TemperatureRange getTemperatureRange();
    
    /**
     * Gets the radiation level on this celestial body.
     * @return The radiation level
     */
    RadiationLevel getRadiationLevel();
    
    /**
     * Gets the minimum rocket tier required to reach this celestial body.
     * @return The minimum rocket tier (1-3)
     */
    int getRocketTierRequired();
    
    /**
     * Whether this celestial body has breathable atmosphere.
     * @return true if the atmosphere is breathable
     */
    boolean hasBreathableAtmosphere();
    
    /**
     * Whether this celestial body has liquid water.
     * @return true if the body has liquid water
     */
    boolean hasLiquidWater();
    
    /**
     * Whether this celestial body has unique resources.
     * @return true if the body has unique resources
     */
    boolean hasUniqueResources();
    
    /**
     * Whether this celestial body has been discovered.
     * @return true if the body has been discovered
     */
    boolean isDiscovered();
    
    /**
     * Sets whether this celestial body has been discovered.
     * @param discovered The discovered state
     */
    void setDiscovered(boolean discovered);
    
    /**
     * Enum representing the types of celestial bodies.
     */
    enum CelestialBodyType {
        PLANET,
        MOON,
        ASTEROID,
        COMET,
        SPACE_STATION,
        STAR,
        DWARF_PLANET,
        GAS_GIANT,
        BLACK_HOLE
    }
    
    /**
     * Enum representing the temperature ranges of celestial bodies.
     */
    enum TemperatureRange {
        FREEZING(-200, -50),
        COLD(-50, 0),
        TEMPERATE(0, 30),
        HOT(30, 100),
        EXTREME_HEAT(100, 500);
        
        private final int minTemperature;
        private final int maxTemperature;
        
        TemperatureRange(int minTemperature, int maxTemperature) {
            this.minTemperature = minTemperature;
            this.maxTemperature = maxTemperature;
        }
        
        /**
         * Gets the minimum temperature in Celsius.
         * @return The minimum temperature
         */
        public int getMinTemperature() {
            return minTemperature;
        }
        
        /**
         * Gets the maximum temperature in Celsius.
         * @return The maximum temperature
         */
        public int getMaxTemperature() {
            return maxTemperature;
        }
    }
    
    /**
     * Enum representing the radiation levels of celestial bodies.
     */
    enum RadiationLevel {
        NONE(0),
        LOW(1),
        MODERATE(2),
        HIGH(3),
        EXTREME(4);
        
        private final int level;
        
        RadiationLevel(int level) {
            this.level = level;
        }
        
        /**
         * Gets the numeric radiation level.
         * @return The radiation level
         */
        public int getLevel() {
            return level;
        }
    }
}