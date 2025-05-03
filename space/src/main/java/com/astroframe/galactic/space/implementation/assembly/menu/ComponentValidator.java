package com.astroframe.galactic.space.implementation.assembly.menu;

import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Utility class for validating rocket components.
 */
public class ComponentValidator {
    
    /**
     * Validates a list of component items.
     * Checks if the components can be used together to build a valid rocket.
     *
     * @param components The component items
     * @return true if valid, false otherwise
     */
    public static boolean validateComponents(List<ItemStack> components) {
        // For now, just check if there are any components
        // In a full implementation, this would check component compatibility,
        // required components, etc.
        if (components == null || components.isEmpty()) {
            return false;
        }
        
        boolean hasNonEmptyComponent = false;
        for (ItemStack stack : components) {
            if (!stack.isEmpty()) {
                hasNonEmptyComponent = true;
                break;
            }
        }
        
        return hasNonEmptyComponent;
    }
    
    /**
     * Checks if an item can be used as a component in the specified slot.
     *
     * @param stack The item stack
     * @param slotIndex The slot index
     * @return true if valid, false otherwise
     */
    public static boolean isValidComponentForSlot(ItemStack stack, int slotIndex) {
        // For now, all items are valid in all slots
        // In a full implementation, this would check component compatibility for specific slots
        return !stack.isEmpty();
    }
}