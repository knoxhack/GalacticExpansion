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
            // In NeoForge 1.21.5, this is direct access without Optional wrapping
            if (tag.contains(key, Tag.TAG_COMPOUND)) {
                return (CompoundTag) tag.get(key);
            }
            return new CompoundTag();
        } catch (Exception e) {
            // Fallback
            return new CompoundTag();
        }
    }
    
    /**
     * Gets a list tag from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param type The element type
     * @return The list tag, or a new one if not found
     */
    public static ListTag getListTag(CompoundTag tag, String key, int type) {
        try {
            // In NeoForge 1.21.5, this is direct access without Optional wrapping
            if (tag.contains(key, Tag.TAG_LIST)) {
                ListTag list = (ListTag) tag.get(key);
                if (list != null) {
                    return list;
                }
            }
            return new ListTag();
        } catch (Exception e) {
            // Fallback
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
            // In NeoForge 1.21.5, this is direct access without Optional wrapping
            return tag.getString(key);
        } catch (Exception e) {
            // Fallback
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
            return tag.getInt(key);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Gets an integer from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The integer, or defaultValue if not found
     */
    public static int getInt(CompoundTag tag, String key, int defaultValue) {
        try {
            if (tag.contains(key)) {
                return tag.getInt(key);
            }
            return defaultValue;
        } catch (Exception e) {
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
            return tag.getFloat(key);
        } catch (Exception e) {
            return 0;
        }
    }
    
    /**
     * Gets a float from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The float, or defaultValue if not found
     */
    public static float getFloat(CompoundTag tag, String key, float defaultValue) {
        try {
            if (tag.contains(key)) {
                return tag.getFloat(key);
            }
            return defaultValue;
        } catch (Exception e) {
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
            return tag.getBoolean(key);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Gets a double from a compound tag safely.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The double, or 0.0 if not found
     */
    public static double getDouble(CompoundTag tag, String key) {
        try {
            return tag.getDouble(key);
        } catch (Exception e) {
            return 0.0;
        }
    }
    
    /**
     * Gets a double from a compound tag safely with a default value.
     *
     * @param tag The parent tag
     * @param key The key to get
     * @param defaultValue The default value to return if not found
     * @return The double, or defaultValue if not found
     */
    public static double getDouble(CompoundTag tag, String key, double defaultValue) {
        try {
            if (tag.contains(key)) {
                return tag.getDouble(key);
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
        }
    }
}