package com.astroframe.galactic.core.api.space;

import com.astroframe.galactic.core.api.space.component.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.Comparator;
import java.util.UUID;

/**
 * Implementation of a modular rocket composed of individual components.
 */
public class ModularRocket implements IRocket {
    
    // Using IRocket.RocketStatus enum
    
    private final ResourceLocation id;
    private final ICommandModule commandModule;
    private final List<IRocketEngine> engines;
    private final List<IFuelTank> fuelTanks;
    private final List<ICargoBay> cargoBays;
    private final List<IPassengerCompartment> passengerCompartments;
    private final List<IShield> shields;
    private final List<ILifeSupport> lifeSupports;
    
    private final List<Player> passengers;
    private final Map<Integer, ItemStack> cargo;
    private IRocket.RocketStatus status;
    private float health;
    private int currentFuel;
    
    /**
     * Creates a new modular rocket.
     * @param builder The builder to use
     */
    private ModularRocket(Builder builder) {
        this.id = builder.id;
        this.commandModule = builder.commandModule;
        this.engines = new ArrayList<>(builder.engines);
        this.fuelTanks = new ArrayList<>(builder.fuelTanks);
        this.cargoBays = new ArrayList<>(builder.cargoBays);
        this.passengerCompartments = new ArrayList<>(builder.passengerCompartments);
        this.shields = new ArrayList<>(builder.shields);
        this.lifeSupports = new ArrayList<>(builder.lifeSupports);
        
        this.passengers = new ArrayList<>();
        this.cargo = new HashMap<>();
        this.status = IRocket.RocketStatus.BUILDING;
        this.health = 100.0f;
        this.currentFuel = 0;
    }
    
    /**
     * Creates a new empty modular rocket.
     * This constructor is used when creating rockets programmatically.
     * Components need to be added manually after construction.
     */
    public ModularRocket() {
        this.id = ResourceLocation.parse("galactic:rocket_" + UUID.randomUUID().toString().substring(0, 8));
        this.commandModule = null;  // Will need to be set
        this.engines = new ArrayList<>();
        this.fuelTanks = new ArrayList<>();
        this.cargoBays = new ArrayList<>();
        this.passengerCompartments = new ArrayList<>();
        this.shields = new ArrayList<>();
        this.lifeSupports = new ArrayList<>();
        
        this.passengers = new ArrayList<>();
        this.cargo = new HashMap<>();
        this.status = IRocket.RocketStatus.BUILDING;
        this.health = 100.0f;
        this.currentFuel = 0;
    }
    
    @Override
    public int getTier() {
        // Tier is determined by the highest tier component
        int commandModuleTier = commandModule != null ? commandModule.getTier() : 1;
        int engineTier = engines.stream().map(IRocketComponent::getTier).max(Integer::compare).orElse(1);
        
        return Math.min(3, Math.max(commandModuleTier, engineTier));
    }
    
    @Override
    public int getFuelCapacity() {
        return fuelTanks.stream().mapToInt(IFuelTank::getMaxFuelCapacity).sum();
    }
    
    @Override
    public int getFuelLevel() {
        return currentFuel;
    }
    
    @Override
    public int addFuel(int amount) {
        int spaceLeft = getFuelCapacity() - currentFuel;
        int amountToAdd = Math.min(amount, spaceLeft);
        currentFuel += amountToAdd;
        return amountToAdd;
    }
    
    @Override
    public int getPayloadCapacity() {
        return cargoBays.stream().mapToInt(ICargoBay::getStorageCapacity).sum();
    }
    
    @Override
    public int getPassengerCapacity() {
        int compartmentCapacity = passengerCompartments.stream().mapToInt(IPassengerCompartment::getPassengerCapacity).sum();
        int commandModuleCapacity = commandModule != null ? commandModule.getCrewCapacity() : 0;
        
        return compartmentCapacity + commandModuleCapacity; // Command modules can hold some crew directly
    }
    
    @Override
    public List<Player> getPassengers() {
        return new ArrayList<>(passengers);
    }
    
