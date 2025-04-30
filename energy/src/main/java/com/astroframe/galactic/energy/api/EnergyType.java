package com.astroframe.galactic.energy.api;

/**
 * Enum representing different types of energy in the Galactic Expansion mod.
 */
public enum EnergyType {
    /**
     * Standard electrical energy.
     */
    ELECTRICAL("electrical", "EU", 0x4286f4),
    
    /**
     * Steam energy, typically produced by boilers.
     */
    STEAM("steam", "SU", 0xd9d9d9),
    
    /**
     * Nuclear energy, produced by nuclear reactors.
     */
    NUCLEAR("nuclear", "NU", 0x7fff00),
    
    /**
     * Solar energy, produced by solar panels.
     */
    SOLAR("solar", "SU", 0xffd700);
    
    private final String id;
    private final String unit;
    private final int color;
    
    /**
     * Create a new energy type.
     * 
     * @param id The identifier for this energy type
     * @param unit The unit of measurement for this energy type
     * @param color The color associated with this energy type (hex RGB)
     */
    EnergyType(String id, String unit, int color) {
        this.id = id;
        this.unit = unit;
        this.color = color;
    }
    
    /**
     * Get the identifier for this energy type.
     * 
     * @return The energy type ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the unit of measurement for this energy type.
     * 
     * @return The energy unit
     */
    public String getUnit() {
        return unit;
    }
    
    /**
     * Get the color associated with this energy type.
     * 
     * @return The color (hex RGB)
     */
    public int getColor() {
        return color;
    }
    
    /**
     * Find an energy type by its ID.
     * 
     * @param id The ID to look for
     * @return The energy type, or ELECTRICAL if not found
     */
    public static EnergyType byId(String id) {
        for (EnergyType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return ELECTRICAL; // Default to electrical if not found
    }
}