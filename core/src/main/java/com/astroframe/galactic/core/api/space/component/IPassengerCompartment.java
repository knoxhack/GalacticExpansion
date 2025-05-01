package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.CompartmentType;

/**
 * Interface for rocket passenger compartments.
 */
public interface IPassengerCompartment extends IRocketComponent {
    
    /**
     * Gets the maximum number of passengers this compartment can hold.
     * @return The passenger capacity
     */
    int getPassengerCapacity();
    
    /**
     * Checks if this compartment has artificial gravity.
     * @return True if has artificial gravity
     */
    boolean hasArtificialGravity();
    
    /**
     * Checks if this compartment has sleeping quarters.
     * @return True if has sleeping quarters
     */
    boolean hasSleepingQuarters();
    
    /**
     * Checks if this compartment has emergency medical facilities.
     * @return True if has emergency medical
     */
    boolean hasEmergencyMedical();
    
    /**
     * Gets the type of this compartment.
     * @return The compartment type
     */
    CompartmentType getCompartmentType();
}