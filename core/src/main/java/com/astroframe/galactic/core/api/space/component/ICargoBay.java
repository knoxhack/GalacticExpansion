package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket cargo bay components.
 */
public interface ICargoBay extends IRocketComponent {
    
    /**
     * Gets the number of inventory slots this cargo bay provides.
     * @return The slot count
     */
    int getSlotCount();
    
    /**
     * Gets the maximum weight capacity of this cargo bay.
     * @return The weight capacity
     */
    int getWeightCapacity();
    
    /**
     * Checks if this cargo bay has automatic sorting capabilities.
     * @return true if automatic sorting is available
     */
    boolean hasAutomaticSorting();
    
    /**
     * Checks if this cargo bay has vacuum sealing for atmospheric protection.
     * @return true if vacuum sealing is available
     */
    boolean hasVacuumSealing();
    
    /**
     * Gets the type of cargo bay.
     * @return The cargo bay type
     */
    CargoBayType getCargoType();
    
    /**
     * Gets the access speed of this cargo bay.
     * Higher values allow for faster item retrieval and storage.
     * @return The access speed
     */
    float getAccessSpeed();
    
    /**
     * Enum representing the different types of cargo bays.
     */
    enum CargoBayType {
        STANDARD,       // Basic storage
        REFRIGERATED,   // For temperature-sensitive items
        REINFORCED,     // For hazardous materials
        QUANTUM,        // Enhanced storage capabilities
        SPECIALIZED     // For specific item types
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.CARGO_BAY;
    }
}