package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ILifeSupport;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket life support component.
 */
public class LifeSupportImpl implements ILifeSupport {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int supportCapacity;
    private final float oxygenGenerationRate;
    private final float waterRecyclingEfficiency;
    private final float foodProductionRate;
    private final float wasteProcessingEfficiency;
    private final float radiationFiltration;
    private final int backupDuration;
    private final LifeSupportType lifeSupportType;
    
    /**
     * Creates a new life support component.
     */
    public LifeSupportImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            int supportCapacity,
            float oxygenGenerationRate,
            float waterRecyclingEfficiency,
            float foodProductionRate,
            float wasteProcessingEfficiency,
            float radiationFiltration,
            int backupDuration,
            LifeSupportType lifeSupportType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.supportCapacity = supportCapacity;
        this.oxygenGenerationRate = oxygenGenerationRate;
        this.waterRecyclingEfficiency = waterRecyclingEfficiency;
        this.foodProductionRate = foodProductionRate;
        this.wasteProcessingEfficiency = wasteProcessingEfficiency;
        this.radiationFiltration = radiationFiltration;
        this.backupDuration = backupDuration;
        this.lifeSupportType = lifeSupportType;
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
    public int getSupportCapacity() {
        return supportCapacity;
    }

    @Override
    public float getOxygenGenerationRate() {
        return oxygenGenerationRate;
    }

    @Override
    public float getWaterRecyclingEfficiency() {
        return waterRecyclingEfficiency;
    }

    @Override
    public float getFoodProductionRate() {
        return foodProductionRate;
    }

    @Override
    public float getWasteProcessingEfficiency() {
        return wasteProcessingEfficiency;
    }

    @Override
    public float getRadiationFiltration() {
        return radiationFiltration;
    }

    @Override
    public LifeSupportType getLifeSupportType() {
        return lifeSupportType;
    }

    @Override
    public int getBackupDuration() {
        return backupDuration;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.life_support.type", lifeSupportType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.life_support.capacity", supportCapacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.oxygen", oxygenGenerationRate));
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.water", waterRecyclingEfficiency * 100));
            
            if (foodProductionRate > 0) {
                tooltip.add(Component.translatable("tooltip.galactic-space.life_support.food", foodProductionRate));
            }
            
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.waste", wasteProcessingEfficiency * 100));
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.radiation", radiationFiltration));
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.backup", backupDuration));
        }
        
        return tooltip;
    }
}