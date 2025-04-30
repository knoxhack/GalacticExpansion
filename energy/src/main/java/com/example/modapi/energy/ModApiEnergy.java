package com.example.modapi.energy;

import com.example.modapi.core.ModApiCore;
import com.example.modapi.core.api.Module;
import com.example.modapi.core.api.ModRegistry;
import com.example.modapi.core.util.ModLogger;
import com.example.modapi.energy.api.EnergyCapability;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * The main mod class for the ModApi Energy module.
 * Provides energy-related functionality building on the core API.
 */
@Mod(ModApiEnergy.MOD_ID)
public class ModApiEnergy implements Module {
    public static final String MOD_ID = "modapi_energy";
    public static final ModLogger LOGGER = new ModLogger(MOD_ID);
    
    private static ModApiEnergy instance;
    
    /**
     * Constructor for the energy module.
     * Registers with the core mod and sets up event listeners.
     */
    public ModApiEnergy() {
        instance = this;
        
        // Register this module with the core
        ModApiCore.getInstance().getRegistry().registerModule(this);
        
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        
        LOGGER.info("ModApi Energy initialized");
    }
    
    /**
     * Common setup method called during mod initialization.
     * 
     * @param event The setup event
     */
    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("ModApi Energy setup phase");
        
        // Register capabilities
        event.enqueueWork(() -> {
            EnergyCapability.register();
        });
    }
    
    /**
     * Get the singleton instance of the energy module.
     * 
     * @return The module instance
     */
    public static ModApiEnergy getInstance() {
        return instance;
    }

    @Override
    public String getModuleId() {
        return "energy";
    }

    @Override
    public String getModuleName() {
        return "ModApi Energy";
    }

    @Override
    public String getModuleVersion() {
        return "1.0.0";
    }

    @Override
    public void registerContent(ModRegistry registry) {
        // Register energy-related blocks, items, etc.
        LOGGER.info("Registering energy module content");
    }
    
    @Override
    public String[] getDependencies() {
        // This module depends only on the core module
        return new String[] { "core" };
    }
}
