package com.astroframe.galactic.space.util;

import net.minecraft.resources.ResourceLocation;

/**
 * Helper methods for working with ResourceLocation objects.
 * Provides compatibility with different Minecraft and modding API versions.
 */
public class ResourceLocationHelper {

    /**
     * Parse a string into a ResourceLocation.
     * Handles differences between Minecraft versions where in some versions,
     * the ResourceLocation constructor takes a single string parameter,
     * and in others requires separate namespace and path parameters.
     *
     * @param resourceString The resource string to parse (e.g., "minecraft:stone" or "stone")
     * @return The ResourceLocation
     */
    public static ResourceLocation parse(String resourceString) {
        // Split the string into namespace and path
        String namespace;
        String path;
        
        int colonIndex = resourceString.indexOf(':');
        if (colonIndex >= 0) {
            // String contains a namespace
            namespace = resourceString.substring(0, colonIndex);
            path = resourceString.substring(colonIndex + 1);
        } else {
            // No namespace specified, use minecraft as default
            namespace = "minecraft";
            path = resourceString;
        }
        
        // In NeoForge 1.21.5, we need to use the two-argument constructor
        return new ResourceLocation(namespace, path);
    }
    
    /**
     * Gets the namespace from a ResourceLocation.
     *
     * @param location The ResourceLocation
     * @return The namespace
     */
    public static String getNamespace(ResourceLocation location) {
        return location.getNamespace();
    }
    
    /**
     * Gets the path from a ResourceLocation.
     *
     * @param location The ResourceLocation
     * @return The path
     */
    public static String getPath(ResourceLocation location) {
        return location.getPath();
    }
    
    /**
     * Returns the string representation of a ResourceLocation.
     *
     * @param location The ResourceLocation
     * @return The string representation
     */
    public static String toString(ResourceLocation location) {
        return location.toString();
    }
}