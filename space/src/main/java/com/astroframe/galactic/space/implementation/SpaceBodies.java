package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of all celestial bodies in the mod.
 */
public class SpaceBodies {
    private static final Map<ResourceLocation, ICelestialBody> REGISTRY = new HashMap<>();
    
    // Earth (Overworld)
    public static final ICelestialBody EARTH = register(
            makeLocation("earth"),
            new CelestialBody(
                    makeLocation("earth"),
                    0, // Distance from home (it is home)
                    1, // Tier 1 rocket required
                    1.0f, // Full atmosphere
                    0.0f, // No radiation
                    9.8f, // Earth gravity
                    Component.translatable("celestial_body.galactic-space.earth")
            )
    );
    
    // Space Station
    public static final ICelestialBody SPACE_STATION = register(
            makeLocation("space_station"),
            new CelestialBody(
                    makeLocation("space_station"),
                    50, // Relatively close to Earth
                    1, // Tier 1 rocket required
                    0.2f, // Minimal atmosphere (life support required)
                    0.3f, // Some radiation
                    0.2f, // Low gravity
                    Component.translatable("celestial_body.galactic-space.space_station")
            )
    );
    
    // Moon (future expansion)
    public static final ICelestialBody MOON = register(
            makeLocation("moon"),
            new CelestialBody(
                    makeLocation("moon"),
                    200, // Further than Space Station
                    2, // Tier 2 rocket required
                    0.0f, // No atmosphere
                    0.5f, // Moderate radiation
                    0.16f, // Moon gravity
                    Component.translatable("celestial_body.galactic-space.moon")
            )
    );
    
    // Mars (future expansion)
    public static final ICelestialBody MARS = register(
            makeLocation("mars"),
            new CelestialBody(
                    makeLocation("mars"),
                    500, // Much further than Moon
                    3, // Tier 3 rocket required
                    0.1f, // Trace atmosphere
                    0.7f, // High radiation
                    0.38f, // Mars gravity
                    Component.translatable("celestial_body.galactic-space.mars")
            )
    );
    
    /**
     * Registers a celestial body.
     *
     * @param id The resource ID
     * @param body The celestial body
     * @return The registered body
     */
    private static ICelestialBody register(ResourceLocation id, ICelestialBody body) {
        REGISTRY.put(id, body);
        return body;
    }
    
    /**
     * Gets a celestial body by ID.
     *
     * @param id The resource ID
     * @return The celestial body, or null if not found
     */
    public static ICelestialBody get(ResourceLocation id) {
        return REGISTRY.get(id);
    }
    
    /**
     * Gets the registry of all celestial bodies.
     *
     * @return The registry
     */
    public static Map<ResourceLocation, ICelestialBody> getRegistry() {
        return REGISTRY;
    }
    
    /**
     * Creates a ResourceLocation in the mod's namespace.
     *
     * @param path The path
     * @return A new ResourceLocation
     */
    private static ResourceLocation makeLocation(String path) {
        String namespace = GalacticSpace.MOD_ID;
        String fullPath = namespace + ":" + path;
        return ResourceLocation.tryParse(fullPath);
    }
    
    /**
     * Registers all celestial bodies.
     * This method is automatically called during mod initialization.
     */
    public static void registerAll() {
        // Force initialization of static fields
        GalacticSpace.LOGGER.info("Registering celestial bodies:");
        GalacticSpace.LOGGER.info(" - Earth");
        GalacticSpace.LOGGER.info(" - Space Station");
        GalacticSpace.LOGGER.info(" - Moon (planned)");
        GalacticSpace.LOGGER.info(" - Mars (planned)");
    }
    
    /**
     * Implementation of a celestial body.
     */
    private static class CelestialBody implements ICelestialBody {
        private final ResourceLocation id;
        private final int distanceFromHome;
        private final int rocketTierRequired;
        private final float atmosphereDensity;
        private final float radiationLevel;
        private final float gravity;
        private final Component name;
        private boolean discovered = true; // Default to discovered for now
        
