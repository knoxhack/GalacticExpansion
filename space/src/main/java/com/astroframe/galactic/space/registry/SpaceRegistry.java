package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import com.astroframe.galactic.space.util.ResourceLocationHelper;
import net.neoforged.bus.api.IEventBus;

/**
 * Centralizes all registry operations for the Space module.
 */
public class SpaceRegistry {
    
    /**
     * Initialize and register all content.
     * @param modEventBus The mod event bus to register content on
     */
    public static void initialize(IEventBus modEventBus) {
        // Register items
        SpaceItems.initialize(modEventBus);
        
        // Register creative tabs
        SpaceCreativeTabs.initialize(modEventBus);
        
        // Register blocks
        SpaceBlocks.initialize(modEventBus);
        
        // Register block entities (currently disabled)
        SpaceBlockEntities.initialize(modEventBus);
    }
    
    /**
     * Helper method to create a namespaced ResourceLocation.
     * @param path The resource path
     * @return A ResourceLocation with the mod's namespace
     */
    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.parse(GalacticSpace.MOD_ID + ":" + path);
    }
}