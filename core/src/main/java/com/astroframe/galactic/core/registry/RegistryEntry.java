package com.astroframe.galactic.core.registry;

import java.util.Objects;

/**
 * Represents a registry entry with identifier and associated object.
 * Used for tracking game objects across the Galactic Expansion mod.
 * 
 * @param <T> The type of object being registered
 */
public class RegistryEntry<T> {
    
    private final String id;
    private final String domain;
    private final String path;
    private final T value;
    
    /**
     * Creates a new registry entry with domain, path, and value.
     * 
     * @param domain The namespace domain (typically mod id)
     * @param path The registration path/name within the domain
     * @param value The object being registered
     */
    public RegistryEntry(String domain, String path, T value) {
        this.domain = domain;
        this.path = path;
        this.id = domain + ":" + path;
        this.value = value;
    }
    
    /**
     * Get the full identifier in domain:path format.
     * 
     * @return The full identifier string
     */
    public String getId() {
        return id;
    }
    
    /**
     * Get the domain/namespace (typically mod id).
     * 
     * @return The domain string
     */
    public String getDomain() {
        return domain;
    }
    
    /**
     * Get the path/name within the domain.
     * 
     * @return The path string
     */
    public String getPath() {
        return path;
    }
    
    /**
     * Get the registered object.
     * 
     * @return The registered object
     */
    public T getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RegistryEntry<?> other = (RegistryEntry<?>) obj;
        return Objects.equals(id, other.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "RegistryEntry{id='" + id + "', value=" + value + "}";
    }
}