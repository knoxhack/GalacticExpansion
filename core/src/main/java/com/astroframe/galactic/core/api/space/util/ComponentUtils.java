package com.astroframe.galactic.core.api.space.util;

import com.astroframe.galactic.core.api.space.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * Utility class for working with rocket components.
 * This class is designed to bridge between the core API and the implementation modules.
 */
public class ComponentUtils {

    /**
     * Creates a component from its saved tag data.
     * This method is implemented to be overridden by the implementation modules.
     * 
     * @param id The component ID
     * @param tag The tag data
     * @return The created component, or null if creation failed
     */
    public static IRocketComponent createComponentFromTag(ResourceLocation id, CompoundTag tag) {
        // This is a placeholder that should be replaced by the implementation module
        return null;
    }
    
    /**
     * Determines the component type from a tag.
     * 
     * @param tag The tag
     * @return The component type, or null if not found
     */
    public static RocketComponentType getComponentTypeFromTag(CompoundTag tag) {
        if (tag.contains("Type")) {
            String typeStr = tag.getString("Type").orElse("");
            if (!typeStr.isEmpty()) {
                try {
                    return RocketComponentType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    return null;
                }
            }
        }
        return null;
    }
}