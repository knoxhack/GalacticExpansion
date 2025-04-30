package com.astroframe.galactic.core.registry.tag;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manages all tags in the Galactic Expansion mod.
 * This is a singleton class that provides access to all type-specific tag collections.
 */
public class TagManager {
    
    private static final TagManager INSTANCE = new TagManager();
    
    private final Map<String, Map<String, Tag<?>>> tagsByType = new HashMap<>();
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private TagManager() {
        // Private constructor to enforce singleton pattern
    }
    
    /**
     * Get the singleton instance of the TagManager.
     * 
     * @return The TagManager instance
     */
    public static TagManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Create a new tag with the specified type and ID.
     * 
     * @param <T> The type of objects the tag will contain
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return The newly created tag
     * @throws IllegalArgumentException if a tag with the same type and ID already exists
     */
    @SuppressWarnings("unchecked")
    public <T> Tag<T> createTag(String typeKey, String id) {
        Map<String, Tag<?>> tags = tagsByType.computeIfAbsent(typeKey, k -> new HashMap<>());
        
        if (tags.containsKey(id)) {
            throw new IllegalArgumentException("Tag with ID '" + id + "' already exists for type '" + typeKey + "'");
        }
        
        Tag<T> tag = new Tag<>(id);
        tags.put(id, tag);
        
        return tag;
    }
    
    /**
     * Get a tag by its type and ID.
     * 
     * @param <T> The expected type of objects in the tag
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return An Optional containing the tag if found, or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<Tag<T>> getTag(String typeKey, String id) {
        Map<String, Tag<?>> tags = tagsByType.get(typeKey);
        
        if (tags == null) {
            return Optional.empty();
        }
        
        return Optional.ofNullable((Tag<T>) tags.get(id));
    }
    
    /**
     * Get or create a tag with the specified type and ID.
     * 
     * @param <T> The type of objects the tag will contain
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return The existing or newly created tag
     */
    @SuppressWarnings("unchecked")
    public <T> Tag<T> getOrCreateTag(String typeKey, String id) {
        return getTag(typeKey, id).orElseGet(() -> (Tag<T>) createTag(typeKey, id));
    }
    
    /**
     * Check if a tag with the specified type and ID exists.
     * 
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return true if a tag with the type and ID exists, false otherwise
     */
    public boolean hasTag(String typeKey, String id) {
        Map<String, Tag<?>> tags = tagsByType.get(typeKey);
        
        if (tags == null) {
            return false;
        }
        
        return tags.containsKey(id);
    }
    
    /**
     * Get all tag IDs for a specific type.
     * 
     * @param typeKey A string key representing the type of objects in the tags
     * @return An unmodifiable set of tag IDs
     */
    public Set<String> getTagIds(String typeKey) {
        Map<String, Tag<?>> tags = tagsByType.get(typeKey);
        
        if (tags == null) {
            return Collections.emptySet();
        }
        
        return Collections.unmodifiableSet(tags.keySet());
    }
    
    /**
     * Get all tags for a specific type.
     * 
     * @param <T> The expected type of objects in the tags
     * @param typeKey A string key representing the type of objects in the tags
     * @return An unmodifiable collection of tags
     */
    @SuppressWarnings("unchecked")
    public <T> Collection<Tag<T>> getTags(String typeKey) {
        Map<String, Tag<?>> tags = tagsByType.get(typeKey);
        
        if (tags == null) {
            return Collections.emptyList();
        }
        
        return Collections.unmodifiableCollection(
                tags.values().stream()
                        .map(tag -> (Tag<T>) tag)
                        .collect(Collectors.toList())
        );
    }
    
    /**
     * Get all types that have tags.
     * 
     * @return An unmodifiable set of type keys
     */
    public Set<String> getTypes() {
        return Collections.unmodifiableSet(tagsByType.keySet());
    }
    
    /**
     * Remove a tag with the specified type and ID.
     * 
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return true if the tag was removed, false if it didn't exist
     */
    public boolean removeTag(String typeKey, String id) {
        Map<String, Tag<?>> tags = tagsByType.get(typeKey);
        
        if (tags == null) {
            return false;
        }
        
        return tags.remove(id) != null;
    }
    
    /**
     * Find all tags of a specific type that match a predicate.
     * 
     * @param <T> The expected type of objects in the tags
     * @param typeKey A string key representing the type of objects in the tags
     * @param predicate The predicate to match tags against
     * @return A collection of matching tags
     */
    @SuppressWarnings("unchecked")
    public <T> Collection<Tag<T>> findTags(String typeKey, Predicate<Tag<T>> predicate) {
        return getTags(typeKey).stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }
    
    /**
     * Clear all tags for a specific type.
     * 
     * @param typeKey A string key representing the type of objects in the tags
     */
    public void clearType(String typeKey) {
        tagsByType.remove(typeKey);
    }
    
    /**
     * Clear all tags.
     * This is primarily for testing purposes and should be used with caution.
     */
    public void clear() {
        tagsByType.clear();
    }
}