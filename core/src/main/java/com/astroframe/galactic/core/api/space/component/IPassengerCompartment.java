package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket passenger compartment components.
 */
public interface IPassengerCompartment extends IRocketComponent {
    
    /**
     * Gets the maximum number of passengers this compartment can hold.
     * @return The passenger capacity
     */
    int getPassengerCapacity();
    
    /**
     * Gets the comfort level of this passenger compartment.
     * Higher values provide health and other bonuses during travel.
     * @return The comfort level
     */
    float getComfortLevel();
    
    /**
     * Gets the type of passenger compartment.
     * @return The compartment type
     */
    CompartmentType getCompartmentType();
    
    /**
     * Checks if this compartment has cryogenic sleep capabilities for long journeys.
     * @return true if cryogenic sleep is available
     */
    boolean hasCryogenicSleep();
    
    /**
     * Gets the radiation protection level of this compartment.
     * Higher values provide better protection against cosmic radiation.
     * @return The radiation protection level
     */
    float getRadiationProtection();
    
    /**
     * Gets the emergency support duration.
     * How long life support functions can operate in emergency mode.
     * @return The emergency support duration in minutes
     */
    int getEmergencySupportDuration();
    
    /**
     * Enum representing the different types of passenger compartments.
     */
    enum CompartmentType {
        BASIC,          // Minimal amenities
        STANDARD,       // Average comfort
        BUSINESS,       // Enhanced comfort and amenities
        LUXURY,         // Maximum comfort and amenities
        SCIENTIFIC      // Specialized for research operations
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.PASSENGER_COMPARTMENT;
    }
}