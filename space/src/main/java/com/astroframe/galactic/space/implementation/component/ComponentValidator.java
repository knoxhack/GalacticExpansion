package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;

/**
 * Validates rocket components and their compatibility.
 * This class handles validating individual components and combinations of components
 * to ensure they can work together in a rocket assembly.
 */
public class ComponentValidator {

    /**
     * Checks if an item is a valid rocket component
     * 
     * @param stack The item stack to check
     * @return True if the item is a valid rocket component
     */
    public boolean isValidComponent(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        // Check if the item has a valid component tag or properties
        // For now just check if it has a specific tag
        return hasComponentTag(stack);
    }
    
    /**
     * Checks if an item has the rocket component tag
     * 
     * @param stack The item stack to check
     * @return True if the item has the rocket component tag
     */
    private boolean hasComponentTag(ItemStack stack) {
        // In NeoForge 1.21.5 we need to access tags differently
        try {
            // Get the registry ID of the item
            ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
            // For now just check if it contains specific substrings in the path
            // Later this would use proper tag checking
            if (itemId != null) {
                String path = itemId.getPath();
                return path.contains("engine") || 
                       path.contains("fuel") || 
                       path.contains("tank") || 
                       path.contains("component") ||
                       path.contains("rocket");
            }
        } catch (Exception e) {
            // Safely handle any registry access errors
            return false;
        }
        
        return false;
    }
    
    /**
     * Checks if two components are compatible with each other
     * 
     * @param component1 The first component
     * @param component2 The second component
     * @return True if the components are compatible
     */
    public boolean areComponentsCompatible(ItemStack component1, ItemStack component2) {
        // For now, all components are compatible
        return true;
    }
    
    /**
     * Checks if a set of components would form a valid rocket
     * 
     * @param components The array of component item stacks
     * @return True if the components would form a valid rocket
     */
    public boolean isValidRocketConfiguration(ItemStack[] components) {
        // For early implementation, just check if there's at least one component
        if (components == null || components.length == 0) {
            return false;
        }
        
        boolean hasAnyComponent = false;
        
        for (ItemStack stack : components) {
            if (!stack.isEmpty() && isValidComponent(stack)) {
                hasAnyComponent = true;
                break;
            }
        }
        
        return hasAnyComponent;
    }
}