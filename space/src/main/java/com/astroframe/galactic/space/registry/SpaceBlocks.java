package com.astroframe.galactic.space.registry;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTable;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * Registry for all blocks in the Space module.
 */
public class SpaceBlocks {
    
    // Create a deferred register for blocks
    public static final DeferredRegister<Block> BLOCKS = 
            DeferredRegister.create(Registries.BLOCK, GalacticSpace.MOD_ID);
    
    // Create a deferred register for block items
    public static final DeferredRegister<Item> BLOCK_ITEMS = 
            DeferredRegister.create(Registries.ITEM, GalacticSpace.MOD_ID);
    
    // Register the rocket assembly table
    public static final Supplier<Block> ROCKET_ASSEMBLY_TABLE = 
            BLOCKS.register("rocket_assembly_table", () -> {
                return new RocketAssemblyTable(Block.Properties.of().strength(3.5F).requiresCorrectToolForDrops());
            });
    
    // Register the rocket assembly table item
    static {
        registerBlockItem("rocket_assembly_table", ROCKET_ASSEMBLY_TABLE);
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