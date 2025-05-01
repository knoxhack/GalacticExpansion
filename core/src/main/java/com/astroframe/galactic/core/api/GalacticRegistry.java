package com.astroframe.galactic.core.api;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Interface for the registry management system.
 * Provides a standardized way to handle game registries across modules.
 */
public interface GalacticRegistry {
    
    /**
     * Adds a deferred register to this registry manager.
     * 
     * @param register The deferred register to add
     */
    void addRegister(DeferredRegister<?> register);
    
    /**
     * Registers all deferred registers to the provided event bus.
     * 
     * @param eventBus The mod event bus to register with
     */
    void registerAllTo(IEventBus eventBus);
    
    /**
     * Registers a handler for when registry validation is complete.
     * 
     * @param handler The handler to call when validation is complete
     */
    void onRegistryValidation(Runnable handler);
}