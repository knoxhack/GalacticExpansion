package com.astroframe.galactic.space;

import com.astroframe.galactic.core.api.space.SpaceAPI;
import com.astroframe.galactic.space.implementation.SpaceBodies;
import com.astroframe.galactic.space.implementation.SpaceTravelManager;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import com.astroframe.galactic.space.registry.SpaceRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class for the Galactic Space module.
 * Handles space travel, planetary exploration, and celestial bodies.
 */
@Mod(GalacticSpace.MOD_ID)
public class GalacticSpace {
    public static final String MOD_ID = "galactic-space";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    private static SpaceTravelManager spaceTravelManager;

    /**
     * Initialize the Galactic Space module.
     *
     * @param modEventBus The mod event bus
     */
    public GalacticSpace(IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Space module");
        
        // Register all content with the registry system
        SpaceRegistry.initialize(modEventBus);
        
        // Register event listeners
        modEventBus.register(this);
    }
    
    /**
     * Common setup event handler.
     * Initializes celestial bodies and other common components.
     */
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Setting up Galactic Space module");
            
            // Register default celestial bodies
            SpaceBodies.registerAll();
            
            // Register rocket components
            LOGGER.info("Registering rocket components");
            RocketComponentFactory.registerAll();
            
            // Initialize and register the space travel manager
            initializeSpaceTravelManager();
        });
    }
    
    /**
     * Initializes the space travel manager and registers it with the SpaceAPI.
     */
    private void initializeSpaceTravelManager() {
        if (spaceTravelManager == null) {
            LOGGER.info("Initializing Space Travel Manager");
            spaceTravelManager = new SpaceTravelManager();
            SpaceAPI.setSpaceTravelManager(spaceTravelManager);
        }
    }
    
    /**
     * Gets the space travel manager.
     * @return The space travel manager
     */
    public static SpaceTravelManager getSpaceTravelManager() {
        return spaceTravelManager;
    }
}