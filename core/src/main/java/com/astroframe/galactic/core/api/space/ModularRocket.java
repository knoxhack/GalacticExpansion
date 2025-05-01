package com.astroframe.galactic.core.api.space;

import com.astroframe.galactic.core.api.space.component.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.Comparator;

/**
 * Implementation of a modular rocket composed of individual components.
 */
public class ModularRocket implements IRocket {
    
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
    private RocketStatus status;
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
        this.status = RocketStatus.BUILDING;
        this.health = 100.0f;
        this.currentFuel = 0;
    }
    
    @Override
    public int getTier() {
        // Tier is determined by the highest tier component
        return Math.min(3, Math.max(
            commandModule.getTier(),
            engines.stream().map(IRocketComponent::getTier).max(Integer::compare).orElse(1)
        ));
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
        return passengerCompartments.stream().mapToInt(IPassengerCompartment::getPassengerCapacity).sum() 
             + commandModule.getCrewCapacity(); // Command modules can hold some crew directly
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
        int componentMass = commandModule.getMass() +
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
        if (canReach(destination) && status == RocketStatus.READY_FOR_LAUNCH) {
            status = RocketStatus.LAUNCHING;
            
            // Consume fuel
            int fuelRequired = calculateFuelRequired(destination);
            currentFuel -= fuelRequired;
            
            // In a real implementation, this would start a launch sequence
            return true;
        }
        return false;
    }
    
    @Override
    public RocketStatus getStatus() {
        return status;
    }
    
    /**
     * Sets the status of this rocket.
     * @param status The new status
     */
    public void setStatus(RocketStatus status) {
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
        if (health == 0 && status != RocketStatus.CRASHED) {
            status = RocketStatus.CRASHED;
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
        components.add(commandModule);
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
                    .sum()
                    + commandModule.getCrewCapacity(); // Command modules can hold crew
                    
            int totalLifeSupportCapacity = lifeSupports.stream()
                    .mapToInt(ILifeSupport::getMaxCrewCapacity)
                    .sum();
                    
            if (totalLifeSupportCapacity < totalPassengerCapacity && !lifeSupports.isEmpty()) {
                return false;
            }
            
            return true;
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