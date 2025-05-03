package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.CelestialBodyType;
import com.astroframe.galactic.core.api.space.RadiationLevel;
import com.astroframe.galactic.core.api.space.TemperatureRange;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Registry for all celestial bodies in the space mod.
 */
public class SpaceBodies {
    
    private static final Map<ResourceLocation, ICelestialBody> CELESTIAL_BODIES = new HashMap<>();
    
    // Built-in celestial bodies
    public static final ICelestialBody SPACE_STATION = createCelestialBody("space_station", "Space Station", true, true);
    public static final ICelestialBody EARTH = createCelestialBody("earth", "Earth", false, false);
    
    /**
     * Registers all built-in celestial bodies.
     */
    public static void registerAll() {
        GalacticSpace.LOGGER.info("Registering celestial bodies");
        
        // All bodies are registered during static initialization
        GalacticSpace.LOGGER.info("Registered {} celestial bodies", CELESTIAL_BODIES.size());
    }
    
    /**
     * Creates and registers a celestial body.
     *
     * @param id The ID of the celestial body
     * @param name The display name
     * @param hasAtmosphere Whether this body has an atmosphere
     * @param hasUniqueResources Whether this body has unique resources
     * @return The created celestial body
     */
    private static ICelestialBody createCelestialBody(String id, String name, boolean hasAtmosphere, boolean hasUniqueResources) {
        ICelestialBody body = new CelestialBody(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":" + id),
                name,
                hasAtmosphere,
                hasUniqueResources
        );
        CELESTIAL_BODIES.put(body.getId(), body);
        return body;
    }
    
    /**
     * Gets a celestial body by ID.
     *
     * @param id The ID of the celestial body
     * @return The celestial body, or null if not found
     */
    public static ICelestialBody get(ResourceLocation id) {
        return CELESTIAL_BODIES.get(id);
    }
    
    /**
     * Gets all registered celestial bodies.
     *
     * @return All celestial bodies
     */
    public static Set<ICelestialBody> getAll() {
        return Set.copyOf(CELESTIAL_BODIES.values());
    }
    
    /**
     * Gets the celestial body for a dimension.
     *
     * @param level The level
     * @return The celestial body, or null if not found
     */
    public static ICelestialBody getForDimension(Level level) {
        // In a more complex implementation, this would check the dimension type
        // against registered dimension types for celestial bodies
        if (level.dimension() == Level.OVERWORLD) {
            return EARTH;
        }
        
        ResourceLocation dimensionId = level.dimension().location();
        if (dimensionId.equals(ResourceLocation.parse(GalacticSpace.MOD_ID + ":" + "space_station"))) {
            return SPACE_STATION;
        }
        
        return null;
    }
    
    /**
     * Registers all celestial bodies with a space travel manager.
     * 
     * @param manager The space travel manager
     */
    public static void registerWithManager(com.astroframe.galactic.core.api.space.ISpaceTravelManager manager) {
        if (manager == null) {
            GalacticSpace.LOGGER.error("Cannot register celestial bodies with null manager");
            return;
        }
        
        GalacticSpace.LOGGER.info("Registering {} celestial bodies with manager", CELESTIAL_BODIES.size());
        
        // Register each celestial body with the manager
        for (ICelestialBody body : CELESTIAL_BODIES.values()) {
            manager.registerCelestialBody(body);
        }
    }
    
    /**
     * Simple implementation of ICelestialBody
     */
    private static class CelestialBody implements ICelestialBody {
        private final ResourceLocation id;
        private final String name;
        private final boolean hasAtmosphere;
        private final boolean hasUniqueResources;
        private boolean discovered = false;
        
        public CelestialBody(ResourceLocation id, String name, boolean hasAtmosphere, boolean hasUniqueResources) {
            this.id = id;
            this.name = name;
            this.hasAtmosphere = hasAtmosphere;
            this.hasUniqueResources = hasUniqueResources;
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
            return "A celestial body in space.";
        }
        
        @Override
        public CelestialBodyType getType() {
            return CelestialBodyType.PLANET;
        }
        
        @Override
        public ICelestialBody getParent() {
            return null; // No parent by default
        }
        
        @Override
        public float getDistance() {
            return 0.0f; // Default distance
        }
        
        // This method is now provided by the default implementation
        // in ICelestialBody which calls getDistance()
        // @Override
        // public float getDistanceFromHome() {
        //     return getDistance();
        // }
        
        @Override
        public float getTemperature() {
            return 20.0f; // Default temperature in Celsius
        }
        
        @Override
        public float getRelativeSize() {
            return 1.0f; // Earth-sized by default
        }
        
        @Override
        public float getGravity() {
            return 1.0f; // Earth-like gravity by default
        }
        
        @Override
        public float getRelativeGravity() {
            return getGravity(); // Use getGravity as per the interface
        }
        
        @Override
        public float getAtmosphericPressure() {
            return hasAtmosphere ? 1.0f : 0.0f;
        }
        
        @Override
        public float getAtmosphereDensity() {
            return getAtmosphericPressure();
        }
        
        @Override
        public TemperatureRange getTemperatureRange() {
            return TemperatureRange.TEMPERATE; // Moderate temperatures by default
        }
        
        @Override
        public RadiationLevel getRadiationLevel() {
            return RadiationLevel.NONE; // No radiation by default
        }
        
        @Override
        public int getRequiredTier() {
            return 1; // Basic rocket tier by default
        }
        
        // This method is now provided by the default implementation
        // in ICelestialBody which calls getRequiredTier()
        // @Override
        // public int getRocketTierRequired() {
        //     return getRequiredTier();
        // }
        
        @Override
        public boolean hasAtmosphere() {
            return hasAtmosphere;
        }
        
        @Override
        public boolean hasBreathableAtmosphere() {
            return hasAtmosphere;
        }
        
        @Override
        public boolean hasLiquidWater() {
            return false; // No liquid water by default
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
    }
}