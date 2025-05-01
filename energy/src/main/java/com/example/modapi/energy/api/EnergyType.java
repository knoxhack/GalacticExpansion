package com.example.modapi.energy.api;

/**
 * Enum representing different types of energy.
 * 
 * @deprecated Use {@link com.astroframe.galactic.energy.api.EnergyType} instead
 */
@Deprecated
public enum EnergyType {
    /**
     * Standard Forge Energy.
     */
    FORGE_ENERGY("forge_energy", "Forge Energy", 0x00AAFF),
    
    /**
     * Energy type compatible with Redstone Flux.
     */
    REDSTONE_FLUX("redstone_flux", "Redstone Flux", 0xFF0000),
    
    /**
     * Mod-specific higher-tier energy.
     */
    MODAPI_ENERGY("modapi_energy", "ModAPI Energy", 0xAA00FF);
    
    private final String id;
    private final String displayName;
    private final int color;
    
    /**
     * Constructor for EnergyType.
     * 
     * @param id The ID of the energy type
     * @param displayName The display name
     * @param color The color
     */
    EnergyType(String id, String displayName, int color) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
    }
    
    /**
     * Gets the ID of the energy type.
     * 
     * @return The ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the display name of the energy type.
     * 
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the color of the energy type.
     * 
     * @return The color
     */
    public int getColor() {
        return color;
    }
    
    /**
     * Gets an energy type by ID.
     * 
     * @param id The energy type ID
     * @return The energy type, or FORGE_ENERGY if not found
     */
    public static EnergyType byId(String id) {
        for (EnergyType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        return FORGE_ENERGY; // Default
    }
}