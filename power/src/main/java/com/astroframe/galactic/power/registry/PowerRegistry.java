package com.astroframe.galactic.power.registry;

import com.astroframe.galactic.power.GalacticPower;
import com.astroframe.galactic.power.items.PowerItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Power module.
 * This centralizes all registrations for the module.
 */
public class PowerRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticPower.MOD_ID);

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticPower.LOGGER.info("Registering Power module objects");
        
        // Initialize items
        PowerItems.init();
        
        // Register objects
        ITEMS.register(eventBus);
        
        GalacticPower.LOGGER.info("Power module registration complete");
    }
}