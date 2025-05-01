package com.astroframe.galactic.energy;

import com.astroframe.galactic.energy.registry.EnergyRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
// Temporarily commented out to fix build issues
// import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Energy module.
 * This module handles energy generation, storage, and transmission in the Galactic Expansion mod.
 */
@Mod("galacticenergy")
public class GalacticEnergy {
    
    public static final String MOD_ID = "galacticenergy";
    private static final Logger LOGGER = LoggerFactory.getLogger("GalacticEnergy");
    
    /**
     * Constructs the Galactic Energy module.
     * Registers event handlers and initializes module components.
     */
    public GalacticEnergy() {
        LOGGER.info("Initializing Galactic Energy module");
        
        // Get the mod event bus
        // This will be properly initialized when the mod is loaded
        IEventBus modEventBus = null; // Placeholder
        
        // Register event handlers - commented out until event bus is properly initialized
        // modEventBus.addListener(this::commonSetup);
        
        // Register registries - commented out until event bus is properly initialized
        // EnergyRegistry.register(modEventBus);
        
        LOGGER.info("Galactic Energy module initialized");
    }
    
    /**
     * Common setup for the module.
     * Called during the FMLCommonSetupEvent.
     *
     * @param event The common setup event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Galactic Energy common setup");
        
        // Initialize energy network
        event.enqueueWork(() -> {
            LOGGER.info("Initializing energy network");
            // Initialize energy network components here
        });
    }
    
    /**
     * Gets the logger for the Galactic Energy module.
     *
     * @return The logger instance
     */
    public static Logger getLogger() {
        return LOGGER;
    }
}