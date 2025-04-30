package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.api.Machine;
import com.astroframe.galactic.machinery.api.MachineType;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles machine registrations for the Galactic Expansion mod.
 * This class contains methods and fields that help register machinery components.
 */
public class MachineRegistrations {
    private static final Logger LOGGER = LoggerFactory.getLogger(MachineRegistrations.class);
    private static MachineRegistry registry;
    
    /**
     * Initializes the machinery registry.
     * This method should be called during mod setup.
     */
    public static void initialize() {
        registry = new MachineRegistry();
        
        // Register machine types
        registry.registerMachineTypes();
        
        LOGGER.info("Machinery registry initialized");
    }
    
    /**
     * Gets the machine registry instance.
     * 
     * @return The machine registry
     */
    public static MachineRegistry getRegistry() {
        if (registry == null) {
            LOGGER.error("Machinery registry accessed before initialization");
            throw new IllegalStateException("Machinery registry not initialized");
        }
        return registry;
    }
}