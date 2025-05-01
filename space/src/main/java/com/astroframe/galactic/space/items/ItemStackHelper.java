package com.astroframe.galactic.space.items;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Helper methods for working with ItemStacks.
 * This version uses an in-memory cache of tags to avoid ItemStack method compatibility issues.
 */
public class ItemStackHelper {
    // In-memory store of tags by item stack identifier
    private static final Map<UUID, CompoundTag> tagCache = new HashMap<>();
    private static final Map<ItemStack, UUID> stackIds = new HashMap<>();
    
    /**
     * Gets the tag for an item stack, creating it if it doesn't exist.
     * This implementation uses an in-memory cache instead of directly using ItemStack methods,
     * to work around API differences between Minecraft versions.
     * 
     * @param stack The item stack
     * @return The compound tag
     */
    public static CompoundTag getOrCreateTag(ItemStack stack) {
        if (stack == null) {
            return new CompoundTag();
        }
        
        // Get or create a unique id for this stack
        UUID stackId = stackIds.computeIfAbsent(stack, k -> UUID.randomUUID());
        
        // Get or create tag for this stack id
        return tagCache.computeIfAbsent(stackId, k -> new CompoundTag());
    }
}