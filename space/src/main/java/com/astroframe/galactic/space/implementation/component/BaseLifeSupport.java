package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ILifeSupport;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.LifeSupportType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the ILifeSupport interface.
 */
public class BaseLifeSupport implements ILifeSupport {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int maxCrewCapacity;
    private final float oxygenEfficiency;
    private final float waterRecyclingRate;
    private final boolean hasAdvancedMedical;
    private final boolean hasRadiationScrubbers;
    private final LifeSupportType lifeSupportType;
    
    private BaseLifeSupport(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.maxCrewCapacity = builder.maxCrewCapacity;
        this.oxygenEfficiency = builder.oxygenEfficiency;
        this.waterRecyclingRate = builder.waterRecyclingRate;
        this.hasAdvancedMedical = builder.hasAdvancedMedical;
        this.hasRadiationScrubbers = builder.hasRadiationScrubbers;
        this.lifeSupportType = builder.lifeSupportType;
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
        return ComponentType.LIFE_SUPPORT;
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
        tooltip.add(Component.literal("Type: " + lifeSupportType.getDisplayName()));
        tooltip.add(Component.literal("Supports: " + maxCrewCapacity + " crew"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Oxygen Efficiency: " + Math.round(oxygenEfficiency * 100) + "%"));
            tooltip.add(Component.literal("Water Recycling: " + Math.round(waterRecyclingRate * 100) + "%"));
            tooltip.add(Component.literal("Advanced Medical: " + (hasAdvancedMedical ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Scrubbers: " + (hasRadiationScrubbers ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    @Override
    public int getMaxCrewCapacity() {
        return maxCrewCapacity;
    }
    
    @Override
    public float getOxygenEfficiency() {
        return oxygenEfficiency;
    }
    
    @Override
    public float getWaterRecyclingRate() {
        return waterRecyclingRate;
    }
    
    @Override
    public boolean hasAdvancedMedical() {
        return hasAdvancedMedical;
    }
    
    @Override
    public boolean hasRadiationScrubbers() {
        return hasRadiationScrubbers;
    }
    
    @Override
    public LifeSupportType getLifeSupportType() {
        return lifeSupportType;
    }
    
    /**
     * Builder for BaseLifeSupport.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 350;
        private float maxHealth = 100.0f;
        private int maxCrewCapacity = 4;
        private float oxygenEfficiency = 0.8f;
        private float waterRecyclingRate = 0.7f;
        private boolean hasAdvancedMedical = false;
        private boolean hasRadiationScrubbers = false;
        private LifeSupportType lifeSupportType = LifeSupportType.BASIC;
        
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
         * Sets the max crew capacity.
         * @param maxCrewCapacity The max crew capacity
         * @return This builder
         */
        public Builder maxCrewCapacity(int maxCrewCapacity) {
            this.maxCrewCapacity = maxCrewCapacity;
            return this;
        }
        
        /**
         * Sets the oxygen efficiency.
         * @param oxygenEfficiency The oxygen efficiency (0.0-1.0)
         * @return This builder
         */
        public Builder oxygenEfficiency(float oxygenEfficiency) {
            this.oxygenEfficiency = Math.max(0.0f, Math.min(1.0f, oxygenEfficiency));
            return this;
        }
        
        /**
         * Sets the water recycling rate.
         * @param waterRecyclingRate The water recycling rate (0.0-1.0)
         * @return This builder
         */
        public Builder waterRecyclingRate(float waterRecyclingRate) {
            this.waterRecyclingRate = Math.max(0.0f, Math.min(1.0f, waterRecyclingRate));
            return this;
        }
        
        /**
         * Sets whether the system has advanced medical facilities.
         * @param hasAdvancedMedical True if has advanced medical
         * @return This builder
         */
        public Builder advancedMedical(boolean hasAdvancedMedical) {
            this.hasAdvancedMedical = hasAdvancedMedical;
            return this;
        }
        
        /**
         * Sets whether the system has radiation scrubbers.
         * @param hasRadiationScrubbers True if has radiation scrubbers
         * @return This builder
         */
        public Builder radiationScrubbers(boolean hasRadiationScrubbers) {
            this.hasRadiationScrubbers = hasRadiationScrubbers;
            return this;
        }
        
        /**
         * Sets the life support type.
         * @param lifeSupportType The life support type
         * @return This builder
         */
        public Builder lifeSupportType(LifeSupportType lifeSupportType) {
            this.lifeSupportType = lifeSupportType;
            return this;
        }
        
        /**
         * Builds the life support system.
         * @return A new BaseLifeSupport
         */
        public BaseLifeSupport build() {
            return new BaseLifeSupport(this);
        }
    }
}