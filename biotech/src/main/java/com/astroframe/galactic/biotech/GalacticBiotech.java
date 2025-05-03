package com.astroframe.galactic.biotech;

import com.astroframe.galactic.biotech.items.BiotechItems;
import com.astroframe.galactic.biotech.registry.BiotechRegistry;
import com.astroframe.galactic.core.registry.CoreRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The main class for the Galactic Biotech module.
 * This module handles all biological technologies and genetic engineering.
 */
@Mod(GalacticBiotech.MOD_ID)
public class GalacticBiotech {
    
    /** The mod ID for the biotech module */
    public static final String MOD_ID = "galacticbiotech";
    
    /** Logger instance for the biotech module */
    public static final Logger LOGGER = LoggerFactory.getLogger("GalacticBiotech");
    
    /** Singleton instance of the biotech mod */
    public static GalacticBiotech INSTANCE;
    
    /** The mod's event bus - static access for NeoForge 1.21.5 registration */
    public static IEventBus MOD_EVENT_BUS;
    
    /**
     * Constructs a new instance of the Galactic Biotech mod.
     * This initializes biological technologies and genetic engineering systems.
     */
    public GalacticBiotech(IEventBus modEventBus) {
        INSTANCE = this;
        MOD_EVENT_BUS = modEventBus; // Store static reference for module registration
        
        LOGGER.info("Initializing Galactic Biotech module");
        
        // Register ourselves for mod events
        modEventBus.register(this);
        
        // Initialize registries
        BiotechRegistry.register(modEventBus);
        
        LOGGER.info("Galactic Biotech module initialized");
    }
    
    /**
     * Event handler for populating the creative tab.
     */
    @SubscribeEvent
    public void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Check if this is our tab that's being built
        if (event.getTabKey() == CoreRegistry.GALACTIC_TAB_KEY) {
            // Add Biotech items
            event.accept(BiotechItems.GENE_SPLICER.get());
            event.accept(BiotechItems.DNA_SAMPLER.get());
            event.accept(BiotechItems.GROWTH_SERUM.get());
            event.accept(BiotechItems.MUTATION_CATALYST.get());
            event.accept(BiotechItems.GENETIC_STABILIZER.get());
        }
    }
}