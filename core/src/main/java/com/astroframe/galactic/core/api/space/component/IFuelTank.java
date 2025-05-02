package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.FuelType;

/**
 * Interface for rocket fuel tanks.
 */
public interface IFuelTank extends IRocketComponent {
    
    /**
     * Gets the maximum fuel capacity of this fuel tank.
     * @return The maximum fuel capacity
     */
    int getMaxFuelCapacity();
    
    /**
     * Gets the current fuel level in this tank.
     * @return The current fuel level
     */
    int getCurrentFuelLevel();
    
    /**
     * Adds fuel to this tank.
     * @param amount The amount of fuel to add
     * @return The amount actually added
     */
    int addFuel(int amount);
    
    /**
     * Consumes fuel from this tank.
     * @param amount The amount of fuel to consume
     * @return The amount actually consumed
     */
    int consumeFuel(int amount);
    
    /**
     * Gets the type of fuel this tank can store.
     * @return The fuel type
     */
    FuelType getFuelType();
    
    /**
     * Gets the leak resistance of this fuel tank.
     * Higher values mean less chance of leaking fuel.
     * @return The leak resistance
     */
    float getLeakResistance();
    
    /**
     * Gets the explosion resistance of this fuel tank.
     * Higher values mean less chance of exploding when damaged.
     * @return The explosion resistance
     */
    float getExplosionResistance();
}