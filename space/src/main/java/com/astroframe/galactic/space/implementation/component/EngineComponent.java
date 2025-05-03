package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.FuelType;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.space.util.TagHelper;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementation of a rocket engine component.
 */
public class EngineComponent extends BasicRocketComponent implements IRocketEngine {
    
    private double thrust;
    private double efficiency;
    private EngineType engineType;
    private FuelType fuelType;
    private double fuelConsumptionRate;
    private double heatGeneration;
    private List<FuelType> compatibleFuels;
    private boolean canOperateInAtmosphere;
    private boolean canOperateInSpace;
    
    /**
     * Create a new engine component.
     *
     * @param type The component type
     * @param tier The tier level
     */
    public EngineComponent(RocketComponentType type, int tier) {
        super(type, tier);
        this.thrust = calculateThrust(tier);
        this.efficiency = calculateEfficiency(tier);
        this.engineType = tier <= 1 ? EngineType.CHEMICAL : (tier == 2 ? EngineType.ION : EngineType.FUSION);
        this.fuelType = tier <= 1 ? FuelType.CHEMICAL : (tier == 2 ? FuelType.ION : FuelType.ANTIMATTER);
        this.fuelConsumptionRate = 100.0 - (tier * 20.0);
        this.heatGeneration = 50.0 + (tier * 25.0);
        this.compatibleFuels = new ArrayList<>();
        this.compatibleFuels.add(fuelType);
        
        // Add backwards compatibility for higher tier engines
        if (tier >= 3) {
            this.compatibleFuels.add(FuelType.ION);
            this.compatibleFuels.add(FuelType.CHEMICAL);
        } else if (tier >= 2) {
            this.compatibleFuels.add(FuelType.CHEMICAL);
        }
        
        this.canOperateInAtmosphere = tier <= 2;
        this.canOperateInSpace = true;
    }
    
    /**
     * Calculate thrust based on tier.
     *
     * @param tier The tier level
     * @return The thrust value
     */
    private double calculateThrust(int tier) {
        return tier * 100.0;
    }
    
    /**
     * Calculate efficiency based on tier.
     *
     * @param tier The tier level
     * @return The efficiency value
     */
    private double calculateEfficiency(int tier) {
        return 0.5 + (tier * 0.1);
    }
    
    @Override
    public EngineType getEngineType() {
        return engineType;
    }
    
    @Override
    public double getThrust() {
        return thrust;
    }
    
    @Override
    public double getEfficiency() {
        return efficiency;
    }
    
    @Override
    public List<FuelType> getCompatibleFuels() {
        return compatibleFuels;
    }
    
    @Override
    public FuelType getFuelType() {
        return fuelType;
    }
    
    @Override
    public boolean canOperateInAtmosphere() {
        return canOperateInAtmosphere;
    }
    
    @Override
    public boolean canOperateInSpace() {
        return canOperateInSpace;
    }
    
    @Override
    public double getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }
    
    @Override
    public double getHeatGeneration() {
        return heatGeneration;
    }
    
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = super.serializeNBT();
        tag.putDouble("thrust", thrust);
        tag.putDouble("efficiency", efficiency);
        tag.putString("engineType", engineType.name());
        tag.putString("fuelType", fuelType.name());
        tag.putDouble("fuelConsumptionRate", fuelConsumptionRate);
        tag.putDouble("heatGeneration", heatGeneration);
        tag.putBoolean("canOperateInAtmosphere", canOperateInAtmosphere);
        tag.putBoolean("canOperateInSpace", canOperateInSpace);
        
        // Store compatible fuels as a comma-separated string
        StringBuilder fuelList = new StringBuilder();
        for (int i = 0; i < compatibleFuels.size(); i++) {
            fuelList.append(compatibleFuels.get(i).name());
            if (i < compatibleFuels.size() - 1) {
                fuelList.append(",");
            }
        }
        tag.putString("compatibleFuels", fuelList.toString());
        
        return tag;
    }
    
    @Override
    public void deserializeNBT(CompoundTag tag) {
        super.deserializeNBT(tag);
        this.thrust = TagHelper.getDouble(tag, "thrust");
        this.efficiency = TagHelper.getDouble(tag, "efficiency");
        
        try {
            this.engineType = EngineType.valueOf(TagHelper.getString(tag, "engineType"));
        } catch (IllegalArgumentException e) {
            this.engineType = EngineType.CHEMICAL;
        }
        
        try {
            this.fuelType = FuelType.valueOf(TagHelper.getString(tag, "fuelType"));
        } catch (IllegalArgumentException e) {
            this.fuelType = FuelType.CHEMICAL;
        }
        
        this.fuelConsumptionRate = TagHelper.getDouble(tag, "fuelConsumptionRate");
        this.heatGeneration = TagHelper.getDouble(tag, "heatGeneration");
        this.canOperateInAtmosphere = TagHelper.getBoolean(tag, "canOperateInAtmosphere");
        this.canOperateInSpace = TagHelper.getBoolean(tag, "canOperateInSpace");
        
        // Parse compatible fuels from comma-separated string
        this.compatibleFuels = new ArrayList<>();
        String fuelList = TagHelper.getString(tag, "compatibleFuels");
        if (!fuelList.isEmpty()) {
            String[] fuels = fuelList.split(",");
            for (String fuel : fuels) {
                try {
                    this.compatibleFuels.add(FuelType.valueOf(fuel));
                } catch (IllegalArgumentException e) {
                    // Skip invalid fuel types
                }
            }
        }
        
        // Ensure we have at least the primary fuel type
        if (this.compatibleFuels.isEmpty()) {
            this.compatibleFuels.add(this.fuelType);
        }
    }
}