package com.astroframe.galactic.space.util;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/**
 * Utility class for working with NBT tags in a NeoForge 1.21.5 compatible way.
 * Provides standardized methods for getting values from tags with proper type checking.
 * 
 * This class handles the transition from direct returns to Optional<T> returns in NeoForge 1.21.5.
 */
public class TagHelper {
    
    /**
     * Get a string value from a tag.
     * @param tag The tag
     * @return The string value, or an empty string if not a string tag
     */
    public static String getStringValue(Tag tag) {
        if (tag instanceof StringTag stringTag) {
            return stringTag.value();
        }
        GalacticSpace.LOGGER.warn("Attempted to get string value from non-string tag: " + tag);
        return "";
    }
    
    /**
     * Get a string value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The string value, or an empty string if not found or not a string
     */
    public static String getStringValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            Tag valueTag = tag.get(key);
            if (valueTag instanceof StringTag) {
                return getStringValue(valueTag);
            }
        }
        return "";
    }
    
    /**
     * Get an integer value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The integer value, or 0 if not found or not an integer
     */
    public static int getIntValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getInt method returns an Optional<Integer>
                return tag.getInt(key).orElse(0);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get int value from tag: " + e.getMessage());
            }
        }
        return 0;
    }
    
    /**
     * Get a boolean value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The boolean value, or false if not found or not a boolean
     */
    public static boolean getBooleanValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getBoolean method returns an Optional<Boolean>
                return tag.getBoolean(key).orElse(false);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get boolean value from tag: " + e.getMessage());
            }
        }
        return false;
    }
    
    /**
     * Get a double value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The double value, or 0.0 if not found or not a double
     */
    public static double getDoubleValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getDouble method returns an Optional<Double>
                return tag.getDouble(key).orElse(0.0);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get double value from tag: " + e.getMessage());
            }
        }
        return 0.0;
    }
    
    /**
     * Get a long value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The long value, or 0 if not found or not a long
     */
    public static long getLongValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getLong method returns an Optional<Long>
                return tag.getLong(key).orElse(0L);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get long value from tag: " + e.getMessage());
            }
        }
        return 0;
    }
    
    /**
     * Get a float value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The float value, or 0.0f if not found or not a float
     */
    public static float getFloatValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getFloat method returns an Optional<Float>
                return tag.getFloat(key).orElse(0.0f);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get float value from tag: " + e.getMessage());
            }
        }
        return 0.0f;
    }
    
    /**
     * Get a byte value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The byte value, or 0 if not found or not a byte
     */
    public static byte getByteValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getByte method returns an Optional<Byte>
                return tag.getByte(key).orElse((byte)0);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get byte value from tag: " + e.getMessage());
            }
        }
        return 0;
    }
    
    /**
     * Get a short value from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The short value, or 0 if not found or not a short
     */
    public static short getShortValue(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getShort method returns an Optional<Short>
                return tag.getShort(key).orElse((short)0);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get short value from tag: " + e.getMessage());
            }
        }
        return 0;
    }
    
    /**
     * Check if a compound tag contains a key.
     * @param tag The compound tag
     * @param key The key
     * @return true if the tag contains the key
     */
    public static boolean contains(CompoundTag tag, String key) {
        return tag.contains(key);
    }
    
    /**
     * Get a compound tag from a compound tag.
     * @param tag The compound tag
     * @param key The key
     * @return The compound tag, or null if not found or not a compound tag
     */
    public static CompoundTag getCompoundTag(CompoundTag tag, String key) {
        if (tag.contains(key)) {
            try {
                // In NeoForge 1.21.5, the getCompound method returns an Optional<CompoundTag>
                return tag.getCompound(key).orElse(null);
            } catch (Exception e) {
                // Fallback if the Optional approach doesn't work
                try {
                    Object tagValue = tag.get(key);
                    if (tagValue instanceof CompoundTag) {
                        return (CompoundTag) tagValue;
                    }
                } catch (Exception ex) {
                    GalacticSpace.LOGGER.warn("Failed to get compound tag from tag: " + ex.getMessage());
                }
            }
        }
        return null;
    }
    
    /**
     * Get a string value from a compound tag with a default value.
     * @param tag The compound tag
     * @param key The key
     * @param defaultValue The default value to return if the key is not found
     * @return The string value, or the default value if not found or not a string
     */
    public static String getString(CompoundTag tag, String key, String defaultValue) {
        String value = getStringValue(tag, key);
        return value.isEmpty() ? defaultValue : value;
    }
    
    /**
     * Get an integer value from a compound tag with a default value.
     * @param tag The compound tag
     * @param key The key
     * @param defaultValue The default value to return if the key is not found
     * @return The integer value, or the default value if not found or not an integer
     */
    public static int getInt(CompoundTag tag, String key, int defaultValue) {
        if (tag.contains(key)) {
            try {
                return tag.getInt(key).orElse(defaultValue);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get int value from tag: " + e.getMessage());
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a float value from a compound tag with a default value.
     * @param tag The compound tag
     * @param key The key
     * @param defaultValue The default value to return if the key is not found
     * @return The float value, or the default value if not found or not a float
     */
    public static float getFloat(CompoundTag tag, String key, float defaultValue) {
        if (tag.contains(key)) {
            try {
                return tag.getFloat(key).orElse(defaultValue);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get float value from tag: " + e.getMessage());
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a boolean value from a compound tag with a default value.
     * @param tag The compound tag
     * @param key The key
     * @param defaultValue The default value to return if the key is not found
     * @return The boolean value, or the default value if not found or not a boolean
     */
    public static boolean getBoolean(CompoundTag tag, String key, boolean defaultValue) {
        if (tag.contains(key)) {
            try {
                return tag.getBoolean(key).orElse(defaultValue);
            } catch (Exception e) {
                GalacticSpace.LOGGER.warn("Failed to get boolean value from tag: " + e.getMessage());
            }
        }
        return defaultValue;
    }
}