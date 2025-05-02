package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Validates rocket component configurations.
 * Ensures that a rocket has all required components and that they are properly positioned.
 */
public class ComponentValidator {
    
    // Constants for validation
    private static final int MIN_COMPONENTS = 3;
    private static final int MAX_COMPONENTS = 20;
    
    /**
     * Validates a list of components to ensure they form a valid rocket.
     *
     * @param components The components to validate
     * @param errors Output list to store validation errors
     * @return true if the configuration is valid
     */
    public boolean validate(List<IRocketComponent> components, List<String> errors) {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        
        // Clear any existing errors
        errors.clear();
        
        // Check if components is null or empty
        if (components == null || components.isEmpty()) {
            errors.add("No components provided. A rocket requires at least a command module, engine, and fuel tank.");
            return false;
        }
        
        // Check minimum and maximum component count
        if (components.size() < MIN_COMPONENTS) {
            errors.add("Too few components. A rocket requires at least " + MIN_COMPONENTS + " components.");
            return false;
        }
        
        if (components.size() > MAX_COMPONENTS) {
            errors.add("Too many components. A rocket cannot have more than " + MAX_COMPONENTS + " components.");
            return false;
        }
        
        // Group components by type
        Map<RocketComponentType, List<IRocketComponent>> componentsByType = 
                components.stream().collect(Collectors.groupingBy(IRocketComponent::getType));
        
        // Validate required components
        boolean hasCommandModule = componentsByType.containsKey(RocketComponentType.COCKPIT) && 
                                  !componentsByType.get(RocketComponentType.COCKPIT).isEmpty();
        boolean hasEngine = componentsByType.containsKey(RocketComponentType.ENGINE) && 
                           !componentsByType.get(RocketComponentType.ENGINE).isEmpty();
        boolean hasFuelTank = componentsByType.containsKey(RocketComponentType.FUEL_TANK) && 
                             !componentsByType.get(RocketComponentType.FUEL_TANK).isEmpty();
        
        if (!hasCommandModule) {
            errors.add("Missing command module. Every rocket requires at least one command module.");
        }
        
        if (!hasEngine) {
            errors.add("Missing engine. Every rocket requires at least one engine.");
        }
        
        if (!hasFuelTank) {
            errors.add("Missing fuel tank. Every rocket requires at least one fuel tank.");
        }
        
        // Early exit if missing required components
        if (!hasCommandModule || !hasEngine || !hasFuelTank) {
            return false;
        }
        
        // Check for component positions - command module should be at top
        List<IRocketComponent> commandModules = componentsByType.get(RocketComponentType.COCKPIT);
        boolean isCommandModuleAtTop = false;
        
        for (IRocketComponent commandModule : commandModules) {
            if (commandModule.getPosition().y > 0) {
                isCommandModuleAtTop = true;
                break;
            }
        }
        
        if (!isCommandModuleAtTop) {
            errors.add("Command module must be positioned at the top of the rocket.");
        }
        
        // Check for engine positions - engines should be at bottom
        List<IRocketComponent> engines = componentsByType.get(RocketComponentType.ENGINE);
        boolean isEngineAtBottom = false;
        
        for (IRocketComponent engine : engines) {
            if (engine.getPosition().y < 0) {
                isEngineAtBottom = true;
                break;
            }
        }
        
        if (!isEngineAtBottom) {
            errors.add("Engines must be positioned at the bottom of the rocket.");
        }
        
        // Check basic component compatibility
        boolean hasCompatibilityIssues = checkComponentCompatibility(componentsByType, errors);
        
        // Calculate weight and thrust
        double totalWeight = calculateTotalWeight(components);
        double totalThrust = calculateTotalThrust(componentsByType.getOrDefault(RocketComponentType.ENGINE, new ArrayList<>()));
        
        // Check if thrust is sufficient for weight
        if (totalThrust < totalWeight * 1.2) { // Require 20% more thrust than weight for safety
            errors.add("Insufficient thrust. The engines need to provide at least 20% more thrust than the total weight.");
        }
        
        // Check if fuel capacity is adequate for the thrust
        double totalFuelCapacity = calculateTotalFuelCapacity(componentsByType.getOrDefault(RocketComponentType.FUEL_TANK, new ArrayList<>()));
        double fuelConsumptionRate = totalThrust * 0.1; // Simplified fuel consumption model
        
        // Minimum flight time in seconds
        double minFlightTime = 60.0;
        
        if (fuelConsumptionRate > 0 && totalFuelCapacity / fuelConsumptionRate < minFlightTime) {
            errors.add("Insufficient fuel capacity. The rocket needs fuel for at least " + minFlightTime + " seconds of flight.");
        }
        
        // Return true if no errors
        return errors.isEmpty();
    }
    
