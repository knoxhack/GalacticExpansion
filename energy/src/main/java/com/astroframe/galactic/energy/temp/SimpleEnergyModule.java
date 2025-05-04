package com.astroframe.galactic.energy.temp;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simplified placeholder class for the Energy module.
 * All functionality is temporarily disabled to address build issues.
 */
@Mod("galacticenergy")
public class SimpleEnergyModule {
    
    private static final Logger LOGGER = LoggerFactory.getLogger("galacticenergy");
    
    public SimpleEnergyModule(IEventBus eventBus) {
        LOGGER.info("Energy Module disabled temporarily");
    }
}