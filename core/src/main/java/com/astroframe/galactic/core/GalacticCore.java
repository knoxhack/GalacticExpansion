package com.astroframe.galactic.core;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(GalacticCore.MOD_ID)
public class GalacticCore {
    public static final String MOD_ID = "galacticcore";
    private static final Logger LOGGER = LoggerFactory.getLogger(GalacticCore.class);

    public GalacticCore() {
        // Register the setup method for mod loading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        
        LOGGER.info("Galactic Core module initialized");
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("Galactic Core setup phase");
    }
}