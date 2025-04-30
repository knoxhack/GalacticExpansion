package com.astroframe.galactic.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages resources for the Galactic Expansion mod.
 * This is a placeholder implementation for demonstration purposes.
 */
public class ResourceManager {
    
    private static final Map<String, Object> resources = new HashMap<>();
    
    /**
     * Register a resource with the resource manager.
     * 
     * @param id The resource ID
     * @param resource The resource object
     */
    public static void register(String id, Object resource) {
        resources.put(id, resource);
        System.out.println("Registered resource: " + id);
    }
    
    /**
     * Get a resource by ID.
     * 
     * @param id The resource ID
     * @return The resource object, or null if not found
     */
    public static Object getResource(String id) {
        return resources.get(id);
    }
    
    /**
     * Check if a resource exists.
     * 
     * @param id The resource ID
     * @return True if the resource exists, false otherwise
     */
    public static boolean hasResource(String id) {
        return resources.containsKey(id);
    }
    
    /**
     * Get the count of registered resources.
     * 
     * @return The number of registered resources
     */
    public static int getResourceCount() {
        return resources.size();
    }
}