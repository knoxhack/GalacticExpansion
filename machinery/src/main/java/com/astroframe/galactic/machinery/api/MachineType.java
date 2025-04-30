package com.astroframe.galactic.machinery.api;

/**
 * Enumeration of machine types in the Galactic Expansion mod.
 * Used to categorize machines by their function and capabilities.
 */
public enum MachineType {
    /**
     * Machines that generate energy.
     */
    GENERATOR("generator"),
    
    /**
     * Machines that process items.
     */
    PROCESSOR("processor"),
    
    /**
     * Machines that store energy.
     */
    BATTERY("battery"),
    
    /**
     * Machines that transfer energy.
     */
    CONDUIT("conduit"),
    
    /**
     * Machines that manipulate fluids.
     */
    FLUID_HANDLER("fluid_handler"),
    
    /**
     * Utility machines with specialized functions.
     */
    UTILITY("utility");
    
    private final String id;
    
    MachineType(String id) {
        this.id = id;
    }
    
    /**
     * Gets the unique ID for this machine type.
     * 
     * @return The machine type ID
     */
    public String getId() {
        return id;
    }
}