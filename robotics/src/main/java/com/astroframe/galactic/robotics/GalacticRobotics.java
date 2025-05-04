package com.astroframe.galactic.robotics;

import com.astroframe.galactic.robotics.registry.RoboticsBlocks;
import com.astroframe.galactic.robotics.registry.RoboticsItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Robotics module of the Galactic Expansion mod.
 * This module adds various robots, drones, and related components.
 */
@Mod(GalacticRobotics.MOD_ID)
public class GalacticRobotics {
    public static final String MOD_ID = "galacticrobotics";
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticRobotics");

    /**
     * Constructor for the Robotics module.
     *
     * @param eventBus The mod event bus
     */
    public GalacticRobotics(IEventBus eventBus) {
        LOGGER.info("Initializing Galactic Robotics Module");
        
        // Register blocks and items
        RoboticsBlocks.register(eventBus);
        RoboticsItems.register(eventBus);
        
        LOGGER.info("Galactic Robotics Module Initialized");
    }
}