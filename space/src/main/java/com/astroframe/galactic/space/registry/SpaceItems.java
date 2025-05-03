package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.item.ModularRocketItem;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import com.astroframe.galactic.space.items.RocketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registers all items for the Space module.
 */
public class SpaceItems {
    
    // Create a deferred register for items
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticSpace.MOD_ID);
    
    // Create a single Item.Properties instance with stacksTo(64) set
    private static final Item.Properties DEFAULT_PROPS = new Item.Properties().stacksTo(64);
    
    // Define basic items
    public static final Supplier<Item> ROCKET_PART = ITEMS.register("rocket_part", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> MOON_DUST = ITEMS.register("moon_dust", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> LUNAR_DUST = ITEMS.register("lunar_dust", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> MOON_ROCK = ITEMS.register("moon_rock", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> MARS_ROCK = ITEMS.register("mars_rock", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> STELLAR_FRAGMENT = ITEMS.register("stellar_fragment", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> SPACE_HELMET = ITEMS.register("space_helmet", 
            () -> new Item(DEFAULT_PROPS));
    
    public static final Supplier<Item> OXYGEN_TANK = ITEMS.register("oxygen_tank", 
            () -> new Item(DEFAULT_PROPS));
    
    // Create a single Item.Properties instance for single-stack items
    private static final Item.Properties SINGLE_STACK_PROPS = new Item.Properties().stacksTo(1);
    
    // Space suit items
    // In NeoForge 1.21.5, use EquipmentSlot directly instead of ArmorItem.Type
    public static final Supplier<Item> SPACE_SUIT_HELMET = ITEMS.register("space_suit_helmet",
            () -> new SpaceSuitItem(EquipmentSlot.HEAD, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> SPACE_SUIT_CHESTPLATE = ITEMS.register("space_suit_chestplate",
            () -> new SpaceSuitItem(EquipmentSlot.CHEST, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> SPACE_SUIT_LEGGINGS = ITEMS.register("space_suit_leggings",
            () -> new SpaceSuitItem(EquipmentSlot.LEGS, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> SPACE_SUIT_BOOTS = ITEMS.register("space_suit_boots",
            () -> new SpaceSuitItem(EquipmentSlot.FEET, SINGLE_STACK_PROPS));
    
    // Rocket items
    public static final Supplier<Item> TIER_1_ROCKET = ITEMS.register("tier_1_rocket",
            () -> new RocketItem(1, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> TIER_2_ROCKET = ITEMS.register("tier_2_rocket",
            () -> new RocketItem(2, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> TIER_3_ROCKET = ITEMS.register("tier_3_rocket",
            () -> new RocketItem(3, SINGLE_STACK_PROPS));
    
    public static final Supplier<Item> MODULAR_ROCKET = ITEMS.register("modular_rocket",
            () -> new ModularRocketItem(SINGLE_STACK_PROPS));
    
    /**
     * Initialize and register all items.
     * @param modEventBus The mod event bus to register items on
     */
    public static void initialize(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}