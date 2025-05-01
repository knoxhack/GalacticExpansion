package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.ShieldType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IShield interface.
 */
public class BaseShield implements IShield {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final float maxShieldStrength;
    private final float regenerationRate;
    private final int impactResistance;
    private final boolean isRadiationShielded;
    private final boolean isEMPShielded;
    private final boolean isThermalShielded;
    private final ShieldType shieldType;
    
    private BaseShield(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.maxShieldStrength = builder.maxShieldStrength;
        this.regenerationRate = builder.regenerationRate;
        this.impactResistance = builder.impactResistance;
        this.isRadiationShielded = builder.isRadiationShielded;
        this.isEMPShielded = builder.isEMPShielded;
        this.isThermalShielded = builder.isThermalShielded;
        this.shieldType = builder.shieldType;
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
        return ComponentType.SHIELD;
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
        tooltip.add(Component.literal("Type: " + shieldType.getDisplayName()));
        tooltip.add(Component.literal("Strength: " + maxShieldStrength));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Impact Resistance: " + impactResistance + "/10"));
            tooltip.add(Component.literal("Regeneration Rate: " + regenerationRate + "/s"));
            tooltip.add(Component.literal("Radiation Shielded: " + (isRadiationShielded ? "Yes" : "No")));
            tooltip.add(Component.literal("EMP Shielded: " + (isEMPShielded ? "Yes" : "No")));
            tooltip.add(Component.literal("Thermal Shielded: " + (isThermalShielded ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    @Override
    public float getMaxShieldStrength() {
        return maxShieldStrength;
    }
    
    @Override
    public float getRegenerationRate() {
        return regenerationRate;
    }
    
    @Override
    public int getImpactResistance() {
        return impactResistance;
    }
    
    @Override
    public boolean isRadiationShielded() {
        return isRadiationShielded;
    }
    
    @Override
    public boolean isEMPShielded() {
        return isEMPShielded;
    }
    
    @Override
    public boolean isThermalShielded() {
        return isThermalShielded;
    }
    
    @Override
    public ShieldType getShieldType() {
        return shieldType;
    }
    
    /**
     * Builder for BaseShield.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 250;
        private float maxHealth = 100.0f;
        private float maxShieldStrength = 100.0f;
        private float regenerationRate = 1.0f;
        private int impactResistance = 5;
        private boolean isRadiationShielded = false;
        private boolean isEMPShielded = false;
        private boolean isThermalShielded = false;
        private ShieldType shieldType = ShieldType.BASIC;
        
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
         * Sets the max shield strength.
         * @param maxShieldStrength The max shield strength
         * @return This builder
         */
        public Builder maxShieldStrength(float maxShieldStrength) {
            this.maxShieldStrength = maxShieldStrength;
            return this;
        }
        
        /**
         * Sets the regeneration rate.
         * @param regenerationRate The regeneration rate per second
         * @return This builder
         */
        public Builder regenerationRate(float regenerationRate) {
            this.regenerationRate = regenerationRate;
            return this;
        }
        
        /**
         * Sets the impact resistance.
         * @param impactResistance The impact resistance (1-10)
         * @return This builder
         */
        public Builder impactResistance(int impactResistance) {
            this.impactResistance = Math.max(1, Math.min(10, impactResistance));
            return this;
        }
        
        /**
         * Sets whether the shield has radiation shielding.
         * @param radiationShielded True if radiation shielded
         * @return This builder
         */
        public Builder radiationShielded(boolean radiationShielded) {
            this.isRadiationShielded = radiationShielded;
            return this;
        }
        
        /**
         * Sets whether the shield has EMP shielding.
         * @param empShielded True if EMP shielded
         * @return This builder
         */
        public Builder empShielded(boolean empShielded) {
            this.isEMPShielded = empShielded;
            return this;
        }
        
        /**
         * Sets whether the shield has thermal shielding.
         * @param thermalShielded True if thermal shielded
         * @return This builder
         */
        public Builder thermalShielded(boolean thermalShielded) {
            this.isThermalShielded = thermalShielded;
            return this;
        }
        
        /**
         * Sets the shield type.
         * @param shieldType The shield type
         * @return This builder
         */
        public Builder shieldType(ShieldType shieldType) {
            this.shieldType = shieldType;
            return this;
        }
        
        /**
         * Builds the shield.
         * @return A new BaseShield
         */
        public BaseShield build() {
            return new BaseShield(this);
        }
    }
}