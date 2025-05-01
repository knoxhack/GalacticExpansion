package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket cargo bay component.
 */
public class CargoBayImpl implements ICargoBay {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int slotCount;
    private final int weightCapacity;
    private final boolean hasAutomaticSorting;
    private final boolean hasVacuumSealing;
    private final float accessSpeed;
    private final CargoBayType cargoType;
    
    /**
     * Creates a new cargo bay.
     */
    public CargoBayImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            int slotCount,
            int weightCapacity,
            boolean hasAutomaticSorting,
            boolean hasVacuumSealing,
            float accessSpeed,
            CargoBayType cargoType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.slotCount = slotCount;
        this.weightCapacity = weightCapacity;
        this.hasAutomaticSorting = hasAutomaticSorting;
        this.hasVacuumSealing = hasVacuumSealing;
        this.accessSpeed = accessSpeed;
        this.cargoType = cargoType;
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
    public int getSlotCount() {
        return slotCount;
    }

    @Override
    public int getWeightCapacity() {
        return weightCapacity;
    }

    @Override
    public boolean hasAutomaticSorting() {
        return hasAutomaticSorting;
    }

    @Override
    public boolean hasVacuumSealing() {
        return hasVacuumSealing;
    }

    @Override
    public CargoBayType getCargoType() {
        return cargoType;
    }

    @Override
    public float getAccessSpeed() {
        return accessSpeed;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.type", cargoType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.slots", slotCount));
        tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.capacity", weightCapacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.access_speed", accessSpeed));
            
            if (hasAutomaticSorting) {
                tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.automatic_sorting"));
            }
            
            if (hasVacuumSealing) {
                tooltip.add(Component.translatable("tooltip.galactic-space.cargo_bay.vacuum_sealing"));
            }
        }
        
        return tooltip;
    }
}