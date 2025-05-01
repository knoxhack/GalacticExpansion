package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket fuel.
 */
public enum FuelType {
    CHEMICAL("Chemical"),
    HYDROGEN("Hydrogen"),
    ION("Ion"),
    NUCLEAR("Nuclear"),
    FUSION("Fusion"),
    EXOTIC("Exotic"),
    ANTIMATTER("Antimatter");
    
    private final String displayName;
    
    /**
     * Creates a new fuel type.
     * @param displayName The display name
     */
    FuelType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this fuel type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}