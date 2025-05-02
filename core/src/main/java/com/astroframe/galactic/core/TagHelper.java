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
                return tag.getInt(key).orElse(defaultValue);
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