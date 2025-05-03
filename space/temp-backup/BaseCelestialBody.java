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
    private final String description;
    private final CelestialBodyType type;
    private final ICelestialBody parent;
    private final int distanceFromHome;
    private final float relativeSize;
    private final float relativeGravity;
    private final float atmosphereDensity;
    private final boolean breathableAtmosphere;
    private final boolean hasLiquidWater;
    private final TemperatureRange temperatureRange;
    private final RadiationLevel radiationLevel;
    private final boolean hasUniqueResources;
    private final int rocketTierRequired;
    private boolean discovered = true;
    private Level level;

    /**
     * Creates a new celestial body.
     */
    protected BaseCelestialBody(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.type = builder.type;
        this.parent = builder.parent;
        this.distanceFromHome = builder.distanceFromHome;
        this.relativeSize = builder.relativeSize;
        this.relativeGravity = builder.relativeGravity;
        this.atmosphereDensity = builder.atmosphereDensity;
        this.breathableAtmosphere = builder.breathableAtmosphere;
        this.hasLiquidWater = builder.hasLiquidWater;
        this.temperatureRange = builder.temperatureRange;
        this.radiationLevel = builder.radiationLevel;
        this.hasUniqueResources = builder.hasUniqueResources;
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

    /**
     * Gets the level associated with this celestial body.
     * This is a custom method, not part of the ICelestialBody interface.
     * @return The associated level, or null if not set
     */
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
    public boolean hasLiquidWater() {
        return hasLiquidWater;
    }
    
    @Override
    public boolean hasUniqueResources() {
        return hasUniqueResources;
    }

    @Override
    public int getRocketTierRequired() {
        return rocketTierRequired;
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
     * Builder for creating celestial bodies.
     */
    public static class Builder {
        private ResourceLocation id;
        private String name;
        private String description;
        private CelestialBodyType type = CelestialBodyType.PLANET;
        private ICelestialBody parent = null;
        private float relativeSize = 1.0f;
        private float relativeGravity = 1.0f;
        private int distanceFromHome = 0;
        private float atmosphereDensity = 1.0f;
        private boolean breathableAtmosphere = true;
        private boolean hasLiquidWater = true;
        private TemperatureRange temperatureRange = TemperatureRange.TEMPERATE;
        private RadiationLevel radiationLevel = RadiationLevel.NONE;
        private boolean hasUniqueResources = true;
        private int rocketTierRequired = 1;

        public Builder(ResourceLocation id, String name) {
            this.id = id;
            this.name = name;
            this.description = "A celestial body named " + name;
        }
        
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        public Builder type(CelestialBodyType type) {
            this.type = type;
            return this;
        }
        
        public Builder parent(ICelestialBody parent) {
            this.parent = parent;
            return this;
        }
        
        public Builder relativeSize(float relativeSize) {
            this.relativeSize = relativeSize;
            return this;
        }

        public Builder relativeGravity(float relativeGravity) {
            this.relativeGravity = relativeGravity;
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
        
        public Builder hasLiquidWater(boolean hasLiquidWater) {
            this.hasLiquidWater = hasLiquidWater;
            return this;
        }

        public Builder temperatureRange(TemperatureRange temperatureRange) {
            this.temperatureRange = temperatureRange;
            return this;
        }

        public Builder radiationLevel(RadiationLevel radiationLevel) {
            this.radiationLevel = radiationLevel;
            return this;
        }

        public Builder hasUniqueResources(boolean hasUniqueResources) {
            this.hasUniqueResources = hasUniqueResources;
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