package com.astroframe.galactic.core.api.space.util;

import com.astroframe.galactic.core.TagHelper;
import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.core.api.space.component.enums.CompartmentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.LifeSupportType;
import com.astroframe.galactic.core.api.space.component.enums.ShieldType;
import com.astroframe.galactic.core.api.common.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Utility class for working with rocket components.
 * This class is designed to bridge between the core API and the implementation modules.
 * 
 * Uses the ServiceLoader pattern to find component factory implementations.
 */
public class ComponentUtils {
    
    /**
     * Interface for component factories that can create components from tag data.
     */
    public interface ComponentFactory {
        /**
         * Creates a component from its tag data.
         * 
         * @param id The component ID
         * @param tag The tag data
         * @return The created component, or null if creation failed
         */
        IRocketComponent createFromTag(ResourceLocation id, CompoundTag tag);
        
        /**
         * Checks if this factory can create components for a given ID.
         * 
         * @param id The component ID
         * @return True if this factory can create components for the ID
         */
        boolean canHandle(ResourceLocation id);
    }
    
    // Lazy-loaded component factories
    private static List<ComponentFactory> factories = null;
    
    /**
     * Gets all available component factories.
     * 
     * @return The list of available factories
     */
    private static List<ComponentFactory> getFactories() {
        if (factories == null) {
            factories = new ArrayList<>();
            ServiceLoader<ComponentFactory> loader = ServiceLoader.load(ComponentFactory.class);
            loader.forEach(factories::add);
        }
        return factories;
    }

    /**
     * Creates a component from its saved tag data.
     * This method will delegate to registered component factories.
     * 
     * @param id The component ID
     * @param tag The tag data
     * @return The created component, or null if creation failed
     */
    public static IRocketComponent createComponentFromTag(ResourceLocation id, CompoundTag tag) {
        for (ComponentFactory factory : getFactories()) {
            if (factory.canHandle(id)) {
                IRocketComponent component = factory.createFromTag(id, tag);
                if (component != null) {
                    return component;
                }
            }
        }
        
        // Fallback behavior for when no factory can handle this component
        RocketComponentType type = getComponentTypeFromTag(tag);
        if (type == null) {
            return null;
        }
        
        // Create a simple default component based on type
        return createDefaultComponent(id, type, tag);
    }
    
    /**
     * Creates a default component with basic properties.
     * 
     * @param id The component ID
     * @param type The component type
     * @param tag The tag data
     * @return A simple component with default properties
     */
    private static IRocketComponent createDefaultComponent(ResourceLocation id, RocketComponentType type, CompoundTag tag) {
        // Extract common properties
        int tier = TagHelper.getInt(tag, "Tier", 1);
        int mass = TagHelper.getInt(tag, "Mass", 100);
        
        // Create appropriate component based on type
        switch (type) {
            case ENGINE:
                return createDefaultEngine(id, tier, mass, tag);
            case FUEL_TANK:
                return createDefaultFuelTank(id, tier, mass, tag);
            case COCKPIT:
                return createDefaultCommandModule(id, tier, mass, tag);
            case STORAGE:
            case CARGO_BAY:
                return createDefaultCargoBay(id, tier, mass, tag);
            case PASSENGER_COMPARTMENT:
                return createDefaultPassengerCompartment(id, tier, mass, tag);
            case SHIELDING:
            case SHIELD:
                return createDefaultShield(id, tier, mass, tag);
            case LIFE_SUPPORT:
                return createDefaultLifeSupport(id, tier, mass, tag);
            default:
                return null;
        }
    }
    
