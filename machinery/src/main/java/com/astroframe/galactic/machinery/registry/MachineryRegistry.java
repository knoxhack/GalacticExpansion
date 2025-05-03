package com.astroframe.galactic.machinery.registry;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import com.astroframe.galactic.machinery.blockentity.MachineryBlockEntities;
import com.astroframe.galactic.machinery.items.MachineryItemBlocks;
import com.astroframe.galactic.machinery.items.MachineryItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Machinery module.
 * This centralizes all registrations for the module.
 */
public class MachineryRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering Machinery module objects");
        
        // Initialize items
        MachineryItems.init();
        
        // Initialize blocks and block entities
        MachineryBlocks.init(eventBus);
        MachineryBlockEntities.init(eventBus);
        
        // Initialize block items (these connect blocks to items)
        MachineryItemBlocks.init();
        
        // Register objects
        ITEMS.register(eventBus);
        
        GalacticMachinery.LOGGER.info("Machinery module registration complete");
    }
}