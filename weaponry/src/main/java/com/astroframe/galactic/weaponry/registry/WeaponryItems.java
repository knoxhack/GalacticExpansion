package com.astroframe.galactic.weaponry.registry;

import com.astroframe.galactic.weaponry.GalacticWeaponry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all items in the Weaponry module.
 */
public class WeaponryItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM, GalacticWeaponry.MOD_ID);

    // Register block items
    public static final Supplier<Item> WEAPON_STATION_ITEM = ITEMS.register(
            "weapon_station", 
            () -> new BlockItem(WeaponryBlocks.WEAPON_STATION.get(), new Item.Properties()));
    
    public static final Supplier<Item> ARMORY_ITEM = ITEMS.register(
            "armory", 
            () -> new BlockItem(WeaponryBlocks.ARMORY.get(), new Item.Properties()));
    
    // Register weapon items
    public static final Supplier<Item> ENERGY_CELL = ITEMS.register(
            "energy_cell", 
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final Supplier<Item> WEAPON_CORE = ITEMS.register(
            "weapon_core", 
            () -> new Item(new Item.Properties()));
    
    /**
     * Register all items with the mod event bus.
     *
     * @param eventBus The mod event bus
     */
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}