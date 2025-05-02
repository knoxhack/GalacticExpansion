package com.astroframe.galactic.space.implementation.component.engine;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic chemical rocket engine implementation.
 * This is a simple, reliable engine that can operate in both atmosphere and space.
 */
public class BasicChemicalEngine implements IRocketEngine {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final double thrust;
    private final double efficiency;
    private final double fuelConsumptionRate;
    private final double heatGeneration;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    private final List<FuelType> compatibleFuels;
    private final MountingPosition mountingPosition;
    
    private int maxDurability;
    private int currentDurability;
    private Vec3 position;
    
    /**
     * Creates a new BasicChemicalEngine.
     *
     * @param id The engine's unique identifier
     * @param tier The technology tier of this engine (1-3)
     * @param name The display name of this engine
     * @param description A short description of this engine
     */
    public BasicChemicalEngine(ResourceLocation id, int tier, String name, String description) {
        this.id = id;
        this.tier = Math.max(1, Math.min(3, tier)); // Clamp between 1-3
        this.name = name;
        this.description = description;
        
        // Calculate properties based on tier
        this.mass = 100 * tier;
        this.thrust = 5000 * tier;
        this.efficiency = 0.4 + (tier * 0.1); // 0.5, 0.6, 0.7 for tiers 1-3
        this.fuelConsumptionRate = thrust / (efficiency * 20);
        this.heatGeneration = thrust / 100;
        
        // All chemical engines can operate in atmosphere
        this.canOperateInAtmosphere = true;
        
        // Higher tier engines can operate in space
        this.canOperateInSpace = tier >= 2;
        
        // Durability increases with tier
        this.maxDurability = 500 * tier;
        this.currentDurability = maxDurability;
        
        // Default position is at origin
        this.position = new Vec3(0, 0, 0);
        
        // Compatible fuels
        this.compatibleFuels = new ArrayList<>();
        this.compatibleFuels.add(FuelType.CHEMICAL);
        this.compatibleFuels.add(FuelType.LIQUID);
        if (tier >= 2) {
            this.compatibleFuels.add(FuelType.BIOFUEL);
        }
        
        // Mounting position for chemical engines is typically at the bottom
        this.mountingPosition = MountingPosition.BOTTOM;
    }
    
    @Override
    public EngineType getEngineType() {
        return EngineType.CHEMICAL;
    }
    
    @Override
    public double getThrust() {
        // Return lower thrust if damaged
        double healthPercentage = (double) currentDurability / maxDurability;
        return thrust * Math.max(0.5, healthPercentage);
    }
    
    @Override
    public double getEfficiency() {
        // Return lower efficiency if damaged
        double healthPercentage = (double) currentDurability / maxDurability;
        return efficiency * Math.max(0.7, healthPercentage);
    }
    
    @Override
    public List<FuelType> getCompatibleFuels() {
        return new ArrayList<>(compatibleFuels);
    }
    
    @Override
    public boolean canOperateInAtmosphere() {
        return canOperateInAtmosphere && !isBroken();
    }
    
    @Override
    public boolean canOperateInSpace() {
        return canOperateInSpace && !isBroken();
    }
    
    @Override
    public double getFuelConsumptionRate() {
        // Damaged engines consume more fuel
        double healthPercentage = (double) currentDurability / maxDurability;
        return fuelConsumptionRate * (1 + (1 - healthPercentage) * 0.5);
    }
    
    @Override
    public double getHeatGeneration() {
        // Damaged engines generate more heat
        double healthPercentage = (double) currentDurability / maxDurability;
        return heatGeneration * (1 + (1 - healthPercentage) * 0.8);
    }
    
    @Override
    public MountingPosition getRequiredMountingPosition() {
        return mountingPosition;
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
    public Vec3 getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vec3 position) {
        this.position = position;
    }
    
    @Override
    public void save(CompoundTag tag) {
        tag.putString("ID", id.toString());
        tag.putString("Type", getType().name());
        tag.putString("EngineType", getEngineType().name());
        tag.putString("Name", name);
        tag.putString("Description", description);
        tag.putInt("Tier", tier);
        tag.putInt("Mass", mass);
        tag.putDouble("Thrust", thrust);
        tag.putDouble("Efficiency", efficiency);
        tag.putDouble("FuelConsumptionRate", fuelConsumptionRate);
        tag.putDouble("HeatGeneration", heatGeneration);
        tag.putBoolean("CanOperateInAtmosphere", canOperateInAtmosphere);
        tag.putBoolean("CanOperateInSpace", canOperateInSpace);
        tag.putInt("MaxDurability", maxDurability);
        tag.putInt("CurrentDurability", currentDurability);
        tag.putString("MountingPosition", mountingPosition.name());
        
        // Save position
        tag.putDouble("PosX", position.x);
        tag.putDouble("PosY", position.y);
        tag.putDouble("PosZ", position.z);
        
        // Save compatible fuels
        CompoundTag fuelsTag = new CompoundTag();
        fuelsTag.putInt("Count", compatibleFuels.size());
        for (int i = 0; i < compatibleFuels.size(); i++) {
            fuelsTag.putString("Fuel" + i, compatibleFuels.get(i).name());
        }
        tag.put("CompatibleFuels", fuelsTag);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Most properties are calculated from tier in constructor
        // But we can load any saved position and durability
        
        // Load position
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            double x = tag.getDouble("PosX").orElse(0.0);
            double y = tag.getDouble("PosY").orElse(0.0);
            double z = tag.getDouble("PosZ").orElse(0.0);
            position = new Vec3(x, y, z);
        }
        
        // Load durability
        if (tag.contains("CurrentDurability")) {
            currentDurability = tag.getInt("CurrentDurability");
            // Ensure it's within valid range
            currentDurability = Math.max(0, Math.min(maxDurability, currentDurability));
        }
    }
}