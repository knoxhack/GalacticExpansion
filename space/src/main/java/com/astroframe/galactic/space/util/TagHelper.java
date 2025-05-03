package com.astroframe.galactic.space.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.Optional;

/**
 * Helper methods for working with NBT tags.
 * Provides compatibility with different Minecraft and modding API versions.
 */
public class TagHelper {
    
    /**
     * Gets a compound tag from another compound tag safely, handling version differences.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The compound tag, or a new one if not found
     */
    public static CompoundTag getCompoundTag(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, we need to check contains first, then get the tag
            if (tag.contains(key)) {
                Tag rawTag = tag.get(key);
                if (rawTag instanceof CompoundTag) {
                    return (CompoundTag) rawTag;
                }
            }
            return new CompoundTag();
        } catch (Exception e) {
            // Fallback for any errors
            return new CompoundTag();
        }
    }
    
    /**
     * Gets a list tag from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param type The element type (not used in NeoForge 1.21.5)
     * @return The list tag, or a new one if not found
     */
    public static ListTag getListTag(CompoundTag tag, String key, int type) {
        try {
            // In NeoForge 1.21.5, we check contains then cast
            if (tag.contains(key)) {
                Tag rawTag = tag.get(key);
                if (rawTag instanceof ListTag) {
                    return (ListTag) rawTag;
                }
            }
            return new ListTag();
        } catch (Exception e) {
            // Fallback for any errors
            return new ListTag();
        }
    }
    
    /**
     * Gets a string from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The string, or empty if not found
     */
    public static String getString(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, getString returns an Optional<String>
            return tag.getString(key).orElse("");
        } catch (Exception e) {
            // Fallback for any errors
            return "";
        }
    }
    
    /**
     * Gets an integer from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The integer, or 0 if not found
     */
    public static int getInt(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, getInt returns an Optional<Integer>
            return tag.getInt(key).orElse(0);
        } catch (Exception e) {
            // Fallback for any errors
            return 0;
        }
    }
    
    /**
     * Gets an integer from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value if not found
     * @return The integer, or defaultValue if not found
     */
    public static int getInt(CompoundTag tag, String key, int defaultValue) {
        try {
            if (tag.contains(key)) {
                // In NeoForge 1.21.5, getInt returns an Optional<Integer>
                return tag.getInt(key).orElse(defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            // Fallback for any errors
            return defaultValue;
        }
    }
    
    /**
     * Gets a float from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The float, or 0 if not found
     */
    public static float getFloat(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, getFloat returns an Optional<Float>
            return tag.getFloat(key).orElse(0.0f);
        } catch (Exception e) {
            // Fallback for any errors
            return 0.0f;
        }
    }
    
    /**
     * Gets a float from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value if not found
     * @return The float, or defaultValue if not found
     */
    public static float getFloat(CompoundTag tag, String key, float defaultValue) {
        try {
            if (tag.contains(key)) {
                // In NeoForge 1.21.5, getFloat returns an Optional<Float>
                return tag.getFloat(key).orElse(defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            // Fallback for any errors
            return defaultValue;
        }
    }
    
    /**
     * Gets a boolean from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The boolean, or false if not found
     */
    public static boolean getBoolean(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, getBoolean returns an Optional<Boolean>
            return tag.getBoolean(key).orElse(false);
        } catch (Exception e) {
            // Fallback for any errors
            return false;
        }
    }
    
    /**
     * Gets a double from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The double, or 0 if not found
     */
    public static double getDouble(CompoundTag tag, String key) {
        try {
            // In NeoForge 1.21.5, getDouble returns an Optional<Double>
            return tag.getDouble(key).orElse(0.0d);
        } catch (Exception e) {
            // Fallback for any errors
            return 0.0d;
        }
    }
    
    /**
     * Gets a double from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value if not found
     * @return The double, or defaultValue if not found
     */
    public static double getDouble(CompoundTag tag, String key, double defaultValue) {
        try {
            if (tag.contains(key)) {
                // In NeoForge 1.21.5, getDouble returns an Optional<Double>
                return tag.getDouble(key).orElse(defaultValue);
            }
            return defaultValue;
        } catch (Exception e) {
            // Fallback for any errors
            return defaultValue;
        }
    }
}