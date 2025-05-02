package com.astroframe.galactic.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.UUID;

/**
 * Utility class for working with CompoundTag.
 * Handles the differences in tag methods between NeoForge 1.21.5 (which returns Optional)
 * and earlier versions.
 */
public class TagHelper {

    /**
     * Gets an integer value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The integer value or the default value
     */
    public static int getInt(CompoundTag tag, String key, int defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getInt returns Optional<Integer>
                return tag.getInt(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets a long value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The long value or the default value
     */
    public static long getLong(CompoundTag tag, String key, long defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getLong returns Optional<Long>
                return tag.getLong(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets a double value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The double value or the default value
     */
    public static double getDouble(CompoundTag tag, String key, double defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getDouble returns Optional<Double>
                return tag.getDouble(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets a float value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The float value or the default value
     */
    public static float getFloat(CompoundTag tag, String key, float defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getFloat returns Optional<Float>
                return tag.getFloat(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets a boolean value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The boolean value or the default value
     */
    public static boolean getBoolean(CompoundTag tag, String key, boolean defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getBoolean returns Optional<Boolean>
                return tag.getBoolean(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Gets a string value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The string value or the default value
     */
    public static String getString(CompoundTag tag, String key, String defaultValue) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, getString returns Optional<String>
                return tag.getString(key).orElse(defaultValue);
            } catch (Exception e) {
                // This is a fallback if the API changes again
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Gets a UUID value from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @return The UUID or null if not found
     */
    public static UUID getUUID(CompoundTag tag, String key) {
        if (tag.contains(key + "Most") && tag.contains(key + "Least")) {
            try {
                long most = getLong(tag, key + "Most", 0L);
                long least = getLong(tag, key + "Least", 0L);
                return new UUID(most, least);
            } catch (Exception e) {
                return null;
            }
        } else if (tag.contains(key)) {
            try {
                String uuidStr = getString(tag, key, "");
                if (!uuidStr.isEmpty()) {
                    return UUID.fromString(uuidStr);
                }
            } catch (Exception e) {
                // Invalid UUID format
            }
        }
        return null;
    }
    
    /**
     * Puts a UUID into a compound tag.
     * Stores it as both Most/Least components and as a string for maximum compatibility.
     * @param tag The compound tag
     * @param key The key to store under
     * @param uuid The UUID to store
     */
    public static void putUUID(CompoundTag tag, String key, UUID uuid) {
        tag.putLong(key + "Most", uuid.getMostSignificantBits());
        tag.putLong(key + "Least", uuid.getLeastSignificantBits());
        tag.putString(key, uuid.toString());
    }
    
    /**
     * Gets a CompoundTag from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @return A new compound tag, or an empty one if not found
     */
    public static CompoundTag getCompound(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                return tag.getCompound(key).orElse(new CompoundTag());
            } catch (Exception e) {
                return new CompoundTag();
            }
        }
        return new CompoundTag();
    }
    
    /**
     * Gets a ListTag from a compound tag.
     * @param tag The compound tag
     * @param key The key to get
     * @return A new list tag, or an empty one if not found
     */
    public static ListTag getList(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                return tag.getList(key).orElse(new ListTag());
            } catch (Exception e) {
                return new ListTag();
            }
        }
        return new ListTag();
    }
    
    /**
     * Creates a ResourceLocation from a string, safely handling namespaces.
     * @param path The resource path
     * @return A new ResourceLocation
     */
    public static ResourceLocation createResourceLocation(String path) {
        if (path == null || path.isEmpty()) {
            return ResourceLocation.parse("galactic:invalid");
        }
        
        // Check if the path already contains a namespace
        if (path.contains(":")) {
            return ResourceLocation.parse(path);
        } else {
            // Default to the mod namespace
            return ResourceLocation.parse("galactic:" + path);
        }
    }
}