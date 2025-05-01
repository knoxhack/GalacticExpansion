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

    // Implementation of ILifeSupport.getId
    public ResourceLocation getId() {
        return id;
    }

    // Implementation of ILifeSupport.getDisplayName
    public Component getDisplayName() {
        return displayName;
    }

    // Implementation of ILifeSupport.getTier
    public int getTier() {
        return tier;
    }
    
    // Implementation of ILifeSupport.getType
    public ComponentType getType() {
        return ComponentType.LIFE_SUPPORT;
    }

    // Implementation of ILifeSupport.getMass
    public int getMass() {
        return mass;
    }

    // Implementation of ILifeSupport.getMaxHealth
    public float getMaxHealth() {
        return maxHealth;
    }

    // Implementation of ILifeSupport.getMaxCrewCapacity
    public int getMaxCrewCapacity() {
        return maxCrewCapacity;
    }

    // Implementation of ILifeSupport.getOxygenEfficiency
    public float getOxygenEfficiency() {
        return oxygenEfficiency;
    }

    // Implementation of ILifeSupport.getWaterRecyclingRate
    public float getWaterRecyclingRate() {
        return waterRecyclingRate;
    }

    // Implementation of ILifeSupport.hasAdvancedMedical
    public boolean hasAdvancedMedical() {
        return hasAdvancedMedical;
    }

    // Implementation of ILifeSupport.hasRadiationScrubbers
    public boolean hasRadiationScrubbers() {
        return hasRadiationScrubbers;
    }

    // Implementation from interface
    public LifeSupportType getLifeSupportType() {
        return lifeSupportType;
    }

    // Implementation from interface
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