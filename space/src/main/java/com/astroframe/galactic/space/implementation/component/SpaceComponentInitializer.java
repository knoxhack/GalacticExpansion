package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.core.api.space.component.enums.CommandModuleType;
import com.astroframe.galactic.core.api.space.component.enums.CompartmentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import com.astroframe.galactic.core.api.space.component.enums.LifeSupportType;
import com.astroframe.galactic.core.api.space.component.enums.ShieldType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

/**
 * Initializes default rocket components for the Space module.
 */
public class SpaceComponentInitializer {
    
    /**
     * Registers all default rocket components.
     * This should be called during mod initialization.
     */
    public static void registerAll() {
        registerCommandModules();
        registerEngines();
        registerFuelTanks();
        registerCargoBays();
        registerPassengerCompartments();
        registerShields();
        registerLifeSupports();
    }
    
    /**
     * Registers default command modules.
     */
    private static void registerCommandModules() {
        // Tier 1 command module (basic)
        RocketComponentRegistry.register(
            new BaseCommandModule.Builder(
                ResourceLocationHelper.create("galactic", "command_module_basic"),
                Component.translatable("component.galactic.command_module.basic")
            ).tier(1)
             .mass(800)
             .navigationLevel(3)
             .crewCapacity(1)
             .computingPower(2)
             .communicationRange(100)
             .commandModuleType(CommandModuleType.BASIC)
             .build()
        );
        
        // Tier 2 command module (advanced)
        RocketComponentRegistry.register(
            new BaseCommandModule.Builder(
                ResourceLocationHelper.create("galactic", "command_module_advanced"),
                Component.translatable("component.galactic.command_module.advanced")
            ).tier(2)
             .mass(1000)
             .navigationLevel(6)
             .crewCapacity(2)
             .computingPower(5)
             .communicationRange(500)
             .commandModuleType(CommandModuleType.ADVANCED)
             .build()
        );
        
        // Tier 3 command module (superior)
        RocketComponentRegistry.register(
            new BaseCommandModule.Builder(
                ResourceLocationHelper.create("galactic", "command_module_superior"),
                Component.translatable("component.galactic.command_module.superior")
            ).tier(3)
             .mass(1200)
             .navigationLevel(9)
             .crewCapacity(3)
             .computingPower(8)
             .communicationRange(1000)
             .commandModuleType(CommandModuleType.SUPERIOR)
             .build()
        );
    }
    
    /**
     * Registers default rocket engines.
     */
    private static void registerEngines() {
        // Tier 1 chemical engine
        RocketComponentRegistry.register(
            new BaseRocketEngine.Builder(
                ResourceLocationHelper.create("galactic", "engine_chemical_basic"),
                Component.translatable("component.galactic.engine.chemical_basic")
            ).tier(1)
             .mass(1500)
             .thrust(100)
             .efficiency(0.7f)
             .heatCapacity(800)
             .canOperateInAtmosphere(true)
             .canOperateInSpace(false)
             .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.CHEMICAL)
             .build()
        );
        
