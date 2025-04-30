package com.astroframe.galactic.core;

import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.RegistryManager;
import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import com.astroframe.galactic.core.registry.tag.TagManager;
import com.astroframe.galactic.core.registry.tag.annotation.TagProcessor;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Consumer;

/**
 * The main class for the Galactic Expansion Core module.
 * This provides core functionality that other modules can build upon.
 */
@Mod(GalacticCore.MOD_ID)
public class GalacticCore {
    // Static instance for easy access
    private static GalacticCore instance;
    
    /**
     * The mod ID for the core module.
     */
    public static final String MOD_ID = "galacticexpansion";
    
    /**
     * Logger for the core module.
     */
    private static final Logger LOGGER = LogManager.getLogger();
    
    /**
     * Registry scanner instance for automatic registration.
     */
    private final RegistryScanner registryScanner;
    
    /**
     * Constructor for the core module.
     * Initializes the mod and sets up event listeners.
     */
    public GalacticCore(final IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Expansion Core");
        
        // Store the instance for external access
        instance = this;
        
        // Create the registry scanner with the mod ID as the default domain
        registryScanner = new RegistryScanner(MOD_ID);
        
        // Register event listeners
        modEventBus.addListener(this::commonSetup);
        
        // Initialize standard registries
        initializeRegistries();
        
        LOGGER.info("Galactic Expansion Core initialized");
    }
    
    /**
     * Get the singleton instance of the core mod.
     * 
     * @return The singleton instance
     */
    public static GalacticCore getInstance() {
        return instance;
    }
    
    /**
     * Initialize standard registries that will be used across modules.
     */
    private void initializeRegistries() {
        RegistryManager manager = RegistryManager.getInstance();
        
        // Create common registries
        manager.createRegistry("block");
        manager.createRegistry("item");
        manager.createRegistry("tile_entity");
        manager.createRegistry("container");
        manager.createRegistry("recipe");
        
        // Map common types to registries
        // These will be used when the registry name is not specified in annotations
        // This would normally map Minecraft/Forge classes to registry names
        // For example: registryScanner.mapTypeToRegistry(Block.class, "block");
    }
    
    /**
     * Common setup event handler.
     * This is called during mod initialization.
     * 
     * @param event The common setup event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("Running Galactic Expansion Core common setup");
        
        // Process registry entries and tags
        // This would normally scan registry classes for annotated fields and methods
        // For example: registryScanner.scan(CoreBlocks.class);
        
        // Log registry statistics
        logRegistryStatistics();
        
        LOGGER.info("Galactic Expansion Core common setup complete");
    }
    
    /**
     * Log statistics about the registries and tags.
     */
    private void logRegistryStatistics() {
        RegistryManager registryManager = RegistryManager.getInstance();
        TagManager tagManager = TagManager.getInstance();
        
        LOGGER.info("Registry statistics:");
        LOGGER.info("  Registry count: " + registryManager.size());
        
        for (String registryName : registryManager.getRegistryNames()) {
            Registry<?> registry = registryManager.getRegistry(registryName).orElseThrow();
            LOGGER.info("  Registry '" + registryName + "': " + registry.size() + " entries");
        }
        
        LOGGER.info("Tag statistics:");
        LOGGER.info("  Tag type count: " + tagManager.getTypes().size());
        
        for (String typeKey : tagManager.getTypes()) {
            LOGGER.info("  Tags for type '" + typeKey + "': " + tagManager.getTagIds(typeKey).size());
        }
    }
    
    /**
     * Get the registry scanner instance.
     * This can be used by other modules to register their objects.
     * 
     * @return The registry scanner
     */
    public RegistryScanner getRegistryScanner() {
        return registryScanner;
    }
}