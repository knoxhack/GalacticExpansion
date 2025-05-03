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
        // In NeoForge 1.21.5, we can use the static parse method
        return ResourceLocation.parse(resourceString);
    }
    
    /**
     * Create a ResourceLocation from a namespace and path.
     *
     * @param namespace The namespace
     * @param path The path
     * @return The ResourceLocation
     */
    public static ResourceLocation create(String namespace, String path) {
        return ResourceLocation.parse(namespace + ":" + path);
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