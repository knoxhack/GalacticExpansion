package com.astroframe.galactic.power.registry;

import com.astroframe.galactic.power.GalacticPower;
import com.astroframe.galactic.power.blocks.PowerBlocks;
import com.astroframe.galactic.power.items.PowerItems;
import net.neoforged.bus.api.IEventBus;

/**
 * Registry handler for the Power module.
 * Initializes all registries for blocks, items, block entities, etc.
 */
public class PowerRegistry {
    
    /**
     * Initialize all registries for this module
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        GalacticPower.LOGGER.info("Initializing Power module registries");
        
        // Register blocks
        PowerBlocks.init(eventBus);
        
        // Register items
        PowerItems.init(eventBus);
        
        // Register block items after blocks and items are set up
        PowerBlocks.registerBlockItems();
    }
}