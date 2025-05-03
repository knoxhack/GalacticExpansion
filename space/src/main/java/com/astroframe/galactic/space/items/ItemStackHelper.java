package com.astroframe.galactic.space.items;

import com.astroframe.galactic.space.util.ResourceLocationHelper;
import net.minecraft.core.Reference;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.ForgeRegistries;

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
            // In NeoForge 1.21.5, the registry returns Optional<Reference<Item>>
            Optional<Reference<Item>> itemRef = BuiltInRegistries.ITEM.get(location);
            Item item = itemRef.isPresent() ? itemRef.get().value() : Items.AIR;
            
            if (item == Items.AIR) {
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
            // In NeoForge 1.21.5, getString may return an Optional<String>
            Object result = tag.getString(key);
            if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                return opt.isPresent() ? opt.get().toString() : "";
            }
            return result.toString();
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
            // In NeoForge 1.21.5, getInt may return an Optional<Integer>
            Object result = tag.getInt(key);
            if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                if (opt.isPresent()) {
                    Object value = opt.get();
                    if (value instanceof Integer) {
                        return (Integer) value;
                    } else {
                        return Integer.parseInt(value.toString());
                    }
                }
                return 0;
            }
            if (result instanceof Integer) {
                return (Integer) result;
            }
            return Integer.parseInt(result.toString());
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
        
        // In NeoForge 1.21.5, getList method may have changed
        try {
            // First try getting without type parameter
            java.lang.reflect.Method method = tag.getClass().getMethod("getList", String.class);
            Object result = method.invoke(tag, key);
            if (result instanceof ListTag) {
                return (ListTag) result;
            } else if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                if (opt.isPresent() && opt.get() instanceof ListTag) {
                    return (ListTag) opt.get();
                }
            }
        } catch (NoSuchMethodException ex) {
            // Try with type parameter
            try {
                java.lang.reflect.Method method = tag.getClass().getMethod("getList", String.class, int.class);
                Object result = method.invoke(tag, key, type);
                if (result instanceof ListTag) {
                    return (ListTag) result;
                } else if (result instanceof Optional) {
                    Optional<?> opt = (Optional<?>) result;
                    if (opt.isPresent() && opt.get() instanceof ListTag) {
                        return (ListTag) opt.get();
                    }
                }
            } catch (Exception ex2) {
                // Fall through to alternate approaches
            }
        } catch (Exception ex3) {
            // Fall through to alternate approaches
        }
        
        // Fallback to null on any error
        try {
            // Last resort: get raw and cast
            Tag rawTag = tag.get(key);
            if (rawTag instanceof ListTag) {
                return (ListTag) rawTag;
            }
        } catch (Exception ex) {
            // Ignore nested exception
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
            // In NeoForge 1.21.5, getCompound might return an Optional<CompoundTag>
            Object result = listTag.getCompound(index);
            if (result instanceof CompoundTag) {
                return (CompoundTag) result;
            } else if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                if (opt.isPresent() && opt.get() instanceof CompoundTag) {
                    return (CompoundTag) opt.get();
                }
            }
            // Try alternate approach
            try {
                Tag tag = listTag.get(index);
                if (tag instanceof CompoundTag) {
                    return (CompoundTag) tag;
                }
            } catch (Exception ex) {
                // Ignore nested exception
            }
            return null;
        } catch (Exception e) {
            // Fall back to direct approach if getCompound fails
            try {
                Tag tag = listTag.get(index);
                if (tag instanceof CompoundTag) {
                    return (CompoundTag) tag;
                }
            } catch (Exception ex) {
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
            // In NeoForge 1.21.5, getCompound might return an Optional<CompoundTag>
            Object result = tag.getCompound(key);
            if (result instanceof CompoundTag) {
                return (CompoundTag) result;
            } else if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                if (opt.isPresent() && opt.get() instanceof CompoundTag) {
                    return (CompoundTag) opt.get();
                }
            }
            // Try to get the raw tag and cast it
            try {
                Tag innerTag = tag.get(key);
                if (innerTag instanceof CompoundTag) {
                    return (CompoundTag) innerTag;
                }
            } catch (Exception ex) {
                // Ignore nested exception
            }
            return null;
        } catch (Exception e) {
            // Fall back to direct approach if getCompound fails
            try {
                Tag innerTag = tag.get(key);
                if (innerTag instanceof CompoundTag) {
                    return (CompoundTag) innerTag;
                }
            } catch (Exception ex) {
                // Ignore nested exception
            }
            return null;
        }
    }
    
    /**
     * This method has been removed as OptionalXXX return types are no longer used in NeoForge 1.21.5
     * Direct access via getInt, getFloat, etc. now returns values directly.
     */
    
    /**
     * Gets the tag from an ItemStack.
     * This provides a compatibility method for NeoForge 1.21.5 where the method is called getNbt().
     * 
     * @param stack The ItemStack
     * @return The tag or null if none
     */
    public static CompoundTag getTag(ItemStack stack) {
        if (stack == null || stack.isEmpty()) {
            return null;
        }
        
        try {
            // In NeoForge 1.21.5, the method is called tag() not getTag()
            CompoundTag tag = null;
            try {
                java.lang.reflect.Method method = stack.getClass().getMethod("tag");
                tag = (CompoundTag) method.invoke(stack);
                return tag;
            } catch (Exception ex) {
                // Try original method name
                try {
                    java.lang.reflect.Method method = stack.getClass().getMethod("getTag");
                    tag = (CompoundTag) method.invoke(stack);
                    return tag;
                } catch (Exception ex2) {
                    // Fall back to cache system
                    return null;
                }
            }
        } catch (Exception e) {
            // Fall back to our tag cache system
            UUID stackId = stackIds.get(stack);
            if (stackId == null) {
                return null;
            }
            return tagCache.get(stackId);
        }
    }
    
    /**
     * Sets the tag for an ItemStack.
     * This provides a compatibility method for NeoForge 1.21.5 where the method is called setNbt().
     * 
     * @param stack The ItemStack
     * @param tag The tag to set
     */
    public static void setTag(ItemStack stack, CompoundTag tag) {
        if (stack == null || stack.isEmpty()) {
            return;
        }
        
        try {
            // In NeoForge 1.21.5, the method is called setTag() is no longer available, use direct assignment
            // Using reflection because the field is not directly accessible
            try {
                java.lang.reflect.Method method = stack.getClass().getMethod("setTag", CompoundTag.class);
                method.invoke(stack, tag);
            } catch (NoSuchMethodException e1) {
                // For NeoForge 1.21.5, we need to use a different approach
                try {
                    // Try to find a method that lets us set the tag data
                    for (java.lang.reflect.Method m : stack.getClass().getMethods()) {
                        if (m.getName().equals("save") && m.getParameterCount() == 1) {
                            // Use the save method as a workaround
                            CompoundTag newTag = new CompoundTag();
                            if (tag != null) {
                                newTag.merge(tag);
                            }
                            m.invoke(stack, newTag);
                            return;
                        }
                    }
                } catch (Exception e2) {
                    // Fall through to cache system
                }
            } catch (Exception e3) {
                // Fall through to cache system  
            }
        } catch (Exception e) {
            // Fall back to our tag cache system
            if (tag == null) {
                UUID stackId = stackIds.remove(stack);
                if (stackId != null) {
                    tagCache.remove(stackId);
                }
            } else {
                UUID stackId = stackIds.computeIfAbsent(stack, k -> UUID.randomUUID());
                tagCache.put(stackId, tag);
            }
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
        
        // In NeoForge 1.21.5, the hasTag method is replaced with another method
        try {
            // First try the modern API
            CompoundTag tag = null;
            try {
                java.lang.reflect.Method method = stack.getClass().getMethod("tag");
                tag = (CompoundTag) method.invoke(stack);
            } catch (Exception ex) {
                // Try original method name
                try {
                    java.lang.reflect.Method method = stack.getClass().getMethod("getTag");
                    tag = (CompoundTag) method.invoke(stack);
                } catch (Exception ex2) {
                    // Fall back to cache system
                    return false;
                }
            }
            return tag != null && !tag.isEmpty();
        } catch (Exception e) {
            // Fall back to our tag cache system if needed
            UUID stackId = stackIds.get(stack);
            if (stackId == null) {
                return false; 
            }
            
            CompoundTag tag = tagCache.get(stackId);
            return tag != null && !tag.isEmpty();
        }
    }
    
    /**
     * Safely gets a float from a compound tag.
     * Updated for NeoForge 1.21.5 where getFloat returns the value directly.
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
            // In NeoForge 1.21.5, getFloat may return an Optional<Float>
            Object result = tag.getFloat(key);
            if (result instanceof Optional) {
                Optional<?> opt = (Optional<?>) result;
                if (opt.isPresent()) {
                    Object value = opt.get();
                    if (value instanceof Float) {
                        return (Float) value;
                    } else {
                        return Float.parseFloat(value.toString());
                    }
                }
                return 0.0f;
            }
            if (result instanceof Float) {
                return (Float) result;
            }
            return Float.parseFloat(result.toString());
        } catch (Exception e) {
            return 0.0f;
        }
    }
}