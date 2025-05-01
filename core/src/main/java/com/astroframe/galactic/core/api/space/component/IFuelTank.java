package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.FuelType;

/**
 * Interface for rocket fuel tanks.
 */
public interface IFuelTank extends IRocketComponent {
    
    /**
     * Gets the maximum fuel capacity of this tank.
     * @return The maximum fuel capacity
     */
    int getMaxFuelCapacity();
    
    /**
     * Gets the type of fuel this tank stores.
     * @return The fuel type
     */
    FuelType getFuelType();
    
    /**
     * Gets the pressure rating of this tank in MPa.
     * @return The pressure rating
     */
    float getPressureRating();
    
    /**
     * Checks if this tank is insulated.
     * @return True if insulated
     */
    boolean isInsulated();
}