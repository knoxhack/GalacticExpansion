package com.astroframe.galactic.core.api.util;

import net.minecraft.nbt.CompoundTag;

/**
 * Utility class to handle NBT tag operations in a safe manner for NeoForge 1.21.5.
 * Uses direct access methods instead of handling Optional types.
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        // Use direct tag access method in NeoForge 1.21.5
        try {
            String value = tag.getAsString(key);
            return value != null && !value.isEmpty() ? value : fallback;
        } catch (Exception e) {
            return fallback;
        }
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        try {
            return tag.getAsInt(key);
        } catch (Exception e) {
            return fallback;
        }
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        try {
            return tag.getAsFloat(key);
        } catch (Exception e) {
            return fallback;
        }
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        try {
            return tag.getAsBoolean(key);
        } catch (Exception e) {
            return fallback;
        }
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        try {
            return tag.getAsDouble(key);
        } catch (Exception e) {
            return fallback;
        }
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
        if (tag == null || !tag.contains(key)) {
            return fallback;
        }
        
        try {
            return tag.getAsLong(key);
        } catch (Exception e) {
            return fallback;
        }
    }
    
    /**
     * Gets a sub-compound tag from a CompoundTag with fallback
     * 
     * @param tag The compound tag
     * @param key The key to get
     * @return The compound tag or a new empty tag if not found
     */
    public static CompoundTag getCompound(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return new CompoundTag();
        }
        
        try {
            CompoundTag compoundTag = tag.getCompound(key);
            return compoundTag != null ? compoundTag : new CompoundTag();
        } catch (Exception e) {
            return new CompoundTag();
        }
    }
}