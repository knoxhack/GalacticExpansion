package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IRocketEngine interface.
 */
public class BaseRocketEngine implements IRocketEngine {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int thrust;
    private final float efficiency;
    private final EngineType engineType;
    private final int heatCapacity;
    private final boolean canOperateUnderwater;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    
    private BaseRocketEngine(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.thrust = builder.thrust;
        this.efficiency = builder.efficiency;
        this.engineType = builder.engineType;
        this.heatCapacity = builder.heatCapacity;
        this.canOperateUnderwater = builder.canOperateUnderwater;
        this.canOperateInAtmosphere = builder.canOperateInAtmosphere;
        this.canOperateInSpace = builder.canOperateInSpace;
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public Component getDisplayName() {
        return displayName;
    }
    
    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public ComponentType getType() {
        return ComponentType.ENGINE;
    }
    
    @Override
    public int getMass() {
        return mass;
    }
    
    @Override
    public float getMaxHealth() {
        return maxHealth;
    }
    
    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(displayName);
        tooltip.add(Component.literal("Type: " + engineType.name()));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Thrust: " + thrust));
            tooltip.add(Component.literal("Efficiency: " + efficiency));
            tooltip.add(Component.literal("Heat Capacity: " + heatCapacity));
            tooltip.add(Component.literal("Operates in Atmosphere: " + (canOperateInAtmosphere ? "Yes" : "No")));
            tooltip.add(Component.literal("Operates in Space: " + (canOperateInSpace ? "Yes" : "No")));
            tooltip.add(Component.literal("Operates Underwater: " + (canOperateUnderwater ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    @Override
    public int getThrust() {
        return thrust;
    }
    
    @Override
    public float getEfficiency() {
        return efficiency;
    }
    
    @Override
    public EngineType getEngineType() {
        return engineType;
    }
    
    @Override
    public int getHeatCapacity() {
        return heatCapacity;
    }
    
    @Override
    public boolean canOperateUnderwater() {
        return canOperateUnderwater;
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
     * Builder for BaseRocketEngine.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 1000;
        private float maxHealth = 100.0f;
        private int thrust = 100;
        private float efficiency = 1.0f;
        private EngineType engineType = EngineType.CHEMICAL;
        private int heatCapacity = 1000;
        private boolean canOperateUnderwater = false;
        private boolean canOperateInAtmosphere = true;
        private boolean canOperateInSpace = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.displayName = displayName;
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
         * Sets the max health.
         * @param maxHealth The max health
         * @return This builder
         */
        public Builder maxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }
        
        /**
         * Sets the thrust.
         * @param thrust The thrust power
         * @return This builder
         */
        public Builder thrust(int thrust) {
            this.thrust = thrust;
            return this;
        }
        
        /**
         * Sets the efficiency.
         * @param efficiency The fuel efficiency
         * @return This builder
         */
        public Builder efficiency(float efficiency) {
            this.efficiency = efficiency;
            return this;
        }
        
        /**
         * Sets the engine type.
         * @param engineType The engine type
         * @return This builder
         */
        public Builder engineType(EngineType engineType) {
            this.engineType = engineType;
            return this;
        }
        
        /**
         * Sets the heat capacity.
         * @param heatCapacity The heat capacity
         * @return This builder
         */
        public Builder heatCapacity(int heatCapacity) {
            this.heatCapacity = heatCapacity;
            return this;
        }
        
        /**
         * Sets whether the engine can operate underwater.
         * @param canOperateUnderwater True if can operate underwater
         * @return This builder
         */
        public Builder canOperateUnderwater(boolean canOperateUnderwater) {
            this.canOperateUnderwater = canOperateUnderwater;
            return this;
        }
        
        /**
         * Sets whether the engine can operate in atmosphere.
         * @param canOperateInAtmosphere True if can operate in atmosphere
         * @return This builder
         */
        public Builder canOperateInAtmosphere(boolean canOperateInAtmosphere) {
            this.canOperateInAtmosphere = canOperateInAtmosphere;
            return this;
        }
        
        /**
         * Sets whether the engine can operate in space.
         * @param canOperateInSpace True if can operate in space
         * @return This builder
         */
        public Builder canOperateInSpace(boolean canOperateInSpace) {
            this.canOperateInSpace = canOperateInSpace;
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