package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket command module.
 */
public class CommandModuleImpl implements ICommandModule {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int computingPower;
    private final int sensorStrength;
    private final float navigationAccuracy;
    private final int crewCapacity;
    private final boolean hasAdvancedLifeSupport;
    private final boolean hasAutomatedLanding;
    private final boolean hasEmergencyEvacuation;
    
    /**
     * Creates a new command module.
     */
    public CommandModuleImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int computingPower,
            int sensorStrength,
            float navigationAccuracy,
            int crewCapacity,
            boolean hasAdvancedLifeSupport,
            boolean hasAutomatedLanding,
            boolean hasEmergencyEvacuation) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.computingPower = computingPower;
        this.sensorStrength = sensorStrength;
        this.navigationAccuracy = navigationAccuracy;
        this.crewCapacity = crewCapacity;
        this.hasAdvancedLifeSupport = hasAdvancedLifeSupport;
        this.hasAutomatedLanding = hasAutomatedLanding;
        this.hasEmergencyEvacuation = hasEmergencyEvacuation;
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
        return RocketComponentType.COCKPIT;
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
    public int getComputingPower() {
        return computingPower;
    }
    
    @Override
    public int getSensorStrength() {
        return sensorStrength;
    }
    
    @Override
    public float getNavigationAccuracy() {
        return navigationAccuracy;
    }
    
    @Override
    public int getCrewCapacity() {
        return crewCapacity;
    }
    
    @Override
    public boolean hasAdvancedLifeSupport() {
        return hasAdvancedLifeSupport;
    }
    
    @Override
    public boolean hasAutomatedLanding() {
        return hasAutomatedLanding;
    }
    
    @Override
    public boolean hasEmergencyEvacuation() {
        return hasEmergencyEvacuation;
    }

    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.command_module.crew", crewCapacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.navigation", navigationAccuracy));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.computer", computingPower));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.sensors", sensorStrength));
            
            if (hasAdvancedLifeSupport) {
                tooltip.add(Component.translatable("tooltip.galactic-space.command_module.advanced_life_support"));
            }
            
            if (hasAutomatedLanding) {
                tooltip.add(Component.translatable("tooltip.galactic-space.command_module.automated_landing"));
            }
            
            if (hasEmergencyEvacuation) {
                tooltip.add(Component.translatable("tooltip.galactic-space.command_module.emergency_evacuation"));
            }
        }
        
        return tooltip;
    }
}