package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for creating resource locations with proper namespace.
 */
public class ResourceLocationHelper {
    
    /**
     * Creates a resource location with the mod's namespace.
     * For NeoForge 1.21.5, I'm using a fully compatible approach.
     * 
     * @param path The path of the resource
     * @return The resource location
     */
    public static ResourceLocation of(String path) {
        try {
            // Try using reflection to handle different ResourceLocation constructor signatures
            // between Minecraft/Forge/Neoforge versions
            if (path.contains(":")) {
                String[] parts = path.split(":", 2);
                try {
                    // Try the most common constructor (namespace, path)
                    return ResourceLocation.class.getConstructor(String.class, String.class)
                            .newInstance(parts[0], parts[1]);
                } catch (Exception e) {
                    // Fall back to the string constructor if available
                    return ResourceLocation.class.getConstructor(String.class)
                            .newInstance(path);
                }
            } else {
                try {
                    // Try the most common constructor (namespace, path)
                    return ResourceLocation.class.getConstructor(String.class, String.class)
                            .newInstance(GalacticSpace.MOD_ID, path);
                } catch (Exception e) {
                    // Fall back to the string constructor if available
                    return ResourceLocation.class.getConstructor(String.class)
                            .newInstance(GalacticSpace.MOD_ID + ":" + path);
                }
            }
        } catch (Exception ex) {
            // Last resort: try to create a static factory method
            try {
                // Try to use the "of" static method if it exists
                return (ResourceLocation) ResourceLocation.class.getMethod("of", String.class, String.class)
                        .invoke(null, GalacticSpace.MOD_ID, path);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create ResourceLocation", ex);
            }
        }
    }
    
    /**
     * Creates a resource location using namespace and path separately.
     * 
     * @param namespace The namespace
     * @param path The path
     * @return The resource location
     */
    public static ResourceLocation ofNamespaceAndPath(String namespace, String path) {
        try {
            try {
                // Try the most common constructor (namespace, path)
                return ResourceLocation.class.getConstructor(String.class, String.class)
                        .newInstance(namespace, path);
            } catch (Exception e) {
                // Fall back to the string constructor if available
                return ResourceLocation.class.getConstructor(String.class)
                        .newInstance(namespace + ":" + path);
            }
        } catch (Exception ex) {
            // Last resort: try to create a static factory method
            try {
                // Try to use the "of" static method if it exists
                return (ResourceLocation) ResourceLocation.class.getMethod("of", String.class, String.class)
                        .invoke(null, namespace, path);
            } catch (Exception e) {
                throw new RuntimeException("Failed to create ResourceLocation", ex);
            }
        }
    }
}