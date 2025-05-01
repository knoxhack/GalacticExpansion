package com.astroframe.galactic.space.implementation.celestial;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Base implementation of the ICelestialBody interface.
 */
public class BaseCelestialBody implements ICelestialBody {
    
    private final ResourceLocation id;
    private final String name;
    private final float gravityFactor;
    private final int distanceFromHome;
    private final float atmosphereDensity;
    private final boolean breathableAtmosphere;
    private final float[] temperatureRange;
    private final float radiationLevel;
    private final boolean hasResources;
    private final int rocketTierRequired;
    private Level level;
    
    /**
     * Private constructor used by the Builder.
     */
    private BaseCelestialBody(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.gravityFactor = builder.gravityFactor;
        this.distanceFromHome = builder.distanceFromHome;
        this.atmosphereDensity = builder.atmosphereDensity;
        this.breathableAtmosphere = builder.breathableAtmosphere;
        this.temperatureRange = builder.temperatureRange;
        this.radiationLevel = builder.radiationLevel;
        this.hasResources = builder.hasResources;
        this.rocketTierRequired = builder.rocketTierRequired;
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public float getGravityFactor() {
        return gravityFactor;
    }
    
    @Override
    public int getDistanceFromHome() {
        return distanceFromHome;
    }
    
    @Override
    public float getAtmosphereDensity() {
        return atmosphereDensity;
    }
    
    @Override
    public Level getLevel() {
        return level;
    }
    
    /**
     * Sets the level for this celestial body.
     * @param level The Minecraft level
     */
    public void setLevel(Level level) {
        this.level = level;
    }
    
    @Override
    public boolean hasBreathableAtmosphere() {
        return breathableAtmosphere;
    }
    
    @Override
    public float[] getTemperatureRange() {
        return temperatureRange;
    }
    
    @Override
    public float getRadiationLevel() {
        return radiationLevel;
    }
    
    @Override
    public boolean hasResources() {
        return hasResources;
    }
    
    @Override
    public int getRocketTierRequired() {
        return rocketTierRequired;
    }
    
    /**
     * Builder class for BaseCelestialBody.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final String name;
        private float gravityFactor = 1.0f;
        private int distanceFromHome = 0;
        private float atmosphereDensity = 1.0f;
        private boolean breathableAtmosphere = true;
        private float[] temperatureRange = new float[]{-60, 60};
        private float radiationLevel = 1.0f;
        private boolean hasResources = true;
        private int rocketTierRequired = 0;
        
        /**
         * Creates a new builder with the minimal required properties.
         * @param id The unique identifier for the celestial body
         * @param name The display name
         */
        public Builder(ResourceLocation id, String name) {
            this.id = id;
            this.name = name;
        }
        
        /**
         * Sets the gravity factor.
         * @param gravityFactor The gravity factor
         * @return The builder
         */
        public Builder gravityFactor(float gravityFactor) {
            this.gravityFactor = gravityFactor;
            return this;
        }
        
        /**
         * Sets the distance from home.
         * @param distanceFromHome The distance
         * @return The builder
         */
        public Builder distanceFromHome(int distanceFromHome) {
            this.distanceFromHome = distanceFromHome;
            return this;
        }
        
        /**
         * Sets the atmosphere density.
         * @param atmosphereDensity The atmosphere density
         * @return The builder
         */
        public Builder atmosphereDensity(float atmosphereDensity) {
            this.atmosphereDensity = atmosphereDensity;
            return this;
        }
        
        /**
         * Sets whether the atmosphere is breathable.
         * @param breathableAtmosphere true if breathable
         * @return The builder
         */
        public Builder breathableAtmosphere(boolean breathableAtmosphere) {
            this.breathableAtmosphere = breathableAtmosphere;
            return this;
        }
        
        /**
         * Sets the temperature range.
         * @param min The minimum temperature in Celsius
         * @param max The maximum temperature in Celsius
         * @return The builder
         */
        public Builder temperatureRange(float min, float max) {
            this.temperatureRange = new float[]{min, max};
            return this;
        }
        
        /**
         * Sets the radiation level.
         * @param radiationLevel The radiation level
         * @return The builder
         */
        public Builder radiationLevel(float radiationLevel) {
            this.radiationLevel = radiationLevel;
            return this;
        }
        
        /**
         * Sets whether this celestial body has resources.
         * @param hasResources true if has resources
         * @return The builder
         */
        public Builder hasResources(boolean hasResources) {
            this.hasResources = hasResources;
            return this;
        }
        
        /**
         * Sets the rocket tier required to reach this celestial body.
         * @param rocketTierRequired The required tier
         * @return The builder
         */
        public Builder rocketTierRequired(int rocketTierRequired) {
            this.rocketTierRequired = rocketTierRequired;
            return this;
        }
        
        /**
         * Builds the celestial body.
         * @return A new BaseCelestialBody
         */
        public BaseCelestialBody build() {
            return new BaseCelestialBody(this);
        }
    }
}