package com.astroframe.galactic.core.api.space;

import java.util.ArrayList;
import java.util.List;
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
    private final List<RocketComponent> components = new ArrayList<>();
    
    /**
     * Creates a new modular rocket with default parameters.
     */
    public ModularRocket() {
        // Default constructor for NeoForge 1.21.5
    }
    
    /**
     * Creates a modular rocket from an NBT tag.
     *
     * @param tag The tag to load from
     * @return The created rocket
     */
    public static ModularRocket fromTag(CompoundTag tag) {
        if (tag == null) {
            return new ModularRocket();
        }
        
        ModularRocket rocket = new ModularRocket();
        rocket.load(tag);
        return rocket;
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
        
        // Save components
        if (!components.isEmpty()) {
            net.minecraft.nbt.ListTag componentsList = new net.minecraft.nbt.ListTag();
            
            for (RocketComponent component : components) {
                CompoundTag componentTag = new CompoundTag();
                component.save(componentTag);
                componentsList.add(componentTag);
            }
            
            tag.put("components", componentsList);
        }
        
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
        
        // Load components
        components.clear();
        
        if (tag.contains("components", net.minecraft.nbt.Tag.TAG_LIST)) {
            net.minecraft.nbt.ListTag componentsList = tag.getList("components", net.minecraft.nbt.Tag.TAG_COMPOUND);
            
            for (int i = 0; i < componentsList.size(); i++) {
                CompoundTag componentTag = componentsList.getCompound(i);
                RocketComponent component = RocketComponent.fromTag(componentTag);
                components.add(component);
            }
        }
    }
    
    /**
     * Implements the saveToTag method from IRocket interface.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    @Override
    public CompoundTag saveToTag(CompoundTag tag) {
        return save(tag);
    }
    
    /**
     * Adds a component to this rocket.
     *
     * @param component The component to add
     */
    public void addComponent(RocketComponent component) {
        if (component != null) {
            components.add(component);
            recalculateStats();
        }
    }
    
    /**
     * Removes a component from this rocket.
     *
     * @param component The component to remove
     */
    public void removeComponent(RocketComponent component) {
        components.remove(component);
        recalculateStats();
    }
    
    /**
     * Recalculates the rocket stats based on its components.
     */
    private void recalculateStats() {
        float totalMass = 0.0f;
        float totalThrust = 0.0f;
        float totalFuelCapacity = 0.0f;
        int maxTier = 1;
        
        for (RocketComponent component : components) {
            totalMass += component.getMass();
            
            if (component.getType() == RocketComponentType.ENGINE) {
                totalThrust += 1000.0f * component.getTier() * component.getEfficiency();
            }
            
            if (component.getType() == RocketComponentType.FUEL_TANK) {
                totalFuelCapacity += 500.0f * component.getTier();
            }
            
            maxTier = Math.max(maxTier, component.getTier());
        }
        
        // Apply minimum values to ensure rocket is always functional
        this.mass = Math.max(500.0f, totalMass);
        this.thrust = Math.max(1000.0f, totalThrust);
        
        float oldCapacity = this.fuelCapacity;
        this.fuelCapacity = Math.max(100.0f, totalFuelCapacity);
        
        // Adjust fuel if capacity changed
        if (oldCapacity != this.fuelCapacity) {
            this.fuel = Math.min(this.fuel, this.fuelCapacity);
        }
        
        this.tier = maxTier;
    }
    
    @Override
    public boolean hasComponent(RocketComponentType type) {
        for (RocketComponent component : components) {
            if (component.getType() == type) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public List<RocketComponent> getAllComponents() {
        return new ArrayList<>(components);
    }
    
    @Override
    public void setFuelLevel(float fuelLevel) {
        setFuel(fuelLevel);
    }
    
    /**
     * Builder class for creating ModularRocket instances.
     */
    public static class Builder {
        private final ModularRocket rocket = new ModularRocket();
        
        /**
         * Sets the tier for the rocket.
         *
         * @param tier The rocket tier
         * @return This builder
         */
        public Builder tier(int tier) {
            rocket.setTier(tier);
            return this;
        }
        
        /**
         * Sets the fuel level for the rocket.
         *
         * @param fuel The fuel level
         * @return This builder
         */
        public Builder fuel(float fuel) {
            rocket.setFuel(fuel);
            return this;
        }
        
        /**
         * Sets the fuel capacity for the rocket.
         *
         * @param fuelCapacity The fuel capacity
         * @return This builder
         */
        public Builder fuelCapacity(float fuelCapacity) {
            rocket.setFuelCapacity(fuelCapacity);
            return this;
        }
        
        /**
         * Sets the mass for the rocket.
         *
         * @param mass The rocket mass
         * @return This builder
         */
        public Builder mass(float mass) {
            rocket.setMass(mass);
            return this;
        }
        
        /**
         * Sets the thrust for the rocket.
         *
         * @param thrust The rocket thrust
         * @return This builder
         */
        public Builder thrust(float thrust) {
            rocket.setThrust(thrust);
            return this;
        }
        
        /**
         * Adds a component to the rocket.
         *
         * @param component The component to add
         * @return This builder
         */
        public Builder addComponent(RocketComponent component) {
            rocket.addComponent(component);
            return this;
        }
        
        /**
         * Builds the rocket instance.
         *
         * @return The built rocket
         */
        public ModularRocket build() {
            return rocket;
        }
    }
}