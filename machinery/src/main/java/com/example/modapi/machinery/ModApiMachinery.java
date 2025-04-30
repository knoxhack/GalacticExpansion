package com.example.modapi.machinery;

import com.example.modapi.core.ModApiCore;
import com.example.modapi.core.api.Module;
import com.example.modapi.core.api.ModRegistry;
import com.example.modapi.core.util.ModLogger;
import com.example.modapi.machinery.api.MachineRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main mod class for the ModApi Machinery module.
 * Provides machinery-related functionality building on the core and energy APIs.
 */
@Mod(ModApiMachinery.MOD_ID)
public class ModApiMachinery implements Module {
    public static final String MOD_ID = "modapi_machinery";
    public static final ModLogger LOGGER = new ModLogger(MOD_ID);
    
    private static ModApiMachinery instance;
    private final MachineRegistry machineRegistry;
    
    /**
     * Constructor for the machinery module.
     * Registers with the core mod and sets up event listeners.
     */
    public ModApiMachinery() {
        instance = this;
        machineRegistry = new MachineRegistry();
        
        // Register this module with the core
        ModApiCore.getInstance().getRegistry().registerModule(this);
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        
        LOGGER.info("ModApi Machinery initialized");
    }
    
    /**
     * Common setup method called during mod initialization.
     * 
     * @param event The setup event
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("ModApi Machinery setup phase");
        
        // Register machine types
        event.enqueueWork(() -> {
            machineRegistry.registerMachineTypes();
        });
    }
    
    /**
     * Get the singleton instance of the machinery module.
     * 
     * @return The module instance
     */
    public static ModApiMachinery getInstance() {
        return instance;
    }
    
    /**
     * Get the machine registry.
     * 
     * @return The machine registry
     */
    public MachineRegistry getMachineRegistry() {
        return machineRegistry;
    }

    @Override
    public String getModuleId() {
        return "machinery";
    }

    @Override
    public String getModuleName() {
        return "ModApi Machinery";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Override
    public void registerContent(ModRegistry registry) {
        // Register machinery-related blocks, items, etc.
        LOGGER.info("Registering machinery module content");
        machineRegistry.registerAllContent(registry);
    }
    
    @Override
    public String[] getDependencies() {
        // This module depends on both core and energy modules
        return new String[] { "core", "energy" };
    }
}
