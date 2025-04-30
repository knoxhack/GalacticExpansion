package com.example.modapi.core.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.example.modapi.core.ModApiCore;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.registries.ForgeRegistries;

/**
 * Central registry for managing mod components.
 * This class handles registration of blocks, items, and other game objects.
 */
public class ModRegistry {

    private final Map<String, Module> modules = new HashMap<>();
    private final List<DeferredRegister<?>> registers = new ArrayList<>();
    
    // Deferred registers for common Minecraft objects
    private final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ModApiCore.MOD_ID);
    private final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ModApiCore.MOD_ID);
    
    /**
     * Constructor for ModRegistry.
     * Initializes and adds the default registers.
     */
    public ModRegistry() {
        registers.add(BLOCKS);
        registers.add(ITEMS);
    }
    
    /**
     * Registers a new module.
     * 
     * @param module The module to register
     */
    public void registerModule(Module module) {
        if (!modules.containsKey(module.getModuleId())) {
            modules.put(module.getModuleId(), module);
            ModApiCore.LOGGER.info("Registered module: " + module.getModuleId());
        } else {
            ModApiCore.LOGGER.warn("Attempted to register duplicate module ID: " + module.getModuleId());
        }
    }
    
    /**
     * Gets a module by its ID.
     * 
     * @param moduleId The module ID
     * @return The module, or null if not found
     */
    public Module getModule(String moduleId) {
        return modules.get(moduleId);
    }
    
    /**
     * Registers all deferred registries with the event bus.
     * 
     * @param eventBus The mod event bus
     */
    public void registerAll(net.neoforged.bus.api.IEventBus eventBus) {
        for (DeferredRegister<?> register : registers) {
            register.register(eventBus);
        }
        
        // Let each module register its content
        for (Module module : modules.values()) {
            module.registerContent(this);
        }
    }
    
    /**
     * Gets the block registry.
     * 
     * @return The block registry
     */
    public DeferredRegister<Block> getBlocks() {
        return BLOCKS;
    }
    
    /**
     * Gets the item registry.
     * 
     * @return The item registry
     */
    public DeferredRegister<Item> getItems() {
        return ITEMS;
    }
    
    /**
     * Registers a block with the given name and supplier.
     * 
     * @param name The block name
     * @param blockSupplier The block supplier
     * @param <T> The block type
     * @return A supplier for the registered block
     */
    public <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(name, blockSupplier);
    }
    
    /**
     * Registers an item with the given name and supplier.
     * 
     * @param name The item name
     * @param itemSupplier The item supplier
     * @param <T> The item type
     * @return A supplier for the registered item
     */
    public <T extends Item> Supplier<T> registerItem(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(name, itemSupplier);
    }
    
    /**
     * Creates a new resource location with the mod ID as the namespace.
     * 
     * @param path The resource path
     * @return A new resource location
     */
    public static ResourceLocation resource(String path) {
        return new ResourceLocation(ModApiCore.MOD_ID, path);
    }
    
    /**
     * Adds a new deferred register to the registry.
     * 
     * @param register The register to add
     */
    public void addRegister(DeferredRegister<?> register) {
        registers.add(register);
    }
}
