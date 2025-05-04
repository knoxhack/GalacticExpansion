package com.astroframe.galactic.construction;

import com.astroframe.galactic.construction.registry.ConstructionBlocks;
import com.astroframe.galactic.construction.registry.ConstructionItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

/**
 * The main class for the Construction module.
 * Contains registry initialization and event setup.
 */
@Mod(GalacticConstruction.MOD_ID)
public class GalacticConstruction {
    
    /**
     * The mod ID for the Construction module.
     */
    public static final String MOD_ID = "galacticconstruction";
    
    /**
     * Constructor for the Construction module.
     * 
     * @param eventBus The event bus to register events with
     */
    public GalacticConstruction(IEventBus eventBus) {
        // Initialize all registries
        ConstructionBlocks.initialize(eventBus);
        ConstructionItems.initialize(eventBus);
    }
}