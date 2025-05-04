package com.astroframe.galactic.vehicles;

import com.astroframe.galactic.vehicles.registry.VehiclesBlocks;
import com.astroframe.galactic.vehicles.registry.VehiclesItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Main class for the Galactic Vehicles module.
 * This module provides vehicle blocks and items for the Galactic mod.
 */
@Mod("galacticvehicles")
public class GalacticVehicles {
    public static final String MOD_ID = "galacticvehicles";
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the Galactic Vehicles module.
     * Registers blocks and items.
     */
    public GalacticVehicles(IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Vehicles module");
        
        // Register blocks and items
        VehiclesBlocks.register(modEventBus);
        VehiclesItems.register(modEventBus);
        
        LOGGER.info("Galactic Vehicles module initialized");
    }
}