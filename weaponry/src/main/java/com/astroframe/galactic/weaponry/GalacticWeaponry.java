package com.astroframe.galactic.weaponry;

import com.astroframe.galactic.weaponry.registry.WeaponryBlocks;
import com.astroframe.galactic.weaponry.registry.WeaponryItems;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

/**
 * Main class for the Galactic Weaponry module.
 * This module provides weapon blocks and items for the Galactic mod.
 */
@Mod("galacticweaponry")
public class GalacticWeaponry {
    public static final String MOD_ID = "galacticweaponry";
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the Galactic Weaponry module.
     * Registers blocks and items.
     */
    public GalacticWeaponry(IEventBus modEventBus) {
        LOGGER.info("Initializing Galactic Weaponry module");
        
        // Register blocks and items
        WeaponryBlocks.register(modEventBus);
        WeaponryItems.register(modEventBus);
        
        LOGGER.info("Galactic Weaponry module initialized");
    }
}