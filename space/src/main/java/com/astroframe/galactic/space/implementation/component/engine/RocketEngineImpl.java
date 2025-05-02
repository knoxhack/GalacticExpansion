package com.astroframe.galactic.space.implementation.component.engine;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import com.astroframe.galactic.core.api.space.component.enums.FuelType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the IRocketEngine interface.
 * This represents a rocket engine component with various properties.
 */
public class RocketEngineImpl implements IRocketEngine {

    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final double mass;
    private final double thrust;
    private final double efficiency;
    private final double fuelConsumptionRate;
    private final FuelType fuelType;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    private final EngineType engineType;
    private final double heatGeneration;
    private final List<FuelType> compatibleFuels;
    private MountingPosition requiredMountingPosition;
    
    // Durability fields required by IRocketComponent
    private final int maxDurability;
    private int currentDurability;

    private RocketEngineImpl(ResourceLocation id, String name, String description, int tier, 
                           double mass, double thrust, double efficiency, 
                           double fuelConsumptionRate, FuelType fuelType, 
                           boolean atmosphereOperable, boolean spaceOperable,
                           EngineType engineType, double heatGeneration,
                           List<FuelType> compatibleFuels,
                           MountingPosition requiredMountingPosition) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.thrust = thrust;
        this.efficiency = efficiency;
        this.fuelConsumptionRate = fuelConsumptionRate;
        this.fuelType = fuelType;
        this.canOperateInAtmosphere = atmosphereOperable;
        this.canOperateInSpace = spaceOperable;
        this.engineType = engineType;
        this.heatGeneration = heatGeneration;
        this.compatibleFuels = compatibleFuels;
        this.requiredMountingPosition = requiredMountingPosition;
        
        // Set durability based on tier
        this.maxDurability = 500 * tier;
        this.currentDurability = this.maxDurability;
    }
    
    /**
     * Gets the maximum durability of this engine.
     * @return The maximum durability
     */
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    /**
     * Gets the current durability of this engine.
     * @return The current durability
     */
    @Override
    public int getCurrentDurability() {
        return currentDurability;
    }
    
    /**
     * Damages this engine.
     * @param amount The amount of damage to apply
     */
    @Override
    public void damage(int amount) {
        this.currentDurability = Math.max(0, this.currentDurability - amount);
    }
    
    /**
     * Repairs this engine.
     * @param amount The amount to repair
     */
    @Override
    public void repair(int amount) {
        this.currentDurability = Math.min(this.maxDurability, this.currentDurability + amount);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

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
        return (int) mass;
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
    public MountingPosition getRequiredMountingPosition() {
        return requiredMountingPosition != null ? requiredMountingPosition : MountingPosition.ANY;
    }

    @Override
    public RocketComponentType getType() {
        return RocketComponentType.ENGINE;
    }

    /**
     * Builder class for creating RocketEngineImpl instances.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "";
        private String description = "";
        private int tier = 1;
        private double mass = 100.0;
        private double thrust = 500.0;
        private double efficiency = 0.8;
        private double fuelConsumptionRate = 10.0;
        private FuelType fuelType = FuelType.CHEMICAL;
        private boolean atmosphereCapable = true;
        private boolean spaceCapable = true;
        private EngineType engineType = EngineType.CHEMICAL;
        private double heatGeneration = 100.0;
        private List<FuelType> compatibleFuels = new ArrayList<>();
        private MountingPosition requiredMountingPosition = MountingPosition.ANY;

        public Builder(ResourceLocation id) {
            this.id = id;
            // Default compatible fuel is the primary fuel type
            this.compatibleFuels.add(fuelType);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tier(int tier) {
            this.tier = tier;
            return this;
        }

        public Builder mass(double mass) {
            this.mass = mass;
            return this;
        }

        public Builder thrust(double thrust) {
            this.thrust = thrust;
            return this;
        }

        public Builder efficiency(double efficiency) {
            this.efficiency = efficiency;
            return this;
        }

        public Builder fuelConsumptionRate(double rate) {
            this.fuelConsumptionRate = rate;
            return this;
        }

        public Builder fuelType(FuelType fuelType) {
            this.fuelType = fuelType;
            // Update compatible fuels if we change the primary fuel type
            if (!this.compatibleFuels.contains(fuelType)) {
                this.compatibleFuels.add(fuelType);
            }
            return this;
        }

        public Builder addCompatibleFuel(FuelType fuelType) {
            if (!this.compatibleFuels.contains(fuelType)) {
                this.compatibleFuels.add(fuelType);
            }
            return this;
        }

        public Builder atmosphereCapable(boolean capable) {
            this.atmosphereCapable = capable;
            return this;
        }

        public Builder spaceCapable(boolean capable) {
            this.spaceCapable = capable;
            return this;
        }

        public Builder engineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }
        
        public Builder heatGeneration(double heatGeneration) {
            this.heatGeneration = heatGeneration;
            return this;
        }

        public Builder requiredMountingPosition(MountingPosition position) {
            this.requiredMountingPosition = position;
            return this;
        }

        public RocketEngineImpl build() {
            return new RocketEngineImpl(id, name, description, tier, mass, thrust, efficiency,
                    fuelConsumptionRate, fuelType, atmosphereCapable, spaceCapable,
                    engineType, heatGeneration, compatibleFuels, requiredMountingPosition);
        }
    }
}