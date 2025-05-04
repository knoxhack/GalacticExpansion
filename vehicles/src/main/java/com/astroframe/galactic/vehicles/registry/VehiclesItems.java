package com.astroframe.galactic.vehicles.registry;

import com.astroframe.galactic.vehicles.GalacticVehicles;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all items in the Vehicles module.
 */
public class VehiclesItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM, GalacticVehicles.MOD_ID);
    
    // Register block items
    public static final Supplier<Item> SIMPLE_VEHICLE_BLOCK_ITEM = ITEMS.register(
            "simple_vehicle_block", 
            () -> new BlockItem(VehiclesBlocks.SIMPLE_VEHICLE_BLOCK.get(), new Item.Properties()));
    
    public static final Supplier<Item> ROVER_ITEM = ITEMS.register(
            "rover", 
            () -> new BlockItem(VehiclesBlocks.ROVER.get(), new Item.Properties()));
    
    // Register regular items
    public static final Supplier<Item> VEHICLE_ENGINE = ITEMS.register(
            "vehicle_engine", 
            () -> new Item(new Item.Properties().stacksTo(16)));
    
    public static final Supplier<Item> VEHICLE_WHEEL = ITEMS.register(
            "vehicle_wheel", 
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