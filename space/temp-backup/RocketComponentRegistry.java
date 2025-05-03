package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.*;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Registry for rocket components.
 * This class manages registration and retrieval of all rocket components.
 */
public class RocketComponentRegistry {
    
    private static final Map<ResourceLocation, IRocketComponent> COMPONENTS = new HashMap<>();
    private static final Map<ResourceLocation, ICommandModule> COMMAND_MODULES = new HashMap<>();
    private static final Map<ResourceLocation, IRocketEngine> ENGINES = new HashMap<>();
    private static final Map<ResourceLocation, IFuelTank> FUEL_TANKS = new HashMap<>();
    private static final Map<ResourceLocation, ICargoBay> CARGO_BAYS = new HashMap<>();
    private static final Map<ResourceLocation, IPassengerCompartment> PASSENGER_COMPARTMENTS = new HashMap<>();
    private static final Map<ResourceLocation, IShield> SHIELDS = new HashMap<>();
    private static final Map<ResourceLocation, ILifeSupport> LIFE_SUPPORTS = new HashMap<>();
    
    /**
     * Registers a rocket component.
     * @param component The component to register
     * @return The registered component
     * @param <T> The component type
     */
    public static <T extends IRocketComponent> T register(T component) {
        ResourceLocation id = component.getId();
        
        if (COMPONENTS.containsKey(id)) {
            throw new IllegalArgumentException("Component with ID " + id + " already registered");
        }
        
        COMPONENTS.put(id, component);
        
        if (component instanceof ICommandModule commandModule) {
            COMMAND_MODULES.put(id, commandModule);
        } else if (component instanceof IRocketEngine engine) {
            ENGINES.put(id, engine);
        } else if (component instanceof IFuelTank fuelTank) {
            FUEL_TANKS.put(id, fuelTank);
        } else if (component instanceof ICargoBay cargoBay) {
            CARGO_BAYS.put(id, cargoBay);
        } else if (component instanceof IPassengerCompartment passengerCompartment) {
            PASSENGER_COMPARTMENTS.put(id, passengerCompartment);
        } else if (component instanceof IShield shield) {
            SHIELDS.put(id, shield);
        } else if (component instanceof ILifeSupport lifeSupport) {
            LIFE_SUPPORTS.put(id, lifeSupport);
        }
        
        return component;
    }
    
    /**
     * Gets a component by ID.
     * @param id The component ID
     * @return The component, or null if not found
     */
    public static IRocketComponent getComponent(ResourceLocation id) {
        return COMPONENTS.get(id);
    }
    
    /**
     * Gets a command module by ID.
     * @param id The component ID
     * @return The command module, or null if not found
     */
    public static ICommandModule getCommandModule(ResourceLocation id) {
        return COMMAND_MODULES.get(id);
    }
    
    /**
     * Gets an engine by ID.
     * @param id The component ID
     * @return The engine, or null if not found
     */
    public static IRocketEngine getEngine(ResourceLocation id) {
        return ENGINES.get(id);
    }
    
    /**
     * Gets a fuel tank by ID.
     * @param id The component ID
     * @return The fuel tank, or null if not found
     */
    public static IFuelTank getFuelTank(ResourceLocation id) {
        return FUEL_TANKS.get(id);
    }
    
    /**
     * Gets a cargo bay by ID.
     * @param id The component ID
     * @return The cargo bay, or null if not found
     */
    public static ICargoBay getCargoBay(ResourceLocation id) {
        return CARGO_BAYS.get(id);
    }
    
    /**
     * Gets a passenger compartment by ID.
     * @param id The component ID
     * @return The passenger compartment, or null if not found
     */
    public static IPassengerCompartment getPassengerCompartment(ResourceLocation id) {
        return PASSENGER_COMPARTMENTS.get(id);
    }
    
    /**
     * Gets a shield by ID.
     * @param id The component ID
     * @return The shield, or null if not found
     */
    public static IShield getShield(ResourceLocation id) {
        return SHIELDS.get(id);
    }
    
    /**
     * Gets a life support system by ID.
     * @param id The component ID
     * @return The life support system, or null if not found
     */
    public static ILifeSupport getLifeSupport(ResourceLocation id) {
        return LIFE_SUPPORTS.get(id);
    }
    
    /**
     * Gets all registered components.
     * @return A stream of all components
     */
    public static Stream<IRocketComponent> getAllComponents() {
        return COMPONENTS.values().stream();
    }
    
    /**
     * Gets all registered command modules.
     * @return A stream of all command modules
     */
    public static Stream<ICommandModule> getAllCommandModules() {
        return COMMAND_MODULES.values().stream();
    }
    
    /**
     * Gets all registered engines.
     * @return A stream of all engines
     */
    public static Stream<IRocketEngine> getAllEngines() {
        return ENGINES.values().stream();
    }
    
    /**
     * Gets all registered fuel tanks.
     * @return A stream of all fuel tanks
     */
    public static Stream<IFuelTank> getAllFuelTanks() {
        return FUEL_TANKS.values().stream();
    }
    
    /**
     * Gets all registered cargo bays.
     * @return A stream of all cargo bays
     */
    public static Stream<ICargoBay> getAllCargoBays() {
        return CARGO_BAYS.values().stream();
    }
    
    /**
     * Gets all registered passenger compartments.
     * @return A stream of all passenger compartments
     */
    public static Stream<IPassengerCompartment> getAllPassengerCompartments() {
        return PASSENGER_COMPARTMENTS.values().stream();
    }
    
    /**
     * Gets all registered shields.
     * @return A stream of all shields
     */
    public static Stream<IShield> getAllShields() {
        return SHIELDS.values().stream();
    }
    
    /**
     * Gets all registered life support systems.
     * @return A stream of all life support systems
     */
    public static Stream<ILifeSupport> getAllLifeSupports() {
        return LIFE_SUPPORTS.values().stream();
    }
    
    /**
     * Gets the number of registered components.
     * @return The component count
     */
    public static int getComponentCount() {
        return COMPONENTS.size();
    }
}