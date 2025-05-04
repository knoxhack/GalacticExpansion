package com.astroframe.galactic.energy;

import com.astroframe.galactic.energy.blocks.EnergyBlocks;
import com.astroframe.galactic.energy.items.EnergyItems;
import com.astroframe.galactic.energy.registry.EnergyRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Energy module of Galactic Expansion.
 * This module handles advanced energy manipulation and special energy types beyond standard power.
 */
@Mod(GalacticEnergy.MOD_ID)
public class GalacticEnergy {
    // Define mod ID
    public static final String MOD_ID = "galacticenergy";
    // Logger for this module
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    /**
     * Constructor for GalacticEnergy module
     *
     * @param eventBus The NeoForge event bus
     */
    public GalacticEnergy(IEventBus eventBus) {
        LOGGER.info("Initializing Galactic Energy Module (Placeholders Only)");
        
        // Initialize the registry
        EnergyRegistry.init(eventBus);
    }
}