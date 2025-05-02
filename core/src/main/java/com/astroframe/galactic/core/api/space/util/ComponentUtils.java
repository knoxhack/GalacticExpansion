package com.astroframe.galactic.core.api.space.util;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
         * @return The created component, or empty if creation failed
         */
        Optional<IRocketComponent> createFromTag(ResourceLocation id, CompoundTag tag);
        
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
                Optional<IRocketComponent> component = factory.createFromTag(id, tag);
                if (component.isPresent()) {
                    return component.get();
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
        int tier = 1;
        if (tag.contains("Tier")) {
            tier = tag.getInt("Tier");
        }
        
        int mass = 100;
        if (tag.contains("Mass")) {
            mass = tag.getInt("Mass");
        }
        
        // Create appropriate component based on type
        switch (type) {
            case ENGINE:
                return createDefaultEngine(id, tier, mass, tag);
            case FUEL_TANK:
                return createDefaultFuelTank(id, tier, mass, tag);
            case COCKPIT:
                return createDefaultCommandModule(id, tier, mass, tag);
            case STORAGE:
                return createDefaultCargoBay(id, tier, mass, tag);
            case PASSENGER_COMPARTMENT:
                return createDefaultPassengerCompartment(id, tier, mass, tag);
            case SHIELDING:
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
        int thrust = 1000 * tier;
        if (tag.contains("Thrust")) {
            thrust = tag.getInt("Thrust");
        }
        
        float efficiency = 0.5f * tier;
        if (tag.contains("Efficiency")) {
            efficiency = tag.getFloat("Efficiency");
        }
        
        boolean atmosphereCapable = false;
        if (tag.contains("AtmosphereCapable")) {
            atmosphereCapable = tag.getBoolean("AtmosphereCapable");
        }
        
        boolean spaceCapable = true;
        if (tag.contains("SpaceCapable")) {
            spaceCapable = tag.getBoolean("SpaceCapable");
        }
        
        EngineType engineType = EngineType.CHEMICAL;
        if (tag.contains("EngineType")) {
            String engineTypeStr = tag.getString("EngineType");
            if (engineTypeStr != null && !engineTypeStr.isEmpty()) {
                try {
                    engineType = EngineType.valueOf(engineTypeStr);
                } catch (IllegalArgumentException e) {
                    // Use default
                }
            }
        }
        
        return new DefaultRocketEngine(id, tier, mass, thrust, efficiency, atmosphereCapable, spaceCapable, engineType);
    }
    
    /**
     * Creates a default fuel tank component.
     */
    private static IFuelTank createDefaultFuelTank(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int capacity = 5000 * tier;
        if (tag.contains("Capacity")) {
            capacity = tag.getInt("Capacity");
        }
        
        return new DefaultFuelTank(id, tier, mass, capacity);
    }
    
    /**
     * Creates a default command module component.
     */
    private static ICommandModule createDefaultCommandModule(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int crewCapacity = tier;
        if (tag.contains("CrewCapacity")) {
            crewCapacity = tag.getInt("CrewCapacity");
        }
        
        int computerLevel = tier;
        if (tag.contains("ComputerLevel")) {
            computerLevel = tag.getInt("ComputerLevel");
        }
        
        return new DefaultCommandModule(id, tier, mass, crewCapacity, computerLevel);
    }
    
    /**
     * Creates a default cargo bay component.
     */
    private static ICargoBay createDefaultCargoBay(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int storageCapacity = 9 * tier;
        if (tag.contains("StorageCapacity")) {
            storageCapacity = tag.getInt("StorageCapacity").orElse(storageCapacity);
        }
        
        return new DefaultCargoBay(id, tier, mass, storageCapacity);
    }
    
    /**
     * Creates a default passenger compartment component.
     */
    private static IPassengerCompartment createDefaultPassengerCompartment(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int passengerCapacity = 2 * tier;
        if (tag.contains("PassengerCapacity")) {
            passengerCapacity = tag.getInt("PassengerCapacity").orElse(passengerCapacity);
        }
        
        CompartmentType compartmentType = CompartmentType.STANDARD;
        if (tag.contains("CompartmentType")) {
            String compartmentTypeStr = tag.getString("CompartmentType").orElse("");
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
        int shieldStrength = 50 * tier;
        if (tag.contains("ShieldStrength")) {
            shieldStrength = tag.getInt("ShieldStrength").orElse(shieldStrength);
        }
        
        int impactResistance = 2 * tier;
        if (tag.contains("ImpactResistance")) {
            impactResistance = tag.getInt("ImpactResistance").orElse(impactResistance);
        }
        
        ShieldType shieldType = ShieldType.BASIC;
        if (tag.contains("ShieldType")) {
            String shieldTypeStr = tag.getString("ShieldType");
            if (shieldTypeStr != null && !shieldTypeStr.isEmpty()) {
                try {
                    shieldType = ShieldType.valueOf(shieldTypeStr);
                } catch (IllegalArgumentException e) {
                    // Use default
                }
            }
        }
        
        return new DefaultShield(id, tier, mass, shieldStrength, impactResistance, shieldType);
    }
    
    /**
     * Creates a default life support component.
     */
    private static ILifeSupport createDefaultLifeSupport(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int maxCrewCapacity = 3 * tier;
        if (tag.contains("MaxCrewCapacity")) {
            maxCrewCapacity = tag.getInt("MaxCrewCapacity").orElse(maxCrewCapacity);
        }
        
        int oxygenEfficiency = tier;
        if (tag.contains("OxygenEfficiency")) {
            oxygenEfficiency = tag.getInt("OxygenEfficiency").orElse(oxygenEfficiency);
        }
        
        LifeSupportType lifeSupportType = LifeSupportType.STANDARD;
        if (tag.contains("LifeSupportType")) {
            String lifeSupportTypeStr = tag.getString("LifeSupportType").orElse("");
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
        if (tag.contains("Type")) {
            String typeStr = tag.getString("Type");
            if (typeStr != null && !typeStr.isEmpty()) {
                try {
                    return RocketComponentType.valueOf(typeStr);
                } catch (IllegalArgumentException e) {
                    return null;
                }
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
                
                // In NeoForge 1.21.5, getString no longer returns Optional
                if (componentTag.contains("ID")) {
                    String idStr = componentTag.getString("ID");
                    if (!idStr.isEmpty()) {
                        try {
                            // Use the constructor to avoid ResourceLocation.parse deprecation
                            ResourceLocation id = new ResourceLocation(idStr);
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
        private final int thrust;
        private final int fuelConsumptionRate;
        private final float efficiency;
        private final boolean atmosphereCapable;
        private final boolean spaceCapable;
        private final EngineType engineType;
        private final IRocketEngine.FuelType fuelType;
        
        public DefaultRocketEngine(ResourceLocation id, int tier, int mass, int thrust, float efficiency, 
                                 boolean atmosphereCapable, boolean spaceCapable, EngineType engineType) {
            super(id, RocketComponentType.ENGINE, tier, mass);
            this.thrust = thrust;
            this.efficiency = efficiency;
            this.atmosphereCapable = atmosphereCapable;
            this.spaceCapable = spaceCapable;
            this.engineType = engineType;
            this.fuelConsumptionRate = (int)(thrust / (efficiency * 10));
            
            // Determine fuel type based on engine type
            if (engineType == EngineType.PLASMA) {
                this.fuelType = IRocketEngine.FuelType.PLASMA;
            } else if (engineType == EngineType.ANTIMATTER) {
                this.fuelType = IRocketEngine.FuelType.ANTIMATTER;
            } else if (engineType == EngineType.ION) {
                this.fuelType = IRocketEngine.FuelType.ELECTRICAL;
            } else {
                this.fuelType = IRocketEngine.FuelType.CHEMICAL;
            }
        }
        
        @Override
        public int getThrust() {
            return thrust;
        }
        
        @Override
        public int getFuelConsumptionRate() {
            return fuelConsumptionRate;
        }
        
        @Override
        public float getEfficiency() {
            return efficiency;
        }
        
        @Override
        public IRocketEngine.FuelType getFuelType() {
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
        public EngineType getEngineType() {
            return engineType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("Thrust", thrust);
            tag.putFloat("Efficiency", efficiency);
            tag.putBoolean("AtmosphereCapable", atmosphereCapable);
            tag.putBoolean("SpaceCapable", spaceCapable);
            tag.putString("EngineType", engineType.name());
            tag.putString("FuelType", fuelType.name());
        }
    }
    
    /**
     * Simple implementation of IFuelTank.
     */
    private static class DefaultFuelTank extends AbstractRocketComponent implements IFuelTank {
        private final int capacity;
        private int currentFuel;
        private final IRocketEngine.FuelType fuelType;
        private final float leakResistance;
        private final float explosionResistance;
        
        public DefaultFuelTank(ResourceLocation id, int tier, int mass, int capacity) {
            super(id, RocketComponentType.FUEL_TANK, tier, mass);
            this.capacity = capacity;
            this.currentFuel = 0;
            this.fuelType = IRocketEngine.FuelType.CHEMICAL; // Default fuel type
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
        public IRocketEngine.FuelType getFuelType() {
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
        private final int storageCapacity;
        private final Map<Integer, ItemStack> contents;
        private final boolean hasVacuumSeal;
        private final boolean hasTemperatureRegulation;
        private final boolean hasRadiationShielding;
        
        public DefaultCargoBay(ResourceLocation id, int tier, int mass, int storageCapacity) {
            super(id, RocketComponentType.STORAGE, tier, mass);
            this.storageCapacity = storageCapacity;
            this.contents = new HashMap<>();
            this.hasVacuumSeal = tier >= 2;
            this.hasTemperatureRegulation = tier >= 2;
            this.hasRadiationShielding = tier >= 3;
        }
        
        @Override
        public int getStorageCapacity() {
            return storageCapacity;
        }
        
        @Override
        public Map<Integer, ItemStack> getContents() {
            return new HashMap<>(contents);
        }
        
        @Override
        public ItemStack addItem(ItemStack stack) {
            if (stack.isEmpty()) {
                return ItemStack.EMPTY;
            }
            
            ItemStack remaining = stack.copy();
            
            for (int i = 0; i < storageCapacity; i++) {
                if (!contents.containsKey(i)) {
                    contents.put(i, remaining);
                    return ItemStack.EMPTY;
                } else if (contents.get(i).isEmpty()) {
                    contents.put(i, remaining);
                    return ItemStack.EMPTY;
                }
            }
            
            return remaining;
        }
        
        @Override
        public ItemStack takeItem(int slotIndex, int amount) {
            if (!contents.containsKey(slotIndex) || contents.get(slotIndex).isEmpty()) {
                return ItemStack.EMPTY;
            }
            
            ItemStack currentStack = contents.get(slotIndex);
            int amountToTake = Math.min(amount, currentStack.getCount());
            
            ItemStack result = currentStack.copy();
            result.setCount(amountToTake);
            
            currentStack.shrink(amountToTake);
            if (currentStack.isEmpty()) {
                contents.remove(slotIndex);
            }
            
            return result;
        }
        
        @Override
        public boolean hasVacuumSeal() {
            return hasVacuumSeal;
        }
        
        @Override
        public boolean hasTemperatureRegulation() {
            return hasTemperatureRegulation;
        }
        
        @Override
        public boolean hasRadiationShielding() {
            return hasRadiationShielding;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("StorageCapacity", storageCapacity);
            tag.putBoolean("HasVacuumSeal", hasVacuumSeal);
            tag.putBoolean("HasTemperatureRegulation", hasTemperatureRegulation);
            tag.putBoolean("HasRadiationShielding", hasRadiationShielding);
            
            // Save contents
            CompoundTag contentsTag = new CompoundTag();
            contentsTag.putInt("Count", contents.size());
            
            int index = 0;
            for (Map.Entry<Integer, ItemStack> entry : contents.entrySet()) {
                CompoundTag slotTag = new CompoundTag();
                slotTag.putInt("Slot", entry.getKey());
                
                // In NeoForge 1.21, ItemStack doesn't have a direct save method that takes a CompoundTag
                // Instead, we'll just record the item's count and ID, which is enough for serialization
                if (!entry.getValue().isEmpty()) {
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putString("ID", entry.getValue().getItem().toString());
                    itemTag.putInt("Count", entry.getValue().getCount());
                    slotTag.put("Item", itemTag);
                }
                
                contentsTag.put("Slot" + index, slotTag);
                index++;
            }
            
            tag.put("Contents", contentsTag);
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
            super(id, RocketComponentType.SHIELDING, tier, mass);
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