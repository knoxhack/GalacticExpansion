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
            new ResourceLocation(GalacticSpace.MOD_ID, "earth"),
            new CelestialBody(
                    "earth",
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
            new ResourceLocation(GalacticSpace.MOD_ID, "space_station"),
            new CelestialBody(
                    "space_station",
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
            new ResourceLocation(GalacticSpace.MOD_ID, "moon"),
            new CelestialBody(
                    "moon",
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
            new ResourceLocation(GalacticSpace.MOD_ID, "mars"),
            new CelestialBody(
                    "mars",
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
        private final String id;
        private final int distanceFromHome;
        private final int rocketTierRequired;
        private final float atmosphereDensity;
        private final float radiationLevel;
        private final float gravity;
        private final Component name;
        
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
        public CelestialBody(String id, int distanceFromHome, int rocketTierRequired,
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
        public String getId() {
            return id;
        }
        
        @Override
        public int getDistanceFromHome() {
            return distanceFromHome;
        }
        
        @Override
        public int getRocketTierRequired() {
            return rocketTierRequired;
        }
        
        @Override
        public float getAtmosphereDensity() {
            return atmosphereDensity;
        }
        
        @Override
        public float getRadiationLevel() {
            return radiationLevel;
        }
        
        @Override
        public float getGravity() {
            return gravity;
        }
        
        @Override
        public String getName() {
            return name.getString();
        }
        
        @Override
        public Component getDisplayName() {
            return name;
        }
    }
}