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
        
        // Register standalone items - we're using simplified approach without BlockItems
        PowerItems.init(eventBus);
    }
}