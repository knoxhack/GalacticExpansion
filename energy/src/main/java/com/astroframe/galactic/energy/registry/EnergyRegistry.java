package com.astroframe.galactic.energy.registry;

import com.astroframe.galactic.energy.GalacticEnergy;
import com.astroframe.galactic.energy.blocks.EnergyBlocks;
import com.astroframe.galactic.energy.items.EnergyItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry handler for the Energy module.
 * Coordinates registration of all blocks, items, and other registry entries for this module.
 */
public class EnergyRegistry {

    // Deferred Register for items
    public static final DeferredRegister<Item> ITEMS = 
        DeferredRegister.create(Registries.ITEM, GalacticEnergy.MOD_ID);
    
    /**
     * Initialize the registry by registering all blocks, items, and other objects.
     * @param eventBus The event bus to register on
     */
    public static void init(IEventBus eventBus) {
        GalacticEnergy.LOGGER.info("Initializing Energy registry");
        
        // Register blocks
        EnergyBlocks.init(eventBus);
        
        // Register items
        EnergyItems.init(eventBus);
        
        // Register block items
        EnergyBlocks.registerBlockItems();
        
        // Register this module's registries to the event bus
        ITEMS.register(eventBus);
    }

    /**
     * Create a standard item properties builder.
     * @return Item Properties builder
     */
    public static Item.Properties createStandardItemProperties() {
        return new Item.Properties();
    }
    
    /**
     * Create a standard block item.
     * @param name Block name
     * @param blockSupplier Block supplier
     * @return BlockItem for the given block
     */
    public static BlockItem createBlockItem(String name, java.util.function.Supplier<net.minecraft.world.level.block.Block> blockSupplier) {
        return new BlockItem(blockSupplier.get(), createStandardItemProperties());
    }
}