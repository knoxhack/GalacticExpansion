package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

/**
 * Factory for creating rocket components.
 */
public class RocketComponentFactory {
    
    /**
     * Creates a cockpit component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static ICommandModule createCockpit(RocketComponentType type, int tier) {
        return new CommandModuleComponent(type, tier);
    }
    
    /**
     * Creates an engine component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketEngine createEngine(RocketComponentType type, int tier) {
        return new EngineComponent(type, tier);
    }
    
    /**
     * Creates a fuel tank component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IFuelTank createFuelTank(RocketComponentType type, int tier) {
        return new FuelTankComponent(type, tier);
    }
    
    /**
     * Creates a storage component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketComponent createStorage(RocketComponentType type, int tier) {
        return new BasicRocketComponent(type, tier);
    }
    
    /**
     * Creates a navigation component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketComponent createNavigation(RocketComponentType type, int tier) {
        return new BasicRocketComponent(type, tier);
    }
    
    /**
     * Creates a life support component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketComponent createLifeSupport(RocketComponentType type, int tier) {
        return new BasicRocketComponent(type, tier);
    }
    
    /**
     * Creates a shield component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketComponent createShield(RocketComponentType type, int tier) {
        return new BasicRocketComponent(type, tier);
    }
    
    /**
     * Creates a structure component.
     *
     * @param type The component type
     * @param tier The tier level
     * @return The component
     */
    public static IRocketComponent createStructure(RocketComponentType type, int tier) {
        return new BasicRocketComponent(type, tier);
    }
}