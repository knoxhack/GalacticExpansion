package com.astroframe.galactic.energy.api;

/**
 * Enum for different types of energy in the Galactic Expansion mod.
 * Each energy type has its own characteristics and is handled separately.
 */
public enum EnergyType {
    /**
     * Electrical energy.
     * Commonly used for most machinery and technology-based systems.
     */
    ELECTRICAL("electrical"),
    
    /**
     * Steam energy.
     * Used in early-game machinery and industrial systems.
     */
    STEAM("steam"),
    
    /**
     * Nuclear energy.
     * High-density energy storage used for advanced machinery.
     */
    NUCLEAR("nuclear"),
    
    /**
     * Solar energy.
     * Renewable energy source from stars and solar panels.
     */
    SOLAR("solar");
    
    private final String id;
    
    /**
     * Constructor for energy types.
     * 
     * @param id The string identifier for this energy type
     */
    EnergyType(String id) {
        this.id = id;
    }
    
    /**
     * Get the string identifier for this energy type.
     * 
     * @return The identifier
     */
    public String getId() {
        return id;
    }
    
    /**
     * Convert a string identifier to an energy type.
     * 
     * @param id The string identifier
     * @return The corresponding energy type, or ELECTRICAL if not found
     */
    public static EnergyType fromId(String id) {
        for (EnergyType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        
        return ELECTRICAL; // Default to electrical if not found
    }
}