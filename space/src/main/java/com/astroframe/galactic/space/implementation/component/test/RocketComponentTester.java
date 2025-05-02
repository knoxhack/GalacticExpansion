package com.astroframe.galactic.space.implementation.component.test;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import com.astroframe.galactic.core.api.space.util.ComponentUtils;
import com.astroframe.galactic.space.implementation.component.engine.BasicChemicalEngine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Test utility class for verifying rocket components.
 * This can be used to ensure that the component system is working properly.
 */
public class RocketComponentTester {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RocketComponentTester.class);
    
    /**
     * Runs a series of tests on the rocket component system.
     * 
     * @return true if all tests pass
     */
    public static boolean runTests() {
        LOGGER.info("Starting rocket component tests...");
        
        boolean success = true;
        
        // Test basic component creation
        if (!testBasicComponentCreation()) {
            LOGGER.error("Basic component creation test failed");
            success = false;
        }
        
        // Test component serialization and deserialization
        if (!testComponentSerialization()) {
            LOGGER.error("Component serialization test failed");
            success = false;
        }
        
        // Test component factory system
        if (!testComponentFactorySystem()) {
            LOGGER.error("Component factory system test failed");
            success = false;
        }
        
        LOGGER.info(success ? "All rocket component tests passed!" : "Some rocket component tests failed!");
        return success;
    }
    
    /**
     * Tests basic component creation.
     * 
     * @return true if the test passes
     */
    private static boolean testBasicComponentCreation() {
        LOGGER.info("Testing basic component creation...");
        
        try {
            // Create a basic chemical engine
            String engineName = "Test Chemical Engine";
            String engineDesc = "A test engine for verification";
            ResourceLocation engineId = new ResourceLocation("galactic_space", "test_chemical_engine");
            
            IRocketEngine engine = new BasicChemicalEngine(engineId, 2, engineName, engineDesc);
            
            // Verify engine properties
            boolean propertiesValid = 
                engine.getType() == RocketComponentType.ENGINE &&
                engine.getEngineType() == EngineType.CHEMICAL &&
                engine.getFuelType() == FuelType.CHEMICAL &&
                engine.getName().equals(engineName) &&
                engine.getDescription().equals(engineDesc) &&
                engine.getTier() == 2 &&
                engine.getMass() > 0 &&
                engine.getThrust() > 0 &&
                engine.getEfficiency() > 0 &&
                engine.getFuelConsumptionRate() > 0 &&
                engine.getHeatGeneration() > 0 &&
                engine.canOperateInAtmosphere() &&
                engine.canOperateInSpace() &&
                !engine.getCompatibleFuels().isEmpty();
            
            if (!propertiesValid) {
                LOGGER.error("Engine properties validation failed");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in basic component creation test", e);
            return false;
        }
    }
    
    /**
     * Tests component serialization and deserialization.
     * 
     * @return true if the test passes
     */
    private static boolean testComponentSerialization() {
        LOGGER.info("Testing component serialization...");
        
        try {
            // Create a basic chemical engine
            ResourceLocation engineId = new ResourceLocation("galactic_space", "test_chemical_engine");
            IRocketEngine engine = new BasicChemicalEngine(engineId, 2, "Test Chemical Engine", "A test engine");
            
            // Serialize the engine
            CompoundTag tag = new CompoundTag();
            engine.save(tag);
            
            // Verify tag content
            if (!tag.contains("Type") || !tag.getString("Type").equals("ENGINE")) {
                LOGGER.error("Serialized tag missing or incorrect Type");
                return false;
            }
            
            if (!tag.contains("EngineType") || !tag.getString("EngineType").equals("CHEMICAL")) {
                LOGGER.error("Serialized tag missing or incorrect EngineType");
                return false;
            }
            
            if (!tag.contains("Tier") || tag.getInt("Tier") != 2) {
                LOGGER.error("Serialized tag missing or incorrect Tier");
                return false;
            }
            
            // Deserialize into a new component
            IRocketComponent deserializedEngine = ComponentUtils.createComponentFromTag(engineId, tag);
            
            // Verify the deserialized component
            if (deserializedEngine == null) {
                LOGGER.error("Deserialization failed, returned null");
                return false;
            }
            
            if (!(deserializedEngine instanceof IRocketEngine)) {
                LOGGER.error("Deserialized component is not an engine");
                return false;
            }
            
            IRocketEngine deserializedRocketEngine = (IRocketEngine) deserializedEngine;
            
            // Verify some key properties
            boolean propertiesValid = 
                deserializedRocketEngine.getType() == RocketComponentType.ENGINE &&
                deserializedRocketEngine.getEngineType() == EngineType.CHEMICAL &&
                deserializedRocketEngine.getTier() == 2;
            
            if (!propertiesValid) {
                LOGGER.error("Deserialized engine properties validation failed");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in component serialization test", e);
            return false;
        }
    }
    
    /**
     * Tests the component factory system.
     * 
     * @return true if the test passes
     */
    private static boolean testComponentFactorySystem() {
        LOGGER.info("Testing component factory system...");
        
        try {
            // Create a tag that represents a component
            ResourceLocation engineId = new ResourceLocation("galactic_space", "test_factory_engine");
            CompoundTag tag = new CompoundTag();
            tag.putString("ID", engineId.toString());
            tag.putString("Type", RocketComponentType.ENGINE.name());
            tag.putString("EngineType", EngineType.CHEMICAL.name());
            tag.putInt("Tier", 3);
            tag.putString("Name", "Factory Test Engine");
            tag.putString("Description", "An engine created through the factory system");
            
            // Try to create a component from the tag
            IRocketComponent component = ComponentUtils.createComponentFromTag(engineId, tag);
            
            // Verify the component was created
            if (component == null) {
                LOGGER.error("Factory failed to create component");
                return false;
            }
            
            // Verify it's the right type
            if (!(component instanceof IRocketEngine)) {
                LOGGER.error("Factory created wrong component type");
                return false;
            }
            
            IRocketEngine engine = (IRocketEngine) component;
            
            // Verify key properties
            boolean propertiesValid = 
                engine.getType() == RocketComponentType.ENGINE &&
                engine.getEngineType() == EngineType.CHEMICAL &&
                engine.getTier() == 3 &&
                engine.getName().equals("Factory Test Engine");
            
            if (!propertiesValid) {
                LOGGER.error("Factory-created engine properties validation failed");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in component factory system test", e);
            return false;
        }
    }
}