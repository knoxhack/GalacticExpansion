package com.astroframe.galactic.space.items;

import com.astroframe.galactic.space.util.ResourceLocationHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
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
        
        // First try to get from the actual ItemStack using reflection for compatibility
        CompoundTag reflectionTag = getTagViaReflection(stack);
        if (reflectionTag != null) {
            return reflectionTag;
        }
        
        // Fall back to our in-memory cache if reflection fails
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
        
        // First try to set via reflection for compatibility
        boolean succeeded = setTagViaReflection(stack, tag);
        
        // Update our in-memory cache regardless
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
     * Attempt to get tag via reflection for compatibility with different Minecraft versions.
     */
    private static CompoundTag getTagViaReflection(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        
        try {
            // In NeoForge 1.21.5, the method is called tag() not getTag()
            try {
                Method method = stack.getClass().getMethod("tag");
                return (CompoundTag) method.invoke(stack);
            } catch (Exception ex) {
                // Try original method name
                try {
                    Method method = stack.getClass().getMethod("getTag");
                    return (CompoundTag) method.invoke(stack);
                } catch (Exception ex2) {
                    // Reflection failed, will fall back to cache
                }
            }
        } catch (Exception ignored) {
            // Ignore exceptions, will fall back to cache
        }
        return null;
    }
    
    /**
     * Attempt to set tag via reflection for compatibility with different Minecraft versions.
     * @return true if succeeded, false otherwise
     */
    private static boolean setTagViaReflection(ItemStack stack, CompoundTag tag) {
        if (stack == null || stack.isEmpty()) {
            return false;
        }
        
        try {
            // Try various method names for compatibility
            try {
                Method method = stack.getClass().getMethod("setTag", CompoundTag.class);
                method.invoke(stack, tag);
                return true;
            } catch (Exception ex) {
                try {
                    Method method = stack.getClass().getMethod("tag", CompoundTag.class);
                    method.invoke(stack, tag);
                    return true;
                } catch (Exception ex2) {
                    // Reflection failed, will fall back to cache
                }
            }
        } catch (Exception ignored) {
            // Ignore exceptions, will fall back to cache
        }
        return false;
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
     * Helper method to handle the Holder<Item> returns in NeoForge 1.21.5
     * 
     * @param location The resource location to resolve
     * @return The resolved Item or Items.AIR if not found
     */
    public static Item resolveItemFromRegistry(ResourceLocation location) {
        try {
            // In NeoForge 1.21.5, we should use BuiltInRegistries instead of Registries
            Registry<Item> registry = BuiltInRegistries.ITEM;
            
            // In NeoForge 1.21.5, registry.get() returns Optional<T>, so we need to handle it
            Optional<Item> itemOpt = registry.getOptional(location);
            if (itemOpt.isPresent()) {
                return itemOpt.get();
            }
            
            // If direct lookup failed, try to get via Holder
            // In NeoForge 1.21.5, we need to get the Holder reference through the registry key
            Optional<ResourceKey<Item>> resourceKey = ResourceKey.createRegistryKey(Registry.ITEM_REGISTRY, location);
            Optional<Holder.Reference<Item>> holderOpt = resourceKey.flatMap(registry::getHolder);
            if (holderOpt.isPresent()) {
                return holderOpt.get().value();
            }
            
            return Items.AIR;
        } catch (Exception e) {
            return Items.AIR;
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
            Item item = resolveItemFromRegistry(location);
            
            // If no item is found, return AIR
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
            Optional<String> result = tag.getString(key);
            return result.orElse("");
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
            Optional<Integer> result = tag.getInt(key);
            return result.orElse(0);
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
            // In NeoForge 1.21.5, getList no longer takes a type parameter
            Optional<ListTag> result = tag.getList(key);
            return result.orElse(null);
        } catch (Exception ex) {
            // Try to get the raw tag and cast it as fallback
            try {
                Tag rawTag = tag.get(key);
                if (rawTag instanceof ListTag) {
                    return (ListTag) rawTag;
                }
            } catch (Exception ignored) {
                // Ignore nested exception
            }
            return null;
        }
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
            Optional<CompoundTag> result = listTag.getCompound(index);
            return result.orElse(null);
        } catch (Exception e) {
            // Try to get the raw tag and cast it as fallback
            try {
                Tag tag = listTag.get(index);
                if (tag instanceof CompoundTag) {
                    return (CompoundTag) tag;
                }
            } catch (Exception ignored) {
                // Ignore nested exception
            }
            return null;
        }
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
            Optional<CompoundTag> result = tag.getCompound(key);
            return result.orElse(null);
        } catch (Exception e) {
            // Try to get the raw tag and cast it as fallback
            try {
                Tag innerTag = tag.get(key);
                if (innerTag instanceof CompoundTag) {
                    return (CompoundTag) innerTag;
                }
            } catch (Exception ignored) {
                // Ignore nested exception
            }
            return null;
        }
    }
    
    /**
     * Checks if an item stack has a tag.
     * 
     * @param stack The item stack
     * @return True if it has a tag
     */
    public static boolean hasTag(ItemStack stack) {
        return getTag(stack) != null;
    }
}