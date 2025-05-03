package com.astroframe.galactic.space;

import com.astroframe.galactic.space.registry.SpaceMenus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import java.util.function.Supplier;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        SpaceMenus.initialize(eventBus);
        
        // Register event handlers
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
        
        event.enqueueWork(() -> {
            try {
                LOGGER.info("Space module setup complete");
            } catch (Exception e) {
                LOGGER.error("Exception during Space module setup", e);
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
}