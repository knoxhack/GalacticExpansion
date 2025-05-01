package com.astroframe.galactic.core.api.space.component;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Interface for all rocket components.
 * Components are used to build custom rockets with different capabilities.
 */
public interface IRocketComponent {
    
    /**
     * Gets the unique identifier for this component.
     * @return The component's identifier
     */
    ResourceLocation getId();
    
    /**
     * Gets the display name of this component.
     * @return The component's name
     */
    Component getDisplayName();
    
    /**
     * Gets the tier of this component.
     * @return The component tier (1-3)
     */
    int getTier();
    
    /**
     * Gets the type of this component.
     * @return The component type
     */
    ComponentType getType();
    
    /**
     * Gets the mass of this component in arbitrary units.
     * Affects fuel consumption and rocket performance.
     * @return The component's mass
     */
    int getMass();
    
    /**
     * Gets the health points of this component.
     * @return The component's maximum health
     */
    float getMaxHealth();
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to show detailed information
     * @return List of tooltip components
     */
    List<Component> getTooltip(boolean detailed);
    
    /**
     * Enum representing the different types of rocket components.
     */
    enum ComponentType {
        COMMAND_MODULE,
        ENGINE,
        FUEL_TANK,
        CARGO_BAY,
        PASSENGER_COMPARTMENT,
        SHIELD,
        LIFE_SUPPORT
    }
}