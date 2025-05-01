package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket cargo bays.
 */
public enum CargoBayType {
    STANDARD("Standard"),
    REINFORCED("Reinforced"),
    INSULATED("Insulated"),
    REFRIGERATED("Refrigerated"),
    PRESSURIZED("Pressurized");
    
    private final String displayName;
    
    /**
     * Creates a new cargo bay type.
     * @param displayName The display name
     */
    CargoBayType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this cargo bay type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}