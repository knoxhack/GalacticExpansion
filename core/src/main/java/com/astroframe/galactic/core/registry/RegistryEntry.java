package com.astroframe.galactic.core.registry;

/**
 * A registry entry represents a registered object with its metadata.
 * @param <T> The type of object
 */
public class RegistryEntry<T> {
    private final String namespace;
    private final String key;
    private final String fullKey;
    private final T value;
    
    /**
     * Creates a new registry entry.
     * @param namespace The namespace (usually mod ID)
     * @param key The object key
     * @param value The registered object
     */
    public RegistryEntry(String namespace, String key, T value) {
        this.namespace = namespace;
        this.key = key;
        this.fullKey = namespace + ":" + key;
        this.value = value;
    }
    
    /**
     * Gets the namespace.
     * @return The namespace
     */
    public String getNamespace() {
        return namespace;
    }
    
    /**
     * Gets the key.
     * @return The key
     */
    public String getKey() {
        return key;
    }
    
    /**
     * Gets the full key (namespace:key).
     * @return The full key
     */
    public String getFullKey() {
        return fullKey;
    }
    
    /**
     * Gets the registered object.
     * @return The registered object
     */
    public T getValue() {
        return value;
    }
}