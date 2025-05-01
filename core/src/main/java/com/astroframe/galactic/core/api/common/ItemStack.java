package com.astroframe.galactic.core.api.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;

/**
 * Abstraction for Minecraft's ItemStack class.
 * This provides item handling for logistics and inventory systems
 * without direct dependency on Minecraft classes.
 */
public class ItemStack {
    private final net.minecraft.world.item.ItemStack mcStack;
    
    /**
     * Creates a new empty ItemStack.
     */
    public ItemStack() {
        this.mcStack = net.minecraft.world.item.ItemStack.EMPTY;
    }
    
    /**
     * Creates a new ItemStack from an Item with a specified count.
     * @param item The Item
     * @param count The stack size
     */
    public ItemStack(Item item, int count) {
        this.mcStack = new net.minecraft.world.item.ItemStack(item, count);
    }
    
    /**
     * Creates a new abstracted ItemStack wrapping a Minecraft ItemStack.
     * @param mcStack The Minecraft ItemStack
     */
    private ItemStack(net.minecraft.world.item.ItemStack mcStack) {
        this.mcStack = mcStack;
    }
    
    /**
     * Creates a new abstracted ItemStack from a Minecraft ItemStack.
     * @param mcStack The Minecraft ItemStack
     * @return A new abstracted ItemStack
     */
    public static ItemStack fromMinecraft(net.minecraft.world.item.ItemStack mcStack) {
        return new ItemStack(mcStack);
    }
    
    /**
     * Gets the underlying Minecraft ItemStack.
     * @return The Minecraft ItemStack
     */
    public net.minecraft.world.item.ItemStack toMinecraft() {
        return mcStack;
    }
    
    /**
     * Gets the Item in this stack.
     * @return The Item
     */
    public Item getItem() {
        return mcStack.getItem();
    }
    
    /**
     * Gets the count of items in this stack.
     * @return The stack size
     */
    public int getCount() {
        return mcStack.getCount();
    }
    
    /**
     * Sets the count of items in this stack.
     * @param count The new stack size
     */
    public void setCount(int count) {
        mcStack.setCount(count);
    }
    
    /**
     * Gets the maximum stack size for this item.
     * @return The maximum stack size
     */
    public int getMaxStackSize() {
        return mcStack.getMaxStackSize();
    }
    
    /**
     * Whether this stack is empty.
     * @return true if the stack is empty
     */
    public boolean isEmpty() {
        return mcStack.isEmpty();
    }
    
    /**
     * Gets a copy of this stack.
     * @return A copy of this stack
     */
    public ItemStack copy() {
        return new ItemStack(mcStack.copy());
    }
    
    /**
     * Increases the stack size.
     * @param amount The amount to grow by
     */
    public void grow(int amount) {
        mcStack.grow(amount);
    }
    
    /**
     * Decreases the stack size.
     * @param amount The amount to shrink by
     */
    public void shrink(int amount) {
        mcStack.shrink(amount);
    }
    
    /**
     * Internal method to get tag data safely.
     * @return The tag data or null
     */
    private CompoundTag getTagSafe() {
        try {
            // Try different methods to get tag data in Minecraft 1.21.5
            java.lang.reflect.Method method = net.minecraft.world.item.ItemStack.class.getMethod("getTag");
            return (CompoundTag) method.invoke(mcStack);
        } catch (Exception e) {
            // Method might not exist in this version
            try {
                java.lang.reflect.Method method = net.minecraft.world.item.ItemStack.class.getMethod("getOrCreateTag");
                return (CompoundTag) method.invoke(mcStack);
            } catch (Exception e2) {
                // If all methods fail, return null
                return null;
            }
        }
    }
    
    /**
     * Gets the NBT tag for this stack.
     * @return The NBT tag
     */
    public CompoundTag getTag() {
        return getTagSafe();
    }
    
    /**
     * Sets the NBT tag for this stack.
     * @param tag The NBT tag
     */
    public void setTag(CompoundTag tag) {
        try {
            // Try to use reflection to set tag in Minecraft 1.21.5
            java.lang.reflect.Method method = net.minecraft.world.item.ItemStack.class.getMethod("setTag", CompoundTag.class);
            method.invoke(mcStack, tag);
        } catch (Exception e) {
            // If method doesn't exist, we can't do much
            // but at least the program won't crash
        }
    }
    
    /**
     * Whether this stack has an NBT tag.
     * @return true if the stack has a tag
     */
    public boolean hasTag() {
        try {
            // Try different methods to check tag existence in Minecraft 1.21.5
            java.lang.reflect.Method method = net.minecraft.world.item.ItemStack.class.getMethod("hasTag");
            return (boolean) method.invoke(mcStack);
        } catch (Exception e) {
            // Method might not exist, try alternative
            return getTagSafe() != null;
        }
    }
    
    /**
     * Checks if two stacks can be merged (same item, same tags).
     * @param other The other stack
     * @return true if the stacks can be merged
     */
    public boolean isSameItemSameTags(ItemStack other) {
        // Check if items are the same
        if (this.getItem() != other.getItem()) {
            return false;
        }
        
        // Check if both have tags
        boolean thisHasTag = this.getTag() != null;
        boolean otherHasTag = other.getTag() != null;
        
        // If neither has tags, they match
        if (!thisHasTag && !otherHasTag) {
            return true;
        }
        
        // If one has a tag and the other doesn't, they don't match
        if (thisHasTag != otherHasTag) {
            return false;
        }
        
        // Compare tags
        return this.getTag().equals(other.getTag());
    }
    
    /**
     * A constant for an empty stack.
     */
    public static final ItemStack EMPTY = new ItemStack();
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ItemStack itemStack = (ItemStack) o;
        return this.isSameItemSameTags(itemStack);
    }
    
    @Override
    public int hashCode() {
        return mcStack.hashCode();
    }
    
    @Override
    public String toString() {
        return mcStack.toString();
    }
}