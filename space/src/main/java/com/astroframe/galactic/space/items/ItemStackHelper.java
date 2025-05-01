package com.astroframe.galactic.space.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

/**
 * Helper methods for working with ItemStacks.
 */
public class ItemStackHelper {
    
    /**
     * Gets the tag for an item stack, creating it if it doesn't exist.
     * This is a workaround for different method names in different Minecraft versions.
     * 
     * @param stack The item stack
     * @return The compound tag
     */
    public static CompoundTag getOrCreateTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTagElement("tag");
        return tag;
    }
}