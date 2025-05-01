package com.astroframe.galactic.core.api.space.registry;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Registry for all celestial bodies.
 * Provides methods to register and retrieve celestial bodies.
 */
public class CelestialBodyRegistry {
    
    private static final Map<ResourceLocation, ICelestialBody> CELESTIAL_BODIES = new HashMap<>();
    
    /**
     * Registers a celestial body.
     * @param celestialBody The celestial body to register
     * @return True if registration was successful
     */
    public static boolean register(ICelestialBody celestialBody) {
        if (celestialBody == null || celestialBody.getId() == null) {
            return false;
        }
        
        if (!CELESTIAL_BODIES.containsKey(celestialBody.getId())) {
            CELESTIAL_BODIES.put(celestialBody.getId(), celestialBody);
            return true;
        }
        return false;
    }
    
    /**
     * Gets a celestial body by its location ID.
     * @param location The celestial body location ID
     * @return The celestial body, or empty if not found
     */
    public static Optional<ICelestialBody> getCelestialBody(ResourceLocation location) {
        return Optional.ofNullable(CELESTIAL_BODIES.get(location));
    }
    
    /**
     * Gets all registered celestial bodies.
     * @return An unmodifiable set of all celestial bodies
     */
    public static Set<ICelestialBody> getAllCelestialBodies() {
        return Collections.unmodifiableSet(CELESTIAL_BODIES.values().stream().collect(Collectors.toSet()));
    }
    
    /**
     * Gets all celestial bodies that match a predicate.
     * @param predicate The predicate to match
     * @return A set of matching celestial bodies
     */
    public static Set<ICelestialBody> getCelestialBodiesByPredicate(Predicate<ICelestialBody> predicate) {
        return CELESTIAL_BODIES.values().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all celestial bodies with breathable atmospheres.
     * @return A set of celestial bodies with breathable atmospheres
     */
    public static Set<ICelestialBody> getHabitableBodies() {
        return getCelestialBodiesByPredicate(ICelestialBody::hasBreathableAtmosphere);
    }
    
    /**
     * Gets all celestial bodies that can be reached with a given rocket tier.
     * @param tier The rocket tier
     * @return A set of reachable celestial bodies
     */
    public static Set<ICelestialBody> getReachableBodies(int tier) {
        return getCelestialBodiesByPredicate(body -> body.getRocketTierRequired() <= tier);
    }
    
    /**
     * Gets all celestial bodies within a certain distance from home.
     * @param maxDistance The maximum distance from home
     * @return A set of celestial bodies within the distance limit
     */
    public static Set<ICelestialBody> getBodiesWithinDistance(int maxDistance) {
        return getCelestialBodiesByPredicate(body -> body.getDistanceFromHome() <= maxDistance);
    }
    
    /**
     * Gets all celestial bodies with resources.
     * @return A set of celestial bodies with resources
     */
    public static Set<ICelestialBody> getResourceBodies() {
        return getCelestialBodiesByPredicate(ICelestialBody::hasResources);
    }
}