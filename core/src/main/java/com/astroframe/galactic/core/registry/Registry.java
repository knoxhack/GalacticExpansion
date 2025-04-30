package com.astroframe.galactic.core.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A registry for managing game objects in the Galactic Expansion mod.
 * Provides methods for registering, retrieving, and managing objects of a specific type.
 * 
 * @param <T> The type of objects this registry manages
 */
public class Registry<T> {
    
    private final String name;
    private final Map<String, RegistryEntry<T>> entries = new HashMap<>();
    private final Map<String, RegistryEntry<T>> entriesByPath = new HashMap<>();
    
    /**
     * Creates a new registry with the specified name.
     * 
     * @param name The name of this registry
     */
    public Registry(String name) {
        this.name = name;
    }
    
    /**
     * Get the name of this registry.
     * 
     * @return The registry name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Register a new object with the specified domain and path.
     * 
     * @param domain The namespace domain (typically mod id)
     * @param path The registration path/name within the domain
     * @param value The object to register
     * @return The registry entry that was created
     * @throws IllegalArgumentException if an entry with the same ID already exists
     */
    public RegistryEntry<T> register(String domain, String path, T value) {
        String id = domain + ":" + path;
        
        if (entries.containsKey(id)) {
            throw new IllegalArgumentException("Entry with ID '" + id + "' already exists in registry '" + name + "'");
        }
        
        RegistryEntry<T> entry = new RegistryEntry<>(domain, path, value);
        entries.put(id, entry);
        entriesByPath.put(path, entry);
        
        return entry;
    }
    
    /**
     * Get an entry by its full ID (domain:path).
     * 
     * @param id The full ID of the entry
     * @return An Optional containing the entry if found, or empty if not found
     */
    public Optional<RegistryEntry<T>> getEntry(String id) {
        return Optional.ofNullable(entries.get(id));
    }
    
    /**
     * Get an entry by its path, ignoring the domain.
     * 
     * @param path The path of the entry
     * @return An Optional containing the entry if found, or empty if not found
     */
    public Optional<RegistryEntry<T>> getEntryByPath(String path) {
        return Optional.ofNullable(entriesByPath.get(path));
    }
    
    /**
     * Get an object by its full ID (domain:path).
     * 
     * @param id The full ID of the entry
     * @return An Optional containing the object if found, or empty if not found
     */
    public Optional<T> get(String id) {
        return getEntry(id).map(RegistryEntry::getValue);
    }
    
    /**
     * Get an object by its path, ignoring the domain.
     * 
     * @param path The path of the entry
     * @return An Optional containing the object if found, or empty if not found
     */
    public Optional<T> getByPath(String path) {
        return getEntryByPath(path).map(RegistryEntry::getValue);
    }
    
    /**
     * Check if an entry with the specified ID exists.
     * 
     * @param id The full ID to check
     * @return true if an entry with the ID exists, false otherwise
     */
    public boolean contains(String id) {
        return entries.containsKey(id);
    }
    
    /**
     * Check if an entry with the specified path exists.
     * 
     * @param path The path to check
     * @return true if an entry with the path exists, false otherwise
     */
    public boolean containsPath(String path) {
        return entriesByPath.containsKey(path);
    }
    
    /**
     * Get all entries in this registry.
     * 
     * @return An unmodifiable collection of all registry entries
     */
    public Collection<RegistryEntry<T>> getEntries() {
        return Collections.unmodifiableCollection(entries.values());
    }
    
    /**
     * Get all objects in this registry.
     * 
     * @return An unmodifiable collection of all registered objects
     */
    public Collection<T> getValues() {
        return entries.values().stream()
                .map(RegistryEntry::getValue)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all entry IDs in this registry.
     * 
     * @return An unmodifiable set of all entry IDs
     */
    public Set<String> getIds() {
        return Collections.unmodifiableSet(entries.keySet());
    }
    
    /**
     * Get the number of entries in this registry.
     * 
     * @return The entry count
     */
    public int size() {
        return entries.size();
    }
    
    /**
     * Create a new registry with transformed values.
     * 
     * @param <R> The type of objects in the new registry
     * @param transformer The function to transform values from type T to type R
     * @return A new registry with transformed values
     */
    public <R> Registry<R> transform(Function<T, R> transformer) {
        Registry<R> result = new Registry<>(name + "_transformed");
        
        for (RegistryEntry<T> entry : entries.values()) {
            R transformedValue = transformer.apply(entry.getValue());
            result.register(entry.getDomain(), entry.getPath(), transformedValue);
        }
        
        return result;
    }
}