    /**
     * Creates a default engine component.
     */
    private static IRocketEngine createDefaultEngine(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int thrust = TagHelper.getInt(tag, "Thrust", 1000 * tier);
        float efficiency = TagHelper.getFloat(tag, "Efficiency", 0.5f * tier);
        boolean atmosphereCapable = TagHelper.getBoolean(tag, "AtmosphereCapable", false);
        boolean spaceCapable = TagHelper.getBoolean(tag, "SpaceCapable", true);
        
        EngineType engineType = EngineType.CHEMICAL;
        String engineTypeStr = TagHelper.getString(tag, "EngineType", "");
        if (!engineTypeStr.isEmpty()) {
            try {
                engineType = EngineType.valueOf(engineTypeStr);
            } catch (IllegalArgumentException e) {
                // Use default
            }
        }
        
        return new DefaultRocketEngine(id, tier, mass, thrust, efficiency, atmosphereCapable, spaceCapable, engineType);
    }
    
    /**
     * Creates a default fuel tank component.
     */
    private static IFuelTank createDefaultFuelTank(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int capacity = TagHelper.getInt(tag, "Capacity", 5000 * tier);
        
        return new DefaultFuelTank(id, tier, mass, capacity);
    }
    
    /**
     * Creates a default command module component.
     */
    private static ICommandModule createDefaultCommandModule(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int crewCapacity = TagHelper.getInt(tag, "CrewCapacity", tier);
        int computerLevel = TagHelper.getInt(tag, "ComputerLevel", tier);
        
        return new DefaultCommandModule(id, tier, mass, crewCapacity, computerLevel);
    }
    
    /**
     * Creates a default cargo bay component.
     */
    private static ICargoBay createDefaultCargoBay(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int storageCapacity = TagHelper.getInt(tag, "StorageCapacity", 9 * tier);
        
        return new DefaultCargoBay(id, tier, mass, storageCapacity);
    }
    
    /**
     * Creates a default passenger compartment component.
     */
    private static IPassengerCompartment createDefaultPassengerCompartment(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int passengerCapacity = TagHelper.getInt(tag, "PassengerCapacity", 2 * tier);
        
        CompartmentType compartmentType = CompartmentType.STANDARD;
        String compartmentTypeStr = TagHelper.getString(tag, "CompartmentType", "");
        if (!compartmentTypeStr.isEmpty()) {
            try {
                compartmentType = CompartmentType.valueOf(compartmentTypeStr);
            } catch (IllegalArgumentException e) {
                // Use default
            }
        }
        
        return new DefaultPassengerCompartment(id, tier, mass, passengerCapacity, compartmentType);
    }
    
    /**
     * Creates a default shield component.
     */
    private static IShield createDefaultShield(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int shieldStrength = TagHelper.getInt(tag, "ShieldStrength", 50 * tier);
        int impactResistance = TagHelper.getInt(tag, "ImpactResistance", 2 * tier);
        
        ShieldType shieldType = ShieldType.BASIC;
        String shieldTypeStr = TagHelper.getString(tag, "ShieldType", "");
        if (!shieldTypeStr.isEmpty()) {
            try {
                shieldType = ShieldType.valueOf(shieldTypeStr);
            } catch (IllegalArgumentException e) {
                // Use default
            }
        }
        
        return new DefaultShield(id, tier, mass, shieldStrength, impactResistance, shieldType);
    }
    
    /**
     * Creates a default life support component.
     */
    private static ILifeSupport createDefaultLifeSupport(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int maxCrewCapacity = TagHelper.getInt(tag, "MaxCrewCapacity", 3 * tier);
        int oxygenEfficiency = TagHelper.getInt(tag, "OxygenEfficiency", tier);
        
        LifeSupportType lifeSupportType = LifeSupportType.STANDARD;
        String lifeSupportTypeStr = TagHelper.getString(tag, "LifeSupportType", "");
        if (!lifeSupportTypeStr.isEmpty()) {
            try {
                lifeSupportType = LifeSupportType.valueOf(lifeSupportTypeStr);
            } catch (IllegalArgumentException e) {
                // Use default
            }
        }
        
        return new DefaultLifeSupport(id, tier, mass, maxCrewCapacity, oxygenEfficiency, lifeSupportType);
    }
    
