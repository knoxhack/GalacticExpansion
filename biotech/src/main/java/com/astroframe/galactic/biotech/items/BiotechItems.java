package com.astroframe.galactic.biotech.items;

import com.astroframe.galactic.biotech.registry.BiotechRegistry;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * Registry for all items in the Biotech module.
 */
public class BiotechItems {

    // Biotech Items
    public static final DeferredHolder<Item, Item> GENE_SPLICER = BiotechRegistry.ITEMS.register(
            "gene_splicer", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> DNA_SAMPLER = BiotechRegistry.ITEMS.register(
            "dna_sampler", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> GROWTH_SERUM = BiotechRegistry.ITEMS.register(
            "growth_serum", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> MUTATION_CATALYST = BiotechRegistry.ITEMS.register(
            "mutation_catalyst", 
            () -> new Item(new Item.Properties())
    );
    
    public static final DeferredHolder<Item, Item> GENETIC_STABILIZER = BiotechRegistry.ITEMS.register(
            "genetic_stabilizer", 
            () -> new Item(new Item.Properties())
    );

    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init() {
        // Nothing needed here, registration happens through the static initializers
    }
}