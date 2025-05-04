package com.astroframe.galactic.utilities;

import com.astroframe.galactic.utilities.registry.UtilitiesBlocks;
import com.astroframe.galactic.utilities.registry.UtilitiesItems;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Main class for the Galactic Utilities module.
 * This module provides utility blocks and items for the Galactic mod.
 */
@Mod("galacticutilities")
public class GalacticUtilities {
    public static final String MOD_ID = "galacticutilities";
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructor for the Galactic Utilities module.
     * Registers blocks and items.
     */
    public GalacticUtilities() {
        LOGGER.info("Initializing Galactic Utilities module");
        
        // Register blocks and items
        UtilitiesBlocks.register(FMLJavaModLoadingContext.get().getModEventBus());
        UtilitiesItems.register(FMLJavaModLoadingContext.get().getModEventBus());
        
        LOGGER.info("Galactic Utilities module initialized");
    }
}