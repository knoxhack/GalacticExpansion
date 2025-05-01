package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.List;

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
     * Gets the display name of this component.
     * @return The display name
     */
    Component getDisplayName();
    
    /**
     * Gets the tier level of this component (1-3).
     * @return The tier level
     */
    int getTier();
    
    /**
     * Gets the type of this component.
     * @return The component type
     */
    ComponentType getType();
    
    /**
     * Gets the mass of this component.
     * @return The mass
     */
    int getMass();
    
    /**
     * Gets the maximum health of this component.
     * @return The maximum health
     */
    float getMaxHealth();
    
    /**
     * Gets the tooltip information for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip
     */
    List<Component> getTooltip(boolean detailed);
}