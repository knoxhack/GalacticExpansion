package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Validates rocket components based on a set of rules.
 * Ensures that a rocket has the necessary components to function properly.
 */
public class ComponentValidator {
    
    /**
     * Validates a list of rocket components.
     * 
     * @param components The components to validate
     * @param errors A list to store validation errors
     * @return True if the components are valid
     */
    public boolean validateComponents(List<IRocketComponent> components, List<String> errors) {
        boolean isValid = true;
        
        // Clear any existing errors
        errors.clear();
        
        // Check if the components list is empty
        if (components.isEmpty()) {
            errors.add("No components found");
            return false;
        }
        
        // Count components by type
        Map<RocketComponentType, Integer> componentCounts = countComponentsByType(components);
        
        // Validation rule 1: Must have at least one command module
        if (!validateComponentCount(componentCounts, RocketComponentType.COCKPIT, count -> count == 1, errors,
                "Exactly one command module is required")) {
            isValid = false;
        }
        
        // Validation rule 2: Must have at least one engine
        if (!validateComponentCount(componentCounts, RocketComponentType.ENGINE, count -> count >= 1, errors,
                "At least one engine is required")) {
            isValid = false;
        }
        
        // Validation rule 3: Must have at least one fuel tank
        if (!validateComponentCount(componentCounts, RocketComponentType.FUEL_TANK, count -> count >= 1, errors,
                "At least one fuel tank is required")) {
            isValid = false;
        }
        
        // Validation rule 4: Engine count should match fuel tank count (approx)
        if (componentCounts.getOrDefault(RocketComponentType.ENGINE, 0) > 
            componentCounts.getOrDefault(RocketComponentType.FUEL_TANK, 0) * 2) {
            errors.add("Too many engines for the number of fuel tanks");
            isValid = false;
        }
        
        // Validation rule 5: Check if passenger compartments have life support
        if (componentCounts.getOrDefault(RocketComponentType.PASSENGER_COMPARTMENT, 0) > 0 && 
            componentCounts.getOrDefault(RocketComponentType.LIFE_SUPPORT, 0) == 0) {
            errors.add("Life support system required for passenger compartments");
            isValid = false;
        }
        
        // Validation rule 6: Check if engines are compatible with each other
        List<IRocketEngine> engines = components.stream()
                .filter(c -> c instanceof IRocketEngine)
                .map(c -> (IRocketEngine) c)
                .collect(Collectors.toList());
        
        if (!validateEngineCompatibility(engines, errors)) {
            isValid = false;
        }
        
        // Validation rule 7: Check total weight against engine power
        double totalWeight = calculateTotalWeight(components);
        double totalEnginePower = calculateTotalEnginePower(engines);
        
        if (totalWeight > totalEnginePower * 2) {
            errors.add("Rocket is too heavy for the engines (weight: " + 
                    String.format("%.1f", totalWeight) + ", power: " + 
                    String.format("%.1f", totalEnginePower) + ")");
            isValid = false;
        }
        
        return isValid;
    }
    
    /**
     * Counts components by type.
     * 
     * @param components The components to count
     * @return A map of component type to count
     */
    private Map<RocketComponentType, Integer> countComponentsByType(List<IRocketComponent> components) {
        Map<RocketComponentType, Integer> counts = new HashMap<>();
        
        for (IRocketComponent component : components) {
            RocketComponentType type = component.getType();
            counts.put(type, counts.getOrDefault(type, 0) + 1);
        }
        
        return counts;
    }
    
    /**
     * Validates a component count against a predicate.
     * 
     * @param counts The component counts
     * @param type The component type to check
     * @param predicate The validation predicate
     * @param errors The list to add errors to
     * @param errorMessage The error message to add if validation fails
     * @return True if the validation passes
     */
    private boolean validateComponentCount(Map<RocketComponentType, Integer> counts, 
                                           RocketComponentType type,
                                           Predicate<Integer> predicate,
                                           List<String> errors,
                                           String errorMessage) {
        int count = counts.getOrDefault(type, 0);
        if (!predicate.test(count)) {
            errors.add(errorMessage);
            return false;
        }
        return true;
    }
    
    /**
     * Validates that all engines are compatible with each other.
     * 
     * @param engines The engines to validate
     * @param errors The list to add errors to
     * @return True if all engines are compatible
     */
    private boolean validateEngineCompatibility(List<IRocketEngine> engines, List<String> errors) {
        if (engines.size() <= 1) {
            return true;
        }
        
        // Check for atmospheric vs space engines
        boolean hasAtmosphericEngines = false;
        boolean hasSpaceEngines = false;
        
        for (IRocketEngine engine : engines) {
            if (engine.canOperateInAtmosphere()) {
                hasAtmosphericEngines = true;
            }
            if (engine.canOperateInSpace()) {
                hasSpaceEngines = true;
            }
        }
        
        // For complete rockets, we want both types
        if (!hasAtmosphericEngines) {
            errors.add("No atmospheric engines found");
            return false;
        }
        
        if (!hasSpaceEngines) {
            errors.add("No space engines found");
            return false;
        }
        
        return true;
    }
    
    /**
     * Calculates the total weight of all components.
     * 
     * @param components The components to calculate weight for
     * @return The total weight
     */
    private double calculateTotalWeight(List<IRocketComponent> components) {
        return components.stream()
                .mapToDouble(IRocketComponent::getMass)
                .sum();
    }
    
    /**
     * Calculates the total power of all engines.
     * 
     * @param engines The engines to calculate power for
     * @return The total power
     */
    private double calculateTotalEnginePower(List<IRocketEngine> engines) {
        return engines.stream()
                .mapToDouble(engine -> engine.getThrust() * engine.getEfficiency())
                .sum();
    }
}