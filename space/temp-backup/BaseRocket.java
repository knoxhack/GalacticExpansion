package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Base implementation of a rocket.
 */
public class BaseRocket implements IRocket {
    private final int tier;
    private final int fuelCapacity;
    private int currentFuel;
    private final int payloadCapacity;
    private final int passengerCapacity;
    private final List<Player> passengers;
    private final Map<Integer, ItemStack> cargo;
    private RocketStatus status;
    private float health;
    
    /**
     * Creates a new rocket.
     * @param tier The rocket tier
     * @param fuelCapacity The maximum fuel capacity
     * @param payloadCapacity The maximum payload capacity
     * @param passengerCapacity The maximum passenger capacity
     */
    public BaseRocket(int tier, int fuelCapacity, int payloadCapacity, int passengerCapacity) {
        this.tier = tier;
        this.fuelCapacity = fuelCapacity;
        this.currentFuel = 0;
        this.payloadCapacity = payloadCapacity;
        this.passengerCapacity = passengerCapacity;
        this.passengers = new ArrayList<>();
        this.cargo = new HashMap<>();
        this.status = RocketStatus.BUILDING;
        this.health = 100.0f;
    }

    @Override
    public int getTier() {
        return tier;
    }

    @Override
    public int getFuelCapacity() {
        return fuelCapacity;
    }

    @Override
    public int getFuelLevel() {
        return currentFuel;
    }
    
    @Override
    public void setFuelLevel(int fuel) {
        this.currentFuel = Math.min(fuel, fuelCapacity);
    }
    
    @Override
    public boolean hasComponent(com.astroframe.galactic.core.api.space.component.RocketComponentType type) {
        // Simple implementation, in a modular rocket this would check the components list
        return true;
    }

    @Override
    public int addFuel(int amount) {
        int spaceLeft = fuelCapacity - currentFuel;
        int amountToAdd = Math.min(amount, spaceLeft);
        currentFuel += amountToAdd;
        return amountToAdd;
    }

    @Override
    public int getPayloadCapacity() {
        return payloadCapacity;
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
    public Map<Integer, ItemStack> getCargo() {
        return new HashMap<>(cargo);
    }

    @Override
    public ItemStack addCargo(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack remaining = stack.copy();
        
        for (int i = 0; i < payloadCapacity; i++) {
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
        return tier >= body.getRocketTierRequired() && currentFuel >= body.getDistanceFromHome() * 10;
    }

    @Override
    public boolean launch(ICelestialBody destination) {
        if (canReach(destination) && status == RocketStatus.READY_FOR_LAUNCH) {
            status = RocketStatus.LAUNCHING;
            // In a real implementation, this would start a launch sequence
            currentFuel -= destination.getDistanceFromHome() * 10;
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
        health = Math.max(0, health - amount);
        if (health == 0 && status != RocketStatus.CRASHED) {
            status = RocketStatus.CRASHED;
        }
    }

    @Override
    public void repair(float amount) {
        health = Math.min(100, health + amount);
    }
    
    @Override
    public List<IRocketComponent> getAllComponents() {
        // Basic implementation - in a real scenario, this would return the actual components
        // For now, return an empty list to satisfy the interface
        return Collections.emptyList();
    }
}