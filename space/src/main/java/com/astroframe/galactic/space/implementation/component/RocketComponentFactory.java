package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.RocketComponent;
import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

/**
 * Factory for creating rocket components.
 * Updated for NeoForge 1.21.5 compatibility.
 */
public class RocketComponentFactory {
    
    /**
     * Creates a cockpit component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createCockpit(RocketComponentType type, int tier) {
        CommandModuleComponent component = new CommandModuleComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Original method to create cockpit component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The command module
     */
    public static ICommandModule createCommandModule(RocketComponentType type, int tier) {
        return new CommandModuleComponent(type, tier);
    }
    
    /**
     * Creates an engine component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createEngine(RocketComponentType type, int tier) {
        EngineComponent component = new EngineComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Original method to create engine component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The engine
     */
    public static IRocketEngine createEngineComponent(RocketComponentType type, int tier) {
        return new EngineComponent(type, tier);
    }
    
    /**
     * Creates a fuel tank component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createFuelTank(RocketComponentType type, int tier) {
        FuelTankComponent component = new FuelTankComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Original method to create fuel tank component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The fuel tank
     */
    public static IFuelTank createFuelTankComponent(RocketComponentType type, int tier) {
        return new FuelTankComponent(type, tier);
    }
    
    /**
     * Creates a storage component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createStorage(RocketComponentType type, int tier) {
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Creates a navigation component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createNavigation(RocketComponentType type, int tier) {
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Creates a life support component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createLifeSupport(RocketComponentType type, int tier) {
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Creates a shield component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createShield(RocketComponentType type, int tier) {
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Creates a structure component for NeoForge 1.21.5.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static RocketComponent createStructure(RocketComponentType type, int tier) {
        BasicRocketComponent component = new BasicRocketComponent(type, tier);
        return new RocketComponentAdapter(component);
    }
    
    /**
     * Creates a rocket component from an interface component.
     * This allows conversion between the interface and concrete implementations.
     *
     * @param component The interface component
     * @return The concrete rocket component
     */
    public static RocketComponent fromInterface(IRocketComponent component) {
        return new RocketComponentAdapter(component);
    }
}