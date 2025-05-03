package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.item.ModularRocketItem;
import com.astroframe.galactic.space.item.SpaceSuitItem;
import com.astroframe.galactic.space.items.RocketItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ArmorItem;
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
    
    // Define basic items
    public static final Supplier<Item> ROCKET_PART = ITEMS.register("rocket_part", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> MOON_DUST = ITEMS.register("moon_dust", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> LUNAR_DUST = ITEMS.register("lunar_dust", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> MOON_ROCK = ITEMS.register("moon_rock", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> MARS_ROCK = ITEMS.register("mars_rock", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> STELLAR_FRAGMENT = ITEMS.register("stellar_fragment", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> SPACE_HELMET = ITEMS.register("space_helmet", 
            () -> new Item(new Item.Properties()));
    
    public static final Supplier<Item> OXYGEN_TANK = ITEMS.register("oxygen_tank", 
            () -> new Item(new Item.Properties()));
    
    // Space suit items
    // In NeoForge 1.21.5, use ArmorItem.Type directly
    public static final Supplier<Item> SPACE_SUIT_HELMET = ITEMS.register("space_suit_helmet",
            () -> new SpaceSuitItem(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> SPACE_SUIT_CHESTPLATE = ITEMS.register("space_suit_chestplate",
            () -> new SpaceSuitItem(ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> SPACE_SUIT_LEGGINGS = ITEMS.register("space_suit_leggings",
            () -> new SpaceSuitItem(ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> SPACE_SUIT_BOOTS = ITEMS.register("space_suit_boots",
            () -> new SpaceSuitItem(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)));
    
    // Rocket items
    public static final Supplier<Item> TIER_1_ROCKET = ITEMS.register("tier_1_rocket",
            () -> new RocketItem(1, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> TIER_2_ROCKET = ITEMS.register("tier_2_rocket",
            () -> new RocketItem(2, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> TIER_3_ROCKET = ITEMS.register("tier_3_rocket",
            () -> new RocketItem(3, new Item.Properties().stacksTo(1)));
    
    public static final Supplier<Item> MODULAR_ROCKET = ITEMS.register("modular_rocket",
            () -> new ModularRocketItem(new Item.Properties().stacksTo(1)));
    
    /**
     * Initialize and register all items.
     * @param modEventBus The mod event bus to register items on
     */
    public static void initialize(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}