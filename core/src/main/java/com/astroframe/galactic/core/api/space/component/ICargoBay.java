package com.astroframe.galactic.core.api.space.component;

import java.util.List;

import net.minecraft.world.item.ItemStack;

/**
 * Interface for rocket cargo bay components.
 * Cargo bays allow for storage of items during space travel.
 */
public interface ICargoBay extends IRocketComponent {
    
    /**
     * Get the maximum number of item stacks this cargo bay can hold.
     * 
     * @return The maximum number of slots
     */
    int getMaxSlots();
    
    /**
     * Get the maximum weight capacity of this cargo bay in kg.
     * 
     * @return The maximum weight capacity
     */
    int getMaxCapacity();
    
    /**
     * Get the currently used weight capacity in kg.
     * 
     * @return The used capacity
     */
    int getCurrentUsedCapacity();
    
    /**
     * Get the remaining weight capacity in kg.
     * 
     * @return The remaining capacity
     */
    default int getRemainingCapacity() {
        return getMaxCapacity() - getCurrentUsedCapacity();
    }
    
    /**
     * Get all items in the cargo bay.
     * 
     * @return A list of all item stacks
     */
    List<ItemStack> getItems();
    
    /**
     * Add an item to the cargo bay if there's room.
     * 
     * @param stack The item stack to add
     * @return True if the item was added successfully
     */
    boolean addItem(ItemStack stack);
    
    /**
     * Remove an item from the cargo bay.
     * 
     * @param index The slot index to remove from
     * @return The removed item stack, or empty stack if none
     */
    ItemStack removeItem(int index);
    
    /**
     * Check if the cargo bay has any security or protection features.
     * 
     * @return True if the cargo bay has security features
     */
    boolean hasSecurityFeatures();
    
    /**
     * Check if the cargo bay has environment control for sensitive cargo.
     * 
     * @return True if the cargo bay has environment control
     */
    boolean hasEnvironmentControl();
    
    /**
     * Check if the cargo bay has automated loading/unloading.
     * 
     * @return True if the cargo bay has automated loading
     */
    boolean hasAutomatedLoading();
    
    /**
     * Calculate the weight of an item stack in kg.
     * This is a utility method for cargo bays to determine item weights.
     * 
     * @param stack The item stack
     * @return The weight in kg
     */
    default float calculateItemWeight(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0f;
        }
        
        // A basic formula for item weights
        // Can be overridden by specific implementations for more complex calculations
        return stack.getCount() * 0.5f; // Default: 0.5kg per item
    }
}