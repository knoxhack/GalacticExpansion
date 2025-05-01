package com.astroframe.galactic.core.registry;

import com.astroframe.galactic.core.GalacticCore;
import com.astroframe.galactic.core.api.GalacticRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Implementation of the GalacticRegistry interface.
 * Handles the registration of objects to the game.
 */
public class CoreRegistry implements GalacticRegistry {
    
    private final List<DeferredRegister<?>> registers = new ArrayList<>();
    private final List<Runnable> validationHandlers = new ArrayList<>();
    
    /**
     * Adds a deferred register to this registry manager.
     *
     * @param register The deferred register to add
     */
    @Override
    public void addRegister(DeferredRegister<?> register) {
        if (register == null) {
            GalacticCore.LOGGER.warn("Attempted to add null DeferredRegister to CoreRegistry");
            return;
        }
        
        registers.add(register);
        GalacticCore.LOGGER.debug("Added {} to CoreRegistry", register.getRegistryKey());
    }
    
    /**
     * Registers all deferred registers to the provided event bus.
     *
     * @param eventBus The mod event bus to register with
     */
    @Override
    public void registerAllTo(IEventBus eventBus) {
        if (eventBus == null) {
            GalacticCore.LOGGER.error("Cannot register to null event bus");
            return;
        }
        
        GalacticCore.LOGGER.info("Registering {} DeferredRegisters to event bus", registers.size());
        registers.forEach(register -> register.register(eventBus));
        
        // Register to receive registry validation complete event
        eventBus.addListener(this::onRegistriesValidated);
    }
    
    /**
     * Registers a handler for when registry validation is complete.
     *
     * @param handler The handler to call when validation is complete
     */
    @Override
    public void onRegistryValidation(Runnable handler) {
        if (handler != null) {
            validationHandlers.add(handler);
        }
    }
    
    /**
     * Handles the registry validation complete event.
     * Calls all registered validation handlers.
     * 
     * @param event The registry validation complete event
     */
    private void onRegistriesValidated(final net.neoforged.neoforge.registries.RegisterEvent event) {
        GalacticCore.LOGGER.debug("Registry validation event for {}", event.getRegistryKey());
        
        // Only run handlers when all registries are validated
        if (event.getRegistryKey() == net.minecraft.core.registries.Registries.BLOCK) {
            GalacticCore.LOGGER.info("All registries validated, running {} handlers", validationHandlers.size());
            validationHandlers.forEach(Runnable::run);
        }
    }
}