package com.astroframe.galactic.space.implementation.component;

/**
 * Represents different types of rocket engines.
 */
public enum EngineType {
    /**
     * Chemical engine - uses chemical fuels.
     */
    CHEMICAL("Chemical"),
    
    /**
     * Liquid fuel engine - uses liquid fuels.
     */
    LIQUID("Liquid"),
    
    /**
     * Ion engine - uses electricity to accelerate ions.
     */
    ION("Ion"),
    
    /**
     * Nuclear engine - uses nuclear reactions for propulsion.
     */
    NUCLEAR("Nuclear"),
    
    /**
     * Fusion engine - uses fusion reactions for propulsion.
     */
    FUSION("Fusion");
    
    private final String displayName;
    
    EngineType(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Gets the display name of this engine type.
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
}