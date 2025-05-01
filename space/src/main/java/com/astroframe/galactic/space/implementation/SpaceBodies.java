package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;

/**
 * Default celestial bodies for the Space module.
 */
public class SpaceBodies {
    // Earth - Home planet
    public static final ICelestialBody EARTH = new BaseCelestialBody.Builder(
            ResourceLocation.parse(GalacticSpace.MOD_ID + ":earth"),
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
    
    // Moon
    public static final ICelestialBody MOON = new BaseCelestialBody.Builder(
            ResourceLocation.parse(GalacticSpace.MOD_ID + ":moon"),
            "Moon")
            .gravityFactor(0.16f)
            .distanceFromHome(10)
            .atmosphereDensity(0.0f)
            .breathableAtmosphere(false)
            .temperatureRange(-170, 130)
            .radiationLevel(2.0f)
            .hasResources(true)
            .rocketTierRequired(1)
            .build();
    
    // Mars
    public static final ICelestialBody MARS = new BaseCelestialBody.Builder(
            ResourceLocation.parse(GalacticSpace.MOD_ID + ":mars"),
            "Mars")
            .gravityFactor(0.38f)
            .distanceFromHome(50)
            .atmosphereDensity(0.1f)
            .breathableAtmosphere(false)
            .temperatureRange(-125, 35)
            .radiationLevel(3.0f)
            .hasResources(true)
            .rocketTierRequired(2)
            .build();
    
    // Jupiter's Moon Europa
    public static final ICelestialBody EUROPA = new BaseCelestialBody.Builder(
            ResourceLocation.parse(GalacticSpace.MOD_ID + ":europa"),
            "Europa")
            .gravityFactor(0.13f)
            .distanceFromHome(150)
            .atmosphereDensity(0.0f)
            .breathableAtmosphere(false)
            .temperatureRange(-220, -120)
            .radiationLevel(5.0f)
            .hasResources(true)
            .rocketTierRequired(3)
            .build();
    
    // Distant exoplanet
    public static final ICelestialBody PROXIMA_B = new BaseCelestialBody.Builder(
            ResourceLocation.parse(GalacticSpace.MOD_ID + ":proxima_b"),
            "Proxima Centauri b")
            .gravityFactor(1.2f)
            .distanceFromHome(1000)
            .atmosphereDensity(0.8f)
            .breathableAtmosphere(false)
            .temperatureRange(-40, 80)
            .radiationLevel(4.0f)
            .hasResources(true)
            .rocketTierRequired(3)
            .build();
    
    /**
     * Registers all default celestial bodies.
     */
    public static void registerAll() {
        CelestialBodyRegistry.register(EARTH);
        CelestialBodyRegistry.register(MOON);
        CelestialBodyRegistry.register(MARS);
        CelestialBodyRegistry.register(EUROPA);
        CelestialBodyRegistry.register(PROXIMA_B);
    }
}