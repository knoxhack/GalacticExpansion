package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket passenger compartments.
 */
public enum CompartmentType {
    BASIC("Basic"),
    STANDARD("Standard"),
    LUXURY("Luxury"),
    MEDICAL("Medical"),
    SCIENTIFIC("Scientific"),
    CRYOGENIC("Cryogenic");
    
    private final String displayName;
    
    /**
     * Creates a new compartment type.
     * @param displayName The display name
     */
    CompartmentType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this compartment type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}