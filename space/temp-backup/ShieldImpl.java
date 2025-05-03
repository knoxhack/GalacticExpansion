package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket shield component.
 */
public class ShieldImpl implements IShield {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int impactResistance;
    private final int heatResistance;
    private final int radiationResistance;
    private final int maxShieldStrength;
    private int currentShieldStrength;
    private boolean active = false;
    
    /**
     * Creates a new shield component.
     */
    public ShieldImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int impactResistance,
            int heatResistance,
            int radiationResistance,
            int maxShieldStrength) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.impactResistance = impactResistance;
        this.heatResistance = heatResistance;
        this.radiationResistance = radiationResistance;
        this.maxShieldStrength = maxShieldStrength;
        this.currentShieldStrength = maxShieldStrength;
    }
    
    /**
     * Creates a new shield component from Component display name, float values and shield type.
     */
    public ShieldImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxDurability,
            float impactResistance,
            float heatResistance,
            float radiationResistance,
            int maxShieldStrength,
            boolean meteorResistance,
            float regenerationRate,
            com.astroframe.galactic.core.api.space.component.enums.ShieldType shieldType) {
        this.id = id;
        this.name = displayName.getString();
        this.description = "Advanced shield with " + shieldType.name().toLowerCase() + " technology.";
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = (int)maxDurability;
        this.currentDurability = this.maxDurability;
        this.impactResistance = (int)impactResistance;
        this.heatResistance = (int)heatResistance;
        this.radiationResistance = (int)radiationResistance;
        this.maxShieldStrength = maxShieldStrength;
        this.currentShieldStrength = maxShieldStrength;
        // meteorResistance and regenerationRate are ignored in this implementation
        // shieldType is only used for the description
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
    }
    
    @Override
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
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
    public int getMaxShieldStrength() {
        return maxShieldStrength;
    }
    
    @Override
    public int getCurrentShieldStrength() {
        return currentShieldStrength;
    }
    
    @Override
    public int applyDamage(int amount) {
        if (!active) {
            return amount;
        }
        
        // Apply damage to shield
        int absorbedDamage = Math.min(currentShieldStrength, amount);
        currentShieldStrength -= absorbedDamage;
        
        // Return penetrating damage
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
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.shield.strength", maxShieldStrength));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.impact", impactResistance));
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.heat", heatResistance));
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.radiation", radiationResistance));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for ShieldImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Shield";
        private String description = "A shield component for protection.";
        private int tier = 1;
        private int mass = 200;
        private int maxDurability = 1000;
        private int impactResistance = 5;
        private int heatResistance = 10;
        private int radiationResistance = 5;
        private int maxShieldStrength = 100;
        private boolean meteorResistance = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
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
         * Sets the impact resistance.
         * @param impactResistance The impact resistance
         * @return This builder
         */
        public Builder impactResistance(int impactResistance) {
            this.impactResistance = impactResistance;
            return this;
        }
        
        /**
         * Sets the heat resistance.
         * @param heatResistance The heat resistance
         * @return This builder
         */
        public Builder heatResistance(int heatResistance) {
            this.heatResistance = heatResistance;
            return this;
        }
        
        /**
         * Sets the radiation resistance.
         * @param radiationResistance The radiation resistance
         * @return This builder
         */
        public Builder radiationResistance(int radiationResistance) {
            this.radiationResistance = radiationResistance;
            return this;
        }
        
        /**
         * Sets the max shield strength.
         * @param shieldStrength The max shield strength
         * @return This builder
         */
        public Builder shieldStrength(int shieldStrength) {
            this.maxShieldStrength = shieldStrength;
            return this;
        }
        
        /**
         * Sets whether this shield has meteor resistance.
         * @param meteorResistance True if has meteor resistance
         * @return This builder
         */
        public Builder meteorResistance(boolean meteorResistance) {
            this.meteorResistance = meteorResistance;
            return this;
        }
        
        /**
         * Builds the shield.
         * @return A new ShieldImpl
         */
        public ShieldImpl build() {
            return new ShieldImpl(
                id, name, description, tier, mass, maxDurability,
                impactResistance, heatResistance, radiationResistance,
                maxShieldStrength
            );
        }
    }
}