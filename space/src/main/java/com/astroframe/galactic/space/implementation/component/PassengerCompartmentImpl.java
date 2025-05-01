package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IPassengerCompartment;
import com.astroframe.galactic.core.api.space.component.enums.CompartmentType;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket passenger compartment component.
 */
public class PassengerCompartmentImpl implements IPassengerCompartment {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int passengerCapacity;
    private final float comfortLevel;
    private final boolean hasCryogenicSleep;
    private final float radiationProtection;
    private final int emergencySupportDuration;
    private final CompartmentType compartmentType;
    
    /**
     * Creates a new passenger compartment.
     */
    public PassengerCompartmentImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            int passengerCapacity,
            float comfortLevel,
            boolean hasCryogenicSleep,
            float radiationProtection,
            int emergencySupportDuration,
            CompartmentType compartmentType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.passengerCapacity = passengerCapacity;
        this.comfortLevel = comfortLevel;
        this.hasCryogenicSleep = hasCryogenicSleep;
        this.radiationProtection = radiationProtection;
        this.emergencySupportDuration = emergencySupportDuration;
        this.compartmentType = compartmentType;
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
    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.PASSENGER_COMPARTMENT;
    }
    
    @Override
    public boolean hasArtificialGravity() {
        // Tier 3 compartments have artificial gravity
        return tier >= 3;
    }
    
    @Override
    public boolean hasSleepingQuarters() {
        // All compartments except emergency ones have sleeping quarters
        return compartmentType != CompartmentType.EMERGENCY;
    }
    
    @Override
    public boolean hasEmergencyMedical() {
        // Medical and luxury compartments have emergency medical
        return compartmentType == CompartmentType.MEDICAL || 
               compartmentType == CompartmentType.LUXURY;
    }
    
    @Override
    public CompartmentType getCompartmentType() {
        return compartmentType;
    }
    
    @Override
    public boolean hasCryogenicSleep() {
        return hasCryogenicSleep;
    }

    @Override
    public float getRadiationProtection() {
        return radiationProtection;
    }

    @Override
    public int getEmergencySupportDuration() {
        return emergencySupportDuration;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.type", compartmentType.name()));
        tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.capacity", passengerCapacity));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.comfort", comfortLevel));
            tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.radiation", radiationProtection));
            tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.emergency", emergencySupportDuration));
            
            if (hasCryogenicSleep) {
                tooltip.add(Component.translatable("tooltip.galactic-space.passenger_compartment.cryogenic"));
            }
        }
        
        return tooltip;
    }
}