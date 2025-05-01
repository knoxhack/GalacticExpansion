package com.astroframe.galactic.core.registry;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.items.CoreItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Core module.
 * This centralizes all registrations for the module.
 */
public class CoreRegistry {

    // Deferred Registers
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, GalacticCore.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GalacticCore.MOD_ID);

    // Creative Tab
    public static final ResourceKey<CreativeModeTab> GALACTIC_TAB_KEY = ResourceKey.create(
            Registries.CREATIVE_MODE_TAB, 
            new ResourceLocation(GalacticCore.MOD_ID + ":galactic_tab")
    );

    /**
     * Registers all registry objects with the given event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    public static void register(IEventBus eventBus) {
        GalacticCore.LOGGER.info("Registering Core module objects");
        
        // Initialize items
        CoreItems.init();
        
        // Register objects
        ITEMS.register(eventBus);
        CREATIVE_MODE_TABS.register(eventBus);
        
        GalacticCore.LOGGER.info("Core module registration complete");
    }
}