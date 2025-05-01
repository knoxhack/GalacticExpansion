package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket life support systems.
 */
public enum LifeSupportType {
    BASIC("Basic"),
    STANDARD("Standard"),
    ADVANCED("Advanced"),
    EXTENDED("Extended"),
    EFFICIENT("Efficient"),
    LUXURIOUS("Luxurious"),
    MILITARY("Military"),
    SCIENTIFIC("Scientific"),
    BIOREGENERATIVE("Bioregenerative");
    
    private final String displayName;
    
    /**
     * Creates a new life support type.
     * @param displayName The display name
     */
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