    /**
     * Checks for compatibility issues between components.
     *
     * @param componentsByType Components grouped by type
     * @param errors Output list for validation errors
     * @return true if there are compatibility issues
     */
    private boolean checkComponentCompatibility(Map<RocketComponentType, List<IRocketComponent>> componentsByType, List<String> errors) {
        boolean hasIssues = false;
        
        // Check for advanced engine compatibility
        List<IRocketComponent> engines = componentsByType.getOrDefault(RocketComponentType.ENGINE, new ArrayList<>());
        List<IRocketComponent> fuelTanks = componentsByType.getOrDefault(RocketComponentType.FUEL_TANK, new ArrayList<>());
        
        boolean hasIonEngine = false;
        boolean hasChemicalEngine = false;
        boolean hasIonFuelTank = false;
        boolean hasChemicalFuelTank = false;
        
        // Simplified check for demonstration - in a real implementation, we would use actual engine types
        for (IRocketComponent engine : engines) {
            if (engine.getName().contains("ion")) {
                hasIonEngine = true;
            } else {
                hasChemicalEngine = true;
            }
        }
        
        for (IRocketComponent fuelTank : fuelTanks) {
            if (fuelTank.getName().contains("ion")) {
                hasIonFuelTank = true;
            } else {
                hasChemicalFuelTank = true;
            }
        }
        
        // Check for mismatched engine and fuel tank types
        if (hasIonEngine && !hasIonFuelTank) {
            errors.add("Ion engines require compatible ion fuel tanks.");
            hasIssues = true;
        }
        
        if (hasChemicalEngine && !hasChemicalFuelTank) {
            errors.add("Chemical engines require compatible chemical fuel tanks.");
            hasIssues = true;
        }
        
        // Check for life support requirements (needed for manned rockets)
        boolean hasCrewModule = componentsByType.containsKey(RocketComponentType.CREW_MODULE) && 
                               !componentsByType.get(RocketComponentType.CREW_MODULE).isEmpty();
        boolean hasLifeSupport = componentsByType.containsKey(RocketComponentType.LIFE_SUPPORT) && 
                                !componentsByType.get(RocketComponentType.LIFE_SUPPORT).isEmpty();
        
        if (hasCrewModule && !hasLifeSupport) {
            errors.add("Crew modules require life support systems.");
            hasIssues = true;
        }
        
        return hasIssues;
    }
    
    /**
     * Calculates the total weight of all components.
     *
     * @param components The list of components
     * @return The total weight
     */
    private double calculateTotalWeight(List<IRocketComponent> components) {
        double totalWeight = 0.0;
        
        for (IRocketComponent component : components) {
            totalWeight += component.getWeight();
        }
        
        return totalWeight;
    }
    
    /**
     * Calculates the total thrust from all engines.
     *
     * @param engines The list of engine components
     * @return The total thrust
     */
    private double calculateTotalThrust(List<IRocketComponent> engines) {
        double totalThrust = 0.0;
        
        for (IRocketComponent engine : engines) {
            // In a real implementation, we would access a specific thrust property
            totalThrust += engine.getPower(); // Using power as a proxy for thrust
        }
        
        return totalThrust;
    }
    
    /**
     * Calculates the total fuel capacity from all fuel tanks.
     *
     * @param fuelTanks The list of fuel tank components
     * @return The total fuel capacity
     */
    private double calculateTotalFuelCapacity(List<IRocketComponent> fuelTanks) {
        double totalCapacity = 0.0;
        
        for (IRocketComponent fuelTank : fuelTanks) {
            // In a real implementation, we would access a specific capacity property
            totalCapacity += fuelTank.getCapacity();
        }
        
        return totalCapacity;
    }
}