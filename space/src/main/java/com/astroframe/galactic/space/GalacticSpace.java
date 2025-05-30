package com.astroframe.galactic.space;

import com.astroframe.galactic.core.api.space.ISpaceTravelManager;
import com.astroframe.galactic.core.api.space.SpaceAPI;
import com.astroframe.galactic.space.attachment.PlayerSpaceDataRegistry;
import com.astroframe.galactic.space.command.SpaceTravelCommands;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import com.astroframe.galactic.space.implementation.SpaceTravelManager;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import com.astroframe.galactic.space.registry.SpaceBodies;
import com.astroframe.galactic.space.registry.SpaceRegistry;
import com.astroframe.galactic.space.resource.SpaceResourceGenerator;
import com.astroframe.galactic.space.test.AttachmentSystemTester;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
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
    public static final String MOD_ID = "galacticspace";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    
    private static ISpaceTravelManager spaceTravelManager;
    private static MinecraftServer server;
    public static IEventBus MOD_EVENT_BUS;
    private boolean isTickTaskScheduled = false;

    /**
     * Initialize the Galactic Space module.
     *
     * @param modEventBus The mod event bus
     */
    public GalacticSpace(IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Space module");
        
        // Store the mod event bus for static access
        MOD_EVENT_BUS = modEventBus;
        
        // Register all content with the registry system
        SpaceRegistry.initialize(modEventBus);
        
        // Register attachment types
        PlayerSpaceDataRegistry.register();
        
        // Register event listeners
        modEventBus.register(this);
        
        // Register server lifecycle events
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
        NeoForge.EVENT_BUS.addListener(this::onServerStopping);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        NeoForge.EVENT_BUS.addListener(this::onLivingHurt);
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
            
            // Initialize space resource generator
            SpaceResourceGenerator.init();
            
            // Initialize the space travel manager
            initializeSpaceTravelManager();
            
            // Register the attachment system tester
            AttachmentSystemTester.register();
            LOGGER.info("Registered attachment system tester");
            
            LOGGER.info("Galactic Space module setup complete");
        });
    }
    
    /**
     * Initialize the space travel manager and register it with SpaceAPI.
     */
    private void initializeSpaceTravelManager() {
        LOGGER.info("Initializing SpaceTravelManager");
        
        // Create a new instance of our manager
        spaceTravelManager = new SpaceTravelManager();
        
        // Register all celestial bodies with the manager
        SpaceBodies.registerWithManager(spaceTravelManager);
        
        // Set the manager in the API for other mods to access
        SpaceAPI.setSpaceTravelManager(spaceTravelManager);
        
        LOGGER.info("SpaceTravelManager initialized with {} celestial bodies", 
            spaceTravelManager.getAllCelestialBodies().size());
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
    public static ISpaceTravelManager getSpaceTravelManager() {
        return spaceTravelManager;
    }
    
    /**
     * Handles damage events for space suit protection.
     * 
     * @param event The living damage event
     */
    private void onLivingHurt(LivingDamageEvent.Pre event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        // Only protect in space environments
        if (!SpaceStationDimension.isSpaceStation(player.level())) {
            return;
        }
        
        // Protect against various damage types based on suit tier
        int minTier = SpaceSuitItem.getMinimumSuitTier(player);
        
        if (SpaceSuitItem.hasFullSpaceSuit(player)) {
            if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
                // Protect from suffocation with any tier
                event.setNewDamage(0);
                return;
            }
            
            if (minTier >= 2 && event.getSource().is(net.minecraft.world.damagesource.DamageTypes.MAGIC)) {
                // Tier 2+ protects from radiation (magic damage)
                event.setNewDamage(event.getNewDamage() * 0.5f);
            }
            
            if (minTier >= 3) {
                if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.FREEZE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE)) {
                    // Tier 3 protects from temperature extremes
                    event.setNewDamage(0);
                }
            }
        }
    }
    
    /**
     * Server tick event handler directly tied to server lifecycle.
     * This method handles timed updates for space travel and rocket launches.
     *
     * @param event The server started event
     */
    private void onServerStarted(ServerStartedEvent event) {
        server = event.getServer();
        LOGGER.info("Galactic Space module detected server start");
        
        // Schedule a repeating task that runs every tick
        // We check if it's already scheduled to avoid duplicates
        if (!isTickTaskScheduled) {
            LOGGER.info("Scheduling server tick task for Galactic Space");
            
            // Register for Post tick events
            NeoForge.EVENT_BUS.addListener(this::onServerTickPost);
            
            isTickTaskScheduled = true;
        }
    }
    
    /**
     * Server tick event handler - post phase.
     * This is called after each server tick by the NeoForge event system.
     * 
     * @param event The server tick event - post phase
     */
    private void onServerTickPost(ServerTickEvent.Post event) {
        // Placeholder for future implementation
        // Space travel updates will be handled here
    }
}