    /**
     * Determines the component type from a tag.
     * 
     * @param tag The tag
     * @return The component type, or null if not found
     */
    public static RocketComponentType getComponentTypeFromTag(CompoundTag tag) {
        String typeStr = TagHelper.getString(tag, "Type", "");
        if (!typeStr.isEmpty()) {
            try {
                return RocketComponentType.valueOf(typeStr);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Saves a list of components to a ListTag.
     * 
     * @param components The components to save
     * @return The list tag containing the saved components
     */
    public static ListTag saveComponentsToTag(List<IRocketComponent> components) {
        ListTag listTag = new ListTag();
        for (IRocketComponent component : components) {
            CompoundTag componentTag = new CompoundTag();
            component.save(componentTag);
            listTag.add(componentTag);
        }
        return listTag;
    }
    
    /**
     * Loads a list of components from a ListTag.
     * 
     * @param listTag The list tag containing component data
     * @return The list of loaded components
     */
    public static List<IRocketComponent> loadComponentsFromTag(ListTag listTag) {
        List<IRocketComponent> components = new ArrayList<>();
        for (int i = 0; i < listTag.size(); i++) {
            // In NeoForge 1.21.5, we need to check the tag type and cast
            if (listTag.get(i) instanceof CompoundTag) {
                CompoundTag componentTag = (CompoundTag) listTag.get(i);
                
                // Use TagHelper to get the ID string
                String idStr = TagHelper.getString(componentTag, "ID", "");
                if (!idStr.isEmpty()) {
                    try {
                        // Use ResourceLocation.parse which is the preferred method in NeoForge 1.21.5
                        ResourceLocation id = ResourceLocation.parse(idStr);
                        IRocketComponent component = createComponentFromTag(id, componentTag);
                        if (component != null) {
                            components.add(component);
                        }
                    } catch (Exception e) {
                        // Log error and continue with next component
                        System.err.println("Error loading component: " + e.getMessage());
                    }
                }
            }
        }
        return components;
    }
    
    /**
     * Default implementation classes for components
     */
    
    /**
     * Simple implementation of IRocketEngine.
     */
    private static class DefaultRocketEngine extends AbstractRocketComponent implements IRocketEngine {
        private final double thrust;
        private final double fuelConsumptionRate;
        private final double efficiency;
        private final double heatGeneration;
        private final boolean atmosphereCapable;
        private final boolean spaceCapable;
        private final EngineType engineType;
        private final com.astroframe.galactic.core.api.space.component.FuelType fuelType;
        private final List<com.astroframe.galactic.core.api.space.component.FuelType> compatibleFuels = new ArrayList<>();
        
        public DefaultRocketEngine(ResourceLocation id, int tier, int mass, double thrust, double efficiency, 
                                 boolean atmosphereCapable, boolean spaceCapable, EngineType engineType) {
            super(id, RocketComponentType.ENGINE, tier, mass);
            this.thrust = thrust;
            this.efficiency = efficiency;
            this.atmosphereCapable = atmosphereCapable;
            this.spaceCapable = spaceCapable;
            this.engineType = engineType;
            this.fuelConsumptionRate = thrust / (efficiency * 10);
            this.heatGeneration = thrust / 100;
            
            // Determine fuel type based on engine type
            if (engineType == EngineType.ANTIMATTER) {
                this.fuelType = com.astroframe.galactic.core.api.space.component.FuelType.ANTIMATTER;
                this.compatibleFuels.add(com.astroframe.galactic.core.api.space.component.FuelType.ANTIMATTER);
            } else if (engineType == EngineType.ION) {
                this.fuelType = com.astroframe.galactic.core.api.space.component.FuelType.ION;
                this.compatibleFuels.add(com.astroframe.galactic.core.api.space.component.FuelType.ION);
            } else {
                this.fuelType = com.astroframe.galactic.core.api.space.component.FuelType.CHEMICAL;
                this.compatibleFuels.add(com.astroframe.galactic.core.api.space.component.FuelType.CHEMICAL);
            }
        }
        
        @Override
        public double getThrust() {
            return thrust;
        }
        
        @Override
        public double getFuelConsumptionRate() {
            return fuelConsumptionRate;
        }
        
        @Override
        public double getEfficiency() {
            return efficiency;
        }
        
        @Override
        public com.astroframe.galactic.core.api.space.component.FuelType getFuelType() {
            return fuelType;
        }
        
        @Override
        public boolean canOperateInAtmosphere() {
            return atmosphereCapable;
        }
        
        @Override
        public boolean canOperateInSpace() {
            return spaceCapable;
        }
        
        @Override
        public double getHeatGeneration() {
            return heatGeneration;
        }
        
        @Override
        public List<com.astroframe.galactic.core.api.space.component.FuelType> getCompatibleFuels() {
            return new ArrayList<>(compatibleFuels);
        }
        
        @Override
        public EngineType getEngineType() {
            return engineType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putDouble("Thrust", thrust);
            tag.putDouble("Efficiency", efficiency);
            tag.putBoolean("AtmosphereCapable", atmosphereCapable);
            tag.putBoolean("SpaceCapable", spaceCapable);
            tag.putString("EngineType", engineType.name());
            tag.putString("FuelType", fuelType.name());
            tag.putDouble("HeatGeneration", heatGeneration);
            
            // Save compatible fuels
            CompoundTag fuelsTag = new CompoundTag();
            fuelsTag.putInt("Count", compatibleFuels.size());
            for (int i = 0; i < compatibleFuels.size(); i++) {
                fuelsTag.putString("Fuel" + i, compatibleFuels.get(i).name());
            }
            tag.put("CompatibleFuels", fuelsTag);
        }
    }
    
    /**
     * Simple implementation of IFuelTank.
     */
    private static class DefaultFuelTank extends AbstractRocketComponent implements IFuelTank {
        private final int capacity;
        private int currentFuel;
        private final com.astroframe.galactic.core.api.space.component.FuelType fuelType;
        private final float leakResistance;
        private final float explosionResistance;
        
        public DefaultFuelTank(ResourceLocation id, int tier, int mass, int capacity) {
            super(id, RocketComponentType.FUEL_TANK, tier, mass);
            this.capacity = capacity;
            this.currentFuel = 0;
            this.fuelType = com.astroframe.galactic.core.api.space.component.FuelType.CHEMICAL; // Default fuel type
            this.leakResistance = 0.8f * tier;
            this.explosionResistance = 0.7f * tier;
        }
        
        @Override
        public int getMaxFuelCapacity() {
            return capacity;
        }
        
        @Override
        public int getCurrentFuelLevel() {
            return currentFuel;
        }
        
        @Override
        public int addFuel(int amount) {
            int spaceLeft = capacity - currentFuel;
            int toAdd = Math.min(amount, spaceLeft);
            currentFuel += toAdd;
            return toAdd;
        }
        
        @Override
        public int consumeFuel(int amount) {
            int available = Math.min(amount, currentFuel);
            currentFuel -= available;
            return available;
        }
        
        @Override
        public com.astroframe.galactic.core.api.space.component.FuelType getFuelType() {
            return fuelType;
        }
        
        @Override
        public float getLeakResistance() {
            return leakResistance;
        }
        
        @Override
        public float getExplosionResistance() {
            return explosionResistance;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("Capacity", capacity);
            tag.putInt("CurrentFuel", currentFuel);
            tag.putString("FuelType", fuelType.name());
            tag.putFloat("LeakResistance", leakResistance);
            tag.putFloat("ExplosionResistance", explosionResistance);
        }
    }
    
    /**
     * Simple implementation of ICommandModule.
     */
    private static class DefaultCommandModule extends AbstractRocketComponent implements ICommandModule {
        private final int crewCapacity;
        private final int computerLevel;
        private final int sensorStrength;
        private final float navigationAccuracy;
        private final boolean hasAdvancedLifeSupport;
        private final boolean hasAutomatedLanding;
        private final boolean hasEmergencyEvacuation;
        
        public DefaultCommandModule(ResourceLocation id, int tier, int mass, int crewCapacity, int computerLevel) {
            super(id, RocketComponentType.COCKPIT, tier, mass);
            this.crewCapacity = crewCapacity;
            this.computerLevel = computerLevel;
            this.sensorStrength = tier * 100;
            this.navigationAccuracy = 0.6f + (tier * 0.1f);
            this.hasAdvancedLifeSupport = tier >= 2;
            this.hasAutomatedLanding = tier >= 2;
            this.hasEmergencyEvacuation = tier >= 3;
        }
        
        @Override
        public int getCrewCapacity() {
            return crewCapacity;
        }
        
        @Override
        public int getComputingPower() {
            return computerLevel;
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
            return hasAdvancedLifeSupport;
        }
        
        @Override
        public boolean hasAutomatedLanding() {
            return hasAutomatedLanding;
        }
        
        @Override
        public boolean hasEmergencyEvacuation() {
            return hasEmergencyEvacuation;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("CrewCapacity", crewCapacity);
            tag.putInt("ComputerLevel", computerLevel);
            tag.putInt("SensorStrength", sensorStrength);
            tag.putFloat("NavigationAccuracy", navigationAccuracy);
            tag.putBoolean("HasAdvancedLifeSupport", hasAdvancedLifeSupport);
            tag.putBoolean("HasAutomatedLanding", hasAutomatedLanding);
            tag.putBoolean("HasEmergencyEvacuation", hasEmergencyEvacuation);
        }
    }
    
    /**
     * Simple implementation of ICargoBay.
     */
    private static class DefaultCargoBay extends AbstractRocketComponent implements ICargoBay {
        private final int maxCapacity;
        private final int maxSlots;
        private final List<net.minecraft.world.item.ItemStack> items;
        private final boolean securityFeatures;
        private final boolean environmentControl;
        private final boolean automatedLoading;
        
        public DefaultCargoBay(ResourceLocation id, int tier, int mass, int maxCapacity) {
            super(id, RocketComponentType.CARGO_BAY, tier, mass);
            this.maxCapacity = maxCapacity;
            this.maxSlots = 9 * tier;
            this.items = new ArrayList<>();
            this.securityFeatures = tier >= 2;
            this.environmentControl = tier >= 2;
            this.automatedLoading = tier >= 3;
        }
        
        @Override
        public int getMaxCapacity() {
            return maxCapacity;
        }
        
        @Override
        public int getMaxSlots() {
            return maxSlots;
        }
        
        @Override
        public int getCurrentUsedCapacity() {
            int usedCapacity = 0;
            for (net.minecraft.world.item.ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    usedCapacity += calculateItemWeight(stack);
                }
            }
            return usedCapacity;
        }
        
        @Override
        public List<net.minecraft.world.item.ItemStack> getItems() {
            return new ArrayList<>(items);
        }
        
        @Override
        public boolean addItem(net.minecraft.world.item.ItemStack stack) {
            if (stack.isEmpty()) {
                return false;
            }
            
            // Check if there's enough space
            if (items.size() >= maxSlots) {
                return false;
            }
            
            float itemWeight = calculateItemWeight(stack);
            if (getCurrentUsedCapacity() + itemWeight > maxCapacity) {
                return false;
            }
            
            // Add the item
            items.add(stack.copy());
            return true;
        }
        
        @Override
        public net.minecraft.world.item.ItemStack removeItem(int index) {
            if (index < 0 || index >= items.size()) {
                return net.minecraft.world.item.ItemStack.EMPTY;
            }
            
            return items.remove(index);
        }
        
        @Override
        public boolean hasSecurityFeatures() {
            return securityFeatures;
        }
        
        @Override
        public boolean hasEnvironmentControl() {
            return environmentControl;
        }
        
        @Override
        public boolean hasAutomatedLoading() {
            return automatedLoading;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("MaxCapacity", maxCapacity);
            tag.putInt("MaxSlots", maxSlots);
            tag.putBoolean("SecurityFeatures", securityFeatures);
            tag.putBoolean("EnvironmentControl", environmentControl);
            tag.putBoolean("AutomatedLoading", automatedLoading);
            
            // Save items
            ListTag itemsTag = new ListTag();
            for (net.minecraft.world.item.ItemStack stack : items) {
                if (!stack.isEmpty()) {
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putString("id", stack.getItem().toString());
                    itemTag.putInt("count", stack.getCount());
                    
                    // Add any tags the item might have
                    // Get item tag data and add it if not empty
                    // Use the tag getter based on what's available in the ItemStack API
                    CompoundTag itemTagData = null;
                    try {
                        // Try to get tag using reflection to handle API differences
                        itemTagData = (CompoundTag) stack.getClass().getMethod("getTag").invoke(stack);
                    } catch (Exception e) {
                        // If that fails, create an empty tag
                        itemTagData = new CompoundTag();
                    }
                    
                    if (itemTagData != null && !itemTagData.isEmpty()) {
                        itemTag.put("tag", itemTagData);
                    }
                    
                    itemsTag.add(itemTag);
                }
            }
            
            tag.put("Items", itemsTag);
        }
    }
    
    /**
     * Simple implementation of IPassengerCompartment.
     */
    private static class DefaultPassengerCompartment extends AbstractRocketComponent implements IPassengerCompartment {
        private final int passengerCapacity;
        private final CompartmentType compartmentType;
        private final int comfortLevel;
        private final boolean hasLifeSupport;
        private final boolean hasGravitySimulation;
        private final boolean hasRadiationShielding;
        private final List<Player> passengers;
        
        public DefaultPassengerCompartment(ResourceLocation id, int tier, int mass, int passengerCapacity, CompartmentType compartmentType) {
            super(id, RocketComponentType.PASSENGER_COMPARTMENT, tier, mass);
            this.passengerCapacity = passengerCapacity;
            this.compartmentType = compartmentType;
            this.comfortLevel = Math.min(10, 2 + tier * 2); // Comfort level from 1-10
            this.hasLifeSupport = tier >= 1;
            this.hasGravitySimulation = tier >= 3;
            this.hasRadiationShielding = tier >= 2;
            this.passengers = new ArrayList<>();
        }
        
        @Override
        public int getPassengerCapacity() {
            return passengerCapacity;
        }
        
        public CompartmentType getCompartmentType() {
            return compartmentType;
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
        
        @Override
        public List<Player> getPassengers() {
            return new ArrayList<>(passengers);
        }
        
        @Override
        public boolean addPassenger(Player player) {
            if (passengers.size() < passengerCapacity && !passengers.contains(player)) {
                return passengers.add(player);
            }
            return false;
        }
        
        @Override
        public void removePassenger(Player player) {
            passengers.remove(player);
        }
        
        public void clearPassengers() {
            passengers.clear();
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("PassengerCapacity", passengerCapacity);
            tag.putString("CompartmentType", compartmentType.name());
            tag.putInt("ComfortLevel", comfortLevel);
            tag.putBoolean("HasLifeSupport", hasLifeSupport);
            tag.putBoolean("HasGravitySimulation", hasGravitySimulation);
            tag.putBoolean("HasRadiationShielding", hasRadiationShielding);
            
            // We don't save passengers since they are entity references
            // and should be managed by the rocket entity itself
        }
    }
    
    /**
     * Simple implementation of IShield.
     */
    private static class DefaultShield extends AbstractRocketComponent implements IShield {
        private final int shieldStrength;
        private final int impactResistance;
        private final int heatResistance;
        private final int radiationResistance;
        private final ShieldType shieldType;
        private int currentShieldStrength;
        private boolean isActive;
        
        public DefaultShield(ResourceLocation id, int tier, int mass, int shieldStrength, int impactResistance, ShieldType shieldType) {
            super(id, RocketComponentType.SHIELD, tier, mass);
            this.shieldStrength = shieldStrength;
            this.impactResistance = impactResistance;
            this.shieldType = shieldType;
            this.currentShieldStrength = shieldStrength;
            this.isActive = false;
            
            // Set resistances based on shield type and tier
            if (shieldType == ShieldType.THERMAL) {
                this.heatResistance = Math.min(10, 5 + tier * 1);
                this.radiationResistance = Math.min(10, 2 + tier);
            } else if (shieldType == ShieldType.RADIATION) {
                this.heatResistance = Math.min(10, 2 + tier);
                this.radiationResistance = Math.min(10, 5 + tier * 1);
            } else if (shieldType == ShieldType.DEFLECTOR || shieldType == ShieldType.ADVANCED) {
                this.heatResistance = Math.min(10, 3 + tier * 1);
                this.radiationResistance = Math.min(10, 3 + tier * 1);
            } else {
                this.heatResistance = Math.min(10, 2 + tier);
                this.radiationResistance = Math.min(10, 2 + tier);
            }
        }
        
        @Override
        public int getMaxShieldStrength() {
            return shieldStrength;
        }
        
        @Override
        public int getCurrentShieldStrength() {
            return currentShieldStrength;
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
        public int applyDamage(int amount) {
            if (!isActive || currentShieldStrength <= 0) {
                return amount;
            }
            
            int damageAbsorbed = Math.min(currentShieldStrength, amount);
            currentShieldStrength -= damageAbsorbed;
            
            return amount - damageAbsorbed;
        }
        
        @Override
        public int regenerate(int amount) {
            int before = currentShieldStrength;
            currentShieldStrength = Math.min(shieldStrength, currentShieldStrength + amount);
            return currentShieldStrength - before;
        }
        
        @Override
        public boolean isActive() {
            return isActive;
        }
        
        @Override
        public void setActive(boolean active) {
            this.isActive = active;
        }
        
        public ShieldType getShieldType() {
            return shieldType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("ShieldStrength", shieldStrength);
            tag.putInt("CurrentShieldStrength", currentShieldStrength);
            tag.putInt("ImpactResistance", impactResistance);
            tag.putInt("HeatResistance", heatResistance);
            tag.putInt("RadiationResistance", radiationResistance);
            tag.putString("ShieldType", shieldType.name());
            tag.putBoolean("IsActive", isActive);
        }
    }
    
    /**
     * Simple implementation of ILifeSupport.
     */
    private static class DefaultLifeSupport extends AbstractRocketComponent implements ILifeSupport {
        private final int maxCrewCapacity;
        private final int oxygenGenerationRate;
        private final float waterRecyclingEfficiency;
        private final int foodProductionRate;
        private final float wasteManagementEfficiency;
        private final float atmosphericQuality;
        private final LifeSupportType lifeSupportType;
        private final boolean hasBackupSystems;
        private final boolean hasRadiationFiltering;
        private final boolean hasEmergencyMode;
        private boolean emergencyModeActive;
        private boolean broken;
        
        public DefaultLifeSupport(ResourceLocation id, int tier, int mass, int maxCrewCapacity, int oxygenEfficiency, LifeSupportType lifeSupportType) {
            super(id, RocketComponentType.LIFE_SUPPORT, tier, mass);
            this.maxCrewCapacity = maxCrewCapacity;
            this.oxygenGenerationRate = oxygenEfficiency * 10;
            this.lifeSupportType = lifeSupportType;
            this.waterRecyclingEfficiency = Math.min(1.0f, 0.6f + (tier * 0.1f));
            this.wasteManagementEfficiency = Math.min(1.0f, 0.5f + (tier * 0.1f));
            this.foodProductionRate = tier * 5;
            this.atmosphericQuality = Math.min(1.0f, 0.7f + (tier * 0.1f));
            this.hasBackupSystems = tier >= 2;
            this.hasRadiationFiltering = tier >= 2;
            this.hasEmergencyMode = tier >= 3;
            this.emergencyModeActive = false;
            this.broken = false;
        }
        
        @Override
        public int getMaxCrewCapacity() {
            return maxCrewCapacity;
        }
        
        @Override
        public int getOxygenGenerationRate() {
            return broken ? 0 : (emergencyModeActive ? oxygenGenerationRate / 2 : oxygenGenerationRate);
        }
        
        @Override
        public float getWaterRecyclingEfficiency() {
            return broken ? 0 : (emergencyModeActive ? waterRecyclingEfficiency / 2 : waterRecyclingEfficiency);
        }
        
        @Override
        public int getFoodProductionRate() {
            return broken ? 0 : (emergencyModeActive ? foodProductionRate / 2 : foodProductionRate);
        }
        
        @Override
        public float getWasteManagementEfficiency() {
            return broken ? 0 : (emergencyModeActive ? wasteManagementEfficiency / 2 : wasteManagementEfficiency);
        }
        
        @Override
        public float getAtmosphericQuality() {
            return broken ? 0.1f : (emergencyModeActive ? atmosphericQuality / 2 : atmosphericQuality);
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
            if (hasEmergencyMode) {
                this.emergencyModeActive = active;
            }
        }
        
        @Override
        public boolean isEmergencyModeActive() {
            return emergencyModeActive;
        }
        
        @Override
        public boolean isBroken() {
            return broken;
        }
        
        public LifeSupportType getLifeSupportType() {
            return lifeSupportType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("MaxCrewCapacity", maxCrewCapacity);
            tag.putInt("OxygenGenerationRate", oxygenGenerationRate);
            tag.putFloat("WaterRecyclingEfficiency", waterRecyclingEfficiency);
            tag.putInt("FoodProductionRate", foodProductionRate);
            tag.putFloat("WasteManagementEfficiency", wasteManagementEfficiency);
            tag.putFloat("AtmosphericQuality", atmosphericQuality);
            tag.putString("LifeSupportType", lifeSupportType.name());
            tag.putBoolean("HasBackupSystems", hasBackupSystems);
            tag.putBoolean("HasRadiationFiltering", hasRadiationFiltering);
            tag.putBoolean("HasEmergencyMode", hasEmergencyMode);
            tag.putBoolean("EmergencyModeActive", emergencyModeActive);
            tag.putBoolean("Broken", broken);
        }
    }
    
    /**
     * Abstract base class for rocket components.
     */
    private static abstract class AbstractRocketComponent implements IRocketComponent {
        private final ResourceLocation id;
        private final RocketComponentType type;
        private final int tier;
        private final int mass;
        private final String name;
        private final String description;
        private final int maxDurability;
        private int currentDurability;
        
        public AbstractRocketComponent(ResourceLocation id, RocketComponentType type, int tier, int mass) {
            this.id = id;
            this.type = type;
            this.tier = tier;
            this.mass = mass;
            this.name = "Default " + type.name() + " (Tier " + tier + ")";
            this.description = "Standard " + type.name().toLowerCase() + " component.";
            this.maxDurability = 500 * tier;
            this.currentDurability = this.maxDurability;
        }
        
        @Override
        public ResourceLocation getId() {
            return id;
        }
        
        @Override
        public RocketComponentType getType() {
            return type;
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
        public void save(CompoundTag tag) {
            tag.putString("ID", id.toString());
            tag.putString("Type", type.name());
            tag.putString("Name", name);
            tag.putString("Description", description);
            tag.putInt("Tier", tier);
            tag.putInt("Mass", mass);
            tag.putInt("MaxDurability", maxDurability);
            tag.putInt("CurrentDurability", currentDurability);
        }
    }
}