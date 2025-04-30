package com.astroframe.galactic.machinery.api;

/**
 * Enum for different types of machines in the Galactic Expansion mod.
 * Each machine type has its own characteristics and behavior.
 */
public enum MachineType {
    /**
     * Generator machines produce energy from various sources.
     */
    GENERATOR("generator", "Produces energy from various sources"),
    
    /**
     * Processor machines transform items into other items.
     */
    PROCESSOR("processor", "Transforms items into other items"),
    
    /**
     * Extractor machines extract resources from the environment.
     */
    EXTRACTOR("extractor", "Extracts resources from the environment"),
    
    /**
     * Storage machines store items, fluids, or energy.
     */
    STORAGE("storage", "Stores items, fluids, or energy"),
    
    /**
     * Transport machines move items, fluids, or energy between locations.
     */
    TRANSPORT("transport", "Moves items, fluids, or energy between locations"),
    
    /**
     * Assembly machines combine multiple items into complex structures.
     */
    ASSEMBLY("assembly", "Combines multiple items into complex structures");
    
    private final String id;
    private final String description;
    
    /**
     * Constructor for machine types.
     * 
     * @param id The string identifier for this machine type
     * @param description A brief description of what this type of machine does
     */
    MachineType(String id, String description) {
        this.id = id;
        this.description = description;
    }
    
    /**
     * Get the string identifier for this machine type.
     * 
     * @return The identifier
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the description for this machine type.
     * 
     * @return The description
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Convert a string identifier to a machine type.
     * 
     * @param id The string identifier
     * @return The corresponding machine type, or PROCESSOR if not found
     */
    public static MachineType fromId(String id) {
        for (MachineType type : values()) {
            if (type.getId().equals(id)) {
                return type;
            }
        }
        
        return PROCESSOR; // Default to processor if not found
    }
}