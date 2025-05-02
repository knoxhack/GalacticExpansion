package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IRocketEngine interface.
 */
public class BaseRocketEngine implements IRocketEngine {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int thrust;
    private final int fuelConsumptionRate;
    private final float efficiency;
    private final FuelType fuelType;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    
    private BaseRocketEngine(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = this.maxDurability;
        this.thrust = builder.thrust;
        this.fuelConsumptionRate = builder.fuelConsumptionRate;
        this.efficiency = builder.efficiency;
        this.fuelType = builder.fuelType;
        this.canOperateInAtmosphere = builder.canOperateInAtmosphere;
        this.canOperateInSpace = builder.canOperateInSpace;
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
        return RocketComponentType.ENGINE;
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
    public int getThrust() {
        return thrust;
    }
    
    @Override
    public int getFuelConsumptionRate() {
        return fuelConsumptionRate;
    }
    
    @Override
    public float getEfficiency() {
        return efficiency;
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
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Tier: " + tier));
        tooltip.add(Component.literal("Thrust: " + thrust + " N"));
        
        if (detailed) {
            tooltip.add(Component.literal("Fuel Type: " + fuelType.name()));
            tooltip.add(Component.literal("Fuel Consumption: " + fuelConsumptionRate + " units/s"));
            tooltip.add(Component.literal("Efficiency: " + String.format("%.2f", efficiency)));
            tooltip.add(Component.literal("Atmosphere Operation: " + (canOperateInAtmosphere ? "Yes" : "No")));
            tooltip.add(Component.literal("Space Operation: " + (canOperateInSpace ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for BaseRocketEngine.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Rocket Engine";
        private String description = "A rocket engine that provides thrust.";
        private int tier = 1;
        private int mass = 500;
        private int maxDurability = 100;
        private int thrust = 1000;
        private int fuelConsumptionRate = 10;
        private float efficiency = 1.0f;
        private FuelType fuelType = FuelType.CHEMICAL;
        private boolean canOperateInAtmosphere = true;
        private boolean canOperateInSpace = true;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name component
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.name = displayName.getString();
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
         * Sets the thrust.
         * @param thrust The thrust
         * @return This builder
         */
        public Builder thrust(int thrust) {
            this.thrust = thrust;
            return this;
        }
        
        /**
         * Sets the fuel consumption rate.
         * @param fuelConsumptionRate The fuel consumption rate
         * @return This builder
         */
        public Builder fuelConsumptionRate(int fuelConsumptionRate) {
            this.fuelConsumptionRate = fuelConsumptionRate;
            return this;
        }
        
        /**
         * Sets the efficiency.
         * @param efficiency The efficiency
         * @return This builder
         */
        public Builder efficiency(float efficiency) {
            this.efficiency = Math.max(0.1f, Math.min(2.0f, efficiency));
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
         * Sets whether the engine can operate in atmosphere.
         * @param canOperate True if the engine can operate in atmosphere
         * @return This builder
         */
        public Builder operatesInAtmosphere(boolean canOperate) {
            this.canOperateInAtmosphere = canOperate;
            return this;
        }
        
        /**
         * Sets whether the engine can operate in space.
         * @param canOperate True if the engine can operate in space
         * @return This builder
         */
        public Builder operatesInSpace(boolean canOperate) {
            this.canOperateInSpace = canOperate;
            return this;
        }
        
        /**
         * Builds the rocket engine.
         * @return A new BaseRocketEngine
         */
        public BaseRocketEngine build() {
            return new BaseRocketEngine(this);
        }
    }
}