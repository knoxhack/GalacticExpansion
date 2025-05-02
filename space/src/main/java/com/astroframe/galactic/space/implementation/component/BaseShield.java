package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IShield interface.
 */
public class BaseShield implements IShield {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int maxShieldStrength;
    private int currentShieldStrength;
    private final int impactResistance;
    private final int heatResistance;
    private final int radiationResistance;
    private boolean active = false;
    
    private BaseShield(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = maxDurability;
        this.maxShieldStrength = builder.maxShieldStrength;
        this.currentShieldStrength = maxShieldStrength;
        this.impactResistance = builder.impactResistance;
        this.heatResistance = builder.heatResistance;
        this.radiationResistance = builder.radiationResistance;
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
        return RocketComponentType.SHIELDING;
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
        if (currentDurability == 0) {
            setActive(false);
        }
    }
    
    @Override
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
    }
    
    @Override
    public int getMaxShieldStrength() {
        return maxShieldStrength;
    }
    
    @Override
    public int getCurrentShieldStrength() {
        return currentShieldStrength;
    }
    
    @Override
    public int getImpactResistance() {
        return impactResistance;
    }
    
    @Override
    public int getHeatResistance() {
        return heatResistance;
    }
    
    @Override
    public int getRadiationResistance() {
        return radiationResistance;
    }
    
    @Override
    public int applyDamage(int amount) {
        if (!active || isBroken()) {
            return amount;
        }
        
        int absorbedDamage = Math.min(currentShieldStrength, amount);
        currentShieldStrength -= absorbedDamage;
        
        return amount - absorbedDamage;
    }
    
    @Override
    public int regenerate(int amount) {
        if (!active || isBroken()) {
            return 0;
        }
        
        int regenerationAmount = Math.min(maxShieldStrength - currentShieldStrength, amount);
        currentShieldStrength += regenerationAmount;
        return regenerationAmount;
    }
    
    @Override
    public boolean isActive() {
        return active && !isBroken();
    }
    
    @Override
    public void setActive(boolean active) {
        this.active = active && !isBroken();
    }
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Strength: " + maxShieldStrength));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Impact Resistance: " + impactResistance + "/10"));
            tooltip.add(Component.literal("Heat Resistance: " + heatResistance + "/10"));
            tooltip.add(Component.literal("Radiation Resistance: " + radiationResistance + "/10"));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
            tooltip.add(Component.literal("Active: " + (active ? "Yes" : "No")));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for BaseShield.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Shield";
        private String description = "A shield component for a rocket.";
        private int tier = 1;
        private int mass = 250;
        private int maxDurability = 100;
        private int maxShieldStrength = 100;
        private int impactResistance = 5;
        private int heatResistance = 5;
        private int radiationResistance = 5;
        
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
         * Sets the max shield strength.
         * @param maxShieldStrength The max shield strength
         * @return This builder
         */
        public Builder maxShieldStrength(int maxShieldStrength) {
            this.maxShieldStrength = maxShieldStrength;
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
         * Sets the heat resistance.
         * @param heatResistance The heat resistance (1-10)
         * @return This builder
         */
        public Builder heatResistance(int heatResistance) {
            this.heatResistance = Math.max(1, Math.min(10, heatResistance));
            return this;
        }
        
        /**
         * Sets the radiation resistance.
         * @param radiationResistance The radiation resistance (1-10)
         * @return This builder
         */
        public Builder radiationResistance(int radiationResistance) {
            this.radiationResistance = Math.max(1, Math.min(10, radiationResistance));
            return this;
        }
        
        /**
         * Sets the shield's thermal shielding capability.
         * @param shielded True if the shield has thermal shielding
         * @return This builder
         */
        public Builder thermalShielded(boolean shielded) {
            // This is for categorization only
            return this;
        }
        
        /**
         * Sets the shield's radiation shielding capability.
         * @param shielded True if the shield has radiation shielding
         * @return This builder
         */
        public Builder radiationShielded(boolean shielded) {
            // This is for categorization only
            return this;
        }
        
        /**
         * Sets the shield's EMP shielding capability.
         * @param shielded True if the shield has EMP shielding
         * @return This builder
         */
        public Builder empShielded(boolean shielded) {
            // This is for categorization only
            return this;
        }
        
        /**
         * Sets the shield type (internal categorization).
         * @param type The shield type
         * @return This builder
         */
        public Builder shieldType(ShieldType type) {
            // This is for categorization only
            return this;
        }
        
        /**
         * Sets the shield's regeneration rate.
         * @param rate The regeneration rate
         * @return This builder
         */
        public Builder regenerationRate(float rate) {
            // For future implementation
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