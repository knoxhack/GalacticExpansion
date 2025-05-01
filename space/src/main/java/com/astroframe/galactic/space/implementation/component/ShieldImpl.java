package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IShield;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.ShieldType;
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
    public int getImpactResistance() {
        return (int)(impactProtection / 10.0f); // Convert to 1-10 scale
    }

    @Override
    public boolean isRadiationShielded() {
        return radiationShielding > 50.0f; // Over 50% radiation protection means it's radiation shielded
    }

    @Override
    public boolean isEMPShielded() {
        // EMP shielding is provided by deflector and quantum shields
        return shieldType == ShieldType.DEFLECTOR || shieldType == ShieldType.QUANTUM;
    }

    @Override
    public boolean isThermalShielded() {
        return heatResistance > 50.0f; // Over 50% heat resistance means it's thermal shielded
    }

    @Override
    public float getMaxShieldStrength() {
        return 100.0f * (tier / 3.0f) * (coveragePercentage / 100.0f);
    }

    @Override
    public float getRegenerationRate() {
        return regenerative ? 5.0f * tier : 0.0f;
    }

    @Override
    public ShieldType getShieldType() {
        return shieldType;
    }

    /**
     * Gets the energy consumption of this shield.
     * @return The energy consumption
     */
    public int getEnergyConsumption() {
        return energyConsumption;
    }

    /**
     * Checks if this shield is regenerative.
     * @return True if regenerative
     */
    public boolean isRegenerative() {
        return regenerative;
    }

    /**
     * Gets the coverage percentage of this shield.
     * @return The coverage percentage
     */
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