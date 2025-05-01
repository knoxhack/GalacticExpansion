package com.astroframe.galactic.core;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Core module.
 * This is the central API module that houses all common code and interfaces.
 */
@Mod(GalacticCore.MOD_ID)
public class GalacticCore {
    
    /** The mod ID for the core module */
    public static final String MOD_ID = "galacticcore";
    
    /** Logger instance for the core module */
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticCore");
    
    /** Singleton instance of the core mod */
    public static GalacticCore INSTANCE;
    
    /** The mod's event bus */
    private final IEventBus modEventBus;
    
    /**
     * Constructs a new instance of the Galactic Core mod.
     * This initializes all core APIs and registers event handlers.
     */
    public GalacticCore(IEventBus modEventBus) {
        INSTANCE = this;
        this.modEventBus = modEventBus;
        
        LOGGER.info("Initializing Galactic Core API module");
        
        // Register ourselves for mod events
        modEventBus.register(this);
        
        // Initialize registries
        registerRegistries();
        
        // Register handlers for different APIs
        registerEnergyHandlers();
        registerMachineHandlers();
        
        LOGGER.info("Galactic Core API module initialized");
    }
    
    /**
     * Registers all registries used by the core and modules.
     */
    private void registerRegistries() {
        LOGGER.debug("Registering core registries");
        // Registry setup will go here
    }
    
    /**
     * Registers handlers related to the energy API.
     */
    private void registerEnergyHandlers() {
        LOGGER.debug("Registering energy API handlers");
        // Energy API setup will go here
    }
    
    /**
     * Registers handlers related to the machine API.
     */
    private void registerMachineHandlers() {
        LOGGER.debug("Registering machine API handlers");
        // Machine API setup will go here
    }
    
    /**
     * Gets the mod event bus for registering events.
     * 
     * @return The mod event bus
     */
    public IEventBus getModEventBus() {
        return modEventBus;
    }
    
    /**
     * Gets the mod container for this mod.
     * 
     * @return The mod container
     */
    public ModContainer getModContainer() {
        return ModList.get().getModContainerById(MOD_ID).orElseThrow();
    }
}