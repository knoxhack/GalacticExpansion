package com.astroframe.galactic.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages all registries in the mod.
 * This is a simple singleton implementation for testing.
 */
public class RegistryManager {
    private static RegistryManager instance;
    
    private final Map<String, Registry<?>> registries = new HashMap<>();
    
    /**
     * Gets the singleton instance.
     * @return The registry manager instance
     */
    public static RegistryManager getInstance() {
        if (instance == null) {
            instance = new RegistryManager();
        }
        return instance;
    }
    
    /**
     * Private constructor to enforce singleton pattern.
     */
    private RegistryManager() {
        // Private constructor
    }
    
    /**
     * Registers a new registry.
     * @param <T> The type of objects in the registry
     * @param registry The registry to register
     */
    public <T> void registerRegistry(Registry<T> registry) {
        registries.put(registry.getName(), registry);
    }
    
    /**
     * Gets a registry by name.
     * @param <T> The expected type of objects in the registry
     * @param name The name of the registry
     * @return An Optional containing the registry if found, or empty if not found
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<Registry<T>> getRegistry(String name) {
        Registry<?> registry = registries.get(name);
        if (registry == null) {
            return Optional.empty();
        }
        // This cast is potentially unsafe, but is managed by the caller
        return Optional.of((Registry<T>) registry);
    }
}