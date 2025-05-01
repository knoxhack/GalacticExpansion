package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * Interface representing a celestial body like a planet, moon, or asteroid.
 * Used by the Space module to manage different planets and their properties.
 */
public interface ICelestialBody {

    /**
     * Gets the unique identifier for this celestial body.
     * @return A unique resource location
     */
    ResourceLocation getId();
    
    /**
     * Gets the name of this celestial body.
     * @return The display name
     */
    String getName();
    
    /**
     * Gets the gravity factor for this celestial body.
     * Earth = 1.0, Moon = 0.16, etc.
     * @return The gravity factor
     */
    float getGravityFactor();
    
    /**
     * Gets the distance from the home planet (Earth) in arbitrary units.
     * @return The distance
     */
    int getDistanceFromHome();
    
    /**
     * Gets the atmospheric density factor.
     * Earth = 1.0, Moon = 0.0 (no atmosphere), etc.
     * @return The atmosphere density
     */
    float getAtmosphereDensity();
    
    /**
     * Gets the dimension level for this celestial body.
     * @return The level, or null if not yet created
     */
    Level getLevel();
    
    /**
     * Returns whether this celestial body has breathable atmosphere.
     * @return true if players can breathe without assistance
     */
    boolean hasBreathableAtmosphere();
    
    /**
     * Gets the temperature range on this celestial body.
     * @return An array of [min, max] temperatures in Celsius
     */
    float[] getTemperatureRange();
    
    /**
     * Gets the amount of radiation present on this celestial body.
     * Earth = 1.0, higher values mean more radiation
     * @return The radiation level
     */
    float getRadiationLevel();
    
    /**
     * Returns whether this celestial body has natural resources that can be mined.
     * @return true if resources are available
     */
    boolean hasResources();
    
    /**
     * Gets the tier of rocket needed to reach this celestial body.
     * @return The rocket tier
     */
    int getRocketTierRequired();
}