package com.astroframe.galactic.energy;

import com.astroframe.galactic.core.api.AbstractModuleIntegration;
import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import com.astroframe.galactic.core.registry.tag.TagManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.BusBuilder;

/**
 * The main class for the Galactic Expansion Energy module.
 * This provides energy system functionality that builds upon the core module.
 */
@Mod(GalacticEnergy.MOD_ID)
public class GalacticEnergy extends AbstractModuleIntegration {
    
    /**
     * The mod ID for the energy module.
     */
    public static final String MOD_ID = "galacticexpansion_energy";
    
    /**
     * Logger for the energy module.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    /**
     * Constructor for the energy module.
     * Initializes the mod and sets up event listeners.
     */
    public GalacticEnergy() {
        super(MOD_ID, "Galactic Expansion Energy");
        
        LOGGER.info("Initializing Galactic Expansion Energy");
        
        // Register event listeners
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        IEventBus modEventBus = container.getEventBus();
        modEventBus.addListener(this::commonSetup);
        
        // Initialize energy-specific registries
        initializeRegistries();
        
        LOGGER.info("Galactic Expansion Energy initialized");
    }
    
    @Override
    protected void configureRegistryMappings(RegistryScanner scanner) {
        // Map energy types to registries
        // For example: scanner.mapTypeToRegistry(EnergyGenerator.class, "energy_generator");
    }
    
    /**
     * Initialize energy-specific registries.
     */
    private void initializeRegistries() {
        // Create energy-specific registries
        getRegistryManager().createRegistry("energy_source");
        getRegistryManager().createRegistry("energy_consumer");
        getRegistryManager().createRegistry("energy_storage");
        getRegistryManager().createRegistry("energy_cable");
        
        // Create tags for energy components
        TagManager tagManager = getTagManager();
        tagManager.createTag("energy_component", "generators");
        tagManager.createTag("energy_component", "consumers");
        tagManager.createTag("energy_component", "storage");
        tagManager.createTag("energy_component", "cables");
        
        // Create tags for energy types
        tagManager.createTag("energy_type", "electrical");
        tagManager.createTag("energy_type", "steam");
        tagManager.createTag("energy_type", "nuclear");
        tagManager.createTag("energy_type", "solar");
    }
    
    /**
     * Common setup event handler.
     * This is called during mod initialization.
     * 
     * @param event The common setup event
     */
    private void commonSetup(final FMLCommonSetupEvent event) {
        info("Running Galactic Expansion Energy common setup");
        
        // Register energy storages, networks, and other components
        registerFromClasses(
            com.astroframe.galactic.energy.registry.EnergyRegistrations.class
        );
        
        // Process energy tags
        processTagsFromClasses(
            com.astroframe.galactic.energy.registry.EnergyRegistrations.class
        );
        
        // Initialize the EnergyRegistry singleton
        com.astroframe.galactic.energy.registry.EnergyRegistry.getInstance();
        
        // Log registry statistics
        logRegistryStatistics();
        
        info("Galactic Expansion Energy common setup complete");
    }
    
    /**
     * Log statistics about the energy registries and tags.
     */
    private void logRegistryStatistics() {
        info("Energy Registry statistics:");
        
        for (String registryName : new String[]{"energy_source", "energy_consumer", "energy_storage", "energy_cable"}) {
            Registry<?> registry = getRegistry(registryName).orElse(null);
            if (registry != null) {
                info("  Registry '{}': {} entries", registryName, registry.size());
            } else {
                warn("  Registry '{}' not found", registryName);
            }
        }
        
        info("Energy Tag statistics:");
        info("  Energy component tags: {}", getTagManager().getTagIds("energy_component").size());
        info("  Energy type tags: {}", getTagManager().getTagIds("energy_type").size());
    }
}