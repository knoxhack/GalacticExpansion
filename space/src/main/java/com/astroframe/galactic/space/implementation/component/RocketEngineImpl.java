package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.EngineType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket engine component.
 */
public class RocketEngineImpl implements IRocketEngine {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int thrust;
    private final float efficiency;
    private final int heatCapacity;
    private final boolean canOperateUnderwater;
    private final boolean canOperateInAtmosphere;
    private final boolean canOperateInSpace;
    private final EngineType engineType;
    
    /**
     * Creates a new rocket engine.
     */
    public RocketEngineImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            int thrust,
            float efficiency,
            int heatCapacity,
            boolean canOperateUnderwater,
            boolean canOperateInAtmosphere,
            boolean canOperateInSpace,
            EngineType engineType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.thrust = thrust;
        this.efficiency = efficiency;
        this.heatCapacity = heatCapacity;
        this.canOperateUnderwater = canOperateUnderwater;
        this.canOperateInAtmosphere = canOperateInAtmosphere;
        this.canOperateInSpace = canOperateInSpace;
        this.engineType = engineType;
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
    public ComponentType getType() {
        return ComponentType.ENGINE;
    }

    @Override
    public int getThrust() {
        return thrust;
    }

    @Override
    public float getEfficiency() {
        return efficiency;
    }

    @Override
    public EngineType getEngineType() {
        return engineType;
    }

    @Override
    public int getHeatCapacity() {
        return heatCapacity;
    }

    @Override
    public boolean canOperateUnderwater() {
        return canOperateUnderwater;
    }

    @Override
    public boolean canOperateInAtmosphere() {
        return canOperateInAtmosphere;
    }

    @Override
    public boolean canOperateInSpace() {
        return canOperateInSpace;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.engine.type", engineType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.engine.thrust", thrust));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.engine.efficiency", efficiency));
            tooltip.add(Component.translatable("tooltip.galactic-space.engine.heat_capacity", heatCapacity));
            
            if (canOperateInAtmosphere) {
                tooltip.add(Component.translatable("tooltip.galactic-space.engine.atmosphere"));
            }
            
            if (canOperateInSpace) {
                tooltip.add(Component.translatable("tooltip.galactic-space.engine.space"));
            }
            
            if (canOperateUnderwater) {
                tooltip.add(Component.translatable("tooltip.galactic-space.engine.underwater"));
            }
        }
        
        return tooltip;
    }
}