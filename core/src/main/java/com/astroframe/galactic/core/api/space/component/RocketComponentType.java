package com.astroframe.galactic.core.api.space.component;

/**
 * Enum for the different types of rocket components.
 */
public enum RocketComponentType {
    /**
     * Rocket engines provide thrust for the rocket.
     */
    ENGINE,
    
    /**
     * Fuel tanks store fuel for the rocket engines.
     */
    FUEL_TANK,
    
    /**
     * The cockpit is the control center for the rocket.
     */
    COCKPIT,
    
    /**
     * The command module is the control center for the rocket with advanced functionality.
     */
    COMMAND_MODULE,
    
    /**
     * Cargo bays provide storage for items.
     */
    CARGO_BAY,
    
    /**
     * Alias for CARGO_BAY for backward compatibility.
     */
    STORAGE,
    
    /**
     * Passenger compartments provide space for passengers.
     */
    PASSENGER_COMPARTMENT,
    
    /**
     * Life support systems provide oxygen and other essentials for passengers.
     */
    LIFE_SUPPORT,
    
    /**
     * Shield systems protect the rocket from damage.
     */
    SHIELD,
    
    /**
     * Alias for SHIELD for backward compatibility.
     */
    SHIELDING,
    
    /**
     * Navigation systems help guide the rocket.
     */
    NAVIGATION,
    
    /**
     * Communication systems allow for communication with ground control.
     */
    COMMUNICATION,
    
    /**
     * Structural components provide the framework for the rocket.
     */
    STRUCTURE,
    
    /**
     * Fuel resource type (not a physical component but used for validation).
     */
    FUEL,
    
    /**
     * Landing gear for safe landings.
     */
    LANDING_GEAR,
    
    /**
     * Heat shield for atmospheric entry.
     */
    HEAT_SHIELD,
    
    /**
     * Solar panels for power generation.
     */
    SOLAR_PANEL;
    
    /**
     * Gets the ID string for this component type.
     * 
     * @return The string ID, lowercase form of the enum name
     */
    public String getId() {
        return this.name().toLowerCase();
    }
    
    /**
     * Gets a component type by its string ID.
     * 
     * @param id The string ID to look up
     * @return The matching component type, or ENGINE if not found
     */
    public static RocketComponentType getById(String id) {
        if (id == null || id.isEmpty()) {
            return ENGINE;
        }
        
        try {
            return RocketComponentType.valueOf(id.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle legacy component IDs or invalid values
            return ENGINE;
        }
    }
}