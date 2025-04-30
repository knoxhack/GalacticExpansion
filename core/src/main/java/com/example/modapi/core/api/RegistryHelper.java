package com.example.modapi.core.api;

import java.util.function.Supplier;

import com.example.modapi.core.ModApiCore;
import com.example.modapi.core.api.block.ModBlock;
import com.example.modapi.core.api.item.ModItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;

/**
 * Helper class for registering various game objects.
 * Simplifies the process of registering blocks, items, and block items.
 */
public class RegistryHelper {

    /**
     * Creates a registry name for a module.
     * 
     * @param module The module
     * @param name The object name
     * @return A new resource location
     */
    public static ResourceLocation registryName(Module module, String name) {
        return new ResourceLocation(ModApiCore.MOD_ID, module.getModuleId() + "/" + name);
    }
    
    /**
     * Creates a deferred register for the specified registry type.
     * 
     * @param <T> The registry type
     * @param registry The forge registry
     * @param modId The mod ID
     * @return A new deferred register
     */
    public static <T> DeferredRegister<T> createRegister(net.neoforged.registries.IForgeRegistry<T> registry, String modId) {
        return DeferredRegister.create(registry, modId);
    }
    
    /**
     * Registers a block and its block item.
     * 
     * @param module The module
     * @param name The block name
     * @param blockSupplier The block supplier
     * @return A supplier for the registered block
     */
    public static <T extends Block> Supplier<T> registerBlockWithItem(Module module, String name, Supplier<T> blockSupplier) {
        Supplier<T> block = ModApiCore.getInstance().getRegistry().registerBlock(
            module.getModuleId() + "_" + name, blockSupplier);
            
        registerBlockItem(module, name, block);
        
        return block;
    }
    
    /**
     * Registers a block item for a block.
     * 
     * @param module The module
     * @param name The block name
     * @param blockSupplier The block supplier
     * @return A supplier for the registered block item
     */
    public static Supplier<Item> registerBlockItem(Module module, String name, Supplier<? extends Block> blockSupplier) {
        return ModApiCore.getInstance().getRegistry().registerItem(
            module.getModuleId() + "_" + name, 
            () -> new BlockItem(blockSupplier.get(), new Item.Properties()));
    }
    
    /**
     * Registers an item.
     * 
     * @param module The module
     * @param name The item name
     * @param itemSupplier The item supplier
     * @return A supplier for the registered item
     */
    public static <T extends Item> Supplier<T> registerItem(Module module, String name, Supplier<T> itemSupplier) {
        return ModApiCore.getInstance().getRegistry().registerItem(
            module.getModuleId() + "_" + name, itemSupplier);
    }
    
    /**
     * Creates a standard mod block with default properties.
     * 
     * @param module The module
     * @param name The block name
     * @param properties The block properties
     * @return A supplier for the registered block
     */
    public static Supplier<ModBlock> createBlock(Module module, String name, Block.Properties properties) {
        return registerBlockWithItem(module, name, () -> new ModBlock(properties));
    }
    
    /**
     * Creates a standard mod item with default properties.
     * 
     * @param module The module
     * @param name The item name
     * @param properties The item properties
     * @return A supplier for the registered item
     */
    public static Supplier<ModItem> createItem(Module module, String name, Item.Properties properties) {
        return registerItem(module, name, () -> new ModItem(properties));
    }
}
