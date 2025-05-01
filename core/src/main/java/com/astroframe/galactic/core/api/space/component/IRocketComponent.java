package com.astroframe.galactic.core.api.space.component;

import net.minecraft.resources.ResourceLocation;

/**
 * Base interface for all rocket components.
 */
public interface IRocketComponent {
    
    /**
     * Gets the unique ID of this component.
     * @return The component ID
     */
    ResourceLocation getId();
    
    /**
     * Gets the type of this component.
     * @return The component type
     */
    RocketComponentType getType();
    
    /**
     * Gets the name of this component.
     * @return The component name
     */
    String getName();
    
    /**
     * Gets the description of this component.
     * @return The component description
     */
    String getDescription();
    
    /**
     * Gets the tier of this component (1-3).
     * @return The component tier
     */
    int getTier();
    
    /**
     * Gets the mass of this component.
     * @return The component mass
     */
    int getMass();
    
    /**
     * Gets the maximum durability of this component.
     * @return The maximum durability
     */
    int getMaxDurability();
    
    /**
     * Gets the current durability of this component.
     * @return The current durability
     */
    int getCurrentDurability();
    
    /**
     * Damages this component.
     * @param amount The amount of damage to apply
     */
    void damage(int amount);
    
    /**
     * Repairs this component.
     * @param amount The amount to repair
     */
    void repair(int amount);
    
    /**
     * Gets the health of this component as a percentage.
     * @return The component health
     */
    default float getHealth() {
        return ((float) getCurrentDurability() / getMaxDurability()) * 100;
    }
    
    /**
     * Whether this component is broken.
     * @return true if the component is broken
     */
    default boolean isBroken() {
        return getCurrentDurability() <= 0;
    }
}