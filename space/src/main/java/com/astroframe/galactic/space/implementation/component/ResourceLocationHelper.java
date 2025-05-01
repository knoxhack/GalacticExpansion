package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;

/**
 * Helper for creating resource locations with proper namespace.
 */
public class ResourceLocationHelper {
    
    /**
     * Creates a resource location with the mod's namespace.
     * @param path The path of the resource
     * @return The resource location
     */
    public static ResourceLocation of(String path) {
        // Split into namespace and path if it already has a namespace
        if (path.contains(":")) {
            String[] parts = path.split(":", 2);
            return new ResourceLocation(parts[0], parts[1]);
        }
        // Otherwise use our mod's namespace
        return new ResourceLocation(GalacticSpace.MOD_ID, path);
    }
}