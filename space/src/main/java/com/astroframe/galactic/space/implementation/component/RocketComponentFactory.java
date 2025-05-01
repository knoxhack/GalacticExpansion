package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.registry.RegistryManager;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

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
        registerCommandModule(
            new ResourceLocation(GalacticSpace.MOD_ID, "command_module_basic"),
            new CommandModule(
                "command_module_basic",
                1, // Tier 1
                100, // 100 mass units
                1 // Can hold 1 crew member
            )
        );
        
        registerCommandModule(
            new ResourceLocation(GalacticSpace.MOD_ID, "command_module_advanced"),
            new CommandModule(
                "command_module_advanced",
                2, // Tier 2
                150, // 150 mass units
                2 // Can hold 2 crew members
            )
        );
        
        registerCommandModule(
            new ResourceLocation(GalacticSpace.MOD_ID, "command_module_elite"),
            new CommandModule(
                "command_module_elite",
                3, // Tier 3
                200, // 200 mass units
                3 // Can hold 3 crew members
            )
        );
        
        // Engines (Various tiers and types)
        registerEngine(
            new ResourceLocation(GalacticSpace.MOD_ID, "solid_fuel_engine"),
            new RocketEngine(
                "solid_fuel_engine",
                1, // Tier 1
                150, // 150 mass units
                0.8f, // 80% efficiency
                RocketEngine.EngineType.SOLID, // Solid fuel type
                true, // Can operate in atmosphere
                true // Can operate in space
            )
        );
        
        registerEngine(
            new ResourceLocation(GalacticSpace.MOD_ID, "liquid_fuel_engine"),
            new RocketEngine(
                "liquid_fuel_engine",
                2, // Tier 2
                180, // 180 mass units
                0.9f, // 90% efficiency
                RocketEngine.EngineType.LIQUID, // Liquid fuel type
                true, // Can operate in atmosphere
                true // Can operate in space
            )
        );
        
        registerEngine(
            new ResourceLocation(GalacticSpace.MOD_ID, "ion_engine"),
            new RocketEngine(
                "ion_engine",
                3, // Tier 3
                100, // 100 mass units (lighter)
                0.95f, // 95% efficiency
                RocketEngine.EngineType.ION, // Ion engine type
                false, // Cannot operate in atmosphere
                true // Can operate in space
            )
        );
        
        // Fuel Tanks (Various sizes)
        registerFuelTank(
            new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_small"),
            new FuelTank(
                "fuel_tank_small",
                1, // Tier 1
                100, // 100 mass units (empty)
                500 // 500 fuel capacity
            )
        );
        
        registerFuelTank(
            new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_medium"),
            new FuelTank(
                "fuel_tank_medium",
                2, // Tier 2
                200, // 200 mass units (empty)
                1000 // 1000 fuel capacity
            )
        );
        
        registerFuelTank(
            new ResourceLocation(GalacticSpace.MOD_ID, "fuel_tank_large"),
            new FuelTank(
                "fuel_tank_large",
                3, // Tier 3
                300, // 300 mass units (empty)
                2000 // 2000 fuel capacity
            )
        );
        
        // Cargo Bays
        registerCargoBay(
            new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_small"),
            new CargoBay(
                "cargo_bay_small",
                1, // Tier 1
                100, // 100 mass units (empty)
                9 // 9 storage slots (single chest)
            )
        );
        
        registerCargoBay(
            new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_medium"),
            new CargoBay(
                "cargo_bay_medium",
                2, // Tier 2
                200, // 200 mass units (empty)
                27 // 27 storage slots (double chest)
            )
        );
        
        registerCargoBay(
            new ResourceLocation(GalacticSpace.MOD_ID, "cargo_bay_large"),
            new CargoBay(
                "cargo_bay_large",
                3, // Tier 3
                300, // 300 mass units (empty)
                54 // 54 storage slots (double chest x2)
            )
        );
        
        // Passenger Compartments
        registerPassengerCompartment(
            new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_basic"),
            new PassengerCompartment(
                "passenger_compartment_basic",
                1, // Tier 1
                150, // 150 mass units
                2 // Can hold 2 passengers
            )
        );
        
        registerPassengerCompartment(
            new ResourceLocation(GalacticSpace.MOD_ID, "passenger_compartment_advanced"),
            new PassengerCompartment(
                "passenger_compartment_advanced",
                2, // Tier 2
                250, // 250 mass units
                4 // Can hold 4 passengers
            )
        );
        
        // Shields
        registerShield(
            new ResourceLocation(GalacticSpace.MOD_ID, "heat_shield_basic"),
            new Shield(
                "heat_shield_basic",
                1, // Tier 1
                100, // 100 mass units
                5, // Impact resistance rating (1-10)
                50 // Shield strength
            )
        );
        
        registerShield(
            new ResourceLocation(GalacticSpace.MOD_ID, "heat_shield_advanced"),
            new Shield(
                "heat_shield_advanced",
                2, // Tier 2
                150, // 150 mass units
                8, // Impact resistance rating (1-10)
                80 // Shield strength
            )
        );
        
        // Life Support Systems
        registerLifeSupport(
            new ResourceLocation(GalacticSpace.MOD_ID, "life_support_basic"),
            new LifeSupport(
                "life_support_basic",
                1, // Tier 1
                100, // 100 mass units
                3 // Supports 3 crew members
            )
        );
        
        registerLifeSupport(
            new ResourceLocation(GalacticSpace.MOD_ID, "life_support_advanced"),
            new LifeSupport(
                "life_support_advanced",
                2, // Tier 2
                150, // 150 mass units
                6 // Supports 6 crew members
            )
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
        private final String id;
        private final int tier;
        private final int mass;
        private final int crewCapacity;
        
        public CommandModule(String id, int tier, int mass, int crewCapacity) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.crewCapacity = crewCapacity;
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
        public String getId() {
            return id;
        }
    }
    
    /**
     * Implementation of a rocket engine.
     */
    private static class RocketEngine implements IRocketEngine {
        private final String id;
        private final int tier;
        private final int mass;
        private final float efficiency;
        private final EngineType type;
        private final boolean atmosphereOperable;
        private final boolean spaceOperable;
        
        public RocketEngine(String id, int tier, int mass, float efficiency,
                          EngineType type, boolean atmosphereOperable, boolean spaceOperable) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.efficiency = efficiency;
            this.type = type;
            this.atmosphereOperable = atmosphereOperable;
            this.spaceOperable = spaceOperable;
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
        public float getEfficiency() {
            return efficiency;
        }
        
        @Override
        public EngineType getEngineType() {
            return type;
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
        public String getId() {
            return id;
        }
        
        /**
         * Types of rocket engines.
         */
        public enum EngineType {
            SOLID,
            LIQUID,
            ION,
            NUCLEAR,
            CHEMICAL,
            PLASMA
        }
    }
    
    /**
     * Implementation of a fuel tank.
     */
    private static class FuelTank implements IFuelTank {
        private final String id;
        private final int tier;
        private final int mass;
        private final int fuelCapacity;
        
        public FuelTank(String id, int tier, int mass, int fuelCapacity) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.fuelCapacity = fuelCapacity;
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
        public int getMaxFuelCapacity() {
            return fuelCapacity;
        }
        
        @Override
        public String getId() {
            return id;
        }
    }
    
    /**
     * Implementation of a cargo bay.
     */
    private static class CargoBay implements ICargoBay {
        private final String id;
        private final int tier;
        private final int mass;
        private final int storageCapacity;
        
        public CargoBay(String id, int tier, int mass, int storageCapacity) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.storageCapacity = storageCapacity;
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
        public int getStorageCapacity() {
            return storageCapacity;
        }
        
        @Override
        public String getId() {
            return id;
        }
    }
    
    /**
     * Implementation of a passenger compartment.
     */
    private static class PassengerCompartment implements IPassengerCompartment {
        private final String id;
        private final int tier;
        private final int mass;
        private final int passengerCapacity;
        
        public PassengerCompartment(String id, int tier, int mass, int passengerCapacity) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.passengerCapacity = passengerCapacity;
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
        public int getPassengerCapacity() {
            return passengerCapacity;
        }
        
        @Override
        public String getId() {
            return id;
        }
    }
    
    /**
     * Implementation of a shield.
     */
    private static class Shield implements IShield {
        private final String id;
        private final int tier;
        private final int mass;
        private final int impactResistance;
        private final int shieldStrength;
        
        public Shield(String id, int tier, int mass, int impactResistance, int shieldStrength) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.impactResistance = impactResistance;
            this.shieldStrength = shieldStrength;
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
        public int getImpactResistance() {
            return impactResistance;
        }
        
        @Override
        public int getMaxShieldStrength() {
            return shieldStrength;
        }
        
        @Override
        public String getId() {
            return id;
        }
    }
    
    /**
     * Implementation of a life support system.
     */
    private static class LifeSupport implements ILifeSupport {
        private final String id;
        private final int tier;
        private final int mass;
        private final int maxCrewCapacity;
        
        public LifeSupport(String id, int tier, int mass, int maxCrewCapacity) {
            this.id = id;
            this.tier = tier;
            this.mass = mass;
            this.maxCrewCapacity = maxCrewCapacity;
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
        public int getMaxCrewCapacity() {
            return maxCrewCapacity;
        }
        
        @Override
        public String getId() {
            return id;
        }
    }
}