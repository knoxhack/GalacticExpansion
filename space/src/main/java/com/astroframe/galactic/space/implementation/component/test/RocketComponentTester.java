package com.astroframe.galactic.space.implementation.component.test;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
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
        
        // Test fuel tank creation and functionality
        if (!testFuelTankCreation()) {
            LOGGER.error("Fuel tank creation test failed");
            success = false;
        }
        
        // Test command module creation and functionality
        if (!testCommandModuleCreation()) {
            LOGGER.error("Command module creation test failed");
            success = false;
        }
        
        // Test cargo bay creation and functionality
        if (!testCargoBayCreation()) {
            LOGGER.error("Cargo bay creation test failed");
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
     * Tests the fuel tank creation and functionality.
     * 
     * @return true if the test passes
     */
    private static boolean testFuelTankCreation() {
        LOGGER.info("Testing fuel tank creation and functionality...");
        
        try {
            // Create a standard fuel tank
            ResourceLocation tankId = new ResourceLocation("galactic_space", "test_fuel_tank");
            String tankName = "Test Chemical Fuel Tank";
            String tankDesc = "A test fuel tank for verification";
            int tier = 2;
            int mass = 75;
            int maxDurability = 150;
            int maxFuelCapacity = 2000;
            float leakResistance = 0.85f;
            float explosionResistance = 0.75f;
            
            IFuelTank fuelTank = new StandardFuelTank(
                tankId, tankName, tankDesc, tier, mass, maxDurability,
                maxFuelCapacity, FuelType.CHEMICAL, leakResistance, explosionResistance
            );
            
            // Verify fuel tank properties
            boolean propertiesValid = 
                fuelTank.getType() == RocketComponentType.FUEL_TANK &&
                fuelTank.getFuelType() == FuelType.CHEMICAL &&
                fuelTank.getName().equals(tankName) &&
                fuelTank.getDescription().equals(tankDesc) &&
                fuelTank.getTier() == tier &&
                fuelTank.getMass() == mass &&
                fuelTank.getMaxDurability() == maxDurability &&
                fuelTank.getCurrentDurability() == maxDurability &&
                fuelTank.getMaxFuelCapacity() == maxFuelCapacity &&
                fuelTank.getCurrentFuelLevel() == 0 &&
                fuelTank.getLeakResistance() == leakResistance &&
                fuelTank.getExplosionResistance() == explosionResistance;
            
            if (!propertiesValid) {
                LOGGER.error("Fuel tank properties validation failed");
                return false;
            }
            
            // Test fuel functionality
            int fuelToAdd = 1000;
            int addedFuel = fuelTank.addFuel(fuelToAdd);
            if (addedFuel != fuelToAdd || fuelTank.getCurrentFuelLevel() != fuelToAdd) {
                LOGGER.error("Fuel tank add fuel operation failed");
                return false;
            }
            
            int fuelToConsume = 400;
            int consumedFuel = fuelTank.consumeFuel(fuelToConsume);
            if (consumedFuel != fuelToConsume || fuelTank.getCurrentFuelLevel() != (fuelToAdd - fuelToConsume)) {
                LOGGER.error("Fuel tank consume fuel operation failed");
                return false;
            }
            
            // Test durability functionality
            int damageAmount = 50;
            fuelTank.damage(damageAmount);
            if (fuelTank.getCurrentDurability() != (maxDurability - damageAmount)) {
                LOGGER.error("Fuel tank damage operation failed");
                return false;
            }
            
            int repairAmount = 20;
            fuelTank.repair(repairAmount);
            if (fuelTank.getCurrentDurability() != (maxDurability - damageAmount + repairAmount)) {
                LOGGER.error("Fuel tank repair operation failed");
                return false;
            }
            
            // Test serialization and deserialization
            CompoundTag tag = new CompoundTag();
            fuelTank.save(tag);
            
            if (!tag.contains("Type") || !tag.getString("Type").equals(RocketComponentType.FUEL_TANK.name())) {
                LOGGER.error("Fuel tank serialization missing or incorrect Type");
                return false;
            }
            
            if (!tag.contains("FuelType") || !tag.getString("FuelType").equals(FuelType.CHEMICAL.name())) {
                LOGGER.error("Fuel tank serialization missing or incorrect FuelType");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in fuel tank creation test", e);
            return false;
        }
    }
    
    /**
     * Tests the command module creation and functionality.
     * 
     * @return true if the test passes
     */
    private static boolean testCommandModuleCreation() {
        LOGGER.info("Testing command module creation and functionality...");
        
        try {
            // Create a basic command module
            ResourceLocation moduleId = new ResourceLocation("galactic_space", "test_command_module");
            String moduleName = "Test Command Module";
            String moduleDesc = "A test command module for verification";
            int tier = 2;
            
            BasicCommandModule commandModule = new BasicCommandModule(moduleId, tier, moduleName, moduleDesc);
            
            // Verify command module properties
            boolean propertiesValid = 
                commandModule.getType() == RocketComponentType.COMMAND_MODULE &&
                commandModule.getName().equals(moduleName) &&
                commandModule.getDescription().equals(moduleDesc) &&
                commandModule.getTier() == tier &&
                commandModule.getMass() > 0 &&
                commandModule.getMaxDurability() > 0 &&
                commandModule.getCurrentDurability() == commandModule.getMaxDurability() &&
                commandModule.getComputingPower() > 0 &&
                commandModule.getSensorStrength() > 0 &&
                commandModule.getNavigationAccuracy() > 0 &&
                commandModule.getCrewCapacity() > 0 &&
                commandModule.hasAdvancedLifeSupport() == (tier >= 2) &&
                commandModule.hasAutomatedLanding() == (tier >= 2) &&
                commandModule.hasEmergencyEvacuation() == (tier >= 3);
            
            if (!propertiesValid) {
                LOGGER.error("Command module properties validation failed");
                return false;
            }
            
            // Test durability functionality
            int damageAmount = 30;
            commandModule.damage(damageAmount);
            if (commandModule.getCurrentDurability() != (commandModule.getMaxDurability() - damageAmount)) {
                LOGGER.error("Command module damage operation failed");
                return false;
            }
            
            int repairAmount = 15;
            commandModule.repair(repairAmount);
            if (commandModule.getCurrentDurability() != (commandModule.getMaxDurability() - damageAmount + repairAmount)) {
                LOGGER.error("Command module repair operation failed");
                return false;
            }
            
            // Test serialization
            CompoundTag tag = new CompoundTag();
            commandModule.save(tag);
            
            if (!tag.contains("Type") || !tag.getString("Type").equals(RocketComponentType.COMMAND_MODULE.name())) {
                LOGGER.error("Command module serialization missing or incorrect Type");
                return false;
            }
            
            if (!tag.contains("ComputingPower") || tag.getInt("ComputingPower") != commandModule.getComputingPower()) {
                LOGGER.error("Command module serialization missing or incorrect ComputingPower");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in command module creation test", e);
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
            // Test 1: Engine creation through factory
            if (!testFactoryEngineCreation()) {
                return false;
            }
            
            // Test 2: Fuel tank creation through factory
            if (!testFactoryFuelTankCreation()) {
                return false;
            }
            
            // Test 3: Command module creation through factory
            if (!testFactoryCommandModuleCreation()) {
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in component factory system test", e);
            return false;
        }
    }
    
    /**
     * Tests engine creation through the factory system.
     * 
     * @return true if the test passes
     */
    private static boolean testFactoryEngineCreation() {
        LOGGER.info("Testing engine creation through factory...");
        
        try {
            // Create a tag that represents an engine component
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
                LOGGER.error("Factory failed to create engine component");
                return false;
            }
            
            // Verify it's the right type
            if (!(component instanceof IRocketEngine)) {
                LOGGER.error("Factory created wrong component type for engine");
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
            LOGGER.error("Error in factory engine creation test", e);
            return false;
        }
    }
    
    /**
     * Tests fuel tank creation through the factory system.
     * 
     * @return true if the test passes
     */
    private static boolean testFactoryFuelTankCreation() {
        LOGGER.info("Testing fuel tank creation through factory...");
        
        try {
            // Create a tag that represents a fuel tank component
            ResourceLocation tankId = new ResourceLocation("galactic_space", "test_factory_fuel_tank");
            CompoundTag tag = new CompoundTag();
            tag.putString("ID", tankId.toString());
            tag.putString("Type", RocketComponentType.FUEL_TANK.name());
            tag.putString("FuelType", FuelType.LIQUID.name());
            tag.putInt("Tier", 2);
            tag.putString("Name", "Factory Test Fuel Tank");
            tag.putString("Description", "A fuel tank created through the factory system");
            tag.putInt("MaxFuelCapacity", 2500);
            tag.putFloat("LeakResistance", 0.9f);
            tag.putFloat("ExplosionResistance", 0.8f);
            
            // Try to create a component from the tag
            IRocketComponent component = ComponentUtils.createComponentFromTag(tankId, tag);
            
            // Verify the component was created
            if (component == null) {
                LOGGER.error("Factory failed to create fuel tank component");
                return false;
            }
            
            // Verify it's the right type
            if (!(component instanceof IFuelTank)) {
                LOGGER.error("Factory created wrong component type for fuel tank");
                return false;
            }
            
            IFuelTank fuelTank = (IFuelTank) component;
            
            // Verify key properties
            boolean propertiesValid = 
                fuelTank.getType() == RocketComponentType.FUEL_TANK &&
                fuelTank.getFuelType() == FuelType.LIQUID &&
                fuelTank.getTier() == 2 &&
                fuelTank.getName().equals("Factory Test Fuel Tank") &&
                fuelTank.getMaxFuelCapacity() == 2500 &&
                fuelTank.getLeakResistance() == 0.9f &&
                fuelTank.getExplosionResistance() == 0.8f;
            
            if (!propertiesValid) {
                LOGGER.error("Factory-created fuel tank properties validation failed");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in factory fuel tank creation test", e);
            return false;
        }
    }
    
    /**
     * Tests command module creation through the factory system.
     * 
     * @return true if the test passes
     */
    private static boolean testFactoryCommandModuleCreation() {
        LOGGER.info("Testing command module creation through factory...");
        
        try {
            // Create a tag that represents a command module component
            ResourceLocation moduleId = new ResourceLocation("galactic_space", "test_factory_command_module");
            CompoundTag tag = new CompoundTag();
            tag.putString("ID", moduleId.toString());
            tag.putString("Type", RocketComponentType.COMMAND_MODULE.name());
            tag.putInt("Tier", 3);
            tag.putString("Name", "Factory Test Command Module");
            tag.putString("Description", "A command module created through the factory system");
            tag.putInt("ComputingPower", 100);
            tag.putInt("SensorStrength", 80);
            tag.putFloat("NavigationAccuracy", 0.95f);
            tag.putInt("CrewCapacity", 3);
            tag.putBoolean("AdvancedLifeSupport", true);
            tag.putBoolean("AutomatedLanding", true);
            tag.putBoolean("EmergencyEvacuation", true);
            
            // Try to create a component from the tag
            IRocketComponent component = ComponentUtils.createComponentFromTag(moduleId, tag);
            
            // Verify the component was created
            if (component == null) {
                LOGGER.error("Factory failed to create command module component");
                return false;
            }
            
            // Verify it's the right type
            if (!(component instanceof ICommandModule)) {
                LOGGER.error("Factory created wrong component type for command module");
                return false;
            }
            
            ICommandModule commandModule = (ICommandModule) component;
            
            // Verify key properties
            boolean propertiesValid = 
                commandModule.getType() == RocketComponentType.COMMAND_MODULE &&
                commandModule.getTier() == 3 &&
                commandModule.getName().equals("Factory Test Command Module") &&
                commandModule.getComputingPower() == 100 &&
                commandModule.getSensorStrength() == 80 &&
                commandModule.getNavigationAccuracy() == 0.95f &&
                commandModule.getCrewCapacity() == 3 &&
                commandModule.hasAdvancedLifeSupport() &&
                commandModule.hasAutomatedLanding() &&
                commandModule.hasEmergencyEvacuation();
            
            if (!propertiesValid) {
                LOGGER.error("Factory-created command module properties validation failed");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in factory command module creation test", e);
            return false;
        }
    }
    
    /**
     * Tests the cargo bay creation and functionality.
     * 
     * @return true if the test passes
     */
    private static boolean testCargoBayCreation() {
        LOGGER.info("Testing cargo bay creation and functionality...");
        
        try {
            // Create a standard cargo bay
            ResourceLocation bayId = new ResourceLocation("galactic_space", "test_cargo_bay");
            String bayName = "Test Cargo Bay";
            String bayDesc = "A test cargo bay for verification";
            int tier = 2;
            int mass = 60;
            int maxCapacity = 1000; // in kg
            boolean securityFeatures = true;
            boolean environmentControl = true;
            boolean automatedLoading = false;
            
            ICargoBay cargoBay = new StandardCargoBay(
                bayId, bayName, bayDesc, tier, mass, maxCapacity,
                securityFeatures, environmentControl, automatedLoading
            );
            
            // Verify cargo bay properties
            boolean propertiesValid = 
                cargoBay.getType() == RocketComponentType.CARGO_BAY &&
                cargoBay.getName().equals(bayName) &&
                cargoBay.getDescription().equals(bayDesc) &&
                cargoBay.getTier() == tier &&
                cargoBay.getMass() == mass &&
                cargoBay.getMaxCapacity() == maxCapacity &&
                cargoBay.getCurrentUsedCapacity() == 0 &&
                cargoBay.getMaxSlots() == 9 * tier &&
                cargoBay.getItems().isEmpty() &&
                cargoBay.hasSecurityFeatures() == securityFeatures &&
                cargoBay.hasEnvironmentControl() == environmentControl &&
                cargoBay.hasAutomatedLoading() == automatedLoading;
            
            if (!propertiesValid) {
                LOGGER.error("Cargo bay properties validation failed");
                return false;
            }
            
            // Create a test item
            net.minecraft.world.item.ItemStack testItem = new net.minecraft.world.item.ItemStack(
                net.minecraft.core.registries.BuiltInRegistries.ITEM.get(new ResourceLocation("minecraft:iron_ingot")), 
                10
            );
            
            // Test item addition
            boolean itemAdded = cargoBay.addItem(testItem);
            if (!itemAdded || cargoBay.getItems().isEmpty()) {
                LOGGER.error("Cargo bay add item operation failed");
                return false;
            }
            
            // Check capacity calculations
            float expectedWeight = cargoBay.calculateItemWeight(testItem);
            if (Math.abs(cargoBay.getCurrentUsedCapacity() - expectedWeight) > 0.01f) {
                LOGGER.error("Cargo bay weight calculation incorrect");
                return false;
            }
            
            // Test item removal
            net.minecraft.world.item.ItemStack removedItem = cargoBay.removeItem(0);
            if (removedItem.isEmpty() || !cargoBay.getItems().isEmpty()) {
                LOGGER.error("Cargo bay remove item operation failed");
                return false;
            }
            
            // Test serialization and deserialization
            CompoundTag tag = new CompoundTag();
            cargoBay.save(tag);
            
            if (!tag.contains("Type") || !tag.getString("Type").equals(RocketComponentType.CARGO_BAY.name())) {
                LOGGER.error("Cargo bay serialization missing or incorrect Type");
                return false;
            }
            
            if (!tag.contains("MaxCapacity") || tag.getInt("MaxCapacity") != maxCapacity) {
                LOGGER.error("Cargo bay serialization missing or incorrect MaxCapacity");
                return false;
            }
            
            return true;
        } catch (Exception e) {
            LOGGER.error("Error in cargo bay creation test", e);
            return false;
        }
    }
}