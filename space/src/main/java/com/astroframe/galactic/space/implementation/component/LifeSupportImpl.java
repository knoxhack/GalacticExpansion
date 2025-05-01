package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ILifeSupport;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.LifeSupportType;
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
    private final int maxCrewCapacity;
    private final float oxygenEfficiency;
    private final float waterRecyclingRate;
    private final boolean hasAdvancedMedical;
    private final boolean hasRadiationScrubbers;
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
            int maxCrewCapacity,
            float oxygenEfficiency,
            float waterRecyclingRate,
            boolean hasAdvancedMedical,
            boolean hasRadiationScrubbers,
            LifeSupportType lifeSupportType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.maxCrewCapacity = maxCrewCapacity;
        this.oxygenEfficiency = oxygenEfficiency;
        this.waterRecyclingRate = waterRecyclingRate;
        this.hasAdvancedMedical = hasAdvancedMedical;
        this.hasRadiationScrubbers = hasRadiationScrubbers;
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
    public ComponentType getType() {
        return ComponentType.LIFE_SUPPORT;
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
    public int getMaxCrewCapacity() {
        return maxCrewCapacity;
    }

    @Override
    public float getOxygenEfficiency() {
        return oxygenEfficiency;
    }

    @Override
    public float getWaterRecyclingRate() {
        return waterRecyclingRate;
    }

    @Override
    public boolean hasAdvancedMedical() {
        return hasAdvancedMedical;
    }

    @Override
    public boolean hasRadiationScrubbers() {
        return hasRadiationScrubbers;
    }

    @Override
    public LifeSupportType getLifeSupportType() {
        return lifeSupportType;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.life_support.type", lifeSupportType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.life_support.capacity", maxCrewCapacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.oxygen", oxygenEfficiency * 100));
            tooltip.add(Component.translatable("tooltip.galactic-space.life_support.water", waterRecyclingRate * 100));
            
            if (hasAdvancedMedical) {
                tooltip.add(Component.translatable("tooltip.galactic-space.life_support.medical"));
            }
            
            if (hasRadiationScrubbers) {
                tooltip.add(Component.translatable("tooltip.galactic-space.life_support.radiation"));
            }
        }
        
        return tooltip;
    }
}