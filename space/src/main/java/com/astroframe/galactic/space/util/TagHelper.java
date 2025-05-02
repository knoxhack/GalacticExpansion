package com.astroframe.galactic.space.util;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

/**
 * Helper class for working with NBT Tags in a NeoForge 1.21.5 compatible way.
 */
public class TagHelper {
    
    /**
     * Gets a string value from a tag.
     */
    public static String getStringValue(Tag tag) {
        if (tag == null) {
            return "";
        }
        
        if (tag instanceof StringTag) {
            return tag.getAsString();
        }
        
        return tag.toString();
    }
    
    /**
     * Gets a string value from a compound tag.
     */
    public static String getStringValue(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return "";
        }
        
        try {
            return tag.getString(key);
        } catch (Exception e) {
            // Fallback for NeoForge 1.21.5 which uses Optional<String>
            Tag valueTag = tag.get(key);
            return getStringValue(valueTag);
        }
    }
    
    /**
     * Gets an integer value from a compound tag.
     */
    public static int getIntValue(CompoundTag tag, String key) {
        if (tag == null || !tag.contains(key)) {
            return 0;
        }
        
        try {
            return tag.getInt(key);
        } catch (Exception e) {
            // Fallback for NeoForge 1.21.5 which uses Optional<Integer>
            GalacticSpace.LOGGER.warn("Failed to get int value for key " + key + ": " + e.getMessage());
            return 0;
        }
    }
    
    /**
     * Gets a string value from a list tag at the given index.
     */
    public static String getStringFromList(ListTag list, int index) {
        if (list == null || index < 0 || index >= list.size()) {
            return "";
        }
        
        Tag tag = list.get(index);
        return getStringValue(tag);
    }
}