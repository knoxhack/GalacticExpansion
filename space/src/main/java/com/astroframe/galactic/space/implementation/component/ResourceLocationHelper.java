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
        return ResourceLocation.parse(GalacticSpace.MOD_ID + ":" + path);
    }
}