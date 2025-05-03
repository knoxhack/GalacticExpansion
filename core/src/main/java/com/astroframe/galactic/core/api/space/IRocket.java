package com.astroframe.galactic.core.api.space;

import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

/**
 * Interface for rocket objects.
 * Defines methods to access rocket properties and capabilities.
 */
public interface IRocket {
    
    /**
     * Gets the tier of this rocket.
     * Higher tier rockets can go further and carry more cargo.
     * 
     * @return The rocket tier (1-5)
     */
    int getTier();
    
    /**
     * Gets the current fuel amount.
     * 
     * @return The fuel amount in standard units
     */
    float getFuel();
    
    /**
     * Gets the maximum fuel capacity.
     * 
     * @return The fuel capacity in standard units
     */
    float getFuelCapacity();
    
    /**
     * Gets the total mass of the rocket.
     * 
     * @return The mass in kilograms
     */
    float getMass();
    
    /**
     * Gets the thrust of the rocket engines.
     * 
     * @return The thrust in kilonewtons
     */
    float getThrust();
    
    /**
     * Determines if this rocket is capable of reaching orbit.
     * This is based on the thrust-to-weight ratio and fuel.
     * 
     * @return true if the rocket can reach orbit, false otherwise
     */
    boolean canReachOrbit();
    
    /**
     * Calculates the maximum altitude this rocket can reach.
     * 
     * @return The maximum altitude in meters
     */
    float getMaxAltitude();
    
    /**
     * Calculates the time required to reach orbit.
     * 
     * @return The time to orbit in seconds, or -1 if orbit cannot be reached
     */
    float getTimeToOrbit();
    
    /**
     * Gets the current fuel level as an alias for getFuel()
     * 
     * @return The current fuel level
     */
    default float getFuelLevel() {
        return getFuel();
    }
    
    /**
     * Sets the current fuel level
     * 
     * @param fuelLevel The new fuel level
     */
    default void setFuelLevel(float fuelLevel) {
        // Default implementation does nothing
    }
    
    /**
     * Consumes the specified amount of fuel
     * 
     * @param amount The amount of fuel to consume
     * @return The amount of fuel actually consumed
     */
    default float consumeFuel(int amount) {
        float currentFuel = getFuelLevel();
        float toConsume = Math.min(currentFuel, amount);
        setFuelLevel(currentFuel - toConsume);
        return toConsume;
    }
    
    /**
     * Checks if the rocket has a specific component
     * 
     * @param type The component type to check
     * @return true if the rocket has this component
     */
    default boolean hasComponent(RocketComponentType type) {
        return false;
    }
    
    /**
     * Gets all components in this rocket
     * 
     * @return List of all rocket components
     */
    default List<com.astroframe.galactic.core.api.space.component.IRocketComponent> getAllComponents() {
        return List.of();
    }
    
    /**
     * Determines if this rocket can reach a specific celestial body.
     * This is based on the rocket's capabilities and fuel level.
     * 
     * @param body The celestial body to check
     * @return true if the rocket can reach the body, false otherwise
     */
    default boolean canReach(ICelestialBody body) {
        if (body == null) return false;
        
        // Default implementation uses simple tier and fuel checks
        if (getTier() < body.getRequiredTier()) return false;
        if (getFuelLevel() < getFuelCapacity() * 0.25f) return false;
        
        return true;
    }
    
    /**
     * Saves the rocket data to an NBT tag
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    default CompoundTag saveToTag(CompoundTag tag) {
        if (tag == null) {
            tag = new CompoundTag();
        }
        
        tag.putInt("tier", getTier());
        tag.putFloat("fuel", getFuel());
        tag.putFloat("fuelCapacity", getFuelCapacity());
        tag.putFloat("mass", getMass());
        tag.putFloat("thrust", getThrust());
        
        return tag;
    }
}