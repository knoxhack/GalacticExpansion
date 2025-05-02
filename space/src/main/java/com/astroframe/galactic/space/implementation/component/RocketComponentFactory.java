package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.common.ItemStack;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.implementation.component.engine.RocketEngineImpl;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Factory for creating and registering rocket components.
 */
public class RocketComponentFactory {
    
    // Maps to store registered components by ID
    private static final Map<ResourceLocation, ICommandModule> COMMAND_MODULES = new HashMap<>();
    private static final Map<ResourceLocation, IRocketEngine> ENGINES = new HashMap<>();
    private static final Map<ResourceLocation, IFuelTank> FUEL_TANKS = new HashMap<>();
    private static final Map<ResourceLocation, ICargoBay> CARGO_BAYS = new HashMap<>();
    private static final Map<ResourceLocation, IPassengerCompartment> PASSENGER_COMPARTMENTS = new HashMap<>();
    private static final Map<ResourceLocation, IShield> SHIELDS = new HashMap<>();
    private static final Map<ResourceLocation, ILifeSupport> LIFE_SUPPORTS = new HashMap<>();
    
    /**
     * Registers all default rocket components.
     * Called during mod initialization.
     */
    public static void registerAll() {
        // Command Modules (Tiers 1-3)
        ResourceLocation cmdBasicId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_basic");
        registerCommandModule(
            cmdBasicId,
            new CommandModuleImpl.Builder(cmdBasicId)
                .name("Basic Command Module")
                .description("A simple command module for rocket control.")
                .tier(1)
                .mass(100)
                .crewCapacity(1)
                .computingPower(50)
                .sensorStrength(40)
                .navigationAccuracy(0.6f)
                .build()
        );
        
        ResourceLocation cmdAdvancedId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_advanced");
        registerCommandModule(
            cmdAdvancedId,
            new CommandModuleImpl.Builder(cmdAdvancedId)
                .name("Advanced Command Module")
                .description("An improved command module with better sensors.")
                .tier(2)
                .mass(150)
                .crewCapacity(2)
                .computingPower(100)
                .sensorStrength(80)
                .navigationAccuracy(0.8f)
                .automatedLanding(true)
                .build()
        );
        
        ResourceLocation cmdEliteId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_elite");
        registerCommandModule(
            cmdEliteId,
            new CommandModuleImpl.Builder(cmdEliteId)
                .name("Elite Command Module")
                .description("A high-tech command module with advanced capabilities.")
                .tier(3)
                .mass(200)
                .crewCapacity(3)
                .computingPower(150)
                .sensorStrength(120)
                .navigationAccuracy(0.95f)
                .advancedLifeSupport(true)
                .automatedLanding(true)
                .emergencyEvacuation(true)
                .build()
        );
        
        // Engines (Various tiers and types)
        ResourceLocation solidEngineId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":solid_fuel_engine");
        registerEngine(
            solidEngineId,
            new RocketEngineImpl.Builder(solidEngineId)
                .name("Solid Fuel Engine")
                .description("A reliable solid fuel engine for initial space travel.")
                .tier(1)
                .mass(150)
                .thrust(500)
                .efficiency(0.8f)
                .fuelConsumptionRate(15)
                .fuelType(FuelType.CHEMICAL)
                .atmosphereCapable(true)
                .spaceCapable(true)
                .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.CHEMICAL)
                .build()
        );
        
        ResourceLocation liquidEngineId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":liquid_fuel_engine");
        registerEngine(
            liquidEngineId,
            new RocketEngineImpl.Builder(liquidEngineId)
                .name("Liquid Fuel Engine")
                .description("A more advanced engine with better fuel efficiency.")
                .tier(2)
                .mass(180)
                .thrust(800)
                .efficiency(0.9f)
                .fuelConsumptionRate(12)
                .fuelType(FuelType.CHEMICAL)
                .atmosphereCapable(true)
                .spaceCapable(true)
                .engineType(EngineType.LIQUID_FUEL)
                .build()
        );
        
        ResourceLocation ionEngineId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":ion_engine");
        registerEngine(
            ionEngineId,
            new RocketEngineImpl.Builder(ionEngineId)
                .name("Ion Engine")
                .description("A highly efficient engine for space travel only.")
                .tier(3)
                .mass(100)
                .thrust(400)
                .efficiency(0.95f)
                .fuelConsumptionRate(5)
                .fuelType(FuelType.ELECTRIC)
                .atmosphereCapable(false)
                .spaceCapable(true)
                .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.ION)
                .build()
        );
        
        // Fuel Tanks (Various sizes)
        ResourceLocation smallTankId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_small");
        registerFuelTank(
            smallTankId,
            new FuelTankImpl.Builder(smallTankId)
                .name("Small Fuel Tank")
                .description("A basic fuel tank with limited capacity.")
                .tier(1)
                .mass(100)
                .maxFuelCapacity(500)
                .fuelType(FuelType.CHEMICAL)
                .build()
        );
        
        ResourceLocation mediumTankId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_medium");
        registerFuelTank(
            mediumTankId,
            new FuelTankImpl.Builder(mediumTankId)
                .name("Medium Fuel Tank")
                .description("A standard fuel tank with decent capacity.")
                .tier(2)
                .mass(200)
                .maxFuelCapacity(1000)
                .fuelType(FuelType.CHEMICAL)
                .leakResistance(0.8f)
                .build()
        );
        
        ResourceLocation largeTankId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_large");
        registerFuelTank(
            largeTankId,
            new FuelTankImpl.Builder(largeTankId)
                .name("Large Fuel Tank")
                .description("A high-capacity fuel tank for extended missions.")
                .tier(3)
                .mass(300)
                .maxFuelCapacity(2000)
                .fuelType(FuelType.CHEMICAL)
                .leakResistance(0.9f)
                .explosionResistance(0.85f)
                .build()
        );
        
        // Cargo Bays
        ResourceLocation smallCargoBayId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_small");
        registerCargoBay(
            smallCargoBayId,
            new CargoBayImpl.Builder(smallCargoBayId)
                .name("Small Cargo Bay")
                .description("A compact cargo bay for basic storage needs.")
                .tier(1)
                .mass(100)
                .storageCapacity(9)
                .build()
        );
        
        ResourceLocation mediumCargoBayId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_medium");
        registerCargoBay(
            mediumCargoBayId,
            new CargoBayImpl.Builder(mediumCargoBayId)
                .name("Medium Cargo Bay")
                .description("A standard cargo bay with moderate storage capacity.")
                .tier(2)
                .mass(200)
                .storageCapacity(27)
                .vacuumSeal(true)
                .build()
        );
        
        ResourceLocation largeCargoBayId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_large");
        registerCargoBay(
            largeCargoBayId,
            new CargoBayImpl.Builder(largeCargoBayId)
                .name("Large Cargo Bay")
                .description("A spacious cargo bay for substantial storage needs.")
                .tier(3)
                .mass(300)
                .storageCapacity(54)
                .vacuumSeal(true)
                .temperatureRegulation(true)
                .radiationShielding(true)
                .build()
        );
        
        // Passenger Compartments
        ResourceLocation basicCompartmentId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_basic");
        registerPassengerCompartment(
            basicCompartmentId,
            new PassengerCompartmentImpl.Builder(basicCompartmentId)
                .name("Basic Passenger Compartment")
                .description("A simple compartment for transporting passengers.")
                .tier(1)
                .mass(150)
                .passengerCapacity(2)
                .comfortLevel(3)
                .build()
        );
        
        ResourceLocation advancedCompartmentId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_advanced");
        registerPassengerCompartment(
            advancedCompartmentId,
            new PassengerCompartmentImpl.Builder(advancedCompartmentId)
                .name("Advanced Passenger Compartment")
                .description("A more comfortable compartment with additional features.")
                .tier(2)
                .mass(250)
                .passengerCapacity(4)
                .comfortLevel(7)
                .lifeSupport(true)
                .radiationShielding(true)
                .build()
        );
        
        // Shields
        ResourceLocation basicShieldId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_basic");
        registerShield(
            basicShieldId,
            new ShieldImpl.Builder(basicShieldId)
                .name("Basic Heat Shield")
                .description("A simple shield for atmospheric reentry.")
                .tier(1)
                .mass(100)
                .maxDurability(500)
                .impactResistance(5)
                .shieldStrength(50)
                .build()
        );
        
        ResourceLocation advancedShieldId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_advanced");
        registerShield(
            advancedShieldId,
            new ShieldImpl.Builder(advancedShieldId)
                .name("Advanced Heat Shield")
                .description("An improved shield with better protection.")
                .tier(2)
                .mass(150)
                .maxDurability(800)
                .impactResistance(8)
                .shieldStrength(80)
                .meteorResistance(true)
                .build()
        );
        
        // Life Support Systems
        ResourceLocation basicLifeSupportId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_basic");
        registerLifeSupport(
            basicLifeSupportId,
            new BaseLifeSupport.Builder(basicLifeSupportId, Component.literal("Basic Life Support System"))
                .description("A simple system providing essential life support functions.")
                .tier(1)
                .mass(100)
                .maxCrewCapacity(3)
                .oxygenGenerationRate(60)
                .waterRecyclingEfficiency(0.7f)
                .build()
        );
        
        ResourceLocation advancedLifeSupportId = ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_advanced");
        registerLifeSupport(
            advancedLifeSupportId,
            new BaseLifeSupport.Builder(advancedLifeSupportId, Component.literal("Advanced Life Support System"))
                .description("An enhanced life support system for extended missions.")
                .tier(2)
                .mass(150)
                .maxCrewCapacity(6)
                .oxygenGenerationRate(100)
                .waterRecyclingEfficiency(0.85f)
                .foodProductionRate(15)
                .wasteManagementEfficiency(0.8f)
                .backupSystems(true)
                .build()
        );
        
        GalacticSpace.LOGGER.info("Registered {} rocket components", 
            COMMAND_MODULES.size() + ENGINES.size() + FUEL_TANKS.size() 
            + CARGO_BAYS.size() + PASSENGER_COMPARTMENTS.size() 
            + SHIELDS.size() + LIFE_SUPPORTS.size()
        );
    }
    
    // Registration methods for each component type
    
    private static void registerCommandModule(ResourceLocation id, ICommandModule module) {
        COMMAND_MODULES.put(id, module);
    }
    
    private static void registerEngine(ResourceLocation id, IRocketEngine engine) {
        ENGINES.put(id, engine);
    }
    
    private static void registerFuelTank(ResourceLocation id, IFuelTank fuelTank) {
        FUEL_TANKS.put(id, fuelTank);
    }
    
    private static void registerCargoBay(ResourceLocation id, ICargoBay cargoBay) {
        CARGO_BAYS.put(id, cargoBay);
    }
    
    private static void registerPassengerCompartment(ResourceLocation id, IPassengerCompartment compartment) {
        PASSENGER_COMPARTMENTS.put(id, compartment);
    }
    
    private static void registerShield(ResourceLocation id, IShield shield) {
        SHIELDS.put(id, shield);
    }
    
    private static void registerLifeSupport(ResourceLocation id, ILifeSupport lifeSupport) {
        LIFE_SUPPORTS.put(id, lifeSupport);
    }
    
    // Getter methods for each component type
    
    public static ICommandModule getCommandModule(ResourceLocation id) {
        return COMMAND_MODULES.get(id);
    }
    
    public static IRocketEngine getEngine(ResourceLocation id) {
        return ENGINES.get(id);
    }
    
    public static IFuelTank getFuelTank(ResourceLocation id) {
        return FUEL_TANKS.get(id);
    }
    
    public static ICargoBay getCargoBay(ResourceLocation id) {
        return CARGO_BAYS.get(id);
    }
    
    public static IPassengerCompartment getPassengerCompartment(ResourceLocation id) {
        return PASSENGER_COMPARTMENTS.get(id);
    }
    
    public static IShield getShield(ResourceLocation id) {
        return SHIELDS.get(id);
    }
    
    public static ILifeSupport getLifeSupport(ResourceLocation id) {
        return LIFE_SUPPORTS.get(id);
    }
    
    // Convenience methods for creating components by tier
    
    /**
     * Creates a basic command module (tier 1).
     * @return A basic command module
     */
    public static ICommandModule createBasicCommandModule() {
        return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_basic"));
    }
    
    /**
     * Creates a standard command module (tier 2).
     * @return A standard command module
     */
    public static ICommandModule createStandardCommandModule() {
        return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_advanced"));
    }
    
    /**
     * Creates an advanced command module (tier 3).
     * @return An advanced command module
     */
    public static ICommandModule createAdvancedCommandModule() {
        return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_elite"));
    }
    
    /**
     * Creates a basic chemical engine.
     * @return A chemical engine
     */
    public static IRocketEngine createChemicalEngine() {
        return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":solid_fuel_engine"));
    }
    
    /**
     * Creates a component based on the specified type and tier.
     * @param type The component type
     * @param tier The component tier
     * @return The component, or null if no matching component exists
     */
    public static IRocketComponent createComponent(RocketComponentType type, int tier) {
        switch (type) {
            case COCKPIT:
                return createCockpit(type, tier);
            case ENGINE:
                return createEngine(type, tier);
            case FUEL_TANK:
                return createFuelTank(type, tier);
            case STORAGE:
                return createCargoBay(type, tier);
            case PASSENGER_COMPARTMENT:
                return createPassengerCompartment(type, tier);
            case SHIELDING:
                return createShield(type, tier);
            case LIFE_SUPPORT:
                return createLifeSupport(type, tier);
            case STRUCTURE:
                // Structure components don't have tiers currently
                return createStructure(type, tier);
            default:
                // Not all component types have implementations
                return null;
        }
    }

    /**
     * Creates a cockpit/command module of the specified tier.
     * @param type The component type (should be COCKPIT)
     * @param tier The component tier (1-3)
     * @return The command module, or null if no matching component exists
     */
    public static ICommandModule createCockpit(RocketComponentType type, int tier) {
        if (type != RocketComponentType.COCKPIT) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_basic"));
            case 2:
                return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_advanced"));
            case 3:
                return getCommandModule(ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_elite"));
            default:
                return null;
        }
    }
    
    /**
     * Creates an engine of the specified tier.
     * @param type The component type (should be ENGINE)
     * @param tier The component tier (1-3)
     * @return The engine, or null if no matching component exists
     */
    public static IRocketEngine createEngine(RocketComponentType type, int tier) {
        if (type != RocketComponentType.ENGINE) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":solid_fuel_engine"));
            case 2:
                return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":liquid_fuel_engine"));
            case 3:
                return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":ion_engine"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a fuel tank of the specified tier.
     * @param type The component type (should be FUEL_TANK)
     * @param tier The component tier (1-3)
     * @return The fuel tank, or null if no matching component exists
     */
    public static IFuelTank createFuelTank(RocketComponentType type, int tier) {
        if (type != RocketComponentType.FUEL_TANK) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_small"));
            case 2:
                return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_medium"));
            case 3:
                return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_large"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a cargo bay of the specified tier.
     * @param type The component type (should be STORAGE)
     * @param tier The component tier (1-3)
     * @return The cargo bay, or null if no matching component exists
     */
    public static ICargoBay createCargoBay(RocketComponentType type, int tier) {
        if (type != RocketComponentType.STORAGE) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_small"));
            case 2:
                return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_medium"));
            case 3:
                return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_large"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a passenger compartment of the specified tier.
     * @param type The component type (should be PASSENGER_COMPARTMENT)
     * @param tier The component tier (1-2)
     * @return The passenger compartment, or null if no matching component exists
     */
    public static IPassengerCompartment createPassengerCompartment(RocketComponentType type, int tier) {
        if (type != RocketComponentType.PASSENGER_COMPARTMENT) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getPassengerCompartment(ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_basic"));
            case 2:
                return getPassengerCompartment(ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_advanced"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a shield of the specified tier.
     * @param type The component type (should be SHIELDING)
     * @param tier The component tier (1-2)
     * @return The shield, or null if no matching component exists
     */
    public static IShield createShield(RocketComponentType type, int tier) {
        if (type != RocketComponentType.SHIELDING) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getShield(ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_basic"));
            case 2:
                return getShield(ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_advanced"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a life support system of the specified tier.
     * @param type The component type (should be LIFE_SUPPORT)
     * @param tier The component tier (1-2)
     * @return The life support system, or null if no matching component exists
     */
    public static ILifeSupport createLifeSupport(RocketComponentType type, int tier) {
        if (type != RocketComponentType.LIFE_SUPPORT) {
            return null;
        }
        
        switch (tier) {
            case 1:
                return getLifeSupport(ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_basic"));
            case 2:
                return getLifeSupport(ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_advanced"));
            default:
                return null;
        }
    }
    
    /**
     * Creates a structural component.
     * @param type The component type (should be STRUCTURE)
     * @param tier The component tier (ignored for structure)
     * @return A structure component
     */
    public static IRocketComponent createStructure(RocketComponentType type, int tier) {
        // Basic implementation for structure - could be expanded later with different structure types
        return new StructureImpl.Builder(ResourceLocation.parse(GalacticSpace.MOD_ID + ":structure_basic"))
                .name("Basic Structural Frame")
                .description("A standard structural framework for rocket assembly.")
                .tier(Math.min(3, Math.max(1, tier)))  // Clamp tier to 1-3
                .mass(100)
                .structuralIntegrity(100)
                .build();
    }
    
    /**
     * Creates an advanced chemical engine.
     * @return An advanced chemical engine
     */
    public static IRocketEngine createAdvancedChemicalEngine() {
        return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":liquid_fuel_engine"));
    }
    
    /**
     * Creates an ion engine.
     * @return An ion engine
     */
    public static IRocketEngine createIonEngine() {
        return getEngine(ResourceLocation.parse(GalacticSpace.MOD_ID + ":ion_engine"));
    }
    
    /**
     * Creates a basic fuel tank.
     * @return A basic fuel tank
     */
    public static IFuelTank createBasicFuelTank() {
        return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_small"));
    }
    
    /**
     * Creates a standard fuel tank.
     * @return A standard fuel tank
     */
    public static IFuelTank createStandardFuelTank() {
        return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_medium"));
    }
    
    /**
     * Creates an advanced fuel tank.
     * @return An advanced fuel tank
     */
    public static IFuelTank createAdvancedFuelTank() {
        return getFuelTank(ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_large"));
    }
    
    /**
     * Creates a basic cargo bay.
     * @return A basic cargo bay
     */
    public static ICargoBay createBasicCargoBay() {
        return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_small"));
    }
    
    /**
     * Creates a standard cargo bay.
     * @return A standard cargo bay
     */
    public static ICargoBay createStandardCargoBay() {
        return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_medium"));
    }
    
    /**
     * Creates an advanced cargo bay.
     * @return An advanced cargo bay
     */
    public static ICargoBay createAdvancedCargoBay() {
        return getCargoBay(ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_large"));
    }
    
    /**
     * Creates a basic passenger compartment.
     * @return A basic passenger compartment
     */
    public static IPassengerCompartment createBasicPassengerCompartment() {
        return getPassengerCompartment(ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_basic"));
    }
    
    /**
     * Creates an advanced passenger compartment.
     * @return An advanced passenger compartment
     */
    public static IPassengerCompartment createAdvancedPassengerCompartment() {
        return getPassengerCompartment(ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_advanced"));
    }
    
    /**
     * Creates a basic shield.
     * @return A basic shield
     */
    public static IShield createBasicShield() {
        return getShield(ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_basic"));
    }
    
    /**
     * Creates an advanced shield.
     * @return An advanced shield
     */
    public static IShield createAdvancedShield() {
        return getShield(ResourceLocation.parse(GalacticSpace.MOD_ID + ":heat_shield_advanced"));
    }
    
    /**
     * Creates a basic life support system.
     * @return A basic life support system
     */
    public static ILifeSupport createBasicLifeSupport() {
        return getLifeSupport(ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_basic"));
    }
    
    /**
     * Creates an advanced life support system.
     * @return An advanced life support system
     */
    public static ILifeSupport createAdvancedLifeSupport() {
        return getLifeSupport(ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_advanced"));
    }
    
    /**
     * Alias for createAdvancedLifeSupport()
     * @return A standard life support system
     */
    public static ILifeSupport createStandardLifeSupport() {
        return createAdvancedLifeSupport();
    }
    
    /**
     * Creates a bioregeneration life support system.
     * This is a tier 3 life support system.
     * @return A tier 3 life support system
     */
    public static ILifeSupport createBioregenerativeLifeSupport() {
        // We'll use the advanced one as a stand-in for now
        return createAdvancedLifeSupport();
    }
    
    /**
     * Alias for createBasicShield()
     * @return A basic thermal shield
     */
    public static IShield createThermalShield() {
        return createBasicShield();
    }
    
    /**
     * Alias for createAdvancedShield()
     * @return An advanced deflector shield
     */
    public static IShield createDeflectorShield() {
        return createAdvancedShield();
    }
    
    /**
     * Alias for createAdvancedPassengerCompartment()
     * @return A standard passenger compartment
     */
    public static IPassengerCompartment createStandardPassengerCompartment() {
        return createAdvancedPassengerCompartment();
    }
    
    /**
     * Creates a scientific passenger compartment.
     * This is a specialized tier 3 passenger compartment.
     * @return A tier 3 passenger compartment
     */
    public static IPassengerCompartment createScientificPassengerCompartment() {
        // We'll use the advanced one as a stand-in for now
        return createAdvancedPassengerCompartment();
    }
    
    /**
     * Creates a quantum fuel tank.
     * This is a specialized tier 3 fuel tank.
     * @return A tier 3 fuel tank
     */
    public static IFuelTank createQuantumFuelTank() {
        // We'll use the advanced one as a stand-in for now
        return createAdvancedFuelTank();
    }
    
    /**
     * Creates a plasma engine.
     * This is a specialized tier 3 engine.
     * @return A tier 3 engine
     */
    public static IRocketEngine createPlasmaEngine() {
        // We'll use the ion engine as a stand-in for now
        return createIonEngine();
    }
    
    /**
     * Creates a rocket component from an NBT tag.
     * This is used when loading components from saved data.
     * 
     * @param id The component ID
     * @param tag The NBT tag containing component data
     * @return The created component, or null if creation failed
     */
    public static IRocketComponent createComponentFromTag(ResourceLocation id, net.minecraft.nbt.CompoundTag tag) {
        // Check the component type
        String typeString = tag.getString("Type").orElse("");
        if (typeString.isEmpty()) {
            GalacticSpace.LOGGER.error("Missing component type for component: {}", id);
            return null;
        }
        
        RocketComponentType type = null;
        
        try {
            type = RocketComponentType.valueOf(typeString);
        } catch (IllegalArgumentException e) {
            GalacticSpace.LOGGER.error("Invalid component type: {}", typeString);
            return null;
        }
        
        // Try to get the component from the registry first
        switch (type) {
            case COCKPIT:
                ICommandModule cmdModule = getCommandModule(id);
                if (cmdModule != null) {
                    return cmdModule;
                }
                break;
                
            case ENGINE:
                IRocketEngine engine = getEngine(id);
                if (engine != null) {
                    return engine;
                }
                break;
                
            case FUEL_TANK:
                IFuelTank fuelTank = getFuelTank(id);
                if (fuelTank != null) {
                    return fuelTank;
                }
                break;
                
            case STORAGE:
                ICargoBay cargoBay = getCargoBay(id);
                if (cargoBay != null) {
                    return cargoBay;
                }
                break;
                
            case PASSENGER_COMPARTMENT:
                IPassengerCompartment compartment = getPassengerCompartment(id);
                if (compartment != null) {
                    return compartment;
                }
                break;
                
            case SHIELDING:
                IShield shield = getShield(id);
                if (shield != null) {
                    return shield;
                }
                break;
                
            case LIFE_SUPPORT:
                ILifeSupport lifeSupport = getLifeSupport(id);
                if (lifeSupport != null) {
                    return lifeSupport;
                }
                break;
                
            default:
                GalacticSpace.LOGGER.error("Unsupported component type: {}", type);
                return null;
        }
        
        // If we couldn't find the component in the registry, try to recreate it from the tag
        try {
            // Extract common properties
            int tier = tag.getInt("Tier").orElse(1);
            int mass = tag.getInt("Mass").orElse(100);
            String name = tag.getString("Name").orElse("Unknown Component");
            String description = tag.getString("Description").orElse("No description");
            
            // Create a new component based on type
            switch (type) {
                case COCKPIT:
                    return new CommandModuleImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .crewCapacity(tag.getInt("CrewCapacity").orElse(1))
                            .computingPower(tag.getInt("ComputingPower").orElse(50))
                            .sensorStrength(tag.getInt("SensorStrength").orElse(40))
                            .navigationAccuracy(tag.getFloat("NavigationAccuracy").orElse(0.6f))
                            .build();
                            
                case ENGINE:
                    return new RocketEngineImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .thrust(tag.getInt("Thrust").orElse(100))
                            .efficiency(tag.getFloat("Efficiency").orElse(0.75f))
                            .fuelConsumptionRate(tag.getInt("FuelConsumptionRate").orElse(10))
                            .fuelType(IRocketEngine.FuelType.valueOf(tag.getString("FuelType").orElse("CHEMICAL")))
                            .atmosphereCapable(tag.getBoolean("AtmosphereCapable").orElse(true))
                            .spaceCapable(tag.getBoolean("SpaceCapable").orElse(true))
                            .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.valueOf(tag.getString("EngineType").orElse("CHEMICAL")))
                            .build();
                            
                case FUEL_TANK:
                    return new FuelTankImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .maxFuelCapacity(tag.getInt("MaxFuelCapacity").orElse(1000))
                            .fuelType(IRocketEngine.FuelType.valueOf(tag.getString("FuelType").orElse("CHEMICAL")))
                            .leakResistance(tag.contains("LeakResistance") ? tag.getFloat("LeakResistance").orElse(0.0f) : 0.0f)
                            .explosionResistance(tag.contains("ExplosionResistance") ? tag.getFloat("ExplosionResistance").orElse(0.0f) : 0.0f)
                            .build();
                            
                case STORAGE:
                    return new CargoBayImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .storageCapacity(tag.getInt("StorageCapacity").orElse(8))
                            .vacuumSeal(tag.getBoolean("VacuumSeal").orElse(false))
                            .temperatureRegulation(tag.getBoolean("TemperatureRegulation").orElse(false))
                            .radiationShielding(tag.getBoolean("RadiationShielding").orElse(false))
                            .build();
                            
                case PASSENGER_COMPARTMENT:
                    return new PassengerCompartmentImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .passengerCapacity(tag.getInt("PassengerCapacity").orElse(2))
                            .comfortLevel(tag.getInt("ComfortLevel").orElse(1))
                            .lifeSupport(tag.getBoolean("LifeSupport").orElse(false))
                            .radiationShielding(tag.getBoolean("RadiationShielding").orElse(false))
                            .build();
                            
                case SHIELDING:
                    return new ShieldImpl.Builder(id)
                            .name(name)
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .maxDurability(tag.getInt("MaxDurability").orElse(1000))
                            .impactResistance(tag.getInt("ImpactResistance").orElse(50))
                            .shieldStrength(tag.getInt("ShieldStrength").orElse(100))
                            .meteorResistance(tag.getBoolean("MeteorResistance").orElse(false))
                            .build();
                            
                case LIFE_SUPPORT:
                    return new BaseLifeSupport.Builder(id, Component.literal(name))
                            .description(description)
                            .tier(tier)
                            .mass(mass)
                            .maxCrewCapacity(tag.getInt("MaxCrewCapacity").orElse(4))
                            .oxygenGenerationRate(tag.getInt("OxygenGenerationRate").orElse(10))
                            .waterRecyclingEfficiency(tag.getFloat("WaterRecyclingEfficiency").orElse(0.8f))
                            .foodProductionRate(tag.getInt("FoodProductionRate").orElse(0))
                            .wasteManagementEfficiency(tag.getFloat("WasteManagementEfficiency").orElse(0.0f))
                            .backupSystems(tag.getBoolean("BackupSystems").orElse(false))
                            .build();
                            
                default:
                    GalacticSpace.LOGGER.error("Failed to create component of type: {}", type);
                    return null;
            }
        } catch (Exception e) {
            GalacticSpace.LOGGER.error("Error creating component from tag", e);
            return null;
        }
    }
    
    // Component implementation classes
    
    /**
     * Implementation of a command module.
     */
    private static class CommandModule implements ICommandModule {
        
        /**
         * Builder for CommandModule
         */
        public static class Builder {
            private final ResourceLocation id;
            private String name = "Command Module";
            private String description = "Standard command module";
            private int tier = 1;
            private int mass = 500;
            private int crewCapacity = 3;
            private int computingPower = 100;
            private int sensorStrength = 50;
            private float navigationAccuracy = 0.75f;
            private boolean advancedLifeSupport = false;
            private boolean automatedLanding = false;
            private boolean emergencyEvacuation = false;
            private int maxDurability = 1000;
            
            public Builder(ResourceLocation id) {
                this.id = id;
            }
            
            public Builder name(String name) {
                this.name = name;
                return this;
            }
            
            public Builder description(String description) {
                this.description = description;
                return this;
            }
            
            public Builder tier(int tier) {
                this.tier = tier;
                return this;
            }
            
            public Builder mass(int mass) {
                this.mass = mass;
                return this;
            }
            
            public Builder crewCapacity(int crewCapacity) {
                this.crewCapacity = crewCapacity;
                return this;
            }
            
            public Builder computingPower(int computingPower) {
                this.computingPower = computingPower;
                return this;
            }
            
            public Builder sensorStrength(int sensorStrength) {
                this.sensorStrength = sensorStrength;
                return this;
            }
            
            public Builder navigationAccuracy(float navigationAccuracy) {
                this.navigationAccuracy = navigationAccuracy;
                return this;
            }
            
            public Builder advancedLifeSupport(boolean advancedLifeSupport) {
                this.advancedLifeSupport = advancedLifeSupport;
                return this;
            }
            
            public Builder automatedLanding(boolean automatedLanding) {
                this.automatedLanding = automatedLanding;
                return this;
            }
            
            public Builder emergencyEvacuation(boolean emergencyEvacuation) {
                this.emergencyEvacuation = emergencyEvacuation;
                return this;
            }
            
            public Builder maxDurability(int maxDurability) {
                this.maxDurability = maxDurability;
                return this;
            }
            
            public CommandModule build() {
                return new CommandModule(
                    id, name, description, tier, mass, crewCapacity, computingPower, 
                    sensorStrength, navigationAccuracy, advancedLifeSupport, 
                    automatedLanding, emergencyEvacuation, maxDurability
                );
            }
        }
    
        private final ResourceLocation id;
        private final int tier;
        private final int mass;
        private final int crewCapacity;
        private final int computingPower;
        private final int sensorStrength;
        private final float navigationAccuracy;
        private final boolean advancedLifeSupport;
        private final boolean automatedLanding;
        private final boolean emergencyEvacuation;
        private final int maxDurability;
        private int currentDurability;
        private final String name;
        private final String description;
        
        public CommandModule(ResourceLocation id, String name, String description, int tier, int mass, 
                              int crewCapacity, int computingPower, int sensorStrength, 
                              float navigationAccuracy, boolean advancedLifeSupport, 
                              boolean automatedLanding, boolean emergencyEvacuation,
                              int maxDurability) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.crewCapacity = crewCapacity;
            this.computingPower = computingPower;
            this.sensorStrength = sensorStrength;
            this.navigationAccuracy = navigationAccuracy;
            this.advancedLifeSupport = advancedLifeSupport;
            this.automatedLanding = automatedLanding;
            this.emergencyEvacuation = emergencyEvacuation;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getCrewCapacity() {
            return crewCapacity;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public int getComputingPower() {
            return computingPower;
        }
        
        @Override
        public int getSensorStrength() {
            return sensorStrength;
        }
        
        @Override
        public float getNavigationAccuracy() {
            return navigationAccuracy;
        }
        
        @Override
        public boolean hasAdvancedLifeSupport() {
            return advancedLifeSupport;
        }
        
        @Override
        public boolean hasAutomatedLanding() {
            return automatedLanding;
        }
        
        @Override
        public boolean hasEmergencyEvacuation() {
            return emergencyEvacuation;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.COCKPIT;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
    }
    
    /**
     * Implementation of a rocket engine.
     */
    private static class RocketEngine implements IRocketEngine {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final float efficiency;
        private final FuelType fuelType;
        private final boolean atmosphereOperable;
        private final boolean spaceOperable;
        private final com.astroframe.galactic.core.api.space.component.enums.EngineType engineType;
        
        public RocketEngine(ResourceLocation id, String name, String description, 
                          int tier, int mass, int maxDurability, float efficiency,
                          FuelType fuelType, boolean atmosphereOperable, boolean spaceOperable,
                          com.astroframe.galactic.core.api.space.component.enums.EngineType engineType) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.efficiency = efficiency;
            this.fuelType = fuelType;
            this.atmosphereOperable = atmosphereOperable;
            this.spaceOperable = spaceOperable;
            this.engineType = engineType;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.ENGINE;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public float getEfficiency() {
            return efficiency;
        }
        
        @Override
        public FuelType getFuelType() {
            return fuelType;
        }
        
        @Override
        public boolean canOperateInAtmosphere() {
            return atmosphereOperable;
        }
        
        @Override
        public boolean canOperateInSpace() {
            return spaceOperable;
        }
        
        @Override
        public int getFuelConsumptionRate() {
            return (int)(100 / efficiency);
        }
        
        @Override
        public int getThrust() {
            return tier * 100;
        }
        
        @Override
        public com.astroframe.galactic.core.api.space.component.enums.EngineType getEngineType() {
            return engineType;
        }
    }
    
    /**
     * Implementation of a fuel tank.
     */
    private static class FuelTank implements IFuelTank {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final int maxFuelCapacity;
        private int currentFuelLevel;
        private final float leakResistance;
        private final float explosionResistance;
        private final IRocketEngine.FuelType fuelType;
        
        public FuelTank(ResourceLocation id, String name, String description, 
                       int tier, int mass, int maxDurability, int maxFuelCapacity, 
                       IRocketEngine.FuelType fuelType, float leakResistance,
                       float explosionResistance) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.maxFuelCapacity = maxFuelCapacity;
            this.currentFuelLevel = 0; // Start empty
            this.leakResistance = leakResistance;
            this.explosionResistance = explosionResistance;
            this.fuelType = fuelType;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.FUEL_TANK;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public int getMaxFuelCapacity() {
            return maxFuelCapacity;
        }
        
        @Override
        public int getCurrentFuelLevel() {
            return currentFuelLevel;
        }
        
        @Override
        public int addFuel(int amount) {
            int spaceAvailable = maxFuelCapacity - currentFuelLevel;
            int actualAmount = Math.min(amount, spaceAvailable);
            currentFuelLevel += actualAmount;
            return actualAmount;
        }
        
        @Override
        public int consumeFuel(int amount) {
            int availableFuel = Math.min(currentFuelLevel, amount);
            currentFuelLevel -= availableFuel;
            return availableFuel;
        }
        
        @Override
        public float getExplosionResistance() {
            return explosionResistance;
        }
        
        @Override
        public IRocketEngine.FuelType getFuelType() {
            return fuelType;
        }
        
        @Override
        public float getLeakResistance() {
            return leakResistance;
        }
    }
    
    /**
     * Implementation of a cargo bay.
     */
    private static class CargoBay implements ICargoBay {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final int storageCapacity;
        private final Map<Integer, ItemStack> contents = new HashMap<>();
        private final int maxCapacity; // Maximum cargo capacity in kg
        private final boolean hasSecurityFeatures;
        private final boolean hasEnvironmentControl;
        private final boolean hasAutomatedLoading;
        
        public CargoBay(ResourceLocation id, String name, String description, 
                       int tier, int mass, int maxDurability, int storageCapacity) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.storageCapacity = storageCapacity;
            
            // Set capacity and features based on tier
            this.maxCapacity = 100 * tier; // 100kg per tier
            this.hasSecurityFeatures = tier >= 2; // Mid and high tier have security features
            this.hasEnvironmentControl = tier >= 2; // Mid and high tier have environment control
            this.hasAutomatedLoading = tier >= 3; // Only high tier has automated loading
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.STRUCTURE;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public int getStorageCapacity() {
            return storageCapacity;
        }
        
        @Override
        public boolean hasRadiationShielding() {
            return tier >= 3; // Only top-tier cargo bays have radiation shielding
        }
        
        @Override
        public boolean hasTemperatureRegulation() {
            return tier >= 2; // Mid and high tier have temperature regulation
        }
        
        @Override
        public boolean hasVacuumSeal() {
            return tier >= 2; // Mid and high tier have vacuum sealing
        }
        
        @Override
        public Map<Integer, ItemStack> getContents() {
            return new HashMap<>(contents);
        }
        
        @Override
        public ItemStack addItem(ItemStack stack) {
            // Don't modify the original stack
            ItemStack remaining = stack.copy();
            
            // Try to add to existing stacks first
            for (int i = 0; i < storageCapacity; i++) {
                if (contents.containsKey(i)) {
                    ItemStack existing = contents.get(i);
                    if (existing.getItem() == remaining.getItem() && existing.getCount() < existing.getMaxStackSize()) {
                        int spaceAvailable = existing.getMaxStackSize() - existing.getCount();
                        int amountToAdd = Math.min(spaceAvailable, remaining.getCount());
                        
                        existing.grow(amountToAdd);
                        remaining.shrink(amountToAdd);
                        
                        if (remaining.isEmpty()) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
            
            // If we still have items, find empty slots
            for (int i = 0; i < storageCapacity; i++) {
                if (!contents.containsKey(i) || contents.get(i).isEmpty()) {
                    // Create a new stack in this slot
                    ItemStack toStore = remaining.copy();
                    int amountToStore = Math.min(remaining.getCount(), remaining.getMaxStackSize());
                    toStore.setCount(amountToStore);
                    remaining.shrink(amountToStore);
                    
                    contents.put(i, toStore);
                    
                    if (remaining.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
            
            // Return any remaining items that couldn't be stored
            return remaining;
        }
        
        @Override
        public ItemStack takeItem(int slotIndex, int amount) {
            if (slotIndex < 0 || slotIndex >= storageCapacity || !contents.containsKey(slotIndex)) {
                return ItemStack.EMPTY;
            }
            
            ItemStack stack = contents.get(slotIndex);
            int amountToTake = Math.min(amount, stack.getCount());
            
            ItemStack result = stack.copy();
            result.setCount(amountToTake);
            
            stack.shrink(amountToTake);
            if (stack.isEmpty()) {
                contents.remove(slotIndex);
            }
            
            return result;
        }
        
        @Override
        public List<ItemStack> getItems() {
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack stack : contents.values()) {
                items.add(stack.copy());
            }
            return items;
        }
        
        @Override
        public ItemStack removeItem(int index) {
            if (index < 0 || index >= contents.size()) {
                return ItemStack.EMPTY;
            }
            
            // Convert map entry index to map key
            Integer slotKey = null;
            int currentIndex = 0;
            for (Integer slotNum : contents.keySet()) {
                if (currentIndex == index) {
                    slotKey = slotNum;
                    break;
                }
                currentIndex++;
            }
            
            if (slotKey != null) {
                ItemStack stack = contents.get(slotKey);
                contents.remove(slotKey);
                return stack;
            }
            
            return ItemStack.EMPTY;
        }
        
        @Override
        public int getMaxSlots() {
            return storageCapacity;
        }
        
        @Override
        public int getMaxCapacity() {
            return maxCapacity;
        }
        
        @Override
        public int getCurrentUsedCapacity() {
            int capacity = 0;
            for (ItemStack stack : contents.values()) {
                capacity += calculateItemWeight(stack);
            }
            return capacity;
        }
        
        @Override
        public boolean hasSecurityFeatures() {
            return hasSecurityFeatures;
        }
        
        @Override
        public boolean hasEnvironmentControl() {
            return hasEnvironmentControl;
        }
        
        @Override
        public boolean hasAutomatedLoading() {
            return hasAutomatedLoading;
        }
    }
    
    /**
     * Implementation of a passenger compartment.
     */
    private static class PassengerCompartment implements IPassengerCompartment {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final int passengerCapacity;
        private final int comfortLevel;
        private final boolean hasLifeSupport;
        private final boolean hasGravitySimulation;
        private final boolean hasRadiationShielding;
        private final List<Player> passengers;
        
        public PassengerCompartment(ResourceLocation id, String name, String description, 
                                   int tier, int mass, int maxDurability, 
                                   int passengerCapacity, int comfortLevel,
                                   boolean hasLifeSupport, boolean hasGravitySimulation,
                                   boolean hasRadiationShielding) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.passengerCapacity = passengerCapacity;
            this.comfortLevel = comfortLevel;
            this.hasLifeSupport = hasLifeSupport;
            this.hasGravitySimulation = hasGravitySimulation;
            this.hasRadiationShielding = hasRadiationShielding;
            this.passengers = new CopyOnWriteArrayList<>();
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.STRUCTURE;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public int getPassengerCapacity() {
            return passengerCapacity;
        }
        
        @Override
        public List<Player> getPassengers() {
            return new ArrayList<>(passengers);
        }
        
        @Override
        public boolean addPassenger(Player player) {
            if (passengers.size() < passengerCapacity && !passengers.contains(player)) {
                passengers.add(player);
                return true;
            }
            return false;
        }
        
        @Override
        public void removePassenger(Player player) {
            passengers.remove(player);
        }
        
        @Override
        public int getComfortLevel() {
            return comfortLevel;
        }
        
        @Override
        public boolean hasLifeSupport() {
            return hasLifeSupport;
        }
        
        @Override
        public boolean hasGravitySimulation() {
            return hasGravitySimulation;
        }
        
        @Override
        public boolean hasRadiationShielding() {
            return hasRadiationShielding;
        }
    }
    
    /**
     * Implementation of a shield.
     */
    private static class Shield implements IShield {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final int impactResistance;
        private final int heatResistance;
        private final int radiationResistance;
        private final int maxShieldStrength;
        private int currentShieldStrength;
        private boolean active = false;
        
        public Shield(ResourceLocation id, String name, String description, 
                     int tier, int mass, int maxDurability, 
                     int impactResistance, int heatResistance, int radiationResistance,
                     int maxShieldStrength) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.impactResistance = impactResistance;
            this.heatResistance = heatResistance;
            this.radiationResistance = radiationResistance;
            this.maxShieldStrength = maxShieldStrength;
            this.currentShieldStrength = maxShieldStrength;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.SHIELDING;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public int getImpactResistance() {
            return impactResistance;
        }
        
        @Override
        public int getHeatResistance() {
            return heatResistance;
        }
        
        @Override
        public int getRadiationResistance() {
            return radiationResistance;
        }
        
        @Override
        public int getMaxShieldStrength() {
            return maxShieldStrength;
        }
        
        @Override
        public int getCurrentShieldStrength() {
            return currentShieldStrength;
        }
        
        @Override
        public int applyDamage(int amount) {
            if (!active) {
                return amount;
            }
            
            // Apply damage to shield
            int absorbedDamage = Math.min(currentShieldStrength, amount);
            currentShieldStrength -= absorbedDamage;
            
            // Return penetrating damage
            return amount - absorbedDamage;
        }
        
        @Override
        public int regenerate(int amount) {
            if (!active || currentShieldStrength <= 0) {
                return 0;
            }
            
            int regenerationAmount = Math.min(maxShieldStrength - currentShieldStrength, amount);
            currentShieldStrength += regenerationAmount;
            return regenerationAmount;
        }
        
        @Override
        public boolean isActive() {
            return active && currentShieldStrength > 0;
        }
        
        @Override
        public void setActive(boolean active) {
            this.active = active && currentShieldStrength > 0;
        }
    }
    
    /**
     * Implementation of a life support system.
     */
    private static class LifeSupport implements ILifeSupport {
        private final ResourceLocation id;
        private final String name;
        private final String description;
        private final int tier;
        private final int mass;
        private final int maxDurability;
        private int currentDurability;
        private final int maxCrewCapacity;
        private final int oxygenGeneration;
        private final float waterRecycling;
        private final int foodProduction;
        private final float wasteManagement;
        private final float atmosphericQuality;
        private final boolean hasBackupSystems;
        private final boolean hasRadiationFiltering;
        private final boolean hasEmergencyMode;
        private boolean emergencyModeActive = false;
        
        public LifeSupport(ResourceLocation id, String name, String description, 
                          int tier, int mass, int maxDurability, int maxCrewCapacity,
                          int oxygenGeneration, float waterRecycling, int foodProduction,
                          float wasteManagement, float atmosphericQuality,
                          boolean hasBackupSystems, boolean hasRadiationFiltering,
                          boolean hasEmergencyMode) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.tier = tier;
            this.mass = mass;
            this.maxDurability = maxDurability;
            this.currentDurability = maxDurability;
            this.maxCrewCapacity = maxCrewCapacity;
            this.oxygenGeneration = oxygenGeneration;
            this.waterRecycling = waterRecycling;
            this.foodProduction = foodProduction;
            this.wasteManagement = wasteManagement;
            this.atmosphericQuality = atmosphericQuality;
            this.hasBackupSystems = hasBackupSystems;
            this.hasRadiationFiltering = hasRadiationFiltering;
            this.hasEmergencyMode = hasEmergencyMode;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public String getName() {
            return name;
        }
        
        @Override
        public String getDescription() {
            return description;
        }
        
        @Override
        public int getTier() {
            return tier;
        }
        
        @Override
        public RocketComponentType getType() {
            return RocketComponentType.LIFE_SUPPORT;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public int getMaxDurability() {
            return maxDurability;
        }
        
        @Override
        public int getCurrentDurability() {
            return currentDurability;
        }
        
        @Override
        public void damage(int amount) {
            currentDurability = Math.max(0, currentDurability - amount);
        }
        
        @Override
        public void repair(int amount) {
            currentDurability = Math.min(maxDurability, currentDurability + amount);
        }
        
        @Override
        public int getMaxCrewCapacity() {
            return maxCrewCapacity;
        }
        
        @Override
        public int getOxygenGenerationRate() {
            return oxygenGeneration;
        }
        
        @Override
        public float getWaterRecyclingEfficiency() {
            return waterRecycling;
        }
        
        @Override
        public int getFoodProductionRate() {
            return foodProduction;
        }
        
        @Override
        public float getWasteManagementEfficiency() {
            return wasteManagement;
        }
        
        @Override
        public float getAtmosphericQuality() {
            return atmosphericQuality;
        }
        
        @Override
        public boolean hasBackupSystems() {
            return hasBackupSystems;
        }
        
        @Override
        public boolean hasRadiationFiltering() {
            return hasRadiationFiltering;
        }
        
        @Override
        public boolean hasEmergencyMode() {
            return hasEmergencyMode;
        }
        
        @Override
        public void setEmergencyMode(boolean active) {
            this.emergencyModeActive = active && hasEmergencyMode && !isBroken();
        }
        
        @Override
        public boolean isEmergencyModeActive() {
            return emergencyModeActive && !isBroken();
        }
        
        /**
         * Check if this component is broken (has no durability left).
         * @return true if broken, false otherwise
         */
        @Override
        public boolean isBroken() {
            return currentDurability <= 0;
        }
    }
}