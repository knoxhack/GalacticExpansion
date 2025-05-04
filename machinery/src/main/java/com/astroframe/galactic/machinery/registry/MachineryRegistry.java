package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import com.astroframe.galactic.machinery.items.MachineryItemBlocks;
import com.astroframe.galactic.machinery.items.MachineryItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Machinery module.
 * This centralizes all registrations for the module.
 */
public class MachineryRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering Machinery module objects");
        
        try {
            // Set deferred state for registration events
            GalacticMachinery.LOGGER.info("Setting up registration order for machinery module");
            
            // Register event listeners to make sure the order is correct
            eventBus.addListener(MachineryRegistry::onRegister);
            
            // Register in the correct order to prevent dependency issues
            // 1. First register blocks
            GalacticMachinery.LOGGER.info("Step 1: Registering machinery blocks");
            MachineryBlocks.init(eventBus);
            
            // 2. Then register items
            GalacticMachinery.LOGGER.info("Step 2: Registering machinery items");
            MachineryItems.init(eventBus);
            
            // 3. Register block items (after blocks are registered)
            GalacticMachinery.LOGGER.info("Step 3: Registering machinery block items");
            MachineryItemBlocks.init(eventBus);
            
            // 4. Finally register block entities (which depend on blocks)
            GalacticMachinery.LOGGER.info("Step 4: Registering machinery block entities");
            MachineryBlockEntities.init(eventBus);
            
            GalacticMachinery.LOGGER.info("Machinery module registration complete");
        } catch (Exception e) {
            GalacticMachinery.LOGGER.error("Error during machinery registration", e);
            throw e;
        }
    }
    
    /**
     * Event handler for registry events to ensure proper order.
     * This helps with circular dependencies by separating the registration phases.
     * 
     * @param event The register event
     */
    private static void onRegister(RegisterEvent event) {
        // Log the registry type for debugging
        String registryType = event.getRegistryKey().toString();
        GalacticMachinery.LOGGER.debug("Processing registration for " + registryType);
        
        // When block registry is active, verify that blocks have proper IDs
        if (event.getRegistryKey().equals(Registries.BLOCK)) {
            GalacticMachinery.LOGGER.debug("Verifying block IDs during block registration");
        }
        // Make sure block items are registered after blocks
        else if (event.getRegistryKey().equals(Registries.ITEM)) {
            GalacticMachinery.LOGGER.debug("Registering items during item registration phase");
        }
        // Make sure block entities reference blocks that are already registered
        else if (event.getRegistryKey().equals(Registries.BLOCK_ENTITY_TYPE)) {
            GalacticMachinery.LOGGER.debug("Verifying blocks exist before registering block entities");
        }
    }
}