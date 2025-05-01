package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.core.api.space.component.enums.*;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import static com.astroframe.galactic.space.implementation.component.ResourceLocationHelper.of;

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
                of("command_module_basic"),
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
                CommandModuleType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 command module.
     * @return A new command module
     */
    public static ICommandModule createStandardCommandModule() {
        return new CommandModuleImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_standard"),
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
                CommandModuleType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 command module.
     * @return A new command module
     */
    public static ICommandModule createAdvancedCommandModule() {
        return new CommandModuleImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_advanced"),
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
                CommandModuleType.ADVANCED
        );
    }
    
    /**
     * Creates a quantum tier 3 command module with emergency teleport.
     * @return A new command module
     */
    public static ICommandModule createQuantumCommandModule() {
        return new CommandModuleImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":command_module_quantum"),
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
                CommandModuleType.QUANTUM
        );
    }
    
    /**
     * Creates a chemical tier 1 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createChemicalEngine() {
        return new RocketEngineImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":engine_chemical"),
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
                EngineType.CHEMICAL
        );
    }
    
    /**
     * Creates an ion tier 2 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createIonEngine() {
        return new RocketEngineImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":engine_ion"),
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
                EngineType.ION
        );
    }
    
    /**
     * Creates a plasma tier 3 rocket engine.
     * @return A new rocket engine
     */
    public static IRocketEngine createPlasmaEngine() {
        return new RocketEngineImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":engine_plasma"),
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
                EngineType.PLASMA
        );
    }
    
    /**
     * Creates a basic tier 1 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createBasicFuelTank() {
        return new FuelTankImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_basic"),
                Component.translatable("component.galactic-space.fuel_tank_basic"),
                1,
                250,
                200.0f,
                0.7f,
                10,
                false,
                0.5f,
                FuelType.CHEMICAL
        );
    }
    
    /**
     * Creates a standard tier 2 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createStandardFuelTank() {
        return new FuelTankImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_standard"),
                Component.translatable("component.galactic-space.fuel_tank_standard"),
                2,
                350,
                500.0f,
                0.85f,
                20,
                false,
                0.7f,
                FuelType.HYDROGEN
        );
    }
    
    /**
     * Creates an advanced tier 3 fuel tank.
     * @return A new fuel tank
     */
    public static IFuelTank createAdvancedFuelTank() {
        return new FuelTankImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_advanced"),
                Component.translatable("component.galactic-space.fuel_tank_advanced"),
                3,
                450,
                1000.0f,
                0.95f,
                30,
                true,
                0.9f,
                FuelType.NUCLEAR
        );
    }
    
    /**
     * Creates a quantum tier 3 fuel tank for exotic fuels.
     * @return A new fuel tank
     */
    public static IFuelTank createQuantumFuelTank() {
        return new FuelTankImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":fuel_tank_quantum"),
                Component.translatable("component.galactic-space.fuel_tank_quantum"),
                3,
                500,
                2000.0f,
                0.99f,
                50,
                true,
                0.95f,
                FuelType.EXOTIC
        );
    }
    
    /**
     * Creates a basic tier 1 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createBasicCargoBay() {
        return new CargoBayImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_basic"),
                Component.translatable("component.galactic-space.cargo_bay_basic"),
                1,
                200,
                100.0f,
                9,
                500,
                false,
                false,
                1.0f,
                CargoBayType.STANDARD
        );
    }
    
    /**
     * Creates a standard tier 2 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createStandardCargoBay() {
        return new CargoBayImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_standard"),
                Component.translatable("component.galactic-space.cargo_bay_standard"),
                2,
                300,
                150.0f,
                18,
                1000,
                true,
                true,
                1.5f,
                CargoBayType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 cargo bay.
     * @return A new cargo bay
     */
    public static ICargoBay createAdvancedCargoBay() {
        return new CargoBayImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":cargo_bay_advanced"),
                Component.translatable("component.galactic-space.cargo_bay_advanced"),
                3,
                400,
                200.0f,
                27,
                2000,
                true,
                true,
                2.0f,
                CargoBayType.REINFORCED
        );
    }
    
    /**
     * Creates a basic tier 1 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createBasicPassengerCompartment() {
        return new PassengerCompartmentImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_basic"),
                Component.translatable("component.galactic-space.passenger_compartment_basic"),
                1,
                200,
                100.0f,
                2,
                0.5f,
                false,
                0.3f,
                30,
                CompartmentType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createStandardPassengerCompartment() {
        return new PassengerCompartmentImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_standard"),
                Component.translatable("component.galactic-space.passenger_compartment_standard"),
                2,
                300,
                150.0f,
                4,
                0.7f,
                false,
                0.6f,
                60,
                CompartmentType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createAdvancedPassengerCompartment() {
        return new PassengerCompartmentImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_advanced"),
                Component.translatable("component.galactic-space.passenger_compartment_advanced"),
                3,
                400,
                200.0f,
                6,
                0.9f,
                true,
                0.8f,
                120,
                CompartmentType.LUXURY
        );
    }
    
    /**
     * Creates a scientific tier 3 passenger compartment.
     * @return A new passenger compartment
     */
    public static IPassengerCompartment createScientificPassengerCompartment() {
        return new PassengerCompartmentImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":passenger_compartment_scientific"),
                Component.translatable("component.galactic-space.passenger_compartment_scientific"),
                3,
                350,
                180.0f,
                4,
                0.8f,
                true,
                0.7f,
                90,
                CompartmentType.SCIENTIFIC
        );
    }
    
    /**
     * Creates a basic tier 1 shield.
     * @return A new shield
     */
    public static IShield createBasicShield() {
        return new ShieldImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":shield_basic"),
                Component.translatable("component.galactic-space.shield_basic"),
                1,
                180,
                150.0f,
                0.3f,
                0.5f,
                0.2f,
                0,
                ShieldType.PHYSICAL
        );
    }
    
    /**
     * Creates a standard tier 2 shield.
     * @return A new shield
     */
    public static IShield createThermalShield() {
        return new ShieldImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":shield_thermal"),
                Component.translatable("component.galactic-space.shield_thermal"),
                2,
                220,
                250.0f,
                0.6f,
                0.8f,
                0.7f,
                10,
                ShieldType.THERMAL
        );
    }
    
    /**
     * Creates an advanced tier 3 shield.
     * @return A new shield
     */
    public static IShield createDeflectorShield() {
        return new ShieldImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":shield_deflector"),
                Component.translatable("component.galactic-space.shield_deflector"),
                3,
                300,
                400.0f,
                0.9f,
                1.2f,
                1.0f,
                25,
                ShieldType.DEFLECTOR
        );
    }
    
    /**
     * Creates a basic tier 1 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createBasicLifeSupport() {
        return new LifeSupportImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_basic"),
                Component.translatable("component.galactic-space.life_support_basic"),
                1,
                200,
                100.0f,
                3,
                0.7f,
                0.6f,
                0.5f,
                false,
                false,
                LifeSupportType.BASIC
        );
    }
    
    /**
     * Creates a standard tier 2 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createStandardLifeSupport() {
        return new LifeSupportImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_standard"),
                Component.translatable("component.galactic-space.life_support_standard"),
                2,
                300,
                200.0f,
                5,
                0.85f,
                0.8f,
                0.7f,
                true,
                false,
                LifeSupportType.STANDARD
        );
    }
    
    /**
     * Creates an advanced tier 3 life support system.
     * @return A new life support system
     */
    public static ILifeSupport createAdvancedLifeSupport() {
        return new LifeSupportImpl(
                ResourceLocation.parse(GalacticSpace.MOD_ID + ":life_support_advanced"),
                Component.translatable("component.galactic-space.life_support_advanced"),
                3,
                400,
                300.0f,
                8,
                0.95f,
                0.95f,
                0.9f,
                true,
                true,
                LifeSupportType.ADVANCED
        );
    }
    
    /**
     * Registers all standard components.
     */
    public static void registerAll() {
        // Command modules
        RocketComponentRegistry.register(createBasicCommandModule());
        RocketComponentRegistry.register(createStandardCommandModule());
        RocketComponentRegistry.register(createAdvancedCommandModule());
        RocketComponentRegistry.register(createQuantumCommandModule());
        
        // Engines
        RocketComponentRegistry.register(createChemicalEngine());
        RocketComponentRegistry.register(createIonEngine());
        RocketComponentRegistry.register(createPlasmaEngine());
        
        // Fuel tanks
        RocketComponentRegistry.register(createBasicFuelTank());
        RocketComponentRegistry.register(createStandardFuelTank());
        RocketComponentRegistry.register(createAdvancedFuelTank());
        RocketComponentRegistry.register(createQuantumFuelTank());
        
        // Cargo bays
        RocketComponentRegistry.register(createBasicCargoBay());
        RocketComponentRegistry.register(createStandardCargoBay());
        RocketComponentRegistry.register(createAdvancedCargoBay());
        
        // Passenger compartments
        RocketComponentRegistry.register(createBasicPassengerCompartment());
        RocketComponentRegistry.register(createStandardPassengerCompartment());
        RocketComponentRegistry.register(createAdvancedPassengerCompartment());
        RocketComponentRegistry.register(createScientificPassengerCompartment());
        
        // Shields
        RocketComponentRegistry.register(createBasicShield());
        RocketComponentRegistry.register(createThermalShield());
        RocketComponentRegistry.register(createDeflectorShield());
        
        // Life support systems
        RocketComponentRegistry.register(createBasicLifeSupport());
        RocketComponentRegistry.register(createStandardLifeSupport());
        RocketComponentRegistry.register(createAdvancedLifeSupport());
    }
}