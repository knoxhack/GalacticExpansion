package com.astroframe.galactic.machinery.items;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.blocks.MachineryBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.function.Supplier;

/**
 * Registry for all block items in the Machinery module.
 * These are items that represent placeable blocks.
 */
public class MachineryItemBlocks {

    // Deferred Register for block items - switching to create for consistency
    private static final DeferredRegister<Item> BLOCK_ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticMachinery.MOD_ID);

    // Define item registration - ensure this happens after block registration
    // We'll use event-based registration instead of static initialization to fix order issues
    private static void registerBlockItems(RegisterEvent event) {
        // Only register items during the ITEM registry event
        if (event.getRegistryKey() != Registries.ITEM) {
            return;
        }
        
        GalacticMachinery.LOGGER.info("Registering machinery block items during item registration event");
    }
    
    // Register the block items with proper lazy initialization
    public static final Supplier<Item> ASSEMBLER_ITEM = BLOCK_ITEMS.register(
        "assembler_block",
        () -> {
            GalacticMachinery.LOGGER.debug("Creating assembler block item");
            return new BlockItem(MachineryBlocks.ASSEMBLER.get(), new Item.Properties());
        }
    );

    public static final Supplier<Item> CRUSHER_ITEM = BLOCK_ITEMS.register(
        "crusher_block",
        () -> {
            GalacticMachinery.LOGGER.debug("Creating crusher block item");
            return new BlockItem(MachineryBlocks.CRUSHER.get(), new Item.Properties());
        }
    );

    public static final Supplier<Item> CENTRIFUGE_ITEM = BLOCK_ITEMS.register(
        "centrifuge_block",
        () -> {
            GalacticMachinery.LOGGER.debug("Creating centrifuge block item");
            return new BlockItem(MachineryBlocks.CENTRIFUGE.get(), new Item.Properties());
        }
    );

    public static final Supplier<Item> SMELTER_ITEM = BLOCK_ITEMS.register(
        "smelter_block",
        () -> {
            GalacticMachinery.LOGGER.debug("Creating smelter block item");
            return new BlockItem(MachineryBlocks.SMELTER.get(), new Item.Properties());
        }
    );

    public static final Supplier<Item> EXTRACTOR_ITEM = BLOCK_ITEMS.register(
        "extractor_block",
        () -> {
            GalacticMachinery.LOGGER.debug("Creating extractor block item");
            return new BlockItem(MachineryBlocks.EXTRACTOR.get(), new Item.Properties());
        }
    );

    /**
     * Initializes the block items registry.
     * Called during the module's registry phase.
     * 
     * @param eventBus The event bus to register with
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery block items");

        // Register after blocks to ensure blocks are registered first
        BLOCK_ITEMS.register(eventBus);
        
        // Register the event handler for manual registration if needed
        eventBus.addListener(MachineryItemBlocks::registerBlockItems);
    }
}