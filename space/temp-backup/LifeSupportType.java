package com.astroframe.galactic.space.implementation.component;

/**
 * Represents different types of life support systems.
 */
public enum LifeSupportType {
    /**
     * Basic life support - minimal features, lower efficiency.
     */
    BASIC("Basic"),
    
    /**
     * Standard life support - moderate features, medium efficiency.
     */
    STANDARD("Standard"),
    
    /**
     * Advanced life support - maximum features, high efficiency.
     */
    ADVANCED("Advanced"),
    
    /**
     * Specialized life support - focused on specific environments.
     */
    SPECIALIZED("Specialized");
    
    private final String displayName;
    
    LifeSupportType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this life support type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}