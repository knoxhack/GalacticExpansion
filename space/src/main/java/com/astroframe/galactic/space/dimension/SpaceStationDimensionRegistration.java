package com.astroframe.galactic.space.dimension;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

/**
 * Handles registration of the Space Station dimension.
 * 
 * Note: Full dimension registration is handled through JSON configuration files
 * in resources/data/galactic-space/dimension and resource/data/galactic-space/dimension_type
 */
public class SpaceStationDimensionRegistration {

    /**
     * Register dimension-related event handlers during common setup.
     * 
     * @param event The common setup event
     */
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        GalacticSpace.LOGGER.info("Registering Space Station dimension events");
        
        // Register server starting event handler
        NeoForge.EVENT_BUS.addListener(SpaceStationDimensionRegistration::onServerStarting);
    }
    
    /**
     * Verify the space station dimension is properly registered when the server starts.
     * 
     * @param event The server starting event
     */
    private static void onServerStarting(ServerStartingEvent event) {
        var server = event.getServer();
        
        // Log space station dimension registration info
        if (server.getLevel(SpaceStationDimension.SPACE_STATION_LEVEL_KEY) != null) {
            GalacticSpace.LOGGER.info("Space Station dimension successfully registered");
        } else {
            GalacticSpace.LOGGER.error("Space Station dimension not found in registry! Check dimension JSON configs.");
        }
        
        // Log information about available levels
        GalacticSpace.LOGGER.info("Available dimensions:");
        for (Level level : server.getAllLevels()) {
            GalacticSpace.LOGGER.info(" - " + level.dimension().location());
        }
    }
}