package com.astroframe.galactic.core.api.space;

import net.minecraft.nbt.CompoundTag;

/**
 * Basic implementation of the IRocket interface.
 * Represents a modular rocket that can be customized with components.
 */
public class ModularRocket implements IRocket {
    
    private int tier = 1;
    private float fuel = 0.0f;
    private float fuelCapacity = 1000.0f;
    private float mass = 1000.0f;
    private float thrust = 5000.0f;
    
    /**
     * Creates a new modular rocket with default parameters.
     */
    public ModularRocket() {
        // Default constructor for NeoForge 1.21.5
        System.out.println("Creating modular rocket for NeoForge 1.21.5");
    }
    
    /**
     * Creates a new modular rocket with the specified parameters.
     *
     * @param tier The rocket tier
     * @param fuel The current fuel level
     * @param fuelCapacity The maximum fuel capacity
     * @param mass The rocket mass
     * @param thrust The rocket thrust
     */
    public ModularRocket(int tier, float fuel, float fuelCapacity, float mass, float thrust) {
        this.tier = tier;
        this.fuel = fuel;
        this.fuelCapacity = fuelCapacity;
        this.mass = mass;
        this.thrust = thrust;
    }
    
    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public float getFuel() {
        return fuel;
    }
    
    @Override
    public float getFuelCapacity() {
        return fuelCapacity;
    }
    
    @Override
    public float getMass() {
        return mass;
    }
    
    @Override
    public float getThrust() {
        return thrust;
    }
    
    @Override
    public boolean canReachOrbit() {
        // Simple calculation: Thrust must be at least twice the mass
        // and fuel must be at least 25% of capacity
        return thrust >= mass * 2.0f && fuel >= fuelCapacity * 0.25f;
    }
    
    @Override
    public float getMaxAltitude() {
        // Calculate maximum altitude based on rocket parameters
        // This is a simplified placeholder formula
        float thrustToMassRatio = thrust / mass;
        float fuelPercentage = fuel / fuelCapacity;
        
        return thrustToMassRatio * fuelPercentage * 100000.0f;
    }
    
    @Override
    public float getTimeToOrbit() {
        if (!canReachOrbit()) {
            return -1.0f; // Cannot reach orbit
        }
        
        // Simple calculation: Mass/Thrust ratio affects time to orbit
        return (mass / thrust) * 60.0f;
    }
    
    /**
     * Sets the rocket tier.
     *
     * @param tier The new tier
     */
    public void setTier(int tier) {
        this.tier = tier;
    }
    
    /**
     * Sets the current fuel amount.
     *
     * @param fuel The new fuel amount
     */
    public void setFuel(float fuel) {
        this.fuel = Math.min(fuel, fuelCapacity);
    }
    
    /**
     * Sets the fuel capacity.
     *
     * @param fuelCapacity The new fuel capacity
     */
    public void setFuelCapacity(float fuelCapacity) {
        this.fuelCapacity = fuelCapacity;
    }
    
    /**
     * Sets the rocket mass.
     *
     * @param mass The new mass
     */
    public void setMass(float mass) {
        this.mass = mass;
    }
    
    /**
     * Sets the rocket thrust.
     *
     * @param thrust The new thrust
     */
    public void setThrust(float thrust) {
        this.thrust = thrust;
    }
    
    /**
     * Saves the rocket data to a compound tag.
     *
     * @param tag The tag to save to
     * @return The updated tag
     */
    public CompoundTag save(CompoundTag tag) {
        if (tag == null) {
            tag = new CompoundTag();
        }
        
        tag.putInt("tier", tier);
        tag.putFloat("fuel", fuel);
        tag.putFloat("fuelCapacity", fuelCapacity);
        tag.putFloat("mass", mass);
        tag.putFloat("thrust", thrust);
        
        return tag;
    }
    
    /**
     * Loads the rocket data from a compound tag.
     *
     * @param tag The tag to load from
     */
    public void load(CompoundTag tag) {
        if (tag == null) {
            return;
        }
        
        if (tag.contains("tier")) {
            tier = tag.getInt("tier").orElse(1);
        }
        
        if (tag.contains("fuel")) {
            fuel = tag.getFloat("fuel").orElse(0.0f);
        }
        
        if (tag.contains("fuelCapacity")) {
            fuelCapacity = tag.getFloat("fuelCapacity").orElse(1000.0f);
        }
        
        if (tag.contains("mass")) {
            mass = tag.getFloat("mass").orElse(1000.0f);
        }
        
        if (tag.contains("thrust")) {
            thrust = tag.getFloat("thrust").orElse(5000.0f);
        }
    }
}