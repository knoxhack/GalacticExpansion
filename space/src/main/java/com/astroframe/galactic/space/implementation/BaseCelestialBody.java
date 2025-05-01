package com.astroframe.galactic.space.implementation;

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
     * Creates a new celestial body.
     */
    protected BaseCelestialBody(Builder builder) {
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
        this.level = null;
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
     * @param level The level
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
        return temperatureRange.clone();
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
     * Builder for creating celestial bodies.
     */
    public static class Builder {
        private ResourceLocation id;
        private String name;
        private float gravityFactor = 1.0f;
        private int distanceFromHome = 0;
        private float atmosphereDensity = 1.0f;
        private boolean breathableAtmosphere = true;
        private float[] temperatureRange = new float[]{-20, 40};
        private float radiationLevel = 1.0f;
        private boolean hasResources = true;
        private int rocketTierRequired = 1;

        public Builder(ResourceLocation id, String name) {
            this.id = id;
            this.name = name;
        }

        public Builder gravityFactor(float gravityFactor) {
            this.gravityFactor = gravityFactor;
            return this;
        }

        public Builder distanceFromHome(int distanceFromHome) {
            this.distanceFromHome = distanceFromHome;
            return this;
        }

        public Builder atmosphereDensity(float atmosphereDensity) {
            this.atmosphereDensity = atmosphereDensity;
            return this;
        }

        public Builder breathableAtmosphere(boolean breathableAtmosphere) {
            this.breathableAtmosphere = breathableAtmosphere;
            return this;
        }

        public Builder temperatureRange(float min, float max) {
            this.temperatureRange = new float[]{min, max};
            return this;
        }

        public Builder radiationLevel(float radiationLevel) {
            this.radiationLevel = radiationLevel;
            return this;
        }

        public Builder hasResources(boolean hasResources) {
            this.hasResources = hasResources;
            return this;
        }

        public Builder rocketTierRequired(int rocketTierRequired) {
            this.rocketTierRequired = rocketTierRequired;
            return this;
        }

        public BaseCelestialBody build() {
            return new BaseCelestialBody(this);
        }
    }
}