package com.astroframe.galactic.space.item;

import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for space module items.
 */
public class SpaceItems {
    
    /**
     * The deferred register for items.
     */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            Registries.ITEM, GalacticSpace.MOD_ID);
    
    // Items registration
    /**
     * The modular rocket item.
     */
    public static final DeferredHolder<Item, Item> MODULAR_ROCKET = 
            ITEMS.register("modular_rocket", ModularRocketItem::new);
    
    /**
     * The space suit helmet item.
     */
    public static final DeferredHolder<Item, Item> SPACE_SUIT_HELMET = 
            ITEMS.register("space_suit_helmet", () -> new SpaceSuitItem.Helmet());
    
    /**
     * The space suit chestplate item.
     */
    public static final DeferredHolder<Item, Item> SPACE_SUIT_CHESTPLATE = 
            ITEMS.register("space_suit_chestplate", () -> new SpaceSuitItem.Chestplate());
    
    /**
     * The space suit leggings item.
     */
    public static final DeferredHolder<Item, Item> SPACE_SUIT_LEGGINGS = 
            ITEMS.register("space_suit_leggings", () -> new SpaceSuitItem.Leggings());
    
    /**
     * The space suit boots item.
     */
    public static final DeferredHolder<Item, Item> SPACE_SUIT_BOOTS = 
            ITEMS.register("space_suit_boots", () -> new SpaceSuitItem.Boots());
    
    /**
     * Space resource - moon rock.
     */
    public static final DeferredHolder<Item, Item> MOON_ROCK = 
            ITEMS.register("moon_rock", () -> new Item(new Item.Properties()));
    
    /**
     * Space resource - stellar fragment.
     */
    public static final DeferredHolder<Item, Item> STELLAR_FRAGMENT = 
            ITEMS.register("stellar_fragment", () -> new Item(new Item.Properties()));
    
    /**
     * Space resource - lunar dust.
     */
    public static final DeferredHolder<Item, Item> LUNAR_DUST = 
            ITEMS.register("lunar_dust", () -> new Item(new Item.Properties()));
    
    /**
     * Register all items.
     */
    public static void register() {
        GalacticSpace.LOGGER.info("Registering Space items");
        ITEMS.register(GalacticSpace.MOD_EVENT_BUS);
    }
}