package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for celestial bodies in the Space module.
 */
public class CelestialBodyRegistry {
    private static final Map<ResourceLocation, ICelestialBody> CELESTIAL_BODIES = new HashMap<>();
    
    /**
     * Registers a celestial body in the registry.
     * @param body The celestial body to register
     */
    public static void register(ICelestialBody body) {
        CELESTIAL_BODIES.put(body.getId(), body);
    }
    
    /**
     * Gets a celestial body by its ID.
     * @param id The celestial body ID
     * @return The celestial body, or null if not found
     */
    public static ICelestialBody get(ResourceLocation id) {
        return CELESTIAL_BODIES.get(id);
    }
    
    /**
     * Gets all registered celestial bodies.
     * @return An unmodifiable collection of all celestial bodies
     */
    public static Collection<ICelestialBody> getAll() {
        return Collections.unmodifiableCollection(CELESTIAL_BODIES.values());
    }
}