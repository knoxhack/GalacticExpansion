package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket shields.
 */
public enum ShieldType {
    BASIC("Basic"),
    THERMAL("Thermal"),
    RADIATION("Radiation"),
    EMP("EMP"),
    ADVANCED("Advanced"),
    SUPERIOR("Superior");
    
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