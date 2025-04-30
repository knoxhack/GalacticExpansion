package com.astroframe.galactic.machinery;

import com.astroframe.galactic.core.api.AbstractModuleIntegration;
import com.astroframe.galactic.core.registry.Registry;
import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.api.MachineType;
import com.astroframe.galactic.machinery.registry.MachineRegistry;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.bus.api.IEventBus;

/**
 * The main class for the Galactic Expansion Machinery module.
 * This provides machinery functionality that builds upon the core and energy modules.
 */
@Mod(GalacticMachinery.MOD_ID)
public class GalacticMachinery extends AbstractModuleIntegration {
    
    /**
     * The mod ID for the machinery module.
     */
    public static final String MOD_ID = "galacticexpansion_machinery";
    
    /**
     * Constructor for the machinery module.
     * Initializes the mod and sets up event listeners.
     */
    public GalacticMachinery() {
        super(MOD_ID, "Galactic Expansion Machinery");
        
        info("Initializing Galactic Expansion Machinery");
        
        // Register event listeners
        net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        
        // Initialize machinery-specific registries
        initializeRegistries();
        
        info("Galactic Expansion Machinery initialized");
    }
    
    @Override
    protected void configureRegistryMappings(RegistryScanner scanner) {
        // Map machinery types to registries
        scanner.mapTypeToRegistry(Machine.class, "machine");
    }
    
    /**
     * Initialize machinery-specific registries.
     */
    private void initializeRegistries() {
        // Create machinery-specific registries
        getRegistryManager().createRegistry("machine");
        
        // Create registries for each machine type
        for (MachineType type : MachineType.values()) {
            getRegistryManager().createRegistry("machine_" + type.getId());
        }
        
        // Create tags for machine components
        for (MachineType type : MachineType.values()) {
            getTagManager().createTag("machine_type", type.getId());
        }
        
        // Create tags for machine capabilities
        getTagManager().createTag("machine_capability", "energy_producer");
        getTagManager().createTag("machine_capability", "energy_consumer");
        getTagManager().createTag("machine_capability", "item_processor");
        getTagManager().createTag("machine_capability", "fluid_processor");
    }
    
    /**
     * Common setup event handler.
     * This is called during mod initialization.
     * 
     * @param event The common setup event
     */
    private void commonSetup(final net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent event) {
        info("Running Galactic Expansion Machinery common setup");
        
        // Register machinery components using annotation-based registration
        registerFromClasses(
            com.astroframe.galactic.machinery.registry.MachineRegistrations.class
        );
        
        // Process machinery tags
        processTagsFromClasses(
            com.astroframe.galactic.machinery.registry.MachineRegistrations.class
        );
        
        // Initialize the MachineRegistry singleton
        com.astroframe.galactic.machinery.registry.MachineRegistrations.initialize();
        
        // Log registry statistics
        logRegistryStatistics();
        
        info("Galactic Expansion Machinery common setup complete");
    }
    
    /**
     * Log statistics about the machinery registries and tags.
     */
    private void logRegistryStatistics() {
        info("Machinery Registry statistics:");
        
        Registry<?> machineRegistry = getRegistry("machine").orElse(null);
        if (machineRegistry != null) {
            info("  Registry 'machine': {} entries", machineRegistry.size());
        }
        
        for (MachineType type : MachineType.values()) {
            String registryName = "machine_" + type.getId();
            Registry<?> typeRegistry = getRegistry(registryName).orElse(null);
            
            if (typeRegistry != null) {
                info("  Registry '{}': {} entries", registryName, typeRegistry.size());
            }
        }
        
        info("Machinery Tag statistics:");
        info("  Machine type tags: {}", getTagManager().getTagIds("machine_type").size());
        info("  Machine capability tags: {}", getTagManager().getTagIds("machine_capability").size());
    }
}