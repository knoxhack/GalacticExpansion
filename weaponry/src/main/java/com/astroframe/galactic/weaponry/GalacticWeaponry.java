package com.astroframe.galactic.weaponry;

import com.astroframe.galactic.weaponry.registry.WeaponryBlocks;
import com.astroframe.galactic.weaponry.registry.WeaponryItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the Weaponry module of the Galactic Expansion mod.
 * This module adds various weapons, weapon crafting, and storage.
 */
@Mod(GalacticWeaponry.MOD_ID)
public class GalacticWeaponry {
    public static final String MOD_ID = "galacticweaponry";
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticWeaponry");

    /**
     * Constructor for the Weaponry module.
     *
     * @param eventBus The mod event bus
     */
    public GalacticWeaponry(IEventBus eventBus) {
        LOGGER.info("Initializing Galactic Weaponry Module");
        
        // Register blocks and items
        WeaponryBlocks.register(eventBus);
        WeaponryItems.register(eventBus);
        
        LOGGER.info("Galactic Weaponry Module Initialized");
    }
}