package com.astroframe.galactic.core.registry.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages all tags in the mod.
 * This is a simple singleton implementation for testing.
 */
public class TagManager {
    private static TagManager instance;
    
    private final Map<String, Map<String, Tag<?>>> tags = new HashMap<>();
    
    /**
     * Gets the singleton instance.
     * @return The tag manager instance
     */
    public static TagManager getInstance() {
        if (instance == null) {
            instance = new TagManager();
        }
        return instance;
    }
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private TagManager() {
        // Private constructor
    }
    
    /**
     * Gets a tag by type and ID.
     * @param <T> The expected type of objects in the tag
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return An Optional containing the tag if found, or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<Tag<T>> getTag(String typeKey, String id) {
        Map<String, Tag<?>> typeTags = tags.get(typeKey);
        if (typeTags == null) {
            return Optional.empty();
        }
        
        Tag<?> tag = typeTags.get(id);
        if (tag == null) {
            return Optional.empty();
        }
        
        // This cast is potentially unsafe, but is managed by the caller
        return Optional.of((Tag<T>) tag);
    }
    
    /**
     * Gets or creates a tag.
     * @param <T> The type of objects in the tag
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return The tag, either existing or newly created
     */
    @SuppressWarnings("unchecked")
    public <T> Tag<T> getOrCreateTag(String typeKey, String id) {
        Optional<Tag<T>> existingTag = getTag(typeKey, id);
        if (existingTag.isPresent()) {
            return existingTag.get();
        }
        
        // Create a new tag
        Tag<T> newTag = new Tag<>(id);
        
        // Ensure the type map exists
        tags.computeIfAbsent(typeKey, k -> new HashMap<>());
        
        // Store the new tag
        tags.get(typeKey).put(id, newTag);
        
        return newTag;
    }
}