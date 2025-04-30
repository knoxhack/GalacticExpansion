package com.example.modapi.core;

import com.example.modapi.core.api.ModRegistry;
import com.example.modapi.core.util.ModLogger;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * The main mod class for the ModApi Core module.
 * This serves as the entry point for the core API that other modules will depend on.
 */
@Mod(ModApiCore.MOD_ID)
public class ModApiCore {
    public static final String MOD_ID = "modapi_core";
    public static final ModLogger LOGGER = new ModLogger(MOD_ID);
    
    private static ModApiCore instance;
    private final ModRegistry registry;
    
    /**
     * Constructor for the core mod.
     * Registers event handlers and initializes the registry.
     */
    public ModApiCore(IEventBus modEventBus) {
        instance = this;
        registry = new ModRegistry();
        
        modEventBus.addListener(this::setup);
        
        LOGGER.info("ModApi Core initialized");
    }
    
    /**
     * Common setup method called during mod initialization.
     * 
     * @param event The setup event
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("ModApi Core setup phase");
        // Register capabilities or other common setup tasks
    }
    
    /**
     * Get the singleton instance of the core mod.
     * 
     * @return The mod instance
     */
    public static ModApiCore getInstance() {
        return instance;
    }
    
    /**
     * Get the mod registry for registering blocks, items, etc.
     * 
     * @return The mod registry
     */
    public ModRegistry getRegistry() {
        return registry;
    }
}
