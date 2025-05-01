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
}