        /**
         * Creates a new celestial body.
         *
         * @param id ID of the celestial body
         * @param distanceFromHome Distance from Earth
         * @param rocketTierRequired Minimum rocket tier required
         * @param atmosphereDensity Atmosphere density (0.0-1.0)
         * @param radiationLevel Radiation level (0.0-1.0)
         * @param gravity Gravity level relative to Earth (1.0 = Earth)
         * @param name Display name
         */
        public CelestialBody(ResourceLocation id, int distanceFromHome, int rocketTierRequired,
                           float atmosphereDensity, float radiationLevel, float gravity,
                           Component name) {
            this.id = id;
            this.distanceFromHome = distanceFromHome;
            this.rocketTierRequired = rocketTierRequired;
            this.atmosphereDensity = atmosphereDensity;
            this.radiationLevel = radiationLevel;
            this.gravity = gravity;
            this.name = name;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name.getString();
        }
        
        @Override
        public String getDescription() {
            // Simple implementation - use the name as the description for now
            return "A " + getName().toLowerCase() + " that can be visited with rockets.";
        }
        
        @Override
        public ICelestialBody.CelestialBodyType getType() {
            // Simple implementation - space station is a space station, everything else is a planet
            if (id.getPath().equals("space_station")) {
                return ICelestialBody.CelestialBodyType.SPACE_STATION;
            } else if (id.getPath().equals("moon")) {
                return ICelestialBody.CelestialBodyType.MOON;
            } else if (id.getPath().equals("mars")) {
                return ICelestialBody.CelestialBodyType.PLANET;
            } else {
                return ICelestialBody.CelestialBodyType.PLANET;
            }
        }
        
        @Override
        public ICelestialBody getParent() {
            // Simple implementation - Earth is parent of all except itself
            if (id.getPath().equals("earth")) {
                return null;
            } else {
                return EARTH;
            }
        }
        
        @Override
        public int getDistanceFromHome() {
            return distanceFromHome;
        }
        
        @Override
        public float getRelativeSize() {
            // Simple implementation - Earth is 1.0, others vary
            if (id.getPath().equals("earth")) {
                return 1.0f;
            } else if (id.getPath().equals("moon")) {
                return 0.27f;
            } else if (id.getPath().equals("mars")) {
                return 0.53f;
            } else if (id.getPath().equals("space_station")) {
                return 0.01f; // Very small compared to Earth
            } else {
                return 0.5f; // Default
            }
        }
        
        @Override
        public float getRelativeGravity() {
            return gravity; // We already store gravity relative to Earth
        }
        
        @Override
        public float getAtmosphereDensity() {
            return atmosphereDensity;
        }
        
        @Override
        public ICelestialBody.TemperatureRange getTemperatureRange() {
            // Simple implementation based on body type
            if (id.getPath().equals("earth")) {
                return ICelestialBody.TemperatureRange.TEMPERATE;
            } else if (id.getPath().equals("moon")) {
                return ICelestialBody.TemperatureRange.COLD;
            } else if (id.getPath().equals("mars")) {
                return ICelestialBody.TemperatureRange.COLD;
            } else if (id.getPath().equals("space_station")) {
                return ICelestialBody.TemperatureRange.TEMPERATE; // Controlled environment
            } else {
                return ICelestialBody.TemperatureRange.TEMPERATE; // Default
            }
        }
        
        @Override
        public ICelestialBody.RadiationLevel getRadiationLevel() {
            // Convert from 0-1 scale to enum
            if (radiationLevel < 0.1f) {
                return ICelestialBody.RadiationLevel.NONE;
            } else if (radiationLevel < 0.3f) {
                return ICelestialBody.RadiationLevel.LOW;
            } else if (radiationLevel < 0.6f) {
                return ICelestialBody.RadiationLevel.MODERATE;
            } else if (radiationLevel < 0.8f) {
                return ICelestialBody.RadiationLevel.HIGH;
            } else {
                return ICelestialBody.RadiationLevel.EXTREME;
            }
        }
        
        @Override
        public int getRocketTierRequired() {
            return rocketTierRequired;
        }
        
        @Override
        public boolean hasBreathableAtmosphere() {
            // Only Earth has breathable atmosphere
            return id.getPath().equals("earth");
        }
        
        @Override
        public boolean hasLiquidWater() {
            // Only Earth has liquid water
            return id.getPath().equals("earth");
        }
        
        @Override
        public boolean hasUniqueResources() {
            // All bodies except Earth have unique resources
            return !id.getPath().equals("earth");
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
         * Gets the display name component of this celestial body.
         * This is a custom method specific to this implementation and not part of the ICelestialBody interface.
         * @return The display name as a Component
         */
        public Component getDisplayComponent() {
            return name;
        }
    }
}