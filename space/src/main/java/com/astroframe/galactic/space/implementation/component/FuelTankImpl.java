package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IFuelTank;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket fuel tank component.
 */
public class FuelTankImpl implements IFuelTank {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final float structuralIntegrity;
    private final int flowRate;
    private final boolean hasCryogenicStorage;
    private final float leakResistance;
    private final FuelType fuelType;
    private final int capacity;
    
    /**
     * Creates a new fuel tank.
     */
    public FuelTankImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            float structuralIntegrity,
            int flowRate,
            boolean hasCryogenicStorage,
            float leakResistance,
            FuelType fuelType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.structuralIntegrity = structuralIntegrity;
        this.flowRate = flowRate;
        this.hasCryogenicStorage = hasCryogenicStorage;
        this.leakResistance = leakResistance;
        this.fuelType = fuelType;
        
        // Calculate capacity based on tier and fuel type
        int baseCapacity = 500;
        float tierMultiplier = tier * 1.5f;
        float typeMultiplier = 1.0f;
        
        switch (fuelType) {
            case CHEMICAL:
                typeMultiplier = 1.0f;
                break;
            case HYDROGEN:
                typeMultiplier = 1.2f;
                break;
            case NUCLEAR:
                typeMultiplier = 1.5f;
                break;
            case ANTIMATTER:
                typeMultiplier = 2.0f;
                break;
            case EXOTIC:
                typeMultiplier = 3.0f;
                break;
        }
        
        this.capacity = Math.round(baseCapacity * tierMultiplier * typeMultiplier);
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
    public int getCapacity() {
        return capacity;
    }

    @Override
    public float getStructuralIntegrity() {
        return structuralIntegrity;
    }

    @Override
    public FuelType getFuelType() {
        return fuelType;
    }

    @Override
    public int getFlowRate() {
        return flowRate;
    }

    @Override
    public boolean hasCryogenicStorage() {
        return hasCryogenicStorage;
    }

    @Override
    public float getLeakResistance() {
        return leakResistance;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.type", fuelType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.capacity", capacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.structural_integrity", structuralIntegrity));
            tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.flow_rate", flowRate));
            tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.leak_resistance", leakResistance));
            
            if (hasCryogenicStorage) {
                tooltip.add(Component.translatable("tooltip.galactic-space.fuel_tank.cryogenic"));
            }
        }
        
        return tooltip;
    }
}