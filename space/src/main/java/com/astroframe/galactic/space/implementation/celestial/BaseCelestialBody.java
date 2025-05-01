package com.astroframe.galactic.space.implementation.celestial;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import net.minecraft.resources.ResourceLocation;

/**
 * Base implementation of the ICelestialBody interface.
 */
public class BaseCelestialBody implements ICelestialBody {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final CelestialBodyType type;
    private final ICelestialBody parent;
    private final int distanceFromHome;
    private final float relativeSize;
    private final float relativeGravity;
    private final float atmosphereDensity;
    private final TemperatureRange temperatureRange;
    private final RadiationLevel radiationLevel;
    private final boolean breathableAtmosphere;
    private final boolean hasLiquidWater;
    private final boolean hasUniqueResources;
    private final int rocketTierRequired;
    private boolean discovered;
    
    /**
     * Private constructor used by the Builder.
     */
    private BaseCelestialBody(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.parent = builder.parent;
        this.distanceFromHome = builder.distanceFromHome;
        this.relativeSize = builder.relativeSize;
        this.relativeGravity = builder.relativeGravity;
        this.atmosphereDensity = builder.atmosphereDensity;
        this.temperatureRange = builder.temperatureRange;
        this.radiationLevel = builder.radiationLevel;
        this.breathableAtmosphere = builder.breathableAtmosphere;
        this.hasLiquidWater = builder.hasLiquidWater;
        this.hasUniqueResources = builder.hasUniqueResources;
        this.rocketTierRequired = builder.rocketTierRequired;
        this.discovered = builder.discovered;
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
    public String getDescription() {
        return description;
    }
    
    @Override
    public CelestialBodyType getType() {
        return type;
    }
    
    @Override
    public ICelestialBody getParent() {
        return parent;
    }
    
    @Override
    public int getDistanceFromHome() {
        return distanceFromHome;
    }
    
    @Override
    public float getRelativeSize() {
        return relativeSize;
    }
    
    @Override
    public float getRelativeGravity() {
        return relativeGravity;
    }
    
    @Override
    public float getAtmosphereDensity() {
        return atmosphereDensity;
    }
    
    @Override
    public TemperatureRange getTemperatureRange() {
        return temperatureRange;
    }
    
    @Override
    public RadiationLevel getRadiationLevel() {
        return radiationLevel;
    }
    
    @Override
    public int getRocketTierRequired() {
        return rocketTierRequired;
    }
    
    @Override
    public boolean hasBreathableAtmosphere() {
        return breathableAtmosphere;
    }
    
    @Override
    public boolean hasLiquidWater() {
        return hasLiquidWater;
    }
    
    @Override
    public boolean hasUniqueResources() {
        return hasUniqueResources;
    }
    
    @Override
    public boolean isDiscovered() {
        return discovered;
    }
    
    @Override
    public void setDiscovered(boolean discovered) {
        this.discovered = discovered;
    }
    
    /**
     * Builder class for BaseCelestialBody.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final String name;
        private String description = "";
        private CelestialBodyType type = CelestialBodyType.PLANET;
        private ICelestialBody parent = null;
        private int distanceFromHome = 0;
        private float relativeSize = 1.0f;
        private float relativeGravity = 1.0f;
        private float atmosphereDensity = 1.0f;
        private TemperatureRange temperatureRange = TemperatureRange.TEMPERATE;
        private RadiationLevel radiationLevel = RadiationLevel.NONE;
        private boolean breathableAtmosphere = true;
        private boolean hasLiquidWater = true;
        private boolean hasUniqueResources = false;
        private int rocketTierRequired = 1;
        private boolean discovered = false;
        
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
         * Sets the description.
         * @param description The description
         * @return The builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Sets the celestial body type.
         * @param type The body type
         * @return The builder
         */
        public Builder type(CelestialBodyType type) {
            this.type = type;
            return this;
        }
        
        /**
         * Sets the parent celestial body.
         * @param parent The parent body
         * @return The builder
         */
        public Builder parent(ICelestialBody parent) {
            this.parent = parent;
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
         * Sets the relative size.
         * @param relativeSize The relative size
         * @return The builder
         */
        public Builder relativeSize(float relativeSize) {
            this.relativeSize = relativeSize;
            return this;
        }
        
        /**
         * Sets the relative gravity.
         * @param relativeGravity The relative gravity
         * @return The builder
         */
        public Builder relativeGravity(float relativeGravity) {
            this.relativeGravity = relativeGravity;
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
         * Sets the temperature range.
         * @param temperatureRange The temperature range
         * @return The builder
         */
        public Builder temperatureRange(TemperatureRange temperatureRange) {
            this.temperatureRange = temperatureRange;
            return this;
        }
        
        /**
         * Sets the radiation level.
         * @param radiationLevel The radiation level
         * @return The builder
         */
        public Builder radiationLevel(RadiationLevel radiationLevel) {
            this.radiationLevel = radiationLevel;
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
         * Sets whether this celestial body has liquid water.
         * @param hasLiquidWater true if has liquid water
         * @return The builder
         */
        public Builder hasLiquidWater(boolean hasLiquidWater) {
            this.hasLiquidWater = hasLiquidWater;
            return this;
        }
        
        /**
         * Sets whether this celestial body has unique resources.
         * @param hasUniqueResources true if has unique resources
         * @return The builder
         */
        public Builder hasUniqueResources(boolean hasUniqueResources) {
            this.hasUniqueResources = hasUniqueResources;
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
         * Sets whether this celestial body has been discovered.
         * @param discovered true if discovered
         * @return The builder
         */
        public Builder discovered(boolean discovered) {
            this.discovered = discovered;
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