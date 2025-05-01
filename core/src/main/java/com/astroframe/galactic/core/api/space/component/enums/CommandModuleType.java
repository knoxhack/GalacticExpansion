package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket command modules.
 */
public enum CommandModuleType {
    BASIC("Basic"),
    ADVANCED("Advanced"),
    SUPERIOR("Superior"),
    SCIENCE("Science"),
    MILITARY("Military");
    
    private final String displayName;
    
    /**
     * Creates a new command module type.
     * @param displayName The display name
     */
    CommandModuleType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this command module type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}