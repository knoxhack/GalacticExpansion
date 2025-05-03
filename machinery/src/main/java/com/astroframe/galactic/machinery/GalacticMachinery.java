package com.astroframe.galactic.machinery;

import com.astroframe.galactic.core.registry.CoreRegistry;
import com.astroframe.galactic.machinery.items.MachineryItems;
import com.astroframe.galactic.machinery.registry.MachineryRegistry;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Machinery module.
 * This module handles all industrial machines and automation.
 */
@Mod(GalacticMachinery.MOD_ID)
public class GalacticMachinery {
    
    /** The mod ID for the machinery module */
    public static final String MOD_ID = "galacticmachinery";
    
    /** Logger instance for the machinery module */
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticMachinery");
    
    /** Singleton instance of the machinery mod */
    public static GalacticMachinery INSTANCE;
    
    /** The mod's event bus - static access for NeoForge 1.21.5 registration */
    public static IEventBus MOD_EVENT_BUS;
    
    /**
     * Constructs a new instance of the Galactic Machinery mod.
     * This initializes industrial machines and automation systems.
     */
    public GalacticMachinery(IEventBus modEventBus) {
        INSTANCE = this;
        MOD_EVENT_BUS = modEventBus; // Store static reference for module registration
        
        LOGGER.info("Initializing Galactic Machinery module");
        
        // Register ourselves for mod events
        modEventBus.register(this);
        
        // Initialize registries
        MachineryRegistry.register(modEventBus);
        
        LOGGER.info("Galactic Machinery module initialized");
    }
    
    /**
     * Event handler for populating the creative tab.
     */
    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Check if this is our tab that's being built
        if (event.getTabKey() == CoreRegistry.GALACTIC_TAB_KEY) {
            // Add Machinery items
            event.accept(MachineryItems.CRUSHER.get());
            event.accept(MachineryItems.SMELTER.get());
            event.accept(MachineryItems.EXTRACTOR.get());
            event.accept(MachineryItems.CENTRIFUGE.get());
            event.accept(MachineryItems.ASSEMBLER.get());
        }
    }
}