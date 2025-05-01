package com.astroframe.galactic.core.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A registry for storing and retrieving objects by ID.
 * This is a simplified version of Minecraft's Registry system for testing.
 * @param <T> The type of object to store
 */
public class Registry<T> {
    private final String name;
    private final Map<String, T> entries = new HashMap<>();
    
    /**
     * Creates a new registry.
     * @param name The name of the registry
     */
    public Registry(String name) {
        this.name = name;
    }
    
    /**
     * Registers an object with the registry.
     * @param namespace The namespace (usually mod ID)
     * @param key The object key
     * @param value The object to register
     * @return The registry entry that was created
     */
    public RegistryEntry<T> register(String namespace, String key, T value) {
        String fullKey = namespace + ":" + key;
        entries.put(fullKey, value);
        return new RegistryEntry<>(namespace, key, value);
    }
    
    /**
     * Gets an object from the registry.
     * @param key The full key (namespace:key)
     * @return An optional containing the object, or empty if not found
     */
    public Optional<T> get(String key) {
        return Optional.ofNullable(entries.get(key));
    }
    
    /**
     * Checks if the registry contains a key.
     * @param key The full key (namespace:key)
     * @return True if the registry contains the key, false otherwise
     */
    public boolean contains(String key) {
        return entries.containsKey(key);
    }
    
    /**
     * Gets all registered values.
     * @return A collection of all registered values
     */
    public Collection<T> getValues() {
        return entries.values();
    }
    
    /**
     * Gets the name of this registry.
     * @return The registry name
     */
    public String getName() {
        return name;
    }
}