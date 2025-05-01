package com.astroframe.galactic.power;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.items.CoreItems;
import com.astroframe.galactic.core.registry.CoreRegistry;
import com.astroframe.galactic.power.items.PowerItems;
import com.astroframe.galactic.power.registry.PowerRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Power module.
 * This module handles all power generation and storage.
 */
@Mod(GalacticPower.MOD_ID)
public class GalacticPower {
    
    /** The mod ID for the power module */
    public static final String MOD_ID = "galacticpower";
    
    /** Logger instance for the power module */
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticPower");
    
    /** Singleton instance of the power mod */
    public static GalacticPower INSTANCE;
    
    /**
     * Constructs a new instance of the Galactic Power mod.
     * This initializes power generation and storage systems.
     */
    public GalacticPower(IEventBus modEventBus) {
        INSTANCE = this;
        
        LOGGER.info("Initializing Galactic Power module");
        
        // Register ourselves for mod events
        modEventBus.register(this);
        
        // Initialize registries
        PowerRegistry.register(modEventBus);
        
        LOGGER.info("Galactic Power module initialized");
    }
    
    /**
     * Event handler for populating the creative tab.
     */
    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        DeferredHolder<CreativeModeTab, CreativeModeTab> tabHolder = CoreRegistry.CREATIVE_MODE_TABS.getHolder(CoreRegistry.GALACTIC_TAB_KEY).get();
        
        if (event.getTab() == tabHolder.get()) {
            // Add Power items
            event.accept(PowerItems.BASIC_GENERATOR.get());
            event.accept(PowerItems.ADVANCED_GENERATOR.get());
            event.accept(PowerItems.SOLAR_PANEL.get());
            event.accept(PowerItems.FUSION_REACTOR.get());
        }
    }
}