package com.astroframe.galactic.space.implementation.component;

import net.minecraft.resources.ResourceLocation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Helper class for creating ResourceLocation objects.
 * Includes fallback mechanisms for compatibility with different Minecraft/Forge versions.
 */
public class ResourceLocationHelper {
    
    private static final Logger LOGGER = Logger.getLogger(ResourceLocationHelper.class.getName());
    
    /**
     * Creates a ResourceLocation with the given namespace and path.
     * Uses reflection as a fallback if the normal constructor fails.
     * 
     * @param namespace The namespace
     * @param path The path
     * @return A ResourceLocation
     */
    public static ResourceLocation create(String namespace, String path) {
        try {
            // In NeoForge 1.21, ResourceLocation use methods from their parent class
            return ResourceLocation.fromNamespaceAndPath(namespace, path);
        } catch (Exception e1) {
            LOGGER.log(Level.WARNING, "Failed to create ResourceLocation using fromNamespaceAndPath, attempting fallbacks", e1);
            
            // Try using the string-only format "namespace:path" and ResourceLocation.parse
            try {
                return ResourceLocation.parse(namespace + ":" + path);
            } catch (Exception e2) {
                LOGGER.log(Level.WARNING, "Failed to create ResourceLocation using parse method, attempting reflection", e2);
                
                // Last resort: try using reflection to create the ResourceLocation
                return createViaReflection(namespace, path)
                        .orElseThrow(() -> new RuntimeException("Failed to create ResourceLocation by any method"));
            }
        }
    }
    
    /**
     * Validates a path component of a resource location according to Minecraft's rules.
     * 
     * @param path The path to validate
     * @return The validated path (lower case, only allowed characters)
     */
    public static String validatePath(String path) {
        // Minecraft expects paths to be lowercase and only contain a-z, 0-9, _, -, /, and .
        return path.toLowerCase()
                .replaceAll("[^a-z0-9_\\-/.]", "_");
    }
    
    /**
     * Validates a namespace component of a resource location according to Minecraft's rules.
     * 
     * @param namespace The namespace to validate
     * @return The validated namespace (lower case, only allowed characters)
     */
    public static String validateNamespace(String namespace) {
        // Minecraft expects namespaces to be lowercase and only contain a-z, 0-9, _, and -
        return namespace.toLowerCase()
                .replaceAll("[^a-z0-9_\\-]", "_");
    }
    
    /**
     * Attempts to create a ResourceLocation using reflection.
     * This is a fallback method for compatibility with different Minecraft/Forge versions.
     * 
     * @param namespace The namespace
     * @param path The path
     * @return An Optional containing the ResourceLocation, or empty if creation failed
     */
    private static Optional<ResourceLocation> createViaReflection(String namespace, String path) {
        try {
            // Try to get the constructor that takes namespace and path
            Constructor<ResourceLocation> constructor = ResourceLocation.class.getDeclaredConstructor(String.class, String.class);
            constructor.setAccessible(true);
            return Optional.of(constructor.newInstance(namespace, path));
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to create ResourceLocation via constructor reflection", e);
            
            // Try to use the static factory method if it exists (some versions use this approach)
            try {
                Method method = ResourceLocation.class.getDeclaredMethod("of", String.class, String.class);
                method.setAccessible(true);
                return Optional.of((ResourceLocation) method.invoke(null, namespace, path));
            } catch (Exception e2) {
                LOGGER.log(Level.SEVERE, "Failed to create ResourceLocation via method reflection", e2);
                
                // Try other known factory methods
                try {
                    Method method = ResourceLocation.class.getDeclaredMethod("create", String.class, String.class);
                    method.setAccessible(true);
                    return Optional.of((ResourceLocation) method.invoke(null, namespace, path));
                } catch (Exception e3) {
                    LOGGER.log(Level.SEVERE, "Failed to create ResourceLocation via all known methods", e3);
                    return Optional.empty();
                }
            }
        }
    }
    
    /**
     * Gets the string representation of a ResourceLocation, handling null safely.
     * 
     * @param location The ResourceLocation (may be null)
     * @return The string representation, or "null" if the location is null
     */
    public static String toString(ResourceLocation location) {
        return location != null ? location.toString() : "null";
    }
    
    /**
     * Parses a string into a ResourceLocation.
     * 
     * @param str The string to parse (format: "namespace:path")
     * @return A ResourceLocation
     * @throws IllegalArgumentException if the string is invalid
     */
    public static ResourceLocation parse(String str) {
        if (str == null || str.isEmpty()) {
            throw new IllegalArgumentException("Cannot parse empty string as ResourceLocation");
        }
        
        // Use Minecraft's built-in parse method which is available in newer versions
        try {
            return ResourceLocation.parse(str);
        } catch (Exception e) {
            // Fall back to our implementation if needed
            String[] parts = str.split(":", 2);
            if (parts.length == 2) {
                return create(parts[0], parts[1]);
            } else {
                return create("minecraft", parts[0]);
            }
        }
    }
    
    /**
     * Creates a ResourceLocation with the given path, using the mod ID as namespace.
     * This is a convenience method for creating resources in the mod's namespace.
     * 
     * @param path The resource path
     * @return A ResourceLocation
     */
    public static ResourceLocation of(String path) {
        return create("galactic", validatePath(path));
    }
}