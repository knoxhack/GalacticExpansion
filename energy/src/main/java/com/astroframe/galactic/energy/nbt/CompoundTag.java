package com.astroframe.galactic.energy.nbt;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple implementation of an NBT-like compound tag.
 * This allows for storing data in a tag-based format without requiring Minecraft dependencies.
 */
public class CompoundTag {
    private final Map<String, Object> data = new HashMap<>();
    
    /**
     * Stores an integer value.
     * 
     * @param key The key
     * @param value The value
     */
    public void putInt(String key, int value) {
        data.put(key, value);
    }
    
    /**
     * Stores a string value.
     * 
     * @param key The key
     * @param value The value
     */
    public void putString(String key, String value) {
        data.put(key, value);
    }
    
    /**
     * Gets an integer value.
     * 
     * @param key The key
     * @return The value, or 0 if not found
     */
    public int getInt(String key) {
        Object value = data.get(key);
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return 0;
    }
    
    /**
     * Gets a string value.
     * 
     * @param key The key
     * @return The value, or an empty string if not found
     */
    public String getString(String key) {
        Object value = data.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return "";
    }
    
    /**
     * Checks if this compound tag contains a key.
     * 
     * @param key The key
     * @return Whether the key exists
     */
    public boolean contains(String key) {
        return data.containsKey(key);
    }
    
    /**
     * Merges data from another compound tag.
     * 
     * @param other The other compound tag
     */
    public void merge(CompoundTag other) {
        data.putAll(other.data);
    }
    
    /**
     * Creates a copy of this compound tag.
     * 
     * @return The copy
     */
    public CompoundTag copy() {
        CompoundTag copy = new CompoundTag();
        copy.data.putAll(this.data);
        return copy;
    }
    
    @Override
    public String toString() {
        return data.toString();
    }
}