# Rocket Component Implementation Guide

This document provides an overview of the component implementations in the Galactic Expansion mod and explains how to properly instantiate and use them.

## Component Updates

We've updated all rocket component implementations to match their respective interfaces and follow a consistent pattern:

1. **BaseLifeSupport**: Updated to match ILifeSupport interface with proper life support metrics
2. **PassengerCompartmentImpl**: Updated to match IPassengerCompartment interface with passenger management
3. **RocketEngineImpl**: Updated to match IRocketEngine interface with proper fuel type handling
4. **CargoBayImpl**: Updated to match ICargoBay interface with inventory management
5. **FuelTankImpl**: Updated to match IFuelTank interface with fuel consumption logic
6. **CommandModuleImpl**: Updated with builder pattern for easier instantiation

## Builder Pattern Usage

All component implementations now use the Builder pattern for easier instantiation. Here are examples for each component:

### Command Module

```java
// Create a basic command module
CommandModuleImpl basicModule = new CommandModuleImpl.Builder(
    new ResourceLocation("galactic", "basic_command_module"))
    .build();

// Create a more advanced command module
CommandModuleImpl advancedModule = new CommandModuleImpl.Builder(
    new ResourceLocation("galactic", "advanced_command_module"))
    .name("Advanced Command Module")
    .description("A high-tech command module with enhanced capabilities.")
    .tier(3)
    .computingPower(300)
    .sensorStrength(250)
    .navigationAccuracy(0.95f)
    .crewCapacity(4)
    .advancedLifeSupport(true)
    .automatedLanding(true)
    .emergencyEvacuation(true)
    .build();
```

### Fuel Tank

```java
// Create a basic fuel tank
FuelTankImpl basicTank = new FuelTankImpl.Builder(
    new ResourceLocation("galactic", "basic_fuel_tank"))
    .build();

// Create a high-capacity antimatter fuel tank
FuelTankImpl antimatterTank = new FuelTankImpl.Builder(
    new ResourceLocation("galactic", "antimatter_fuel_tank"))
    .name("Antimatter Containment Tank")
    .description("A specialized tank for storing volatile antimatter fuel.")
    .tier(3)
    .maxFuelCapacity(5000)
    .fuelType(IRocketEngine.FuelType.ANTIMATTER)
    .leakResistance(0.99f)
    .explosionResistance(0.95f)
    .build();
```

### Life Support

```java
// Create a basic life support system
BaseLifeSupport basicLifeSupport = new BaseLifeSupport.Builder(
    new ResourceLocation("galactic", "basic_life_support"))
    .build();

// Create an advanced life support system
BaseLifeSupport advancedLifeSupport = new BaseLifeSupport.Builder(
    new ResourceLocation("galactic", "advanced_life_support"))
    .name("Advanced Life Support System")
    .description("A state-of-the-art life support system with extended capabilities.")
    .tier(3)
    .maxCrewCapacity(8)
    .oxygenGenerationRate(120)
    .waterRecyclingEfficiency(0.95f)
    .foodProductionRate(20)
    .wasteManagementEfficiency(0.9f)
    .atmosphericQuality(0.98f)
    .backupSystems(true)
    .radiationFiltering(true)
    .emergencyMode(true)
    .build();
```

### Passenger Compartment

```java
// Create a basic passenger compartment
PassengerCompartmentImpl basicCompartment = new PassengerCompartmentImpl.Builder(
    new ResourceLocation("galactic", "basic_passenger_compartment"))
    .build();

// Create a luxury passenger compartment
PassengerCompartmentImpl luxuryCompartment = new PassengerCompartmentImpl.Builder(
    new ResourceLocation("galactic", "luxury_passenger_compartment"))
    .name("Luxury Passenger Compartment")
    .description("A spacious and comfortable compartment for passengers.")
    .tier(3)
    .passengerCapacity(6)
    .comfortLevel(9)
    .hasLifeSupport(true)
    .hasGravitySimulation(true)
    .hasRadiationShielding(true)
    .build();
```

### Cargo Bay

```java
// Create a basic cargo bay
CargoBayImpl basicCargoBay = new CargoBayImpl.Builder(
    new ResourceLocation("galactic", "basic_cargo_bay"))
    .build();

// Create a specialized cargo bay
CargoBayImpl specializedCargoBay = new CargoBayImpl.Builder(
    new ResourceLocation("galactic", "specialized_cargo_bay"))
    .name("Specialized Cargo Bay")
    .description("A cargo bay with environmental controls and shielding.")
    .tier(2)
    .storageCapacity(54)
    .vacuumSeal(true)
    .temperatureRegulation(true)
    .radiationShielding(true)
    .build();
```

### Rocket Engine

```java
// Create a basic chemical rocket engine
RocketEngineImpl basicEngine = new RocketEngineImpl.Builder(
    new ResourceLocation("galactic", "basic_rocket_engine"))
    .build();

// Create an advanced antimatter engine
RocketEngineImpl antimatterEngine = new RocketEngineImpl.Builder(
    new ResourceLocation("galactic", "antimatter_rocket_engine"))
    .name("Antimatter Propulsion Engine")
    .description("An extremely powerful engine using antimatter for propulsion.")
    .tier(3)
    .thrust(10000)
    .fuelConsumptionRate(5)
    .efficiency(1.8f)
    .fuelType(IRocketEngine.FuelType.ANTIMATTER)
    .atmosphereCapable(false)
    .spaceCapable(true)
    .build();
```

## Component Type Changes

All component implementations now use the `RocketComponentType` enum instead of the deprecated `ComponentType` enum:

```java
public enum RocketComponentType {
    ENGINE,
    FUEL,
    COCKPIT,
    STORAGE,
    STRUCTURE,
    SHIELD,
    LIFE_SUPPORT
}
```

## Tooltip Updates

All components now have improved tooltips with more descriptive text and proper formatting. The tooltips display basic information by default, and more detailed information when the detailed flag is set to true.

## Interface Compliance

All component implementations now fully comply with their respective interfaces, with proper method signatures and type compatibility. This ensures that components can be properly registered and used throughout the mod.