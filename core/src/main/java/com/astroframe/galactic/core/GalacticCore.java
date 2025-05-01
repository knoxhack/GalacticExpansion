package com.astroframe.galactic.core;

import com.astroframe.galactic.core.registry.CoreRegistry;
import com.astroframe.galactic.core.api.GalacticRegistry;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Expansion Core module.
 * This module provides the core API and utilities for all other Galactic Expansion modules.
 */
@Mod(GalacticCore.MOD_ID)
public class GalacticCore {
    public static final String MOD_ID = "galacticcore";
    public static final Logger LOGGER = LoggerFactory.getLogger("Galactic Core");

    // Registry manager instance
    private static final GalacticRegistry REGISTRY = new CoreRegistry();

    /**
     * Constructor for the Galactic Core mod.
     * Initializes core components and registers event handlers.
     */
    public GalacticCore() {
        LOGGER.info("Initializing Galactic Core module");
        
        // Get the mod event bus
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);
        
        // Register all deferred registers to the mod event bus
        REGISTRY.registerAllTo(modEventBus);
        
        // Register mod lifecycle events
        modEventBus.addListener(this::setup);
        
        LOGGER.info("Galactic Core initialization complete");
    }
    
    /**
     * Setup method called during mod initialization.
     * 
     * @param event The setup event
     */
    private void setup(final net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event) {
        LOGGER.info("Galactic Core setup phase starting");
        
        // Perform initialization steps that need to happen after registry objects exist
        event.enqueueWork(() -> {
            // Initialize components that depend on registered objects
            LOGGER.info("Registering core capabilities");
            // TODO: Register capabilities and other systems
        });
        
        LOGGER.info("Galactic Core setup phase complete");
    }
    
    /**
     * Get the global registry manager instance
     * 
     * @return The registry manager
     */
    public static GalacticRegistry getRegistry() {
        return REGISTRY;
    }
}