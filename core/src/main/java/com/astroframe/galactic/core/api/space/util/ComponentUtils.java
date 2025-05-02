package com.astroframe.galactic.core.api.space.util;

import com.astroframe.galactic.core.api.space.component.*;
import com.astroframe.galactic.core.api.space.component.properties.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import java.util.List;
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
            tier = tag.getInt("Tier").orElse(1);
        }
        
        int mass = 100;
        if (tag.contains("Mass")) {
            mass = tag.getInt("Mass").orElse(100);
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
            thrust = tag.getInt("Thrust").orElse(thrust);
        }
        
        float efficiency = 0.5f * tier;
        if (tag.contains("Efficiency")) {
            efficiency = tag.getFloat("Efficiency").orElse(efficiency);
        }
        
        boolean atmosphereCapable = false;
        if (tag.contains("AtmosphereCapable")) {
            atmosphereCapable = tag.getBoolean("AtmosphereCapable").orElse(false);
        }
        
        boolean spaceCapable = true;
        if (tag.contains("SpaceCapable")) {
            spaceCapable = tag.getBoolean("SpaceCapable").orElse(true);
        }
        
        EngineType engineType = EngineType.CHEMICAL;
        if (tag.contains("EngineType")) {
            String engineTypeStr = tag.getString("EngineType").orElse("");
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
        int capacity = 5000 * tier;
        if (tag.contains("Capacity")) {
            capacity = tag.getInt("Capacity").orElse(capacity);
        }
        
        return new DefaultFuelTank(id, tier, mass, capacity);
    }
    
    /**
     * Creates a default command module component.
     */
    private static ICommandModule createDefaultCommandModule(ResourceLocation id, int tier, int mass, CompoundTag tag) {
        int crewCapacity = tier;
        if (tag.contains("CrewCapacity")) {
            crewCapacity = tag.getInt("CrewCapacity").orElse(crewCapacity);
        }
        
        int computerLevel = tier;
        if (tag.contains("ComputerLevel")) {
            computerLevel = tag.getInt("ComputerLevel").orElse(computerLevel);
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
        
        ShieldType shieldType = ShieldType.STANDARD;
        if (tag.contains("ShieldType")) {
            String shieldTypeStr = tag.getString("ShieldType").orElse("");
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
            String typeStr = tag.getString("Type").orElse("");
            if (!typeStr.isEmpty()) {
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
            CompoundTag componentTag = listTag.getCompound(i);
            
            String idStr = componentTag.getString("ID").orElse("");
            if (!idStr.isEmpty()) {
                ResourceLocation id = ResourceLocation.parse(idStr);
                IRocketComponent component = createComponentFromTag(id, componentTag);
                if (component != null) {
                    components.add(component);
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
        private final float efficiency;
        private final boolean atmosphereCapable;
        private final boolean spaceCapable;
        private final EngineType engineType;
        
        public DefaultRocketEngine(ResourceLocation id, int tier, int mass, int thrust, float efficiency, 
                                 boolean atmosphereCapable, boolean spaceCapable, EngineType engineType) {
            super(id, RocketComponentType.ENGINE, tier, mass);
            this.thrust = thrust;
            this.efficiency = efficiency;
            this.atmosphereCapable = atmosphereCapable;
            this.spaceCapable = spaceCapable;
            this.engineType = engineType;
        }
        
        @Override
        public int getThrust() {
            return thrust;
        }
        
        @Override
        public float getEfficiency() {
            return efficiency;
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
        }
    }
    
    /**
     * Simple implementation of IFuelTank.
     */
    private static class DefaultFuelTank extends AbstractRocketComponent implements IFuelTank {
        private final int capacity;
        
        public DefaultFuelTank(ResourceLocation id, int tier, int mass, int capacity) {
            super(id, RocketComponentType.FUEL_TANK, tier, mass);
            this.capacity = capacity;
        }
        
        @Override
        public int getMaxFuelCapacity() {
            return capacity;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("Capacity", capacity);
        }
    }
    
    /**
     * Simple implementation of ICommandModule.
     */
    private static class DefaultCommandModule extends AbstractRocketComponent implements ICommandModule {
        private final int crewCapacity;
        private final int computerLevel;
        
        public DefaultCommandModule(ResourceLocation id, int tier, int mass, int crewCapacity, int computerLevel) {
            super(id, RocketComponentType.COCKPIT, tier, mass);
            this.crewCapacity = crewCapacity;
            this.computerLevel = computerLevel;
        }
        
        @Override
        public int getCrewCapacity() {
            return crewCapacity;
        }
        
        @Override
        public int getComputerLevel() {
            return computerLevel;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("CrewCapacity", crewCapacity);
            tag.putInt("ComputerLevel", computerLevel);
        }
    }
    
    /**
     * Simple implementation of ICargoBay.
     */
    private static class DefaultCargoBay extends AbstractRocketComponent implements ICargoBay {
        private final int storageCapacity;
        
        public DefaultCargoBay(ResourceLocation id, int tier, int mass, int storageCapacity) {
            super(id, RocketComponentType.STORAGE, tier, mass);
            this.storageCapacity = storageCapacity;
        }
        
        @Override
        public int getStorageCapacity() {
            return storageCapacity;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("StorageCapacity", storageCapacity);
        }
    }
    
    /**
     * Simple implementation of IPassengerCompartment.
     */
    private static class DefaultPassengerCompartment extends AbstractRocketComponent implements IPassengerCompartment {
        private final int passengerCapacity;
        private final CompartmentType compartmentType;
        
        public DefaultPassengerCompartment(ResourceLocation id, int tier, int mass, int passengerCapacity, CompartmentType compartmentType) {
            super(id, RocketComponentType.PASSENGER_COMPARTMENT, tier, mass);
            this.passengerCapacity = passengerCapacity;
            this.compartmentType = compartmentType;
        }
        
        @Override
        public int getPassengerCapacity() {
            return passengerCapacity;
        }
        
        @Override
        public CompartmentType getCompartmentType() {
            return compartmentType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("PassengerCapacity", passengerCapacity);
            tag.putString("CompartmentType", compartmentType.name());
        }
    }
    
    /**
     * Simple implementation of IShield.
     */
    private static class DefaultShield extends AbstractRocketComponent implements IShield {
        private final int shieldStrength;
        private final int impactResistance;
        private final ShieldType shieldType;
        
        public DefaultShield(ResourceLocation id, int tier, int mass, int shieldStrength, int impactResistance, ShieldType shieldType) {
            super(id, RocketComponentType.SHIELDING, tier, mass);
            this.shieldStrength = shieldStrength;
            this.impactResistance = impactResistance;
            this.shieldType = shieldType;
        }
        
        @Override
        public int getMaxShieldStrength() {
            return shieldStrength;
        }
        
        @Override
        public int getImpactResistance() {
            return impactResistance;
        }
        
        @Override
        public ShieldType getShieldType() {
            return shieldType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("ShieldStrength", shieldStrength);
            tag.putInt("ImpactResistance", impactResistance);
            tag.putString("ShieldType", shieldType.name());
        }
    }
    
    /**
     * Simple implementation of ILifeSupport.
     */
    private static class DefaultLifeSupport extends AbstractRocketComponent implements ILifeSupport {
        private final int maxCrewCapacity;
        private final int oxygenEfficiency;
        private final LifeSupportType lifeSupportType;
        
        public DefaultLifeSupport(ResourceLocation id, int tier, int mass, int maxCrewCapacity, int oxygenEfficiency, LifeSupportType lifeSupportType) {
            super(id, RocketComponentType.LIFE_SUPPORT, tier, mass);
            this.maxCrewCapacity = maxCrewCapacity;
            this.oxygenEfficiency = oxygenEfficiency;
            this.lifeSupportType = lifeSupportType;
        }
        
        @Override
        public int getMaxCrewCapacity() {
            return maxCrewCapacity;
        }
        
        @Override
        public int getOxygenEfficiency() {
            return oxygenEfficiency;
        }
        
        @Override
        public LifeSupportType getLifeSupportType() {
            return lifeSupportType;
        }
        
        @Override
        public void save(CompoundTag tag) {
            super.save(tag);
            tag.putInt("MaxCrewCapacity", maxCrewCapacity);
            tag.putInt("OxygenEfficiency", oxygenEfficiency);
            tag.putString("LifeSupportType", lifeSupportType.name());
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
        
        public AbstractRocketComponent(ResourceLocation id, RocketComponentType type, int tier, int mass) {
            this.id = id;
            this.type = type;
            this.tier = tier;
            this.mass = mass;
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
        public int getTier() {
            return tier;
        }
        
        @Override
        public int getMass() {
            return mass;
        }
        
        @Override
        public void save(CompoundTag tag) {
            tag.putString("ID", id.toString());
            tag.putString("Type", type.name());
            tag.putInt("Tier", tier);
            tag.putInt("Mass", mass);
        }
    }
}