package com.astroframe.galactic.space.items;

import com.astroframe.galactic.space.implementation.component.ResourceLocationHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

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
        if (stack == null || stack.isEmpty()) {
            return new CompoundTag();
        }
        
        // Get or create a unique id for this stack
        UUID stackId = stackIds.computeIfAbsent(stack, k -> UUID.randomUUID());
        
        // Get or create tag for this stack id
        return tagCache.computeIfAbsent(stackId, k -> new CompoundTag());
    }
    
    /**
     * Gets the tag for an item stack.
     * 
     * @param stack The item stack
     * @return The tag, or null if none exists
     */
    public static CompoundTag getTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        
        // Get the id for this stack
        UUID stackId = stackIds.get(stack);
        if (stackId == null) {
            return null;
        }
        
        // Return the tag if it exists
        return tagCache.get(stackId);
    }
    
    /**
     * Sets the tag for an item stack.
     * 
     * @param stack The item stack
     * @param tag The tag to set, or null to remove
     */
    public static void setTag(ItemStack stack, CompoundTag tag) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        
        if (tag == null) {
            // Remove any existing tag
            UUID stackId = stackIds.remove(stack);
            if (stackId != null) {
                tagCache.remove(stackId);
            }
        } else {
            // Set the tag
            UUID stackId = stackIds.computeIfAbsent(stack, k -> UUID.randomUUID());
            tagCache.put(stackId, tag);
        }
    }
    
    /**
     * Creates an ItemStack from an item and count.
     * This abstracts the ItemStack constructor to handle API differences.
     * 
     * @param item The item
     * @param count The count
     * @return The created ItemStack
     */
    public static ItemStack createStack(Item item, int count) {
        if (item == null) {
            return ItemStack.EMPTY;
        }
        try {
            return new ItemStack(item, count);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
    
    /**
     * Creates an ItemStack from a ResourceLocation and count.
     * 
     * @param location The resource location for the item
     * @param count The count
     * @return The created ItemStack
     */
    public static ItemStack createStack(ResourceLocation location, int count) {
        try {
            // Handle Optional return type in NeoForge 1.21.5
            java.util.Optional<Item> itemOpt = net.minecraft.core.registries.BuiltInRegistries.ITEM.getOptional(location);
            Item item = itemOpt.orElse(Items.AIR);
            if (item == null || item == Items.AIR) {
                return ItemStack.EMPTY;
            }
            return createStack(item, count);
        } catch (Exception e) {
            return ItemStack.EMPTY;
        }
    }
    
    /**
     * Creates an ItemStack from a namespaced item id and count.
     * 
     * @param namespace The namespace (mod id)
     * @param path The item id
     * @param count The count
     * @return The created ItemStack
     */
    public static ItemStack createStack(String namespace, String path, int count) {
        ResourceLocation location = ResourceLocationHelper.create(namespace, path);
        return createStack(location, count);
    }
    
    /**
     * Safely gets a string from a compound tag.
     * 
     * @param tag The tag
     * @param key The key
     * @return The string or empty string if not found
     */
    public static String getString(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return "";
        }
        
        try {
            // In NeoForge 1.21.5, getString returns an Optional<String>
            Object result = tag.getString(key);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<String> opt = (java.util.Optional<String>) result;
                return opt.orElse("");
            } else if (result instanceof String) {
                return (String) result;
            }
            return "";
        } catch (Exception e) {
            return "";
        }
    }
    
    /**
     * Safely gets an int from a compound tag.
     * 
     * @param tag The tag
     * @param key The key
     * @return The int or 0 if not found
     */
    public static int getInt(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return 0;
        }
        
        try {
            // In NeoForge 1.21.5, getInt returns an Optional<Integer>
            Object result = tag.getInt(key);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<Integer> opt = (java.util.Optional<Integer>) result;
                return opt.orElse(0);
            } else if (result instanceof Integer) {
                return (Integer) result;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Gets a list tag from a compound tag.
     * 
     * @param tag The compound tag
     * @param key The key
     * @param type The tag type (not used in NeoForge 1.21.5)
     * @return The list tag or null if not found
     */
    public static ListTag getList(CompoundTag tag, String key, int type) {
        if (tag == null || !tag.contains(key)) {
            return null;
        }
        
        try {
            // In NeoForge 1.21.5, getList() returns Optional<ListTag>
            Object result = tag.getList(key);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<ListTag> opt = (java.util.Optional<ListTag>) result;
                return opt.orElse(null);
            } else if (result instanceof ListTag) {
                return (ListTag) result;
            }
            
            // Last resort: get raw and cast
            Tag rawTag = tag.get(key);
            if (rawTag instanceof ListTag) {
                return (ListTag) rawTag;
            }
        } catch (Exception e) {
            // Fallback to null on any error
        }
        
        return null;
    }
    
    /**
     * Gets a compound tag from a list tag at the specified index.
     * 
     * @param listTag The list tag
     * @param index The index
     * @return The compound tag or null if not found
     */
    public static CompoundTag getCompound(ListTag listTag, int index) {
        if (listTag == null || index < 0 || index >= listTag.size()) {
            return null;
        }
        
        try {
            // In NeoForge 1.21.5, getCompound() returns Optional<CompoundTag>
            Object result = listTag.getCompound(index);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<CompoundTag> opt = (java.util.Optional<CompoundTag>) result;
                return opt.orElse(null);
            } else if (result instanceof CompoundTag) {
                return (CompoundTag) result;
            }
            
            // Try alternate approach
            Tag tag = listTag.get(index);
            if (tag instanceof CompoundTag) {
                return (CompoundTag) tag;
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        
        return null;
    }
    
    /**
     * Gets a compound tag from another compound tag.
     * 
     * @param tag The compound tag
     * @param key The key
     * @return The compound tag or null if not found
     */
    public static CompoundTag getCompound(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return null;
        }
        
        try {
            // In NeoForge 1.21.5, getCompound() returns Optional<CompoundTag>
            Object result = tag.getCompound(key);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<CompoundTag> opt = (java.util.Optional<CompoundTag>) result;
                return opt.orElse(null);
            } else if (result instanceof CompoundTag) {
                return (CompoundTag) result;
            }
            
            // Try to get the raw tag and cast it
            Tag innerTag = tag.get(key);
            if (innerTag instanceof CompoundTag) {
                return (CompoundTag) innerTag;
            }
        } catch (Exception e) {
            // Ignore and return null
        }
        
        return null;
    }
    
    /**
     * Helper method to handle OptionalXXX return types in NeoForge 1.21.5
     */
    @SuppressWarnings("unchecked")
    private static <T> java.util.Optional<T> getOptional(CompoundTag tag, String key, Function<String, Object> getter) {
        try {
            Object result = getter.apply(key);
            if (result instanceof java.util.Optional) {
                return (java.util.Optional<T>) result;
            }
            return java.util.Optional.ofNullable((T) result);
        } catch (Exception e) {
            return java.util.Optional.empty();
        }
    }
    
    /**
     * Checks if an ItemStack has a tag.
     * This is a compatibility method for handling API differences between Minecraft versions.
     * 
     * @param stack The ItemStack to check
     * @return true if the ItemStack has a tag, false otherwise
     */
    public static boolean hasTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        
        // For NeoForge 1.21.5: use our tag cache system
        UUID stackId = stackIds.get(stack);
        if (stackId == null) {
            return false; 
        }
        
        CompoundTag tag = tagCache.get(stackId);
        return tag != null && !tag.isEmpty();
    }
    
    /**
     * Safely gets a float from a compound tag.
     * Works with NeoForge 1.21.5 where getFloat returns Optional<Float>.
     * 
     * @param tag The tag
     * @param key The key
     * @return The float or 0.0f if not found
     */
    public static float getFloat(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return 0.0f;
        }
        
        try {
            // In NeoForge 1.21.5, getFloat returns an Optional<Float>
            Object result = tag.getFloat(key);
            if (result instanceof java.util.Optional) {
                @SuppressWarnings("unchecked")
                java.util.Optional<Float> opt = (java.util.Optional<Float>) result;
                return opt.orElse(0.0f);
            } else if (result instanceof Float) {
                return (Float) result;
            }
            return 0.0f;
        } catch (Exception e) {
            return 0.0f;
        }
    }
}