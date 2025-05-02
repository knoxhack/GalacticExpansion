package com.astroframe.galactic.space.implementation.component.fueltank;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

/**
 * A standard chemical fuel tank implementation.
 * Provides basic fuel storage capabilities.
 */
public class StandardFuelTank implements IFuelTank {
    
    private static final String DEFAULT_NAME = "Standard Fuel Tank";
    private static final String DEFAULT_DESCRIPTION = "A standard fuel tank for chemical propellants.";
    private static final int DEFAULT_TIER = 1;
    private static final int DEFAULT_MASS = 50;
    private static final int DEFAULT_MAX_DURABILITY = 100;
    private static final int DEFAULT_MAX_FUEL_CAPACITY = 1000;
    private static final float DEFAULT_LEAK_RESISTANCE = 0.8f;
    private static final float DEFAULT_EXPLOSION_RESISTANCE = 0.7f;
    
    private final ResourceLocation id;
    private String name;
    private String description;
    private int tier;
    private int mass;
    private int maxDurability;
    private int currentDurability;
    private int maxFuelCapacity;
    private int currentFuelLevel;
    private FuelType fuelType;
    private float leakResistance;
    private float explosionResistance;
    private Vec3 position;
    
    /**
     * Creates a standard fuel tank with default values.
     * 
     * @param id The unique identifier
     * @param fuelType The type of fuel this tank can store
     */
    public StandardFuelTank(ResourceLocation id, FuelType fuelType) {
        this.id = id;
        this.name = DEFAULT_NAME;
        this.description = DEFAULT_DESCRIPTION;
        this.tier = DEFAULT_TIER;
        this.mass = DEFAULT_MASS;
        this.maxDurability = DEFAULT_MAX_DURABILITY;
        this.currentDurability = maxDurability;
        this.maxFuelCapacity = DEFAULT_MAX_FUEL_CAPACITY;
        this.currentFuelLevel = 0;
        this.fuelType = fuelType;
        this.leakResistance = DEFAULT_LEAK_RESISTANCE;
        this.explosionResistance = DEFAULT_EXPLOSION_RESISTANCE;
        this.position = new Vec3(0, 0, 0);
    }
    
    /**
     * Creates a standard fuel tank with custom values.
     * 
     * @param id The unique identifier
     * @param name The display name
     * @param description The description
     * @param tier The technology tier
     * @param mass The mass in kg
     * @param maxDurability The maximum durability
     * @param maxFuelCapacity The maximum fuel capacity
     * @param fuelType The type of fuel this tank can store
     * @param leakResistance The leak resistance (0-1)
     * @param explosionResistance The explosion resistance (0-1)
     */
    public StandardFuelTank(ResourceLocation id, String name, String description, int tier, int mass,
                            int maxDurability, int maxFuelCapacity, FuelType fuelType,
                            float leakResistance, float explosionResistance) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.maxFuelCapacity = maxFuelCapacity;
        this.currentFuelLevel = 0;
        this.fuelType = fuelType;
        this.leakResistance = leakResistance;
        this.explosionResistance = explosionResistance;
        this.position = new Vec3(0, 0, 0);
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RocketComponentType getType() {
        return RocketComponentType.FUEL_TANK;
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
        // Calculate actual mass based on current fuel level
        return mass + (int)(currentFuelLevel * 0.5); // Fuel has mass too
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
        // Damaged tanks might leak
        if (currentDurability < maxDurability / 2) {
            int leakAmount = (int)((1 - leakResistance) * currentFuelLevel * 0.01);
            if (leakAmount > 0) {
                currentFuelLevel = Math.max(0, currentFuelLevel - leakAmount);
            }
        }
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
        int spaceAvailable = maxFuelCapacity - currentFuelLevel;
        int amountToAdd = Math.min(amount, spaceAvailable);
        
        currentFuelLevel += amountToAdd;
        return amountToAdd;
    }
    
    @Override
    public int consumeFuel(int amount) {
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
    public Vec3 getSize() {
        // Return appropriate size for rendering
        // Standard fuel tanks are cylindrical and taller than they are wide
        return new Vec3(0.5, 0.8, 0.5);
    }
    
    @Override
    public double getCapacity() {
        return maxFuelCapacity;
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Call parent implementation to save common properties
        IRocketComponent.super.save(tag);
        
        // Save fuel tank specific properties
        tag.putInt("MaxFuelCapacity", maxFuelCapacity);
        tag.putInt("CurrentFuelLevel", currentFuelLevel);
        tag.putString("FuelType", fuelType.name());
        tag.putFloat("LeakResistance", leakResistance);
        tag.putFloat("ExplosionResistance", explosionResistance);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Call parent implementation to load common properties
        IRocketComponent.super.load(tag);
        
        // Load fuel tank specific properties
        if (tag.contains("MaxFuelCapacity")) {
            this.maxFuelCapacity = tag.getInt("MaxFuelCapacity");
        }
        if (tag.contains("CurrentFuelLevel")) {
            this.currentFuelLevel = tag.getInt("CurrentFuelLevel");
        }
        
        // Load fuel type with fallback to default
        if (tag.contains("FuelType")) {
            try {
                String fuelTypeStr = com.astroframe.galactic.space.items.ItemStackHelper.getString(tag, "FuelType");
                this.fuelType = FuelType.valueOf(fuelTypeStr);
            } catch (IllegalArgumentException e) {
                this.fuelType = FuelType.CHEMICAL;
            }
        }
        
        if (tag.contains("LeakResistance")) {
            this.leakResistance = com.astroframe.galactic.space.items.ItemStackHelper.getFloat(tag, "LeakResistance");
        }
        if (tag.contains("ExplosionResistance")) {
            this.explosionResistance = com.astroframe.galactic.space.items.ItemStackHelper.getFloat(tag, "ExplosionResistance");
        }
    }
}