package com.astroframe.galactic.construction.registry;

import com.astroframe.galactic.construction.GalacticConstruction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all items in the Construction module.
 */
public class ConstructionItems {
    
    // Create a deferred register for items
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticConstruction.MOD_ID);
    
    // Register construction component item
    public static final Supplier<Item> CONSTRUCTION_COMPONENT = 
            ITEMS.register("construction_component", () -> new Item(new Item.Properties()));
    
    // Register nano fabricator item
    public static final Supplier<Item> NANO_FABRICATOR = 
            ITEMS.register("nano_fabricator", () -> new Item(new Item.Properties()));
    
    /**
     * Register the item registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}