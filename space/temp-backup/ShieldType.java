package com.astroframe.galactic.space.implementation.component;

/**
 * Represents different types of shield systems.
 */
public enum ShieldType {
    /**
     * Basic shield - minimal features, lower efficiency.
     */
    BASIC("Basic"),
    
    /**
     * Thermal shield - specialized for heat resistance.
     */
    THERMAL("Thermal"),
    
    /**
     * Radiation shield - specialized for radiation protection.
     */
    RADIATION("Radiation"),
    
    /**
     * Impact shield - specialized for micrometeorite protection.
     */
    IMPACT("Impact"),
    
    /**
     * Advanced shield - maximum features, high efficiency.
     */
    ADVANCED("Advanced");
    
    private final String displayName;
    
    ShieldType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this shield type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}