package com.astroframe.galactic.core.api.space;

/**
 * Enumeration of all possible rocket component types.
 */
public enum RocketComponentType {
    ENGINE("engine", "Engine"),
    COCKPIT("cockpit", "Cockpit"),
    FUEL_TANK("fuel_tank", "Fuel Tank"),
    CARGO_BAY("cargo_bay", "Cargo Bay"),
    LANDING_GEAR("landing_gear", "Landing Gear"),
    HEAT_SHIELD("heat_shield", "Heat Shield"),
    SOLAR_PANEL("solar_panel", "Solar Panel"),
    COMMUNICATION("communication", "Communication"),
    NAVIGATION("navigation", "Navigation"),
    LIFE_SUPPORT("life_support", "Life Support");
    
    private final String id;
    private final String displayName;
    
    /**
     * Constructs a rocket component type.
     *
     * @param id The component type ID
     * @param displayName The component display name
     */
    RocketComponentType(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
    
    /**
     * Gets the component type ID.
     *
     * @return The component type ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the component display name.
     *
     * @return The component display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the component type by ID.
     *
     * @param id The component ID to find
     * @return The matching component type, or ENGINE if not found
     */
    public static RocketComponentType getById(String id) {
        for (RocketComponentType type : values()) {
            if (type.id.equals(id)) {
                return type;
            }
        }
        return ENGINE; // Default to ENGINE if not found
    }
}