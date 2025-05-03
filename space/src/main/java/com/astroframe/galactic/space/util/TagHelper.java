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
     * In newer versions of Minecraft, getCompound returns an Optional<CompoundTag>
     *
     * @param tag The parent tag
     * @param key The key to get
     * @return The compound tag, or a new one if not found
     */
    public static CompoundTag getCompoundTag(CompoundTag tag, String key) {
        // In NeoForge 1.21.5, this returns Optional<CompoundTag>
        // We need to unwrap the optional
        try {
            // Attempt to handle as Optional (newer versions)
            try {
                Optional<CompoundTag> result = tag.getCompound(key);
                if (result.isPresent()) {
                    return result.get();
                }
                return new CompoundTag();
            } catch (ClassCastException e) {
                // Direct access (older versions)
                CompoundTag directResult = (CompoundTag) tag.get(key);
                if (directResult != null) {
                    return directResult;
                }
                return new CompoundTag();
            }
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
            // Attempt to handle as Optional (newer versions)
            try {
                Optional<ListTag> result = tag.getList(key, type);
                if (result.isPresent()) {
                    return result.get();
                }
                return new ListTag();
            } catch (ClassCastException e) {
                // Direct access (older versions)
                ListTag directResult = (ListTag) tag.get(key);
                if (directResult != null) {
                    return directResult;
                }
                return new ListTag();
            }
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
            // Attempt to handle as Optional (newer versions)
            try {
                Optional<String> result = tag.getString(key);
                if (result.isPresent()) {
                    return result.get();
                }
                return "";
            } catch (ClassCastException e) {
                // Direct access (older versions)
                String directResult = tag.getString(key);
                if (directResult != null) {
                    return directResult;
                }
                return "";
            }
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
}