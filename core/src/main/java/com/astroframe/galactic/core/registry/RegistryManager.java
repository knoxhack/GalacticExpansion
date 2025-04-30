package com.astroframe.galactic.core.registry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Manages all registries in the Galactic Expansion mod.
 * This is a singleton class that provides access to all type-specific registries.
 */
public class RegistryManager {
    
    private static final RegistryManager INSTANCE = new RegistryManager();
    
    private final Map<String, Registry<?>> registries = new HashMap<>();
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private RegistryManager() {
        // Private constructor to enforce singleton pattern
    }
    
    /**
     * Get the singleton instance of the RegistryManager.
     * 
     * @return The RegistryManager instance
     */
    public static RegistryManager getInstance() {
        return INSTANCE;
    }
    
    /**
     * Create and register a new registry with the specified name and type.
     * 
     * @param <T> The type of objects the registry will manage
     * @param name The name of the registry
     * @return The newly created registry
     * @throws IllegalArgumentException if a registry with the same name already exists
     */
    public <T> Registry<T> createRegistry(String name) {
        if (registries.containsKey(name)) {
            throw new IllegalArgumentException("Registry with name '" + name + "' already exists");
        }
        
        Registry<T> registry = new Registry<>(name);
        registries.put(name, registry);
        
        return registry;
    }
    
    /**
     * Get a registry by name.
     * 
     * @param <T> The expected type of objects in the registry
     * @param name The name of the registry
     * @return An Optional containing the registry if found, or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<Registry<T>> getRegistry(String name) {
        return Optional.ofNullable((Registry<T>) registries.get(name));
    }
    
    /**
     * Check if a registry with the specified name exists.
     * 
     * @param name The registry name to check
     * @return true if a registry with the name exists, false otherwise
     */
    public boolean hasRegistry(String name) {
        return registries.containsKey(name);
    }
    
    /**
     * Get the names of all registered registries.
     * 
     * @return An unmodifiable set of registry names
     */
    public Set<String> getRegistryNames() {
        return Collections.unmodifiableSet(registries.keySet());
    }
    
    /**
     * Get the number of registries.
     * 
     * @return The registry count
     */
    public int size() {
        return registries.size();
    }
    
    /**
     * Clear all registries.
     * This is primarily for testing purposes and should be used with caution.
     */
    public void clear() {
        registries.clear();
    }
}