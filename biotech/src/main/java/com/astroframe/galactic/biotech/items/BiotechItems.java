package com.astroframe.galactic.biotech.items;

import com.astroframe.galactic.biotech.GalacticBiotech;
import com.astroframe.galactic.biotech.registry.BiotechRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all items in the Biotech module.
 */
public class BiotechItems {

    /**
     * Define a Items DeferredRegister with the registerItem helper method
     * This properly sets the item ID in the Item.Properties as required in NeoForge 1.21.5+
     */
    private static final DeferredRegister.Items ITEMS_HELPER = DeferredRegister.createItems(GalacticBiotech.MOD_ID);
    
    // Biotech Items - using the DeferredRegister.Items.registerItem to properly set IDs
    public static final DeferredHolder<Item, Item> GENE_SPLICER = ITEMS_HELPER.registerItem(
            "gene_splicer", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> DNA_SAMPLER = ITEMS_HELPER.registerItem(
            "dna_sampler", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> GROWTH_SERUM = ITEMS_HELPER.registerItem(
            "growth_serum", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> MUTATION_CATALYST = ITEMS_HELPER.registerItem(
            "mutation_catalyst", 
            properties -> new Item(properties.stacksTo(64))
    );
    
    public static final DeferredHolder<Item, Item> GENETIC_STABILIZER = ITEMS_HELPER.registerItem(
            "genetic_stabilizer", 
            properties -> new Item(properties.stacksTo(64))
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Register our Items DeferredRegister to the EVENT BUS, not to another DeferredRegister
        ITEMS_HELPER.register(GalacticBiotech.MOD_EVENT_BUS);
        
        // Validate that important items are registered correctly
        if (GENE_SPLICER == null || DNA_SAMPLER == null || GROWTH_SERUM == null) {
            throw new IllegalStateException("Biotech items failed to initialize properly");
        }
    }
}