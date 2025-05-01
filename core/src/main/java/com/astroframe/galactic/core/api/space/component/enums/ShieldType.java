package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket shields.
 */
public enum ShieldType {
    BASIC("Basic"),
    THERMAL("Thermal"),
    PHYSICAL("Physical"),
    RADIATION("Radiation"),
    DEFLECTOR("Deflector"),
    QUANTUM("Quantum"),
    ENERGY("Energy"),
    ADVANCED("Advanced");
    
    private final String displayName;
    
    /**
     * Creates a new shield type.
     * @param displayName The display name
     */
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