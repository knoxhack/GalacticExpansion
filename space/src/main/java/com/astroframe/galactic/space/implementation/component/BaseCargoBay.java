package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the ICargoBay interface.
 */
public class BaseCargoBay implements ICargoBay {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int storageCapacity;
    private final boolean isClimateControlled;
    private final boolean isRadiationShielded;
    private final boolean isEMPShielded;
    
    private BaseCargoBay(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.storageCapacity = builder.storageCapacity;
        this.isClimateControlled = builder.isClimateControlled;
        this.isRadiationShielded = builder.isRadiationShielded;
        this.isEMPShielded = builder.isEMPShielded;
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
        return ComponentType.CARGO_BAY;
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
        tooltip.add(Component.literal("Storage Capacity: " + storageCapacity + " slots"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Climate Controlled: " + (isClimateControlled ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Shielded: " + (isRadiationShielded ? "Yes" : "No")));
            tooltip.add(Component.literal("EMP Shielded: " + (isEMPShielded ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    @Override
    public int getStorageCapacity() {
        return storageCapacity;
    }
    
    @Override
    public boolean isClimateControlled() {
        return isClimateControlled;
    }
    
    @Override
    public boolean isRadiationShielded() {
        return isRadiationShielded;
    }
    
    @Override
    public boolean isEMPShielded() {
        return isEMPShielded;
    }
    
    /**
     * Builder for BaseCargoBay.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 300;
        private float maxHealth = 100.0f;
        private int storageCapacity = 9;
        private boolean isClimateControlled = false;
        private boolean isRadiationShielded = false;
        private boolean isEMPShielded = false;
        
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
         * Sets the storage capacity in slots.
         * @param storageCapacity The storage capacity
         * @return This builder
         */
        public Builder storageCapacity(int storageCapacity) {
            this.storageCapacity = storageCapacity;
            return this;
        }
        
        /**
         * Sets whether the cargo bay is climate controlled.
         * @param climateControlled True if climate controlled
         * @return This builder
         */
        public Builder climateControlled(boolean climateControlled) {
            this.isClimateControlled = climateControlled;
            return this;
        }
        
        /**
         * Sets whether the cargo bay is radiation shielded.
         * @param radiationShielded True if radiation shielded
         * @return This builder
         */
        public Builder radiationShielded(boolean radiationShielded) {
            this.isRadiationShielded = radiationShielded;
            return this;
        }
        
        /**
         * Sets whether the cargo bay is EMP shielded.
         * @param empShielded True if EMP shielded
         * @return This builder
         */
        public Builder empShielded(boolean empShielded) {
            this.isEMPShielded = empShielded;
            return this;
        }
        
        /**
         * Builds the cargo bay.
         * @return A new BaseCargoBay
         */
        public BaseCargoBay build() {
            return new BaseCargoBay(this);
        }
    }
}