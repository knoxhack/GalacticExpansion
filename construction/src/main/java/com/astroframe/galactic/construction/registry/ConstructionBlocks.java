package com.astroframe.galactic.construction.registry;

import com.astroframe.galactic.construction.GalacticConstruction;
import com.astroframe.galactic.construction.blocks.FabricatorBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all blocks in the Construction module.
 */
public class ConstructionBlocks {
    
    // Create a deferred register for blocks
    public static final DeferredRegister<Block> BLOCKS = 
            DeferredRegister.create(Registries.BLOCK, GalacticConstruction.MOD_ID);
    
    // Create a deferred register for block items
    public static final DeferredRegister<Item> BLOCK_ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticConstruction.MOD_ID);
    
    // Register the fabricator block
    public static final Supplier<Block> FABRICATOR = 
            BLOCKS.register("fabricator", () -> {
                return new FabricatorBlock(Block.Properties.of().strength(4.0F).requiresCorrectToolForDrops());
            });
    
    // Register the fabricator block item
    static {
        registerBlockItem("fabricator", FABRICATOR);
    }
    
    /**
     * Helper method to register a block item.
     *
     * @param name The name of the block
     * @param blockSupplier The block supplier
     * @return The block item supplier
     */
    private static Supplier<Item> registerBlockItem(String name, Supplier<Block> blockSupplier) {
        return BLOCK_ITEMS.register(name, () -> new BlockItem(blockSupplier.get(), new Item.Properties()));
    }
    
    /**
     * Register the block registry with the event bus.
     *
     * @param eventBus The event bus to register with
     */
    public static void initialize(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        BLOCK_ITEMS.register(eventBus);
    }
}