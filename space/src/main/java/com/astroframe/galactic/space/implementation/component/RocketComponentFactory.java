package com.astroframe.galactic.space.implementation.component;

import net.minecraft.world.item.ItemStack;
import com.astroframe.galactic.core.api.registry.RegistryManager;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
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
                .fuelType(IRocketEngine.FuelType.CHEMICAL)
                .atmosphereCapable(true)
                .spaceCapable(true)
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
                .fuelType(IRocketEngine.FuelType.CHEMICAL)
                .atmosphereCapable(true)
                .spaceCapable(true)
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
                .fuelType(IRocketEngine.FuelType.ELECTRICAL)
                .atmosphereCapable(false)
                .spaceCapable(true)
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
                .fuelType(IRocketEngine.FuelType.CHEMICAL)
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
                .fuelType(IRocketEngine.FuelType.CHEMICAL)
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
                .fuelType(IRocketEngine.FuelType.CHEMICAL)
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
            new BaseLifeSupport.Builder(basicLifeSupportId)
                .name("Basic Life Support System")
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
            new BaseLifeSupport.Builder(advancedLifeSupportId)
                .name("Advanced Life Support System")
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
        
        public RocketEngine(ResourceLocation id, String name, String description, 
                          int tier, int mass, int maxDurability, float efficiency,
                          FuelType fuelType, boolean atmosphereOperable, boolean spaceOperable) {
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