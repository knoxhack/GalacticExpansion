package com.astroframe.galactic.core.registry.tag;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * A tag is a named collection of elements.
 * Used for grouping related objects together for easier access.
 * @param <T> The type of element in the tag
 */
public class Tag<T> {
    private final String id;
    private final Set<T> values = new HashSet<>();
    
    /**
     * Creates a new tag.
     * @param id The tag ID
     */
    public Tag(String id) {
        this.id = id;
    }
    
    /**
     * Gets the tag ID.
     * @return The tag ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Adds a value to the tag.
     * @param value The value to add
     */
    public void add(T value) {
        values.add(value);
    }
    
    /**
     * Removes a value from the tag.
     * @param value The value to remove
     */
    public void remove(T value) {
        values.remove(value);
    }
    
    /**
     * Checks if the tag contains a value.
     * @param value The value to check
     * @return True if the tag contains the value, false otherwise
     */
    public boolean contains(T value) {
        return values.contains(value);
    }
    
    /**
     * Gets all values in the tag.
     * @return A collection of all values
     */
    public Collection<T> getValues() {
        return values;
    }
}