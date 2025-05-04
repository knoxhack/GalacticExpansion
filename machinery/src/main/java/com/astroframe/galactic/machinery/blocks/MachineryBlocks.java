package com.astroframe.galactic.machinery.blocks;

import com.astroframe.galactic.machinery.GalacticMachinery;
import com.astroframe.galactic.machinery.api.MachineBlock;
import com.astroframe.galactic.machinery.implementation.AssemblerBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registry for all blocks in the Machinery module.
 */
public class MachineryBlocks {

    // Deferred Register for blocks
    private static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, GalacticMachinery.MOD_ID);

    // Machinery Blocks with deferred holders - must match resource file names
    public static final DeferredHolder<Block, Block> ASSEMBLER = BLOCKS.register(
        "assembler", 
        () -> {
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);
                
            return new AssemblerBlock(properties);
        }
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);

            // Return the block with properties that have a properly set ID
            return new AssemblerBlock(properties, assemblerBlockId);
        }
    );

    public static final DeferredHolder<Block, Block> CRUSHER = BLOCKS.register(
        "crusher_block", 
        () -> {
            // Create a ResourceLocation with the proper ID format using proper NeoForge 1.21.5 syntax
            ResourceLocation crusherId = ResourceLocation.parse(GalacticMachinery.MOD_ID + ":crusher_block");

            // Create properties with explicit block ID
            GalacticMachinery.LOGGER.debug("Creating crusher block with ID: " + crusherId);

            // Use this more verbose approach to ensure the block ID is properly set
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);

            return new Block(properties);
        }
    );

    public static final DeferredHolder<Block, Block> CENTRIFUGE = BLOCKS.register(
        "centrifuge_block", 
        () -> {
            // Create a ResourceLocation with the proper ID format using proper NeoForge 1.21.5 syntax
            ResourceLocation centrifugeId = ResourceLocation.parse(GalacticMachinery.MOD_ID + ":centrifuge_block");

            // Create properties with explicit block ID
            GalacticMachinery.LOGGER.debug("Creating centrifuge block with ID: " + centrifugeId);

            // Use this more verbose approach to ensure the block ID is properly set
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);

            return new Block(properties);
        }
    );

    public static final DeferredHolder<Block, Block> SMELTER = BLOCKS.register(
        "smelter_block", 
        () -> {
            // Create a ResourceLocation with the proper ID format using proper NeoForge 1.21.5 syntax
            ResourceLocation smelterId = ResourceLocation.parse(GalacticMachinery.MOD_ID + ":smelter_block");

            // Create properties with explicit block ID
            GalacticMachinery.LOGGER.debug("Creating smelter block with ID: " + smelterId);

            // Use this more verbose approach to ensure the block ID is properly set
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);

            return new Block(properties);
        }
    );

    public static final DeferredHolder<Block, Block> EXTRACTOR = BLOCKS.register(
        "extractor_block", 
        () -> {
            // Create a ResourceLocation with the proper ID format using proper NeoForge 1.21.5 syntax
            ResourceLocation extractorId = ResourceLocation.parse(GalacticMachinery.MOD_ID + ":extractor_block");

            // Create properties with explicit block ID
            GalacticMachinery.LOGGER.debug("Creating extractor block with ID: " + extractorId);

            // Use this more verbose approach to ensure the block ID is properly set
            BlockBehaviour.Properties properties = BlockBehaviour.Properties.of()
                .mapColor(MapColor.METAL)
                .requiresCorrectToolForDrops()
                .strength(5.0F, 6.0F)
                .sound(SoundType.METAL);

            return new Block(properties);
        }
    );

    /**
     * Register the block items for this module.
     * Called during the MachineryRegistry setup.
     */
    public static void registerBlockItems() {
        // Register block items
        GalacticMachinery.LOGGER.info("Registering machinery block items");
    }

    /**
     * Initializes the blocks registry.
     * Called during the module's registry phase.
     */
    public static void init(IEventBus eventBus) {
        GalacticMachinery.LOGGER.info("Registering machinery blocks");
        BLOCKS.register(eventBus);
    }
}