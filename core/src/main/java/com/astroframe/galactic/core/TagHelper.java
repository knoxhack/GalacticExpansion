package com.astroframe.galactic.core;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

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
                Optional<Integer> opt = tag.getInt(key);
                return opt.orElse(defaultValue);
            } catch (Exception e) {
                // Fallback for direct integer access in case API changes again
                try {
                    return tag.getInt(key);
                } catch (Exception ex) {
                    return defaultValue;
                }
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
                Optional<Float> opt = tag.getFloat(key);
                return opt.orElse(defaultValue);
            } catch (Exception e) {
                // Fallback for direct float access in case API changes again
                try {
                    return tag.getFloat(key);
                } catch (Exception ex) {
                    return defaultValue;
                }
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
                Optional<Boolean> opt = tag.getBoolean(key);
                return opt.orElse(defaultValue);
            } catch (Exception e) {
                // Fallback for direct boolean access in case API changes again
                try {
                    return tag.getBoolean(key);
                } catch (Exception ex) {
                    return defaultValue;
                }
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
                Optional<String> opt = tag.getString(key);
                return opt.orElse(defaultValue);
            } catch (Exception e) {
                // Fallback for direct string access in case API changes again
                try {
                    String value = tag.getString(key);
                    return value != null ? value : defaultValue;
                } catch (Exception ex) {
                    return defaultValue;
                }
            }
        }
        return defaultValue;
    }
    
    /**
     * Creates a ResourceLocation from a string, safely handling namespaces.
     * @param path The resource path
     * @return A new ResourceLocation
     */
    public static ResourceLocation createResourceLocation(String path) {
        if (path == null || path.isEmpty()) {
            return new ResourceLocation("galactic", "invalid");
        }
        
        // Check if the path already contains a namespace
        if (path.contains(":")) {
            String[] parts = path.split(":", 2);
            return new ResourceLocation(parts[0], parts[1]);
        } else {
            // Default to the mod namespace
            return new ResourceLocation("galactic", path);
        }
    }
}