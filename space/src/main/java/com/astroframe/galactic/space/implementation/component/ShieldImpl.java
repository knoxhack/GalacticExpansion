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
}