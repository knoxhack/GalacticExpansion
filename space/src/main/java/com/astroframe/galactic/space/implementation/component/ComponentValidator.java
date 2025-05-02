package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Validator for rocket components.
 * Ensures that components are compatible and that the rocket has all required components.
 */
public class ComponentValidator {
    
    /**
     * Validates the component list.
     *
     * @param components The components to validate
     * @param errors List to fill with error messages
     * @return true if valid
     */
    public boolean validate(List<IRocketComponent> components, List<String> errors) {
        if (components == null || components.isEmpty()) {
            errors.add("No components present. A rocket requires at least a command module and an engine.");
            return false;
        }
        
        // Count components by type
        Map<RocketComponentType, Integer> componentCounts = new HashMap<>();
        
        for (IRocketComponent component : components) {
            RocketComponentType type = component.getType();
            componentCounts.put(type, componentCounts.getOrDefault(type, 0) + 1);
        }
        
        // Check required components
        boolean hasCommandModule = componentCounts.getOrDefault(RocketComponentType.COCKPIT, 0) > 0;
        boolean hasEngine = componentCounts.getOrDefault(RocketComponentType.ENGINE, 0) > 0;
        boolean hasFuelTank = componentCounts.getOrDefault(RocketComponentType.FUEL_TANK, 0) > 0;
        
        // Required components check
        if (!hasCommandModule) {
            errors.add("Missing command module. Every rocket requires a command module.");
        }
        
        if (!hasEngine) {
            errors.add("Missing engine. Every rocket requires at least one engine.");
        }
        
        if (!hasFuelTank && hasEngine) {
            errors.add("Missing fuel tank. Engines require a fuel tank to function.");
        }
        
        // Too many components check
        if (componentCounts.getOrDefault(RocketComponentType.COCKPIT, 0) > 1) {
            errors.add("Too many command modules. Only one command module is allowed.");
        }
        
        // Weight / power ratio check
        int engineCount = componentCounts.getOrDefault(RocketComponentType.ENGINE, 0);
        int totalComponents = components.size();
        
        // Simple check: each engine can lift 5 components
        if (engineCount > 0 && totalComponents > engineCount * 5) {
            errors.add("Rocket is too heavy. Each engine can only lift 5 components.");
        }
        
        // Life support check for passenger capacity
        boolean hasLifeSupport = componentCounts.getOrDefault(RocketComponentType.LIFE_SUPPORT, 0) > 0;
        
        if (hasCommandModule && !hasLifeSupport) {
            errors.add("Warning: No life support systems. Rocket can only operate in Earth's atmosphere.");
        }
        
        // Shielding check for space travel
        boolean hasShielding = componentCounts.getOrDefault(RocketComponentType.SHIELDING, 0) > 0;
        
        if (hasCommandModule && !hasShielding) {
            errors.add("Warning: No radiation shielding. Rocket will be vulnerable to solar radiation.");
        }
        
        // If any errors exist, validation failed (warning-only errors don't count)
        for (String error : errors) {
            if (!error.startsWith("Warning:")) {
                return false;
            }
        }
        
        return true;
    }
}