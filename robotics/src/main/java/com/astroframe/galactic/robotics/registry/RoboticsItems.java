package com.astroframe.galactic.robotics.registry;

import com.astroframe.galactic.robotics.GalacticRobotics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry class for all items in the Robotics module.
 */
public class RoboticsItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM, GalacticRobotics.MOD_ID);

    // Register block items
    public static final Supplier<Item> ROBOT_STATION_ITEM = ITEMS.register(
            "robot_station", 
            () -> new BlockItem(RoboticsBlocks.ROBOT_STATION.get(), new Item.Properties()));
    
    public static final Supplier<Item> DRONE_STATION_ITEM = ITEMS.register(
            "drone_station", 
            () -> new BlockItem(RoboticsBlocks.DRONE_STATION.get(), new Item.Properties()));
    
    // Register component items
    public static final Supplier<Item> ROBOT_CIRCUIT = ITEMS.register(
            "robot_circuit", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> DRONE_CONTROLLER = ITEMS.register(
            "drone_controller", 
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