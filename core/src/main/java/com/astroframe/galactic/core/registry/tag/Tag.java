package com.astroframe.galactic.core.registry.tag;

import com.astroframe.galactic.core.registry.RegistryEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Represents a tag that can be used to categorize and group related objects.
 * Tags are similar to Minecraft's tag system but with additional functionality.
 * 
 * @param <T> The type of objects this tag contains
 */
public class Tag<T> {
    
    private final String id;
    private final Set<T> values = new HashSet<>();
    private final Set<RegistryEntry<T>> entries = new HashSet<>();
    private final Set<Tag<T>> children = new HashSet<>();
    
    /**
     * Creates a new tag with the specified ID.
     * 
     * @param id The unique identifier for this tag
     */
    public Tag(String id) {
        this.id = id;
    }
    
    /**
     * Get the ID of this tag.
     * 
     * @return The tag ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Add a value to this tag.
     * 
     * @param value The value to add
     * @return This tag for method chaining
     */
    public Tag<T> add(T value) {
        values.add(value);
        return this;
    }
    
    /**
     * Add all values from a collection to this tag.
     * 
     * @param collection The collection of values to add
     * @return This tag for method chaining
     */
    public Tag<T> addAll(Collection<T> collection) {
        values.addAll(collection);
        return this;
    }
    
    /**
     * Add a registry entry to this tag.
     * This will add both the entry and its value.
     * 
     * @param entry The registry entry to add
     * @return This tag for method chaining
     */
    public Tag<T> add(RegistryEntry<T> entry) {
        entries.add(entry);
        values.add(entry.getValue());
        return this;
    }
    
    /**
     * Add all entries from a collection to this tag.
     * 
     * @param collection The collection of entries to add
     * @return This tag for method chaining
     */
    public Tag<T> addAllEntries(Collection<RegistryEntry<T>> collection) {
        for (RegistryEntry<T> entry : collection) {
            add(entry);
        }
        return this;
    }
    
    /**
     * Add another tag as a child of this tag.
     * This creates a hierarchical relationship where this tag contains all values
     * from its children.
     * 
     * @param child The child tag to add
     * @return This tag for method chaining
     */
    public Tag<T> addChild(Tag<T> child) {
        children.add(child);
        return this;
    }
    
    /**
     * Get all values in this tag, including those from child tags.
     * 
     * @return An unmodifiable set of all values
     */
    public Set<T> getValues() {
        Set<T> result = new HashSet<>(values);
        
        for (Tag<T> child : children) {
            result.addAll(child.getValues());
        }
        
        return Collections.unmodifiableSet(result);
    }
    
    /**
     * Get all entries in this tag, including those from child tags.
     * 
     * @return An unmodifiable set of all entries
     */
    public Set<RegistryEntry<T>> getEntries() {
        Set<RegistryEntry<T>> result = new HashSet<>(entries);
        
        for (Tag<T> child : children) {
            result.addAll(child.getEntries());
        }
        
        return Collections.unmodifiableSet(result);
    }
    
    /**
     * Get all child tags of this tag.
     * 
     * @return An unmodifiable set of all child tags
     */
    public Set<Tag<T>> getChildren() {
        return Collections.unmodifiableSet(children);
    }
    
    /**
     * Check if this tag contains a specific value.
     * This will also check child tags.
     * 
     * @param value The value to check
     * @return true if the value is in this tag or any child tag
     */
    public boolean contains(T value) {
        if (values.contains(value)) {
            return true;
        }
        
        for (Tag<T> child : children) {
            if (child.contains(value)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Check if this tag contains a specific entry.
     * This will also check child tags.
     * 
     * @param entry The entry to check
     * @return true if the entry is in this tag or any child tag
     */
    public boolean contains(RegistryEntry<T> entry) {
        if (entries.contains(entry)) {
            return true;
        }
        
        for (Tag<T> child : children) {
            if (child.contains(entry)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Filter values in this tag using a predicate.
     * 
     * @param predicate The predicate to filter with
     * @return A new tag containing only values that match the predicate
     */
    public Tag<T> filter(Predicate<T> predicate) {
        Tag<T> result = new Tag<>(id + "_filtered");
        
        getValues().stream()
                .filter(predicate)
                .forEach(result::add);
        
        return result;
    }
    
    /**
     * Create a new tag that is the intersection of this tag and another.
     * 
     * @param other The other tag to intersect with
     * @return A new tag containing only values that are in both tags
     */
    public Tag<T> intersect(Tag<T> other) {
        Tag<T> result = new Tag<>(id + "_intersect_" + other.getId());
        
        Set<T> thisValues = getValues();
        Set<T> otherValues = other.getValues();
        
        thisValues.stream()
                .filter(otherValues::contains)
                .forEach(result::add);
        
        return result;
    }
    
    /**
     * Create a new tag that is the union of this tag and another.
     * 
     * @param other The other tag to union with
     * @return A new tag containing values from both tags
     */
    public Tag<T> union(Tag<T> other) {
        Tag<T> result = new Tag<>(id + "_union_" + other.getId());
        
        result.addAll(getValues());
        result.addAll(other.getValues());
        
        return result;
    }
    
    /**
     * Create a new tag that contains values in this tag but not in another.
     * 
     * @param other The other tag to subtract
     * @return A new tag containing values in this tag but not in the other tag
     */
    public Tag<T> difference(Tag<T> other) {
        Tag<T> result = new Tag<>(id + "_diff_" + other.getId());
        
        Set<T> otherValues = other.getValues();
        
        getValues().stream()
                .filter(value -> !otherValues.contains(value))
                .forEach(result::add);
        
        return result;
    }
    
    /**
     * Get the number of values in this tag, including those from child tags.
     * 
     * @return The value count
     */
    public int size() {
        return getValues().size();
    }
    
    /**
     * Check if this tag is empty.
     * 
     * @return true if this tag has no values or child tags with values
     */
    public boolean isEmpty() {
        return values.isEmpty() && children.stream().allMatch(Tag::isEmpty);
    }
    
    /**
     * Remove a value from this tag.
     * This only removes from the direct values, not from children.
     * 
     * @param value The value to remove
     * @return true if the value was removed, false if it wasn't in the tag
     */
    public boolean remove(T value) {
        return values.remove(value);
    }
    
    @Override
    public String toString() {
        return "Tag{id='" + id + "', size=" + size() + "}";
    }
}