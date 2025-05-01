package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;

/**
 * Default celestial bodies for the Space module.
 * 
 * Note: This module only contains the Space Station.
 * Planetary exploration is handled by the Exploration module.
 */
public class SpaceBodies {
    // Earth - Home planet reference (required as starting point)
    public static final ICelestialBody EARTH = new BaseCelestialBody.Builder(
            new ResourceLocation("minecraft:overworld"), // Use vanilla minecraft namespace
            "Earth")
            .gravityFactor(1.0f)
            .distanceFromHome(0)
            .atmosphereDensity(1.0f)
            .breathableAtmosphere(true)
            .temperatureRange(-60, 60)
            .radiationLevel(1.0f)
            .hasResources(true)
            .rocketTierRequired(0)
            .build();
    
    // Space Station
    public static final ICelestialBody SPACE_STATION = new BaseCelestialBody.Builder(
            new ResourceLocation(GalacticSpace.MOD_ID, "space_station"),
            "Orbital Space Station")
            .gravityFactor(0.05f)
            .distanceFromHome(5)
            .atmosphereDensity(0.0f)
            .breathableAtmosphere(false)  // Requires life support
            .temperatureRange(-100, 100)  // Extreme temperature variations in space
            .radiationLevel(2.5f)
            .hasResources(false)          // No natural resources, needs supply missions
            .rocketTierRequired(1)        // Only tier 1 required to reach orbit
            .build();
    
    /**
     * Registers all celestial bodies for the Space module.
     * Note: This only includes the Space Station.
     * All planetary destinations are registered by the Exploration module.
     */
    public static void registerAll() {
        CelestialBodyRegistry.register(EARTH);    // Reference to home planet
        CelestialBodyRegistry.register(SPACE_STATION);
    }
}