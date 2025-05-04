package com.astroframe.galactic.power.items;

import com.astroframe.galactic.power.GalacticPower;
import com.astroframe.galactic.power.blocks.PowerBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all items in the Power module.
 */
public class PowerItems {

    // Deferred Register for items
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticPower.MOD_ID);
    
    // Properties for standard items
    private static Item.Properties defaultProperties() {
        return new Item.Properties();
    }

    // Block items for the power blocks
    public static final Supplier<BlockItem> BASIC_GENERATOR = ITEMS.register(
        "basic_generator_block",
        () -> new BlockItem(PowerBlocks.BASIC_GENERATOR.get(), defaultProperties())
    );
    
    // For use in creative tab - these are placeholder items
    public static final Supplier<Item> ADVANCED_GENERATOR = ITEMS.register(
        "advanced_generator_item",
        () -> new Item(defaultProperties())
    );
    
    public static final Supplier<Item> SOLAR_PANEL = ITEMS.register(
        "solar_panel_item",
        () -> new Item(defaultProperties())
    );
    
    public static final Supplier<Item> FUSION_REACTOR = ITEMS.register(
        "fusion_reactor_item",
        () -> new Item(defaultProperties())
    );
    
    /**
     * Initializes the items registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticPower.LOGGER.info("Registering power items");
        ITEMS.register(eventBus);
    }
}