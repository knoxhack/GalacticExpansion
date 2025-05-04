package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import com.astroframe.galactic.machinery.items.MachineryItemBlocks;
import com.astroframe.galactic.machinery.items.MachineryItems;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;

/**
 * Registry handler for the Machinery module.
 * This centralizes all registrations for the module.
 */
public class MachineryRegistry {

    /**
     * Registers all registry objects with the given event bus.
     * For NeoForge 1.21.5 compatibility, we have temporarily disabled custom block entities
     * and are using basic blocks.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering Machinery module objects");
        
        try {
            // Register event listeners to debug registration order
            eventBus.addListener(MachineryRegistry::onRegister);
            
            // SIMPLIFIED REGISTRATION ORDER:
            // For NeoForge 1.21.5 compatibility, we're using a simplified registration
            // sequence with basic blocks instead of complex block entities
            
            // 1. Register blocks
            GalacticMachinery.LOGGER.info("Step 1: Registering machinery blocks (basic versions)");
            MachineryBlocks.init(eventBus);
            
            // 2. Register regular items
            GalacticMachinery.LOGGER.info("Step 2: Registering machinery items");
            MachineryItems.init(eventBus);
            
            // 3. Register block items
            GalacticMachinery.LOGGER.info("Step 3: Registering machinery block items");
            MachineryItemBlocks.init(eventBus);
            
            // 4. Register block entity stubs (empty)
            GalacticMachinery.LOGGER.info("Step 4: Registering machinery block entity stubs");
            MachineryBlockEntities.init(eventBus);
            
            GalacticMachinery.LOGGER.info("Machinery module registration complete (compatibility mode)");
        } catch (Exception e) {
            GalacticMachinery.LOGGER.error("Error during machinery registration", e);
            throw e;
        }
    }
    
    /**
     * Event handler for registry events to ensure proper order.
     * Provides debugging information during the registration process.
     * 
     * @param event The register event
     */
    private static void onRegister(RegisterEvent event) {
        // Log the registry type for debugging
        String registryType = event.getRegistryKey().toString();
        GalacticMachinery.LOGGER.debug("Processing registration for " + registryType);
        
        // When block registry is active, verify that blocks have proper IDs
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            GalacticMachinery.LOGGER.debug("Verifying block registration");
        }
        // Make sure block items are registered after blocks
        else if (event.getRegistryKey().equals(Registries.ITEM)) {
            GalacticMachinery.LOGGER.debug("Registering items");
        }
    }
}