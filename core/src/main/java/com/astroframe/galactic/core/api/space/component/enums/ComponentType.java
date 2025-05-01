package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Represents different types of rocket components.
 */
public enum ComponentType {
    COMMAND_MODULE("Command Module"),
    ENGINE("Engine"),
    FUEL_TANK("Fuel Tank"),
    CARGO_BAY("Cargo Bay"),
    PASSENGER_COMPARTMENT("Passenger Compartment"),
    SHIELD("Shield"),
    LIFE_SUPPORT("Life Support");
    
    private final String displayName;
    
    /**
     * Creates a new component type.
     * @param displayName The display name
     */
    ComponentType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this component type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}