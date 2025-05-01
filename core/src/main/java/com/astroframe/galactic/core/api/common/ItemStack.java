package com.astroframe.galactic.core.api.common;

/**
 * Abstraction for a stack of items.
 * This allows us to avoid direct Minecraft dependencies in our API.
 */
public class ItemStack {
    /**
     * Represents an empty item stack.
     */
    public static final ItemStack EMPTY = new ItemStack("", 0, null);
    
    private final String itemId;
    private int count;
    private final Object nbt; // This would be a proper NBT class in a real implementation
    
    /**
     * Creates a new item stack.
     * 
     * @param itemId The item identifier
     * @param count The item count
     * @param nbt Additional NBT data
     */
    public ItemStack(String itemId, int count, Object nbt) {
        this.itemId = itemId;
        this.count = Math.max(0, count);
        this.nbt = nbt;
    }
    
    /**
     * Creates a new item stack without NBT data.
     * 
     * @param itemId The item identifier
     * @param count The item count
     */
    public ItemStack(String itemId, int count) {
        this(itemId, count, null);
    }
    
    /**
     * Gets the item identifier.
     * 
     * @return The item identifier
     */
    public String getItemId() {
        return itemId;
    }
    
    /**
     * Gets the item count.
     * 
     * @return The item count
     */
    public int getCount() {
        return count;
    }
    
    /**
     * Sets the item count.
     * 
     * @param count The new count
     */
    public void setCount(int count) {
        this.count = Math.max(0, count);
    }
    
    /**
     * Gets the NBT data.
     * 
     * @return The NBT data
     */
    public Object getNbt() {
        return nbt;
    }
    
    /**
     * Creates a copy of this item stack.
     * 
     * @return A copy of this item stack
     */
    public ItemStack copy() {
        return new ItemStack(itemId, count, nbt);
    }
    
    /**
     * Checks if this item stack is empty.
     * 
     * @return true if this stack is empty
     */
    public boolean isEmpty() {
        return count <= 0 || itemId.isEmpty();
    }
    
    /**
     * Increases the size of this stack.
     * 
     * @param amount The amount to add
     */
    public void grow(int amount) {
        count += amount;
    }
    
    /**
     * Decreases the size of this stack.
     * 
     * @param amount The amount to shrink
     */
    public void shrink(int amount) {
        count = Math.max(0, count - amount);
    }
    
    /**
     * Gets the maximum stack size for this item.
     * 
     * @return The maximum stack size
     */
    public int getMaxStackSize() {
        // For simplicity, we'll assume a default max stack size
        return 64;
    }
    
    /**
     * Checks if two item stacks are identical (including NBT).
     * 
     * @param stack1 The first stack
     * @param stack2 The second stack
     * @return true if the stacks contain the same item with the same NBT
     */
    public static boolean isSameItemSameTags(ItemStack stack1, ItemStack stack2) {
        if (stack1.isEmpty() && stack2.isEmpty()) {
            return true;
        }
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        }
        return stack1.itemId.equals(stack2.itemId) && 
               ((stack1.nbt == null && stack2.nbt == null) || 
                (stack1.nbt != null && stack1.nbt.equals(stack2.nbt)));
    }
    
    /**
     * Gets the item.
     * This would normally return an Item instance, but for our API we'll just use the item ID.
     * 
     * @return The item (represented by its ID for our API)
     */
    public String getItem() {
        return itemId;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemStack other = (ItemStack) obj;
        return count == other.count && 
               itemId.equals(other.itemId) && 
               ((nbt == null && other.nbt == null) || 
                (nbt != null && nbt.equals(other.nbt)));
    }
    
    @Override
    public int hashCode() {
        int result = itemId.hashCode();
        result = 31 * result + count;
        result = 31 * result + (nbt != null ? nbt.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return "ItemStack{" +
               "itemId='" + itemId + '\'' +
               ", count=" + count +
               ", nbt=" + nbt +
               '}';
    }
}