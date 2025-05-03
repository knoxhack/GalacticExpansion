package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IPassengerCompartment;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Implementation of a rocket passenger compartment component.
 */
public class PassengerCompartmentImpl implements IPassengerCompartment {
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
    
    /**
     * Creates a new passenger compartment.
     */
    public PassengerCompartmentImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int passengerCapacity,
            int comfortLevel,
            boolean hasLifeSupport,
            boolean hasGravitySimulation,
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
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Capacity: " + passengerCapacity + " passengers"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Comfort Level: " + comfortLevel + "/10"));
            tooltip.add(Component.literal("Life Support: " + (hasLifeSupport ? "Yes" : "No")));
            tooltip.add(Component.literal("Gravity Simulation: " + (hasGravitySimulation ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Shielding: " + (hasRadiationShielding ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for PassengerCompartmentImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Passenger Compartment";
        private String description = "A compartment for passengers.";
        private int tier = 1;
        private int mass = 300;
        private int maxDurability = 800;
        private int passengerCapacity = 4;
        private int comfortLevel = 5;
        private boolean hasLifeSupport = false;
        private boolean hasGravitySimulation = false;
        private boolean hasRadiationShielding = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        /**
         * Sets the name.
         * @param name The name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Sets the description.
         * @param description The description
         * @return This builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Sets the tier.
         * @param tier The tier level (1-3)
         * @return This builder
         */
        public Builder tier(int tier) {
            this.tier = Math.max(1, Math.min(3, tier));
            return this;
        }
        
        /**
         * Sets the mass.
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the max durability.
         * @param maxDurability The max durability
         * @return This builder
         */
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }
        
        /**
         * Sets the passenger capacity.
         * @param passengerCapacity The passenger capacity
         * @return This builder
         */
        public Builder passengerCapacity(int passengerCapacity) {
            this.passengerCapacity = passengerCapacity;
            return this;
        }
        
        /**
         * Sets the comfort level.
         * @param comfortLevel The comfort level (1-10)
         * @return This builder
         */
        public Builder comfortLevel(int comfortLevel) {
            this.comfortLevel = Math.max(1, Math.min(10, comfortLevel));
            return this;
        }
        
        /**
         * Sets whether this compartment has life support.
         * @param hasLifeSupport True if has life support
         * @return This builder
         */
        public Builder lifeSupport(boolean hasLifeSupport) {
            this.hasLifeSupport = hasLifeSupport;
            return this;
        }
        
        /**
         * Sets whether this compartment has gravity simulation.
         * @param hasGravitySimulation True if has gravity simulation
         * @return This builder
         */
        public Builder gravitySimulation(boolean hasGravitySimulation) {
            this.hasGravitySimulation = hasGravitySimulation;
            return this;
        }
        
        /**
         * Sets whether this compartment has radiation shielding.
         * @param hasRadiationShielding True if has radiation shielding
         * @return This builder
         */
        public Builder radiationShielding(boolean hasRadiationShielding) {
            this.hasRadiationShielding = hasRadiationShielding;
            return this;
        }
        
        /**
         * Builds the passenger compartment.
         * @return A new PassengerCompartmentImpl
         */
        public PassengerCompartmentImpl build() {
            return new PassengerCompartmentImpl(
                id, name, description, tier, mass, maxDurability,
                passengerCapacity, comfortLevel, hasLifeSupport,
                hasGravitySimulation, hasRadiationShielding
            );
        }
    }
}