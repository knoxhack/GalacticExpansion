package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket shield component.
 */
public class ShieldImpl implements IShield {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final float impactProtection;
    private final float heatResistance;
    private final float radiationShielding;
    private final int energyConsumption;
    private final boolean regenerative;
    private final float coveragePercentage;
    private final ShieldType shieldType;
    
    /**
     * Creates a new shield component.
     */
    public ShieldImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            float impactProtection,
            float heatResistance,
            float radiationShielding,
            int energyConsumption,
            boolean regenerative,
            float coveragePercentage,
            ShieldType shieldType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.impactProtection = impactProtection;
        this.heatResistance = heatResistance;
        this.radiationShielding = radiationShielding;
        this.energyConsumption = energyConsumption;
        this.regenerative = regenerative;
        this.coveragePercentage = coveragePercentage;
        this.shieldType = shieldType;
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
    public int getMass() {
        return mass;
    }

    @Override
    public float getMaxHealth() {
        return maxHealth;
    }

    @Override
    public float getImpactProtection() {
        return impactProtection;
    }

    @Override
    public float getHeatResistance() {
        return heatResistance;
    }

    @Override
    public float getRadiationShielding() {
        return radiationShielding;
    }

    @Override
    public ShieldType getShieldType() {
        return shieldType;
    }

    @Override
    public int getEnergyConsumption() {
        return energyConsumption;
    }

    @Override
    public boolean isRegenerative() {
        return regenerative;
    }

    @Override
    public float getCoveragePercentage() {
        return coveragePercentage;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.shield.type", shieldType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.shield.coverage", coveragePercentage));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.impact", impactProtection));
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.heat", heatResistance));
            tooltip.add(Component.translatable("tooltip.galactic-space.shield.radiation", radiationShielding));
            
            if (energyConsumption > 0) {
                tooltip.add(Component.translatable("tooltip.galactic-space.shield.energy", energyConsumption));
            }
            
            if (regenerative) {
                tooltip.add(Component.translatable("tooltip.galactic-space.shield.regenerative"));
            }
        }
        
        return tooltip;
    }
}