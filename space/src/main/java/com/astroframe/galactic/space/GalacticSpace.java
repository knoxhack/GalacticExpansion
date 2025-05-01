package com.astroframe.galactic.space;

import com.astroframe.galactic.core.api.space.SpaceAPI;
import com.astroframe.galactic.space.command.SpaceTravelCommands;
import com.astroframe.galactic.space.implementation.SpaceBodies;
import com.astroframe.galactic.space.implementation.SpaceTravelManager;
import com.astroframe.galactic.space.implementation.component.RocketComponentFactory;
import com.astroframe.galactic.space.item.SpaceItems;
import com.astroframe.galactic.space.registry.SpaceRegistry;
import com.astroframe.galactic.space.resource.SpaceResourceGenerator;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class for the Galactic Space module.
 * Handles rocket systems and space station orbital mechanics.
 * Note: Planetary exploration is handled by the Exploration module.
 */
@Mod(GalacticSpace.MOD_ID)
public class GalacticSpace {
    public static final String MOD_ID = "galactic-space";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    private static SpaceTravelManager spaceTravelManager;
    private static MinecraftServer server;

    /**
     * Initialize the Galactic Space module.
     *
     * @param modEventBus The mod event bus
     */
    public GalacticSpace(IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Space module");
        
        // Register all items
        SpaceItems.register();
        
        // Register all content with the registry system
        SpaceRegistry.initialize(modEventBus);
        
        // Register event listeners
        modEventBus.register(this);
        
        // Register server lifecycle events
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
    }
    
    /**
     * Register commands for the mod.
     * 
     * @param event The register commands event
     */
    private void registerCommands(RegisterCommandsEvent event) {
        LOGGER.info("Registering Galactic Space commands");
        SpaceTravelCommands.register(event.getDispatcher(), event.getBuildContext());
    }
    
    /**
     * Server started event handler.
     * Caches the server instance for later use.
     */
    public void onServerStarted(ServerStartedEvent event) {
        server = event.getServer();
        LOGGER.info("Galactic Space module detected server start");
        
        // Initialize the space travel manager if not already done
        if (spaceTravelManager != null) {
            spaceTravelManager.initialize();
        }
    }
    
    /**
     * Server stopping event handler.
     * Clears the server instance.
     */
    public void onServerStopping(ServerStoppingEvent event) {
        LOGGER.info("Galactic Space module detected server stopping");
        server = null;
    }
    
    /**
     * Common setup event handler.
     * Initializes celestial bodies and other common components.
     */
    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("Setting up Galactic Space module");
            
            // Initialize the space travel manager
            initializeSpaceTravelManager();
            
            // Initialize space bodies
            SpaceBodies.registerAll();
            
            // Initialize space resource generator
            SpaceResourceGenerator.init();
            
            LOGGER.info("Galactic Space module setup complete");
        });
    }
    
    /**
     * Initializes the space travel manager and registers it with the SpaceAPI.
     */
    private void initializeSpaceTravelManager() {
        if (spaceTravelManager == null) {
            LOGGER.info("Initializing Space Travel Manager");
            spaceTravelManager = new SpaceTravelManager();
            // Register with SpaceAPI - but only register if the API is ready
            try {
                SpaceAPI.setSpaceTravelManagerImpl(spaceTravelManager);
                LOGGER.info("Space Travel Manager registered with SpaceAPI");
            } catch (Exception e) {
                LOGGER.error("Failed to register Space Travel Manager with SpaceAPI: {}", e.getMessage());
            }
        }
    }
    
    /**
     * Gets the Minecraft server instance.
     * @return The server instance, or null if not available
     */
    public static MinecraftServer getServer() {
        // Try getting from cache first
        if (server != null) {
            return server;
        }
        
        // Otherwise try to get from ServerLifecycleHooks
        return ServerLifecycleHooks.getCurrentServer();
    }
    
    /**
     * Gets the space travel manager.
     * @return The space travel manager
     */
    public static SpaceTravelManager getSpaceTravelManager() {
        return spaceTravelManager;
    }
}