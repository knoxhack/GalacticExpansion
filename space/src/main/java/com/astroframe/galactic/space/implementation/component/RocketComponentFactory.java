package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

/**
 * Factory class for creating rocket components.
 * This class provides methods to create standard components for rockets.
 */
public class RocketComponentFactory {
    
    /**
     * Creates a basic tier 1 command module.
     * @return A new command module
     */
    public static ICommandModule createBasicCommandModule() {
        return new CommandModuleImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "command_module_basic"),
                Component.translatable("component.galactic-space.command_module_basic"),
                1,
                150,
                100.0f,
                0.7f,
                1,
                10,
                1,
                false,
                1,
                ICommandModule.CommandModuleType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 command module.
     * @return A new command module
     */
    public static ICommandModule createStandardCommandModule() {
        return new CommandModuleImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "command_module_standard"),
                Component.translatable("component.galactic-space.command_module_standard"),
                2,
                200,
                150.0f,
                0.85f,
                2,
                25,
                2,
                false,
                2,
                ICommandModule.CommandModuleType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 command module.
     * @return A new command module
     */
    public static ICommandModule createAdvancedCommandModule() {
        return new CommandModuleImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "command_module_advanced"),
                Component.translatable("component.galactic-space.command_module_advanced"),
                3,
                250,
                200.0f,
                0.95f,
                3,
                50,
                3,
                false,
                3,
                ICommandModule.CommandModuleType.ADVANCED
        );
    }
    
    /**
     * Creates a quantum tier 3 command module with emergency teleport.
     * @return A new command module
     */
    public static ICommandModule createQuantumCommandModule() {
        return new CommandModuleImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "command_module_quantum"),
                Component.translatable("component.galactic-space.command_module_quantum"),
                3,
                300,
                250.0f,
                0.99f,
                3,
                100,
                4,
                true,
                4,
                ICommandModule.CommandModuleType.QUANTUM
        );
    }
    
    /**
     * Creates a chemical tier 1 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createChemicalEngine() {
        return new RocketEngineImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "engine_chemical"),
                Component.translatable("component.galactic-space.engine_chemical"),
                1,
                300,
                150.0f,
                100,
                1.0f,
                100,
                true,
                true,
                false,
                IRocketEngine.EngineType.CHEMICAL
        );
    }
    
    /**
     * Creates an ion tier 2 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createIonEngine() {
        return new RocketEngineImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "engine_ion"),
                Component.translatable("component.galactic-space.engine_ion"),
                2,
                150,
                200.0f,
                80,
                2.5f,
                150,
                false,
                false,
                true,
                IRocketEngine.EngineType.ION
        );
    }
    
    /**
     * Creates a plasma tier 3 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createPlasmaEngine() {
        return new RocketEngineImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "engine_plasma"),
                Component.translatable("component.galactic-space.engine_plasma"),
                3,
                200,
                300.0f,
                150,
                3.0f,
                200,
                false,
                true,
                true,
                IRocketEngine.EngineType.PLASMA
        );
    }
    
    /**
     * Creates a basic tier 1 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createBasicFuelTank() {
        return new FuelTankImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_basic"),
                Component.translatable("component.galactic-space.fuel_tank_basic"),
                1,
                250,
                200.0f,
                0.7f,
                10,
                false,
                0.5f,
                IFuelTank.FuelType.CHEMICAL
        );
    }
    
    /**
     * Creates a standard tier 2 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createStandardFuelTank() {
        return new FuelTankImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_standard"),
                Component.translatable("component.galactic-space.fuel_tank_standard"),
                2,
                350,
                500.0f,
                0.85f,
                20,
                false,
                0.7f,
                IFuelTank.FuelType.HYDROGEN
        );
    }
    
    /**
     * Creates an advanced tier 3 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createAdvancedFuelTank() {
        return new FuelTankImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_advanced"),
                Component.translatable("component.galactic-space.fuel_tank_advanced"),
                3,
                450,
                1000.0f,
                0.95f,
                30,
                true,
                0.9f,
                IFuelTank.FuelType.NUCLEAR
        );
    }
    
    /**
     * Creates a quantum tier 3 fuel tank for exotic fuels.
     * @return A new fuel tank
     */
    public static IFuelTank createQuantumFuelTank() {
        return new FuelTankImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_quantum"),
                Component.translatable("component.galactic-space.fuel_tank_quantum"),
                3,
                500,
                2000.0f,
                0.99f,
                50,
                true,
                0.95f,
                IFuelTank.FuelType.EXOTIC
        );
    }
    
    /**
     * Creates a basic tier 1 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createBasicCargoBay() {
        return new CargoBayImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_basic"),
                Component.translatable("component.galactic-space.cargo_bay_basic"),
                1,
                200,
                100.0f,
                9,
                500,
                false,
                false,
                1.0f,
                ICargoBay.CargoBayType.STANDARD
        );
    }
    
    /**
     * Creates a standard tier 2 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createStandardCargoBay() {
        return new CargoBayImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_standard"),
                Component.translatable("component.galactic-space.cargo_bay_standard"),
                2,
                300,
                150.0f,
                18,
                1000,
                true,
                true,
                1.5f,
                ICargoBay.CargoBayType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createAdvancedCargoBay() {
        return new CargoBayImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_advanced"),
                Component.translatable("component.galactic-space.cargo_bay_advanced"),
                3,
                400,
                200.0f,
                27,
                2000,
                true,
                true,
                2.0f,
                ICargoBay.CargoBayType.REINFORCED
        );
    }
    
    /**
     * Creates a basic tier 1 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createBasicPassengerCompartment() {
        return new PassengerCompartmentImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_basic"),
                Component.translatable("component.galactic-space.passenger_compartment_basic"),
                1,
                200,
                100.0f,
                2,
                0.5f,
                false,
                0.3f,
                30,
                IPassengerCompartment.CompartmentType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createStandardPassengerCompartment() {
        return new PassengerCompartmentImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_standard"),
                Component.translatable("component.galactic-space.passenger_compartment_standard"),
                2,
                300,
                150.0f,
                4,
                0.7f,
                false,
                0.6f,
                60,
                IPassengerCompartment.CompartmentType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createAdvancedPassengerCompartment() {
        return new PassengerCompartmentImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_advanced"),
                Component.translatable("component.galactic-space.passenger_compartment_advanced"),
                3,
                400,
                200.0f,
                6,
                0.9f,
                true,
                0.8f,
                120,
                IPassengerCompartment.CompartmentType.LUXURY
        );
    }
    
    /**
     * Creates a scientific tier 3 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createScientificPassengerCompartment() {
        return new PassengerCompartmentImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_scientific"),
                Component.translatable("component.galactic-space.passenger_compartment_scientific"),
                3,
                350,
                180.0f,
                4,
                0.8f,
                true,
                0.7f,
                90,
                IPassengerCompartment.CompartmentType.SCIENTIFIC
        );
    }
    
    /**
     * Creates a basic tier 1 shield.
     * @return A new shield
     */
    public static IShield createBasicShield() {
        return new ShieldImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "shield_basic"),
                Component.translatable("component.galactic-space.shield_basic"),
                1,
                180,
                150.0f,
                0.3f,
                0.5f,
                0.2f,
                0,
                false,
                50.0f,
                IShield.ShieldType.PHYSICAL
        );
    }
    
    /**
     * Creates a thermal tier 2 shield.
     * @return A new shield
     */
    public static IShield createThermalShield() {
        return new ShieldImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "shield_thermal"),
                Component.translatable("component.galactic-space.shield_thermal"),
                2,
                220,
                200.0f,
                0.4f,
                0.9f,
                0.3f,
                0,
                false,
                70.0f,
                IShield.ShieldType.THERMAL
        );
    }
    
    /**
     * Creates a deflector tier 3 shield.
     * @return A new shield
     */
    public static IShield createDeflectorShield() {
        return new ShieldImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "shield_deflector"),
                Component.translatable("component.galactic-space.shield_deflector"),
                3,
                250,
                300.0f,
                0.8f,
                0.7f,
                0.6f,
                20,
                true,
                90.0f,
                IShield.ShieldType.DEFLECTOR
        );
    }
    
    /**
     * Creates a quantum tier 3 shield.
     * @return A new shield
     */
    public static IShield createQuantumShield() {
        return new ShieldImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "shield_quantum"),
                Component.translatable("component.galactic-space.shield_quantum"),
                3,
                300,
                400.0f,
                0.95f,
                0.8f,
                0.9f,
                50,
                true,
                100.0f,
                IShield.ShieldType.QUANTUM
        );
    }
    
    /**
     * Creates a basic tier 1 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createBasicLifeSupport() {
        return new LifeSupportImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "life_support_basic"),
                Component.translatable("component.galactic-space.life_support_basic"),
                1,
                150,
                100.0f,
                2,
                1.0f,
                0.5f,
                0.0f,
                0.4f,
                0.2f,
                15,
                ILifeSupport.LifeSupportType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createStandardLifeSupport() {
        return new LifeSupportImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "life_support_standard"),
                Component.translatable("component.galactic-space.life_support_standard"),
                2,
                200,
                150.0f,
                4,
                1.5f,
                0.7f,
                0.3f,
                0.6f,
                0.5f,
                30,
                ILifeSupport.LifeSupportType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createAdvancedLifeSupport() {
        return new LifeSupportImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "life_support_advanced"),
                Component.translatable("component.galactic-space.life_support_advanced"),
                3,
                250,
                200.0f,
                6,
                2.0f,
                0.9f,
                0.6f,
                0.8f,
                0.7f,
                60,
                ILifeSupport.LifeSupportType.ADVANCED
        );
    }
    
    /**
     * Creates a bioregenerative tier 3 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createBioregenerativeLifeSupport() {
        return new LifeSupportImpl(
                new ResourceLocation(GalacticSpace.MOD_ID, "life_support_bioregenerative"),
                Component.translatable("component.galactic-space.life_support_bioregenerative"),
                3,
                350,
                250.0f,
                8,
                2.5f,
                0.95f,
                0.9f,
                0.95f,
                0.9f,
                120,
                ILifeSupport.LifeSupportType.BIOREGENERATIVE
        );
    }
    
    /**
     * Registers all default rocket components.
     */
    public static void registerAll() {
        // Register command modules
        RocketComponentRegistry.register(createBasicCommandModule());
        RocketComponentRegistry.register(createStandardCommandModule());
        RocketComponentRegistry.register(createAdvancedCommandModule());
        RocketComponentRegistry.register(createQuantumCommandModule());
        
        // Register engines
        RocketComponentRegistry.register(createChemicalEngine());
        RocketComponentRegistry.register(createIonEngine());
        RocketComponentRegistry.register(createPlasmaEngine());
        
        // Register fuel tanks
        RocketComponentRegistry.register(createBasicFuelTank());
        RocketComponentRegistry.register(createStandardFuelTank());
        RocketComponentRegistry.register(createAdvancedFuelTank());
        RocketComponentRegistry.register(createQuantumFuelTank());
        
        // Register cargo bays
        RocketComponentRegistry.register(createBasicCargoBay());
        RocketComponentRegistry.register(createStandardCargoBay());
        RocketComponentRegistry.register(createAdvancedCargoBay());
        
        // Register passenger compartments
        RocketComponentRegistry.register(createBasicPassengerCompartment());
        RocketComponentRegistry.register(createStandardPassengerCompartment());
        RocketComponentRegistry.register(createAdvancedPassengerCompartment());
        RocketComponentRegistry.register(createScientificPassengerCompartment());
        
        // Register shields
        RocketComponentRegistry.register(createBasicShield());
        RocketComponentRegistry.register(createThermalShield());
        RocketComponentRegistry.register(createDeflectorShield());
        RocketComponentRegistry.register(createQuantumShield());
        
        // Register life support systems
        RocketComponentRegistry.register(createBasicLifeSupport());
        RocketComponentRegistry.register(createStandardLifeSupport());
        RocketComponentRegistry.register(createAdvancedLifeSupport());
        RocketComponentRegistry.register(createBioregenerativeLifeSupport());
    }
}