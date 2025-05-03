package com.astroframe.galactic.space;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main mod class for the Space module
 */
@Mod("galacticspace")
public class GalacticSpaceMod {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("GalacticSpace");
    
    public GalacticSpaceMod() {
        LOGGER.info("Galactic Space module initializing");
        
        // Don't register the mod class as an event handler if it doesn't have @SubscribeEvent methods
        // NeoForge.EVENT_BUS.register(this);  // This was causing the error
        
        LOGGER.info("Galactic Space module initialized");
    }
}