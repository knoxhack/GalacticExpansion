package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.core.api.space.ICelestialBody;
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
                new ResourceLocation(GalacticSpace.MOD_ID, id),
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
        if (dimensionId.equals(new ResourceLocation(GalacticSpace.MOD_ID, "space_station"))) {
            return SPACE_STATION;
        }
        
        return null;
    }
    
    /**
     * Simple implementation of ICelestialBody
     */
    private static class CelestialBody implements ICelestialBody {
        private final ResourceLocation id;
        private final String name;
        private final boolean hasAtmosphere;
        private final boolean hasUniqueResources;
        
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
        public boolean hasAtmosphere() {
            return hasAtmosphere;
        }
        
        @Override
        public boolean hasUniqueResources() {
            return hasUniqueResources;
        }
    }
}