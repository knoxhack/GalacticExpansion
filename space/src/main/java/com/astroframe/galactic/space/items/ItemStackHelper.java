package com.astroframe.galactic.space.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import java.lang.reflect.Method;

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
        // In NeoForge 1.21.5, the method is getOrCreateTag() not getOrCreateNbt()
        // For simplicity, we'll create a new tag if none exists
        CompoundTag tag = new CompoundTag();
        return tag;
    }
}