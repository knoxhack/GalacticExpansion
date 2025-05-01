package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket engines.
 */
public enum EngineType {
    CHEMICAL("Chemical"),
    ION("Ion"),
    NUCLEAR("Nuclear"),
    FUSION("Fusion"),
    ANTIMATTER("Antimatter");
    
    private final String displayName;
    
    /**
     * Creates a new engine type.
     * @param displayName The display name
     */
    EngineType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this engine type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}