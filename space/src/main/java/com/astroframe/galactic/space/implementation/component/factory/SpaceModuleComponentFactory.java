package com.astroframe.galactic.space.implementation.component.factory;

import com.astroframe.galactic.core.TagHelper;
import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import com.astroframe.galactic.core.api.space.util.ComponentUtils;
import com.astroframe.galactic.space.implementation.component.cargobay.StandardCargoBay;
import com.astroframe.galactic.space.implementation.component.command.BasicCommandModule;
import com.astroframe.galactic.space.implementation.component.engine.BasicChemicalEngine;
import com.astroframe.galactic.space.implementation.component.fueltank.StandardFuelTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

/**
 * Factory for creating components from the space module.
 * Implements the ComponentFactory interface to integrate with the core component system.
 */
public class SpaceModuleComponentFactory implements ComponentUtils.ComponentFactory {

    // List of component namespaces handled by this factory
    private static final String NAMESPACE = "galactic_space";
    
    @Override
    public Optional<IRocketComponent> createFromTag(ResourceLocation id, CompoundTag tag) {
        // Check if this is a rocket component type
        String typeStr = TagHelper.getString(tag, "Type", "");
        if (typeStr.isEmpty()) {
            return Optional.empty();
        }
        
        RocketComponentType type;
        try {
            type = RocketComponentType.valueOf(typeStr);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        
        // Create appropriate component based on type
        switch (type) {
            case ENGINE:
                return createEngine(id, tag);
            case FUEL_TANK:
                return createFuelTank(id, tag);
            case COMMAND_MODULE:
                return createCommandModule(id, tag);
            case CARGO_BAY:
            case STORAGE: // Handle alias for backward compatibility
                return createCargoBay(id, tag);
            default:
                return Optional.empty();
        }
    }
    
    /**
     * Creates an engine component from tag data.
     */
    private Optional<IRocketComponent> createEngine(ResourceLocation id, CompoundTag tag) {
        // Get engine type
        String engineTypeStr = TagHelper.getString(tag, "EngineType", "");
        if (engineTypeStr.isEmpty()) {
            return Optional.empty();
        }
        
        EngineType engineType;
        try {
            engineType = EngineType.valueOf(engineTypeStr);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        
        // Extract common properties
        int tier = TagHelper.getInt(tag, "Tier", 1);
        String name = TagHelper.getString(tag, "Name", "Unnamed Engine");
        String description = TagHelper.getString(tag, "Description", "A rocket engine.");
        
        // Create the appropriate engine based on engine type
        IRocketComponent engine;
        switch (engineType) {
            case CHEMICAL:
                engine = new BasicChemicalEngine(id, tier, name, description);
                break;
            default:
                return Optional.empty();
        }
        
        // Load saved state
        engine.load(tag);
        
        return Optional.of(engine);
    }
    
    /**
     * Creates a fuel tank component from tag data.
     */
    private Optional<IRocketComponent> createFuelTank(ResourceLocation id, CompoundTag tag) {
        // Get fuel type
        String fuelTypeStr = TagHelper.getString(tag, "FuelType", "");
        if (fuelTypeStr.isEmpty()) {
            return Optional.empty();
        }
        
        FuelType fuelType;
        try {
            fuelType = FuelType.valueOf(fuelTypeStr);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
        
        // Extract common properties
        String name = TagHelper.getString(tag, "Name", "Standard Fuel Tank");
        String description = TagHelper.getString(tag, "Description", "A standard fuel tank.");
        int tier = TagHelper.getInt(tag, "Tier", 1);
        int mass = TagHelper.getInt(tag, "Mass", 50);
        int maxDurability = TagHelper.getInt(tag, "MaxDurability", 100);
        int maxFuelCapacity = TagHelper.getInt(tag, "MaxFuelCapacity", 1000);
        float leakResistance = TagHelper.getFloat(tag, "LeakResistance", 0.8f);
        float explosionResistance = TagHelper.getFloat(tag, "ExplosionResistance", 0.7f);
        
        // Create the fuel tank
        StandardFuelTank fuelTank = new StandardFuelTank(
            id, name, description, tier, mass, maxDurability, maxFuelCapacity,
            fuelType, leakResistance, explosionResistance
        );
        
        // Load saved state
        fuelTank.load(tag);
        
        return Optional.of(fuelTank);
    }
    
    /**
     * Creates a command module component from tag data.
     */
    private Optional<IRocketComponent> createCommandModule(ResourceLocation id, CompoundTag tag) {
        // Extract common properties
        String name = TagHelper.getString(tag, "Name", "Basic Command Module");
        String description = TagHelper.getString(tag, "Description", "A basic command module.");
        int tier = TagHelper.getInt(tag, "Tier", 1);
        
        // Create basic command module with tier
        BasicCommandModule commandModule = new BasicCommandModule(id, tier, name, description);
        
        // Load saved state
        commandModule.load(tag);
        
        return Optional.of(commandModule);
    }
    
    /**
     * Creates a cargo bay component from tag data.
     */
    private Optional<IRocketComponent> createCargoBay(ResourceLocation id, CompoundTag tag) {
        // Extract common properties
        String name = TagHelper.getString(tag, "Name", "Standard Cargo Bay");
        String description = TagHelper.getString(tag, "Description", "A standard cargo bay.");
        int tier = TagHelper.getInt(tag, "Tier", 1);
        int mass = TagHelper.getInt(tag, "Mass", 40);
        int maxCapacity = TagHelper.getInt(tag, "MaxCapacity", 800);
        boolean securityFeatures = TagHelper.getBoolean(tag, "SecurityFeatures", tier >= 2);
        boolean environmentControl = TagHelper.getBoolean(tag, "EnvironmentControl", tier >= 2);
        boolean automatedLoading = TagHelper.getBoolean(tag, "AutomatedLoading", tier >= 3);
        
        // Create standard cargo bay
        StandardCargoBay cargoBay = new StandardCargoBay(
            id, name, description, tier, mass, maxCapacity,
            securityFeatures, environmentControl, automatedLoading
        );
        
        // Load saved state
        cargoBay.load(tag);
        
        return Optional.of(cargoBay);
    }
    
    @Override
    public boolean canHandle(ResourceLocation id) {
        // Check if this factory handles this component
        return id.getNamespace().equals(NAMESPACE);
    }
}