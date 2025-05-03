package com.astroframe.galactic.biotech.registry;

import com.astroframe.galactic.biotech.GalacticBiotech;
import com.astroframe.galactic.biotech.items.BiotechItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Biotech module.
 * This centralizes all registrations for the module.
 */
public class BiotechRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticBiotech.MOD_ID);

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticBiotech.LOGGER.info("Registering Biotech module objects");
        
        // Initialize items
        BiotechItems.init();
        
        // Register objects
        ITEMS.register(eventBus);
        
        // Initialize creative tabs
        BiotechCreativeTabs.initialize(eventBus);
        
        GalacticBiotech.LOGGER.info("Biotech module registration complete");
    }
}