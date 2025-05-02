package com.astroframe.galactic.space;

import com.astroframe.galactic.space.implementation.component.test.RocketComponentTester;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlock;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlockEntity;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;
import com.astroframe.galactic.space.registry.SpaceMenus;
import net.minecraft.world.inventory.MenuType;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTable;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.function.Supplier;
import java.util.Set;

/**
 * Main class for the Space module of the Galactic mod.
 * Handles registration of blocks, items, and block entities related to space exploration.
 */
@Mod("galactic_space")
public class SpaceModule {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SpaceModule.class);
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
            
    // Registry for menus
    public static final DeferredRegister<MenuType<?>> MENUS = 
            DeferredRegister.create(BuiltInRegistries.MENU, MODID);
    
    // Blocks
    public static final DeferredHolder<Block, Block> ROCKET_ASSEMBLY_TABLE = registerBlock("rocket_assembly_table",
            () -> new RocketAssemblyTable(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()));
    
    public static final DeferredHolder<Block, Block> HOLOGRAPHIC_PROJECTOR = registerBlock("holographic_projector",
            () -> new HolographicProjectorBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.METAL)
                    .strength(3.5F)
                    .sound(SoundType.METAL)
                    .requiresCorrectToolForDrops()
                    .lightLevel(state -> state.getValue(HolographicProjectorBlock.ACTIVE) ? 10 : 0)));
    
    // Block Entities
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<HolographicProjectorBlockEntity>> HOLOGRAPHIC_PROJECTOR_BLOCK_ENTITY = 
            BLOCK_ENTITIES.register("holographic_projector", 
                    () -> BlockEntityType.create(
                            HolographicProjectorBlockEntity::new, 
                            HOLOGRAPHIC_PROJECTOR.get()));
    
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<RocketAssemblyTableBlockEntity>> ROCKET_ASSEMBLY_TABLE_BLOCK_ENTITY = 
            BLOCK_ENTITIES.register("rocket_assembly_table", 
                    () -> BlockEntityType.create(
                            RocketAssemblyTableBlockEntity::new, 
                            ROCKET_ASSEMBLY_TABLE.get()));
                            
    // Menus
    public static final DeferredHolder<MenuType<?>, MenuType<RocketAssemblyMenu>> ROCKET_ASSEMBLY_MENU =
            MENUS.register("rocket_assembly", 
                    () -> new MenuType<>((containerId, inv) -> 
                        new RocketAssemblyMenu(containerId, inv, 
                            net.minecraft.network.FriendlyByteBuf.class.cast(null)), 
                        net.minecraft.world.flag.FeatureFlags.DEFAULT_FLAGS));
    
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
        MENUS.register(eventBus);
        
        // Register custom menu registry
        SpaceMenus.register();
        
        // Register event handlers
        eventBus.addListener(this::registerRenderers);
        eventBus.addListener(this::addItemsToTabs);
        eventBus.addListener(this::onCommonSetup);
    }
    
    /**
     * Handles common setup tasks when the mod is initialized.
     * This is a good place to run tests and perform other initialization.
     *
     * @param event The common setup event
     */
    private void onCommonSetup(FMLCommonSetupEvent event) {
        LOGGER.info("Space Module initializing...");
        
        // Run tests for the component system
        LOGGER.info("Testing rocket component system...");
        event.enqueueWork(() -> {
            try {
                boolean success = RocketComponentTester.runTests();
                if (success) {
                    LOGGER.info("Rocket component tests completed successfully!");
                } else {
                    LOGGER.error("Rocket component tests failed. Check logs for details.");
                }
            } catch (Exception e) {
                LOGGER.error("Exception during rocket component tests", e);
            }
        });
    }
    
    /**
     * Helper method to register a block and its item form.
     *
     * @param name The registry name
     * @param blockSupplier The block supplier
     * @return The registry entry
     */
    private static <T extends Block> DeferredHolder<Block, T> registerBlock(String name, Supplier<T> blockSupplier) {
        DeferredHolder<Block, T> block = BLOCKS.register(name, blockSupplier);
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
            // Get the item forms of the blocks and add them
            event.accept(HOLOGRAPHIC_PROJECTOR.get().asItem());
            event.accept(ROCKET_ASSEMBLY_TABLE.get().asItem());
        }
    }
}