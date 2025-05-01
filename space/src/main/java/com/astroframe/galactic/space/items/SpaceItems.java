package com.astroframe.galactic.space.items;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all items in the Space module.
 */
public class SpaceItems {
    private static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticSpace.MOD_ID);
    
    /**
     * The modular rocket item, used to create and manage rockets.
     */
    public static final Supplier<Item> MODULAR_ROCKET = ITEMS.register("modular_rocket",
            () -> new ModularRocketItem(new Item.Properties()
                    .stacksTo(1)
                    .fireResistant()));
    
    /**
     * Initializes the item registry.
     * @param eventBus The mod event bus
     */
    public static void initialize(net.neoforged.bus.api.IEventBus eventBus) {
        GalacticSpace.LOGGER.info("Initializing Space items");
        ITEMS.register(eventBus);
    }
}