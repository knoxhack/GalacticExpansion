package com.astroframe.galactic.space;

import com.astroframe.galactic.core.api.registry.Registry;
import com.astroframe.galactic.core.api.registry.RegistryEntry;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlock;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlockEntity;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTable;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import java.util.function.Supplier;

/**
 * Main class for the Space module of the Galactic mod.
 * Handles registration of blocks, items, and block entities related to space exploration.
 */
@Mod("galactic_space")
public class SpaceModule {
    
    public static final String MODID = "galactic_space";
    
    // Registry for blocks
    public static final DeferredRegister<Block> BLOCKS = 
            DeferredRegister.create(BuiltInRegistries.BLOCK, MODID);
    
    // Registry for items
    public static final DeferredRegister<Item> ITEMS = 
            DeferredRegister.create(BuiltInRegistries.ITEM, MODID);
    
    // Registry for block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, MODID);
    
    // Blocks
    public static final RegistryEntry<Block> ROCKET_ASSEMBLY_TABLE = registerBlock("rocket_assembly_table",
            () -> new RocketAssemblyTable(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));
    
    public static final RegistryEntry<Block> HOLOGRAPHIC_PROJECTOR = registerBlock("holographic_projector",
            () -> new HolographicProjectorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(HolographicProjectorBlock.ACTIVE) ? 10 : 0)));
    
    // Block Entities
    public static final RegistryEntry<BlockEntityType<HolographicProjectorBlockEntity>> HOLOGRAPHIC_PROJECTOR_BLOCK_ENTITY = 
            BLOCK_ENTITIES.register("holographic_projector", 
                    () -> BlockEntityType.Builder.of(HolographicProjectorBlockEntity::new, 
                            HOLOGRAPHIC_PROJECTOR.get()).build(null));
    
    /**
     * Constructor for the Space module.
     * Registers all content and sets up event handlers.
     *
     * @param eventBus The mod event bus
     */
    public SpaceModule(IEventBus eventBus) {
        // Register deferred registers to the event bus
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
        BLOCK_ENTITIES.register(eventBus);
        
        // Register event handlers
        eventBus.addListener(this::registerRenderers);
        eventBus.addListener(this::addItemsToTabs);
    }
    
    /**
     * Helper method to register a block and its item form.
     *
     * @param name The registry name
     * @param blockSupplier The block supplier
     * @return The registry entry
     */
    private static <T extends Block> RegistryEntry<T> registerBlock(String name, Supplier<T> blockSupplier) {
        RegistryEntry<T> block = BLOCKS.register(name, blockSupplier);
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
        return block;
    }
    
    /**
     * Registers client-side renderers for block entities.
     *
     * @param event The renderer registration event
     */
    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(HOLOGRAPHIC_PROJECTOR_BLOCK_ENTITY.get(), 
                com.astroframe.galactic.space.implementation.hologram.HolographicProjectorRenderer::new);
    }
    
    /**
     * Adds items to creative tabs.
     *
     * @param event The creative tab event
     */
    private void addItemsToTabs(BuildCreativeModeTabContentsEvent event) {
        // Add space module items to appropriate tabs
        if (event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(HOLOGRAPHIC_PROJECTOR);
            event.accept(ROCKET_ASSEMBLY_TABLE);
        }
    }
}