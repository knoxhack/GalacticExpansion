package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket fuel tank component.
 */
public class FuelTankImpl implements IFuelTank {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int maxFuelCapacity;
    private int currentFuelLevel;
    private final float leakResistance;
    private final float explosionResistance;
    private final FuelType fuelType;
    private Vec3 position;
    
    /**
     * Creates a new fuel tank.
     */
    public FuelTankImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int maxFuelCapacity,
            float leakResistance,
            float explosionResistance,
            FuelType fuelType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.maxFuelCapacity = maxFuelCapacity;
        this.currentFuelLevel = 0; // Start empty
        this.leakResistance = leakResistance;
        this.explosionResistance = explosionResistance;
        this.fuelType = fuelType;
        this.position = new Vec3(0, 0, 0);
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
        return RocketComponentType.FUEL_TANK;
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
    public int getMaxFuelCapacity() {
        return maxFuelCapacity;
    }
    
    @Override
    public int getCurrentFuelLevel() {
        return currentFuelLevel;
    }
    
    @Override
    public int addFuel(int amount) {
        if (amount <= 0 || isBroken()) {
            return 0;
        }
        
        int spaceAvailable = maxFuelCapacity - currentFuelLevel;
        int amountToAdd = Math.min(amount, spaceAvailable);
        
        currentFuelLevel += amountToAdd;
        return amountToAdd;
    }
    
    @Override
    public int consumeFuel(int amount) {
        if (amount <= 0 || isBroken()) {
            return 0;
        }
        
        int amountToConsume = Math.min(amount, currentFuelLevel);
        currentFuelLevel -= amountToConsume;
        return amountToConsume;
    }
    
    @Override
    public FuelType getFuelType() {
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
    public Vec3 getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vec3 position) {
        this.position = position;
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Call the parent implementation to save common properties
        super.save(tag);
        
        // Save fuel tank specific properties
        tag.putInt("CurrentFuelLevel", currentFuelLevel);
        tag.putInt("MaxFuelCapacity", maxFuelCapacity);
        tag.putFloat("LeakResistance", leakResistance);
        tag.putFloat("ExplosionResistance", explosionResistance);
        tag.putString("FuelType", fuelType.name());
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Call the parent implementation to load common properties
        super.load(tag);
        
        // Load fuel tank specific properties
        if (tag.contains("CurrentFuelLevel")) {
            this.currentFuelLevel = tag.getInt("CurrentFuelLevel");
        }
        
        // We don't need to load constants like maxFuelCapacity, leakResistance, etc.
        // as they are already set in the constructor
    }

    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Capacity: " + maxFuelCapacity + " units"));
        tooltip.add(Component.literal("Fuel Type: " + fuelType.name()));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Current Fuel: " + currentFuelLevel + " / " + maxFuelCapacity));
            tooltip.add(Component.literal("Leak Resistance: " + Math.round(leakResistance * 100) + "%"));
            tooltip.add(Component.literal("Explosion Resistance: " + Math.round(explosionResistance * 100) + "%"));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + currentDurability + " / " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for FuelTankImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Fuel Tank";
        private String description = "A tank for storing rocket fuel.";
        private int tier = 1;
        private int mass = 300;
        private int maxDurability = 100;
        private int maxFuelCapacity = 1000;
        private float leakResistance = 0.8f;
        private float explosionResistance = 0.5f;
        private FuelType fuelType = FuelType.CHEMICAL;
        
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
         * Sets the max fuel capacity.
         * @param maxFuelCapacity The max fuel capacity
         * @return This builder
         */
        public Builder maxFuelCapacity(int maxFuelCapacity) {
            this.maxFuelCapacity = maxFuelCapacity;
            return this;
        }
        
        /**
         * Sets the leak resistance.
         * @param leakResistance The leak resistance (0.0-1.0)
         * @return This builder
         */
        public Builder leakResistance(float leakResistance) {
            this.leakResistance = Math.max(0.0f, Math.min(1.0f, leakResistance));
            return this;
        }
        
        /**
         * Sets the explosion resistance.
         * @param explosionResistance The explosion resistance (0.0-1.0)
         * @return This builder
         */
        public Builder explosionResistance(float explosionResistance) {
            this.explosionResistance = Math.max(0.0f, Math.min(1.0f, explosionResistance));
            return this;
        }
        
        /**
         * Sets the fuel type.
         * @param fuelType The fuel type
         * @return This builder
         */
        public Builder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            return this;
        }
        
        /**
         * Builds the fuel tank.
         * @return A new FuelTankImpl
         */
        public FuelTankImpl build() {
            return new FuelTankImpl(
                    id, name, description, tier, mass, maxDurability,
                    maxFuelCapacity, leakResistance, explosionResistance, fuelType
            );
        }
    }
}