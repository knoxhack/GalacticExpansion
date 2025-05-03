package com.astroframe.galactic.core.api.util;

import net.minecraft.nbt.CompoundTag;

/**
 * Utility class to handle the transition from Optional-wrapped tag getters in older versions
 * to direct value returns in NeoForge 1.21.5.
 */
public class TagHelper {

    /**
     * Gets a string from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The string value or fallback
     */
    public static String getString(CompoundTag tag, String key, String fallback) {
        if (tag != null && tag.contains(key)) {
            String value = tag.getString(key);
            return value != null && !value.isEmpty() ? value : fallback;
        }
        return fallback;
    }
    
    /**
     * Gets an integer from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The integer value or fallback
     */
    public static int getInt(CompoundTag tag, String key, int fallback) {
        return tag != null && tag.contains(key) ? tag.getInt(key) : fallback;
    }
    
    /**
     * Gets a float from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The float value or fallback
     */
    public static float getFloat(CompoundTag tag, String key, float fallback) {
        return tag != null && tag.contains(key) ? tag.getFloat(key) : fallback;
    }
    
    /**
     * Gets a boolean from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The boolean value or fallback
     */
    public static boolean getBoolean(CompoundTag tag, String key, boolean fallback) {
        return tag != null && tag.contains(key) ? tag.getBoolean(key) : fallback;
    }
    
    /**
     * Gets a double from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The double value or fallback
     */
    public static double getDouble(CompoundTag tag, String key, double fallback) {
        return tag != null && tag.contains(key) ? tag.getDouble(key) : fallback;
    }
    
    /**
     * Gets a long from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @param fallback The fallback value if the key doesn't exist
     * @return The long value or fallback
     */
    public static long getLong(CompoundTag tag, String key, long fallback) {
        return tag != null && tag.contains(key) ? tag.getLong(key) : fallback;
    }
    
    /**
     * Gets a sub-compound tag from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @return The compound tag or a new empty tag if not found
     */
    public static CompoundTag getCompound(CompoundTag tag, String key) {
        if (tag != null && tag.contains(key)) {
            return tag.getCompound(key);
        }
        return new CompoundTag();
    }
}