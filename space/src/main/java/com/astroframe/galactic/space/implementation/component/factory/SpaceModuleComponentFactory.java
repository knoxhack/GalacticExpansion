package com.astroframe.galactic.space.implementation.component.factory;

import com.astroframe.galactic.core.TagHelper;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.util.ComponentUtils;
import com.astroframe.galactic.space.implementation.component.engine.BasicChemicalEngine;
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
    
    @Override
    public boolean canHandle(ResourceLocation id) {
        // Check if this factory handles this component
        return id.getNamespace().equals(NAMESPACE);
    }
}