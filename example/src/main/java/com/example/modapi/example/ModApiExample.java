package com.example.modapi.example;

import com.example.modapi.core.ModApiCore;
import com.example.modapi.core.api.Module;
import com.example.modapi.core.api.ModRegistry;
import com.example.modapi.core.api.RegistryHelper;
import com.example.modapi.core.util.ModLogger;
import com.example.modapi.example.common.ExampleMachine;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;

import java.util.function.Supplier;

/**
 * Example mod that utilizes the ModApi framework.
 * Showcases how to build a mod using the core, energy, and machinery modules.
 */
@Mod(ModApiExample.MOD_ID)
public class ModApiExample implements Module {
    public static final String MOD_ID = "modapi_example";
    public static final ModLogger LOGGER = new ModLogger(MOD_ID);
    
    private static ModApiExample instance;
    
    // Example blocks and items
    public static Supplier<Block> EXAMPLE_MACHINE_BLOCK;
    public static Supplier<Item> ENERGY_CRYSTAL;
    
    /**
     * Constructor for the example mod.
     * Registers with the core mod and sets up event listeners.
     */
    public ModApiExample() {
        instance = this;
        
        // Register this module with the core
        ModApiCore.getInstance().getRegistry().registerModule(this);
        
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        IEventBus modEventBus = container.getEventBus();
        modEventBus.addListener(this::setup);
        
        LOGGER.info("ModApi Example initialized");
    }
    
    /**
     * Common setup method called during mod initialization.
     * 
     * @param event The setup event
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("ModApi Example setup phase");
    }
    
    /**
     * Get the singleton instance of the example mod.
     * 
     * @return The mod instance
     */
    public static ModApiExample getInstance() {
        return instance;
    }

    @Override
    public String getModuleId() {
        return "example";
    }

    @Override
    public String getModuleName() {
        return "ModApi Example";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Override
    public void registerContent(ModRegistry registry) {
        // Register example blocks, items, etc.
        LOGGER.info("Registering example module content");
        
        // Register example machine block
        EXAMPLE_MACHINE_BLOCK = RegistryHelper.registerBlockWithItem(
            this, 
            "example_machine", 
            () -> new ExampleMachine.Block(net.minecraft.world.level.block.Block.Properties.of().mapColor(net.minecraft.world.level.material.MapColor.STONE))
        );
        
        // Register energy crystal item
        ENERGY_CRYSTAL = RegistryHelper.registerItem(
            this,
            "energy_crystal",
            () -> new Item(new net.minecraft.world.item.Item.Properties())
        );
    }
    
    @Override
    public String[] getDependencies() {
        // This module depends on all other modules
        return new String[] { "core", "energy", "machinery" };
    }
}