    @Override
    public boolean addPassenger(Player player) {
        if (passengers.size() < getPassengerCapacity() && !passengers.contains(player)) {
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
    public Map<Integer, ItemStack> getCargo() {
        return new HashMap<>(cargo);
    }
    
    @Override
    public ItemStack addCargo(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack remaining = stack.copy();
        
        for (int i = 0; i < getPayloadCapacity(); i++) {
            if (!cargo.containsKey(i)) {
                cargo.put(i, remaining);
                return ItemStack.EMPTY;
            } else if (cargo.get(i).isEmpty()) {
                cargo.put(i, remaining);
                return ItemStack.EMPTY;
            } else if (cargo.get(i).getItem() == remaining.getItem() && cargo.get(i).getCount() < cargo.get(i).getMaxStackSize()) {
                int canAdd = cargo.get(i).getMaxStackSize() - cargo.get(i).getCount();
                if (canAdd >= remaining.getCount()) {
                    cargo.get(i).grow(remaining.getCount());
                    return ItemStack.EMPTY;
                } else {
                    cargo.get(i).grow(canAdd);
                    remaining.shrink(canAdd);
                }
            }
        }
        
        return remaining;
    }
    
    @Override
    public boolean canReach(ICelestialBody body) {
        // Check tier requirement
        if (getTier() < body.getRocketTierRequired()) {
            return false;
        }
        
        // Check fuel requirement
        int fuelRequired = calculateFuelRequired(body);
        if (currentFuel < fuelRequired) {
            return false;
        }
        
        // Check engine compatibility with destination
        boolean hasCompatibleEngine = false;
        if (body.getAtmosphereDensity() > 0) {
            // Need an engine that works in atmosphere
            hasCompatibleEngine = engines.stream().anyMatch(IRocketEngine::canOperateInAtmosphere);
        } else {
            // Need an engine that works in space
            hasCompatibleEngine = engines.stream().anyMatch(IRocketEngine::canOperateInSpace);
        }
        
        return hasCompatibleEngine;
    }
    
    /**
     * Calculates the fuel required to reach a celestial body.
     * @param body The destination
     * @return The amount of fuel required
     */
    private int calculateFuelRequired(ICelestialBody body) {
        // Base fuel requirement based on distance
        int baseFuel = body.getDistanceFromHome() * 10;
        
        // Adjust for engine efficiency
        float avgEfficiency = (float) engines.stream()
                .mapToDouble(IRocketEngine::getEfficiency)
                .average()
                .orElse(1.0);
        
        // Adjust for total rocket mass
        int totalMass = getTotalMass();
        
        // Final calculation
        return Math.round(baseFuel * (totalMass / 100.0f) / avgEfficiency);
    }
    
    /**
     * Calculates the total mass of the rocket.
     * @return The total mass
     */
    private int getTotalMass() {
        int commandModuleMass = commandModule != null ? commandModule.getMass() : 0;
        
        int componentMass = commandModuleMass +
                engines.stream().mapToInt(IRocketComponent::getMass).sum() +
                fuelTanks.stream().mapToInt(IRocketComponent::getMass).sum() +
                cargoBays.stream().mapToInt(IRocketComponent::getMass).sum() +
                passengerCompartments.stream().mapToInt(IRocketComponent::getMass).sum() +
                shields.stream().mapToInt(IRocketComponent::getMass).sum() +
                lifeSupports.stream().mapToInt(IRocketComponent::getMass).sum();
        
        // Add passenger and cargo mass
        int passengerMass = passengers.size() * 80; // Each passenger weighs 80 units
        
        // Add current fuel mass
        int fuelMass = currentFuel / 10;
        
        return componentMass + passengerMass + fuelMass;
    }
    
    @Override
    public boolean launch(ICelestialBody destination) {
        if (canReach(destination) && status == IRocket.RocketStatus.READY_FOR_LAUNCH) {
            status = IRocket.RocketStatus.LAUNCHING;
            
            // Consume fuel
            int fuelRequired = calculateFuelRequired(destination);
            currentFuel -= fuelRequired;
            
            // In a real implementation, this would start a launch sequence
            return true;
        }
        return false;
    }
    
    @Override
    public IRocket.RocketStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the status of this rocket.
     * @param status The new status
     */
    @Override
    public void setStatus(IRocket.RocketStatus status) {
        this.status = status;
    }
    
    @Override
    public float getHealth() {
        return health;
    }
    
    @Override
    public void damage(float amount) {
        // Apply shield protection if available
        if (!shields.isEmpty()) {
            float totalShieldCoverage = shields.stream()
                    .map(shield -> shield.getMaxShieldStrength() / 100f) // Shield strength as coverage percentage
                    .max(Float::compare)
                    .orElse(0f);
            
            float totalImpactProtection = shields.stream()
                    .map(IShield::getImpactResistance)
                    .max(Integer::compare)
                    .orElse(0) * 10f; // Convert 1-10 scale to percentage
            
            // Reduce damage based on shields
            amount *= (1 - (totalShieldCoverage * (totalImpactProtection / 100)));
        }
        
        health = Math.max(0, health - amount);
        if (health == 0 && status != IRocket.RocketStatus.CRASHED) {
            status = IRocket.RocketStatus.CRASHED;
        }
    }
    
    @Override
    public void repair(float amount) {
        health = Math.min(100, health + amount);
    }
    
    /**
     * Gets all components of this rocket.
     * @return A list of all components
     */
    public List<IRocketComponent> getAllComponents() {
        List<IRocketComponent> components = new ArrayList<>();
        if (commandModule != null) {
            components.add(commandModule);
        }
        components.addAll(engines);
        components.addAll(fuelTanks);
        components.addAll(cargoBays);
        components.addAll(passengerCompartments);
        components.addAll(shields);
        components.addAll(lifeSupports);
        return components;
    }
    
    /**
     * Gets the command module of this rocket.
     * @return The command module
     */
    public ICommandModule getCommandModule() {
        return commandModule;
    }
    
    /**
     * Gets the engines of this rocket.
     * @return The engines
     */
    public List<IRocketEngine> getEngines() {
        return Collections.unmodifiableList(engines);
    }
    
    /**
     * Gets the primary engine of this rocket.
     * Returns the most powerful engine if multiple are available.
     * @return The primary engine, or null if no engines are installed
     */
    public IRocketEngine getEngine() {
        if (engines.isEmpty()) {
            return null;
        }
        
        // Return the most powerful engine (highest tier)
        return engines.stream()
                .max(Comparator.comparingInt(IRocketEngine::getTier))
                .orElse(engines.get(0));
    }
    
    /**
     * Gets the fuel tanks of this rocket.
     * @return The fuel tanks
     */
    public List<IFuelTank> getFuelTanks() {
        return Collections.unmodifiableList(fuelTanks);
    }
    
    /**
     * Gets the cargo bays of this rocket.
     * @return The cargo bays
     */
    public List<ICargoBay> getCargoBays() {
        return Collections.unmodifiableList(cargoBays);
    }
    
    /**
     * Gets the passenger compartments of this rocket.
     * @return The passenger compartments
     */
    public List<IPassengerCompartment> getPassengerCompartments() {
        return Collections.unmodifiableList(passengerCompartments);
    }
    
    /**
     * Gets the shields of this rocket.
     * @return The shields
     */
    public List<IShield> getShields() {
        return Collections.unmodifiableList(shields);
    }
    
    /**
     * Gets the life support systems of this rocket.
     * @return The life support systems
     */
    public List<ILifeSupport> getLifeSupports() {
        return Collections.unmodifiableList(lifeSupports);
    }
    
    /**
     * Gets the ID of this rocket.
     * @return The rocket ID
     */
    public ResourceLocation getId() {
        return id;
    }
    
    /**
     * Sets the fuel level of this rocket.
     * @param fuel The new fuel level
     */
    public void setFuelLevel(int fuel) {
        this.currentFuel = Math.min(fuel, getFuelCapacity());
    }
    
    /**
     * Saves this rocket to an NBT tag.
     * 
     * @param tag The tag to save to
     */
    public void save(CompoundTag tag) {
        // Save basic properties
        tag.putString("ID", id.toString());
        tag.putString("Status", status.name());
        tag.putFloat("Health", health);
        tag.putInt("Fuel", currentFuel);
        
        // Save components as a list
        ListTag componentsList = new ListTag();
        
        // Save command module if present
        if (commandModule != null) {
            CompoundTag componentTag = new CompoundTag();
            commandModule.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save engines
        for (IRocketEngine engine : engines) {
            CompoundTag componentTag = new CompoundTag();
            engine.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save fuel tanks
        for (IFuelTank fuelTank : fuelTanks) {
            CompoundTag componentTag = new CompoundTag();
            fuelTank.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save cargo bays
        for (ICargoBay cargoBay : cargoBays) {
            CompoundTag componentTag = new CompoundTag();
            cargoBay.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save passenger compartments
        for (IPassengerCompartment compartment : passengerCompartments) {
            CompoundTag componentTag = new CompoundTag();
            compartment.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save shields
        for (IShield shield : shields) {
            CompoundTag componentTag = new CompoundTag();
            shield.save(componentTag);
            componentsList.add(componentTag);
        }
        
        // Save life support systems
        for (ILifeSupport lifeSupport : lifeSupports) {
            CompoundTag componentTag = new CompoundTag();
            lifeSupport.save(componentTag);
            componentsList.add(componentTag);
        }
        
        tag.put("Components", componentsList);
        
        // Save cargo
        CompoundTag cargoTag = new CompoundTag();
        cargoTag.putInt("Count", cargo.size());
        
        int i = 0;
        for (Map.Entry<Integer, ItemStack> entry : cargo.entrySet()) {
            CompoundTag itemTag = new CompoundTag();
            itemTag.putInt("Slot", entry.getKey());
            ItemStack stack = entry.getValue();
            // In NeoForge 1.21.5, handle ItemStack serialization directly
            CompoundTag itemData = new CompoundTag();
            // Just store the item registry ID and count for now
            itemData.putString("id", stack.getItem().toString());
            itemData.putInt("count", stack.getCount());
            itemTag.put("Item", itemData);
            cargoTag.put("Item" + i, itemTag);
            i++;
        }
        
        tag.put("Cargo", cargoTag);
    }
    
    /**
     * Clears all components from this rocket.
     * Used primarily for reassembly in the rocket assembly table.
     */
    public void clearComponents() {
        engines.clear();
        fuelTanks.clear();
        cargoBays.clear();
        passengerCompartments.clear();
        shields.clear();
        lifeSupports.clear();
        // Note: we don't set commandModule to null here to preserve the rocket's identity
    }
    
    /**
     * Adds a component to this rocket.
     * 
     * @param component The component to add
     * @return True if the component was added successfully
     */
    public boolean addComponent(IRocketComponent component) {
        if (component == null) {
            return false;
        }
        
        // Add component based on its type
        RocketComponentType type = component.getType();
        
        switch (type) {
            case COCKPIT:
                if (component instanceof ICommandModule) {
                    // Set the command module through reflection as the field is final
                    try {
                        java.lang.reflect.Field field = ModularRocket.class.getDeclaredField("commandModule");
                        field.setAccessible(true);
                        field.set(this, component);
                        return true;
                    } catch (Exception e) {
                        // Fallback to normal behavior if reflection fails
                        return false;
                    }
                }
                break;
            case ENGINE:
                if (component instanceof IRocketEngine) {
                    engines.add((IRocketEngine) component);
                    return true;
                }
                break;
            case FUEL_TANK:
                if (component instanceof IFuelTank) {
                    fuelTanks.add((IFuelTank) component);
                    return true;
                }
                break;
            case STORAGE:
                if (component instanceof ICargoBay) {
                    cargoBays.add((ICargoBay) component);
                    return true;
                }
                break;
            case PASSENGER_COMPARTMENT:
                if (component instanceof IPassengerCompartment) {
                    passengerCompartments.add((IPassengerCompartment) component);
                    return true;
                }
                break;
            case SHIELDING:
                if (component instanceof IShield) {
                    shields.add((IShield) component);
                    return true;
                }
                break;
            case LIFE_SUPPORT:
                if (component instanceof ILifeSupport) {
                    lifeSupports.add((ILifeSupport) component);
                    return true;
                }
                break;
            default:
                return false;
        }
        
        return false;
    }
    
    /**
     * Saves rocket data to an NBT tag.
     * @param tag The tag to save to
     */
    @Override
    public void saveToTag(CompoundTag tag) {
        // Save basic properties
        tag.putString("id", id.toString());
        tag.putInt("tier", getTier());
        tag.putInt("fuel", currentFuel);
        tag.putFloat("health", health);
        tag.putInt("status", status.ordinal());
        
        // Save component counts
        tag.putInt("engineCount", engines.size());
        tag.putInt("fuelTankCount", fuelTanks.size());
        tag.putInt("cargoBayCount", cargoBays.size());
        tag.putInt("passengerCompartmentCount", passengerCompartments.size());
        tag.putInt("shieldCount", shields.size());
        tag.putInt("lifeSupportCount", lifeSupports.size());
        
        // More detailed component info could be saved here in a real implementation
    }
    
    /**
     * Creates a rocket from an NBT tag.
     * This simplified implementation creates a default rocket for the given tier.
     * 
     * @param tag The tag to load from
     * @return The rocket, or null if invalid
     */
    public static ModularRocket fromTag(CompoundTag tag) {
        if (!tag.contains("tier") || !tag.contains("id")) {
            return null;
        }
        
        try {
            // Direct access to fields in NeoForge 1.21.5
            String idStr = "";
            if (tag.contains("id")) {
                idStr = tag.getString("id").orElse("");
            }
            
            if (idStr.isEmpty()) {
                return null;
            }
            
            ResourceLocation id = ResourceLocation.parse(idStr);
            
            // Get numeric values with direct tag access
            int tier = 1;
            if (tag.contains("tier")) {
                tier = tag.getInt("tier").orElse(1);
            }
            
            int fuel = 0;
            if (tag.contains("fuel")) {
                fuel = tag.getInt("fuel").orElse(0);
            }
            
            float health = 100f;
            if (tag.contains("health")) {
                health = tag.getFloat("health").orElse(100f);
            }
            
            IRocket.RocketStatus status = IRocket.RocketStatus.BUILDING;
            if (tag.contains("status")) {
                int statusValue = tag.getInt("status").orElse(0);
                if (statusValue >= 0 && statusValue < IRocket.RocketStatus.values().length) {
                    status = IRocket.RocketStatus.values()[statusValue];
                }
            }
            
            // Create a new rocket with default components for the tier
            ModularRocket rocket = createDefaultRocket(id, tier);
            if (rocket != null) {
                rocket.setFuelLevel(fuel);
                rocket.health = health;
                rocket.status = status;
            }
            
            return rocket;
        } catch (Exception e) {
            // Log error in real implementation
            return null;
        }
    }
    
    /**
     * Creates a default rocket for the given tier.
     * 
     * @param id The rocket ID
     * @param tier The tier level (1-3)
     * @return A new rocket with default components
     */
    private static ModularRocket createDefaultRocket(ResourceLocation id, int tier) {
        // We return a basic rocket shell that can be customized later
        // A real implementation would add more components based on tier
        ModularRocket.Builder builder = new ModularRocket.Builder(id);
        return builder.build();
    }
    
    /**
     * Checks if this rocket has a component of the specified type.
     * @param type The component type to check for
     * @return true if the rocket has the component
     */
    public boolean hasComponent(RocketComponentType type) {
        switch (type) {
            case COCKPIT:
                return commandModule != null;
            case ENGINE:
                return !engines.isEmpty();
            case FUEL_TANK:
                return !fuelTanks.isEmpty();
            case STORAGE:
                return !cargoBays.isEmpty();
            case PASSENGER_COMPARTMENT:
                return !passengerCompartments.isEmpty();
            case SHIELDING:
                return !shields.isEmpty();
            case LIFE_SUPPORT:
                return !lifeSupports.isEmpty();
            case STRUCTURE:
                // Structure is always present if the rocket is valid
                return true;
            case NAVIGATION:
                // Navigation is typically part of the command module
                return commandModule != null;
            case FUEL:
                // Fuel is present if fuel level is > 0
                return currentFuel > 0;
            default:
                return false;
        }
    }
    
    /**
     * Builder for creating modular rockets.
     */
    public static class Builder {
        private ResourceLocation id;
        private ICommandModule commandModule;
        private final List<IRocketEngine> engines = new ArrayList<>();
        private final List<IFuelTank> fuelTanks = new ArrayList<>();
        private final List<ICargoBay> cargoBays = new ArrayList<>();
        private final List<IPassengerCompartment> passengerCompartments = new ArrayList<>();
        private final List<IShield> shields = new ArrayList<>();
        private final List<ILifeSupport> lifeSupports = new ArrayList<>();
        
        /**
         * Creates a new rocket builder.
         * @param id The rocket ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        /**
         * Sets the command module.
         * @param commandModule The command module
         * @return This builder
         */
        public Builder commandModule(ICommandModule commandModule) {
            this.commandModule = commandModule;
            return this;
        }
        
        /**
         * Adds an engine.
         * @param engine The engine to add
         * @return This builder
         */
        public Builder addEngine(IRocketEngine engine) {
            engines.add(engine);
            return this;
        }
        
        /**
         * Adds a fuel tank.
         * @param fuelTank The fuel tank to add
         * @return This builder
         */
        public Builder addFuelTank(IFuelTank fuelTank) {
            fuelTanks.add(fuelTank);
            return this;
        }
        
        /**
         * Adds a cargo bay.
         * @param cargoBay The cargo bay to add
         * @return This builder
         */
        public Builder addCargoBay(ICargoBay cargoBay) {
            cargoBays.add(cargoBay);
            return this;
        }
        
        /**
         * Adds a passenger compartment.
         * @param passengerCompartment The passenger compartment to add
         * @return This builder
         */
        public Builder addPassengerCompartment(IPassengerCompartment passengerCompartment) {
            passengerCompartments.add(passengerCompartment);
            return this;
        }
        
        /**
         * Adds a shield.
         * @param shield The shield to add
         * @return This builder
         */
        public Builder addShield(IShield shield) {
            shields.add(shield);
            return this;
        }
        
        /**
         * Adds a life support system.
         * @param lifeSupport The life support system to add
         * @return This builder
         */
        public Builder addLifeSupport(ILifeSupport lifeSupport) {
            lifeSupports.add(lifeSupport);
            return this;
        }
        
        /**
         * Validates the rocket configuration.
         * @return True if the configuration is valid
         */
        public boolean isValid() {
            // Allow empty rockets for creation from tag
            // This handles the case where a rocket is loaded from NBT
            return true;
            
            // The following checks would be used for a fully implemented rocket system:
            /*
            // Must have a command module
            if (commandModule == null) {
                return false;
            }
            
            // Must have at least one engine
            if (engines.isEmpty()) {
                return false;
            }
            
            // Must have at least one fuel tank
            if (fuelTanks.isEmpty()) {
                return false;
            }
            
            // Check if has sufficient life support
            int totalPassengerCapacity = passengerCompartments.stream()
                    .mapToInt(IPassengerCompartment::getPassengerCapacity)
                    .sum();
                    
            if (commandModule != null) {
                totalPassengerCapacity += commandModule.getCrewCapacity(); // Command modules can hold crew
            }
            
            int totalLifeSupportCapacity = lifeSupports.stream()
                    .mapToInt(ILifeSupport::getMaxCrewCapacity)
                    .sum();
                    
            if (totalLifeSupportCapacity < totalPassengerCapacity && !lifeSupports.isEmpty()) {
                return false;
            }
            
            return true;
            */
        }
        
        /**
         * Builds a new modular rocket.
         * @return The new rocket
         * @throws IllegalStateException if the configuration is invalid
         */
        public ModularRocket build() {
            if (!isValid()) {
                throw new IllegalStateException("Invalid rocket configuration");
            }
            
            return new ModularRocket(this);
        }
    }
}