        // Tier 2 chemical engine
        RocketComponentRegistry.register(
            new BaseRocketEngine.Builder(
                ResourceLocationHelper.create("galactic", "engine_chemical_advanced"),
                Component.translatable("component.galactic.engine.chemical_advanced")
            ).tier(2)
             .mass(1800)
             .thrust(200)
             .efficiency(0.8f)
             .heatCapacity(1200)
             .canOperateInAtmosphere(true)
             .canOperateInSpace(true)
             .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.CHEMICAL)
             .build()
        );
        
        // Tier 2 ion engine
        RocketComponentRegistry.register(
            new BaseRocketEngine.Builder(
                ResourceLocationHelper.create("galactic", "engine_ion_basic"),
                Component.translatable("component.galactic.engine.ion_basic")
            ).tier(2)
             .mass(1200)
             .thrust(80)
             .efficiency(0.95f)
             .heatCapacity(600)
             .canOperateInAtmosphere(false)
             .canOperateInSpace(true)
             .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.ION)
             .build()
        );
        
        // Tier 3 fusion engine
        RocketComponentRegistry.register(
            new BaseRocketEngine.Builder(
                ResourceLocationHelper.create("galactic", "engine_fusion"),
                Component.translatable("component.galactic.engine.fusion")
            ).tier(3)
             .mass(2500)
             .thrust(400)
             .efficiency(0.9f)
             .heatCapacity(2000)
             .canOperateInAtmosphere(true)
             .canOperateInSpace(true)
             .engineType(com.astroframe.galactic.core.api.space.component.enums.EngineType.FUSION)
             .build()
        );
    }
    
    /**
     * Registers default fuel tanks.
     */
    private static void registerFuelTanks() {
        // Tier 1 small chemical fuel tank
        RocketComponentRegistry.register(
            new BaseFuelTank.Builder(
                ResourceLocationHelper.create("galactic", "fuel_tank_chemical_small"),
                Component.translatable("component.galactic.fuel_tank.chemical_small")
            ).tier(1)
             .mass(500)
             .maxFuelCapacity(2000)
             .pressureRating(10.0f)
             .fuelType(FuelType.CHEMICAL)
             .build()
        );
        
        // Tier 2 medium chemical fuel tank
        RocketComponentRegistry.register(
            new BaseFuelTank.Builder(
                ResourceLocationHelper.create("galactic", "fuel_tank_chemical_medium"),
                Component.translatable("component.galactic.fuel_tank.chemical_medium")
            ).tier(2)
             .mass(1000)
             .maxFuelCapacity(5000)
             .pressureRating(15.0f)
             .fuelType(FuelType.CHEMICAL)
             .build()
        );
        
        // Tier 2 small ion fuel tank
        RocketComponentRegistry.register(
            new BaseFuelTank.Builder(
                ResourceLocationHelper.create("galactic", "fuel_tank_ion_small"),
                Component.translatable("component.galactic.fuel_tank.ion_small")
            ).tier(2)
             .mass(400)
             .maxFuelCapacity(3000)
             .pressureRating(20.0f)
             .insulated(true)
             .fuelType(FuelType.ELECTRIC)
             .build()
        );
        
        // Tier 3 large fusion fuel tank
        RocketComponentRegistry.register(
            new BaseFuelTank.Builder(
                ResourceLocationHelper.create("galactic", "fuel_tank_fusion"),
                Component.translatable("component.galactic.fuel_tank.fusion")
            ).tier(3)
             .mass(1500)
             .maxFuelCapacity(10000)
             .pressureRating(50.0f)
             .insulated(true)
             .fuelType(FuelType.FUSION)
             .build()
        );
    }
    
    /**
     * Registers default cargo bays.
     */
    private static void registerCargoBays() {
        // Tier 1 small cargo bay
        RocketComponentRegistry.register(
            new BaseCargoBay.Builder(
                ResourceLocationHelper.create("galactic", "cargo_bay_small"),
                Component.translatable("component.galactic.cargo_bay.small")
            ).tier(1)
             .mass(300)
             .storageCapacity(9)
             .build()
        );
        
        // Tier 2 medium cargo bay
        RocketComponentRegistry.register(
            new BaseCargoBay.Builder(
                ResourceLocationHelper.create("galactic", "cargo_bay_medium"),
                Component.translatable("component.galactic.cargo_bay.medium")
            ).tier(2)
             .mass(600)
             .storageCapacity(18)
             .climateControlled(true)
             .build()
        );
        
        // Tier 3 large cargo bay
        RocketComponentRegistry.register(
            new BaseCargoBay.Builder(
                ResourceLocationHelper.create("galactic", "cargo_bay_large"),
                Component.translatable("component.galactic.cargo_bay.large")
            ).tier(3)
             .mass(1000)
             .storageCapacity(36)
             .climateControlled(true)
             .radiationShielded(true)
             .empShielded(true)
             .build()
        );
    }
    
    /**
     * Registers default passenger compartments.
     */
    private static void registerPassengerCompartments() {
        // Tier 1 basic passenger compartment
        RocketComponentRegistry.register(
            new BasePassengerCompartment.Builder(
                ResourceLocationHelper.create("galactic", "passenger_compartment_basic"),
                Component.translatable("component.galactic.passenger_compartment.basic")
            ).tier(1)
             .mass(400)
             .passengerCapacity(2)
             .compartmentType(CompartmentType.BASIC)
             .build()
        );
        
        // Tier 2 standard passenger compartment
        RocketComponentRegistry.register(
            new BasePassengerCompartment.Builder(
                ResourceLocationHelper.create("galactic", "passenger_compartment_standard"),
                Component.translatable("component.galactic.passenger_compartment.standard")
            ).tier(2)
             .mass(800)
             .passengerCapacity(4)
             .sleepingQuarters(true)
             .compartmentType(CompartmentType.STANDARD)
             .build()
        );
        
        // Tier 3 luxury passenger compartment
        RocketComponentRegistry.register(
            new BasePassengerCompartment.Builder(
                ResourceLocationHelper.create("galactic", "passenger_compartment_luxury"),
                Component.translatable("component.galactic.passenger_compartment.luxury")
            ).tier(3)
             .mass(1200)
             .passengerCapacity(6)
             .artificialGravity(true)
             .sleepingQuarters(true)
             .emergencyMedical(true)
             .compartmentType(CompartmentType.LUXURY)
             .build()
        );
    }
    
    /**
     * Registers default shields.
     */
    private static void registerShields() {
        // Tier 1 basic shield
        RocketComponentRegistry.register(
            new BaseShield.Builder(
                ResourceLocationHelper.create("galactic", "shield_basic"),
                Component.translatable("component.galactic.shield.basic")
            ).tier(1)
             .mass(250)
             .maxShieldStrength(100)
             .regenerationRate(0.5f)
             .impactResistance(3)
             .shieldType(ShieldType.BASIC)
             .build()
        );
        
        // Tier 2 thermal shield
        RocketComponentRegistry.register(
            new BaseShield.Builder(
                ResourceLocationHelper.create("galactic", "shield_thermal"),
                Component.translatable("component.galactic.shield.thermal")
            ).tier(2)
             .mass(350)
             .maxShieldStrength(150)
             .regenerationRate(1.0f)
             .impactResistance(5)
             .thermalShielded(true)
             .shieldType(ShieldType.THERMAL)
             .build()
        );
        
        // Tier 3 advanced shield
        RocketComponentRegistry.register(
            new BaseShield.Builder(
                ResourceLocationHelper.create("galactic", "shield_advanced"),
                Component.translatable("component.galactic.shield.advanced")
            ).tier(3)
             .mass(500)
             .maxShieldStrength(300)
             .regenerationRate(2.0f)
             .impactResistance(8)
             .radiationShielded(true)
             .empShielded(true)
             .thermalShielded(true)
             .shieldType(ShieldType.ADVANCED)
             .build()
        );
    }
    
    /**
     * Registers default life support systems.
     */
    private static void registerLifeSupports() {
        // Tier 1 basic life support
        RocketComponentRegistry.register(
            new BaseLifeSupport.Builder(
                ResourceLocationHelper.create("galactic", "life_support_basic"),
                Component.translatable("component.galactic.life_support.basic")
            ).tier(1)
             .mass(300)
             .maxCrewCapacity(3)
             .oxygenEfficiency(0.7f)
             .waterRecyclingRate(0.6f)
             .lifeSupportType(LifeSupportType.BASIC)
             .build()
        );
        
        // Tier 2 standard life support
        RocketComponentRegistry.register(
            new BaseLifeSupport.Builder(
                ResourceLocationHelper.create("galactic", "life_support_standard"),
                Component.translatable("component.galactic.life_support.standard")
            ).tier(2)
             .mass(500)
             .maxCrewCapacity(6)
             .oxygenEfficiency(0.85f)
             .waterRecyclingRate(0.8f)
             .advancedMedical(true)
             .lifeSupportType(LifeSupportType.STANDARD)
             .build()
        );
        
        // Tier 3 advanced life support
        RocketComponentRegistry.register(
            new BaseLifeSupport.Builder(
                ResourceLocationHelper.create("galactic", "life_support_advanced"),
                Component.translatable("component.galactic.life_support.advanced")
            ).tier(3)
             .mass(800)
             .maxCrewCapacity(10)
             .oxygenEfficiency(0.95f)
             .waterRecyclingRate(0.95f)
             .advancedMedical(true)
             .radiationScrubbers(true)
             .lifeSupportType(LifeSupportType.ADVANCED)
             .build()
        );
    }
}