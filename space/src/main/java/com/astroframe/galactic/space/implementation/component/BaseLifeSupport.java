package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ILifeSupport;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the ILifeSupport interface.
 */
public class BaseLifeSupport implements ILifeSupport {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int maxCrewCapacity;
    private final int oxygenGenerationRate;
    private final float waterRecyclingEfficiency;
    private final int foodProductionRate;
    private final float wasteManagementEfficiency;
    private final float atmosphericQuality;
    private final boolean hasBackupSystems;
    private final boolean hasRadiationFiltering;
    private final boolean hasEmergencyMode;
    private boolean emergencyModeActive = false;
    
    private BaseLifeSupport(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = maxDurability;
        this.maxCrewCapacity = builder.maxCrewCapacity;
        this.oxygenGenerationRate = builder.oxygenGenerationRate;
        this.waterRecyclingEfficiency = builder.waterRecyclingEfficiency;
        this.foodProductionRate = builder.foodProductionRate;
        this.wasteManagementEfficiency = builder.wasteManagementEfficiency;
        this.atmosphericQuality = builder.atmosphericQuality;
        this.hasBackupSystems = builder.hasBackupSystems;
        this.hasRadiationFiltering = builder.hasRadiationFiltering;
        this.hasEmergencyMode = builder.hasEmergencyMode;
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
        return RocketComponentType.LIFE_SUPPORT;
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
    public int getMaxCrewCapacity() {
        return maxCrewCapacity;
    }
    
    @Override
    public int getOxygenGenerationRate() {
        return oxygenGenerationRate;
    }
    
    @Override
    public float getWaterRecyclingEfficiency() {
        return waterRecyclingEfficiency;
    }
    
    @Override
    public int getFoodProductionRate() {
        return foodProductionRate;
    }
    
    @Override
    public float getWasteManagementEfficiency() {
        return wasteManagementEfficiency;
    }
    
    @Override
    public float getAtmosphericQuality() {
        return atmosphericQuality;
    }
    
    @Override
    public boolean hasBackupSystems() {
        return hasBackupSystems;
    }
    
    @Override
    public boolean hasRadiationFiltering() {
        return hasRadiationFiltering;
    }
    
    @Override
    public boolean hasEmergencyMode() {
        return hasEmergencyMode;
    }
    
    @Override
    public void setEmergencyMode(boolean active) {
        this.emergencyModeActive = active && hasEmergencyMode && !isBroken();
    }
    
    @Override
    public boolean isEmergencyModeActive() {
        return emergencyModeActive && !isBroken();
    }
    
    /**
     * Check if this component is broken (has no durability left).
     * @return true if broken, false otherwise
     */
    @Override
    public boolean isBroken() {
        return currentDurability <= 0;
    }
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Supports: " + maxCrewCapacity + " crew"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Oxygen Generation: " + oxygenGenerationRate + " units/min"));
            tooltip.add(Component.literal("Water Recycling: " + Math.round(waterRecyclingEfficiency * 100) + "%"));
            tooltip.add(Component.literal("Food Production: " + foodProductionRate + " units/day"));
            tooltip.add(Component.literal("Waste Management: " + Math.round(wasteManagementEfficiency * 100) + "%"));
            tooltip.add(Component.literal("Atmospheric Quality: " + Math.round(atmosphericQuality * 100) + "%"));
            tooltip.add(Component.literal("Backup Systems: " + (hasBackupSystems ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Filtering: " + (hasRadiationFiltering ? "Yes" : "No")));
            tooltip.add(Component.literal("Emergency Mode: " + (hasEmergencyMode ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for BaseLifeSupport.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Life Support";
        private String description = "A life support system for a rocket.";
        private int tier = 1;
        private int mass = 350;
        private int maxDurability = 100;
        private int maxCrewCapacity = 4;
        private int oxygenGenerationRate = 60;
        private float waterRecyclingEfficiency = 0.7f;
        private int foodProductionRate = 10;
        private float wasteManagementEfficiency = 0.8f;
        private float atmosphericQuality = 0.9f;
        private boolean hasBackupSystems = false;
        private boolean hasRadiationFiltering = false;
        private boolean hasEmergencyMode = false;
        
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
         * Sets the max crew capacity.
         * @param maxCrewCapacity The max crew capacity
         * @return This builder
         */
        public Builder maxCrewCapacity(int maxCrewCapacity) {
            this.maxCrewCapacity = maxCrewCapacity;
            return this;
        }
        
        /**
         * Sets the oxygen generation rate.
         * @param oxygenGenerationRate The oxygen generation rate
         * @return This builder
         */
        public Builder oxygenGenerationRate(int oxygenGenerationRate) {
            this.oxygenGenerationRate = oxygenGenerationRate;
            return this;
        }
        
        /**
         * Sets the oxygen efficiency (alias for atmospheric quality).
         * @param efficiency The oxygen efficiency (0.0-1.0)
         * @return This builder
         */
        public Builder oxygenEfficiency(float efficiency) {
            this.atmosphericQuality = Math.max(0.0f, Math.min(1.0f, efficiency));
            return this;
        }
        
        /**
         * Sets the water recycling efficiency.
         * @param waterRecyclingEfficiency The water recycling efficiency (0.0-1.0)
         * @return This builder
         */
        public Builder waterRecyclingEfficiency(float waterRecyclingEfficiency) {
            this.waterRecyclingEfficiency = Math.max(0.0f, Math.min(1.0f, waterRecyclingEfficiency));
            return this;
        }
        
        /**
         * Sets the food production rate.
         * @param foodProductionRate The food production rate
         * @return This builder
         */
        public Builder foodProductionRate(int foodProductionRate) {
            this.foodProductionRate = foodProductionRate;
            return this;
        }
        
        /**
         * Sets the waste management efficiency.
         * @param wasteManagementEfficiency The waste management efficiency (0.0-1.0)
         * @return This builder
         */
        public Builder wasteManagementEfficiency(float wasteManagementEfficiency) {
            this.wasteManagementEfficiency = Math.max(0.0f, Math.min(1.0f, wasteManagementEfficiency));
            return this;
        }
        
        /**
         * Sets the atmospheric quality.
         * @param atmosphericQuality The atmospheric quality (0.0-1.0)
         * @return This builder
         */
        public Builder atmosphericQuality(float atmosphericQuality) {
            this.atmosphericQuality = Math.max(0.0f, Math.min(1.0f, atmosphericQuality));
            return this;
        }
        
        /**
         * Sets whether the system has backup systems.
         * @param hasBackupSystems True if has backup systems
         * @return This builder
         */
        public Builder backupSystems(boolean hasBackupSystems) {
            this.hasBackupSystems = hasBackupSystems;
            return this;
        }
        
        /**
         * Sets whether the system has radiation filtering.
         * @param hasRadiationFiltering True if has radiation filtering
         * @return This builder
         */
        public Builder radiationFiltering(boolean hasRadiationFiltering) {
            this.hasRadiationFiltering = hasRadiationFiltering;
            return this;
        }
        
        /**
         * Sets whether the system has emergency mode.
         * @param hasEmergencyMode True if has emergency mode
         * @return This builder
         */
        public Builder emergencyMode(boolean hasEmergencyMode) {
            this.hasEmergencyMode = hasEmergencyMode;
            return this;
        }
        
        /**
         * Sets the water recycling rate (alias for water recycling efficiency).
         * @param rate The water recycling rate (0.0-1.0)
         * @return This builder
         */
        public Builder waterRecyclingRate(float rate) {
            this.waterRecyclingEfficiency = Math.max(0.0f, Math.min(1.0f, rate));
            return this;
        }
        
        /**
         * Sets the life support type (internal categorization).
         * @param type The life support type
         * @return This builder
         */
        public Builder lifeSupportType(LifeSupportType type) {
            // This is just for categorization, doesn't affect properties
            return this;
        }
        
        /**
         * Sets whether the life support has advanced medical capabilities.
         * @param hasAdvancedMedical True if the system has advanced medical
         * @return This builder
         */
        public Builder advancedMedical(boolean hasAdvancedMedical) {
            // This is a placeholder for future functionality
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