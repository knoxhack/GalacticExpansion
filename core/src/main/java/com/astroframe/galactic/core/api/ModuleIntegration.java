package com.astroframe.galactic.core.api;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import com.astroframe.galactic.core.registry.tag.Tag;
import com.astroframe.galactic.core.registry.tag.TagManager;
import com.astroframe.galactic.core.registry.tag.annotation.TagProcessor;

import java.util.Optional;

/**
 * Interface for module integration with the Galactic Expansion Core.
 * Provides methods for accessing core functionality and registering module-specific objects.
 */
public interface ModuleIntegration {
    
    /**
     * Get the mod ID of the module.
     * 
     * @return The module's mod ID
     */
    String getModId();
    
    /**
     * Get the name of the module.
     * 
     * @return The module's name
     */
    String getModuleName();
    
    /**
     * Get the registry manager instance.
     * 
     * @return The registry manager
     */
    default RegistryManager getRegistryManager() {
        return RegistryManager.getInstance();
    }
    
    /**
     * Get a registry by name.
     * 
     * @param <T> The expected type of objects in the registry
     * @param name The name of the registry
     * @return An Optional containing the registry if found, or empty if not found
     */
    default <T> Optional<Registry<T>> getRegistry(String name) {
        return getRegistryManager().getRegistry(name);
    }
    
    /**
     * Get the tag manager instance.
     * 
     * @return The tag manager
     */
    default TagManager getTagManager() {
        return TagManager.getInstance();
    }
    
    /**
     * Get a tag by type and ID.
     * 
     * @param <T> The expected type of objects in the tag
     * @param typeKey A string key representing the type of objects in the tag
     * @param id The unique identifier for the tag
     * @return An Optional containing the tag if found, or empty if not found
     */
    default <T> Optional<Tag<T>> getTag(String typeKey, String id) {
        return getTagManager().getTag(typeKey, id);
    }
    
    /**
     * Create a registry scanner for this module.
     * 
     * @return A new registry scanner with this module's mod ID as the default domain
     */
    default RegistryScanner createRegistryScanner() {
        return new RegistryScanner(getModId());
    }
    
    /**
     * Register objects from classes using annotations.
     * 
     * @param classes The classes to scan for annotated fields and methods
     * @return The number of objects registered
     */
    default int registerFromClasses(Class<?>... classes) {
        return createRegistryScanner().scanAll(classes);
    }
    
    /**
     * Process tag annotations from classes.
     * 
     * @param classes The classes to process for tag annotations
     * @return The number of objects processed
     */
    default int processTagsFromClasses(Class<?>... classes) {
        return TagProcessor.processAll(classes);
    }
    
    /**
     * Register an object with the specified registry, domain, and path.
     * 
     * @param <T> The type of object to register
     * @param registryName The name of the registry
     * @param path The registration path/name within the domain
     * @param value The object to register
     * @return The registry entry that was created
     */
    default <T> com.astroframe.galactic.core.registry.RegistryEntry<T> register(
            String registryName, String path, T value) {
        
        Registry<T> registry = getRegistry(registryName)
                .orElseThrow(() -> new IllegalArgumentException("Registry '" + registryName + "' not found"));
        
        return registry.register(getModId(), path, value);
    }
    
    /**
     * Add an object to a tag.
     * 
     * @param <T> The type of object to add
     * @param typeKey A string key representing the type of objects in the tag
     * @param tagId The unique identifier for the tag
     * @param value The object to add to the tag
     */
    default <T> void addToTag(String typeKey, String tagId, T value) {
        Tag<T> tag = getTagManager().getOrCreateTag(typeKey, tagId);
        tag.add(value);
    }
    
    /**
     * Get the Galactic Core instance.
     * 
     * @return The Galactic Core instance
     */
    default GalacticCore getCore() {
        try {
            // In a real mod, this would get the instance from ModContainer or similar
            return null; // Placeholder, would be actual instance in real implementation
        } catch (Exception e) {
            throw new IllegalStateException("Failed to get GalacticCore instance", e);
        }
    }
}