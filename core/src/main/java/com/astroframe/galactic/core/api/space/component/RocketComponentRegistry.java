package com.astroframe.galactic.core.api.space.component;

import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Registry for all rocket components.
 * Provides methods to register and retrieve components.
 */
public class RocketComponentRegistry {
    
    private static final Map<ResourceLocation, IRocketComponent> COMPONENTS = new HashMap<>();
    
    /**
     * Registers a rocket component.
     * @param component The component to register
     * @return True if registration was successful
     */
    public static boolean register(IRocketComponent component) {
        if (!COMPONENTS.containsKey(component.getId())) {
            COMPONENTS.put(component.getId(), component);
            return true;
        }
        return false;
    }
    
    /**
     * Gets a component by its ID.
     * @param id The component ID
     * @return The component, or empty if not found
     */
    public static Optional<IRocketComponent> getComponent(ResourceLocation id) {
        return Optional.ofNullable(COMPONENTS.get(id));
    }
    
    /**
     * Gets all registered components.
     * @return An unmodifiable set of all components
     */
    public static Set<IRocketComponent> getAllComponents() {
        return Collections.unmodifiableSet(COMPONENTS.values().stream().collect(Collectors.toSet()));
    }
    
    /**
     * Gets all components of a specific type.
     * @param type The component type
     * @return A set of components matching the type
     */
    public static Set<IRocketComponent> getComponentsByType(IRocketComponent.ComponentType type) {
        return COMPONENTS.values().stream()
                .filter(component -> component.getType() == type)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all components of a specific tier.
     * @param tier The component tier
     * @return A set of components matching the tier
     */
    public static Set<IRocketComponent> getComponentsByTier(int tier) {
        return COMPONENTS.values().stream()
                .filter(component -> component.getTier() == tier)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all components matching a predicate.
     * @param predicate The predicate to match
     * @return A set of components matching the predicate
     */
    public static Set<IRocketComponent> getComponentsMatching(Predicate<IRocketComponent> predicate) {
        return COMPONENTS.values().stream()
                .filter(predicate)
                .collect(Collectors.toSet());
    }
    
    /**
     * Finds a component by its ID string.
     * @param id The component ID string
     * @return The component, or empty if not found
     */
    public static Optional<IRocketComponent> getComponent(String id) {
        try {
            return getComponent(ResourceLocation.parse(id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    /**
     * Gets all engine components.
     * @return A set of all engine components
     */
    public static Set<IRocketEngine> getAllEngines() {
        return getComponentsByType(IRocketComponent.ComponentType.ENGINE).stream()
                .filter(component -> component instanceof IRocketEngine)
                .map(component -> (IRocketEngine) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all command module components.
     * @return A set of all command module components
     */
    public static Set<ICommandModule> getAllCommandModules() {
        return getComponentsByType(IRocketComponent.ComponentType.COMMAND_MODULE).stream()
                .filter(component -> component instanceof ICommandModule)
                .map(component -> (ICommandModule) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all fuel tank components.
     * @return A set of all fuel tank components
     */
    public static Set<IFuelTank> getAllFuelTanks() {
        return getComponentsByType(IRocketComponent.ComponentType.FUEL_TANK).stream()
                .filter(component -> component instanceof IFuelTank)
                .map(component -> (IFuelTank) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all cargo bay components.
     * @return A set of all cargo bay components
     */
    public static Set<ICargoBay> getAllCargoBays() {
        return getComponentsByType(IRocketComponent.ComponentType.CARGO_BAY).stream()
                .filter(component -> component instanceof ICargoBay)
                .map(component -> (ICargoBay) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all passenger compartment components.
     * @return A set of all passenger compartment components
     */
    public static Set<IPassengerCompartment> getAllPassengerCompartments() {
        return getComponentsByType(IRocketComponent.ComponentType.PASSENGER_COMPARTMENT).stream()
                .filter(component -> component instanceof IPassengerCompartment)
                .map(component -> (IPassengerCompartment) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all shield components.
     * @return A set of all shield components
     */
    public static Set<IShield> getAllShields() {
        return getComponentsByType(IRocketComponent.ComponentType.SHIELD).stream()
                .filter(component -> component instanceof IShield)
                .map(component -> (IShield) component)
                .collect(Collectors.toSet());
    }
    
    /**
     * Gets all life support components.
     * @return A set of all life support components
     */
    public static Set<ILifeSupport> getAllLifeSupports() {
        return getComponentsByType(IRocketComponent.ComponentType.LIFE_SUPPORT).stream()
                .filter(component -> component instanceof ILifeSupport)
                .map(component -> (ILifeSupport) component)
                .collect(Collectors.toSet());
    }
}