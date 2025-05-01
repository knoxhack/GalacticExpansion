package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.IPassengerCompartment;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.CompartmentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the IPassengerCompartment interface.
 */
public class BasePassengerCompartment implements IPassengerCompartment {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int passengerCapacity;
    private final boolean hasArtificialGravity;
    private final boolean hasSleepingQuarters;
    private final boolean hasEmergencyMedical;
    private final CompartmentType compartmentType;
    
    private BasePassengerCompartment(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.passengerCapacity = builder.passengerCapacity;
        this.hasArtificialGravity = builder.hasArtificialGravity;
        this.hasSleepingQuarters = builder.hasSleepingQuarters;
        this.hasEmergencyMedical = builder.hasEmergencyMedical;
        this.compartmentType = builder.compartmentType;
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
        return ComponentType.PASSENGER_COMPARTMENT;
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
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(displayName);
        tooltip.add(Component.literal("Type: " + compartmentType.getDisplayName()));
        tooltip.add(Component.literal("Passenger Capacity: " + passengerCapacity));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Artificial Gravity: " + (hasArtificialGravity ? "Yes" : "No")));
            tooltip.add(Component.literal("Sleeping Quarters: " + (hasSleepingQuarters ? "Yes" : "No")));
            tooltip.add(Component.literal("Emergency Medical: " + (hasEmergencyMedical ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    @Override
    public int getPassengerCapacity() {
        return passengerCapacity;
    }
    
    @Override
    public boolean hasArtificialGravity() {
        return hasArtificialGravity;
    }
    
    @Override
    public boolean hasSleepingQuarters() {
        return hasSleepingQuarters;
    }
    
    @Override
    public boolean hasEmergencyMedical() {
        return hasEmergencyMedical;
    }
    
    @Override
    public CompartmentType getCompartmentType() {
        return compartmentType;
    }
    
    /**
     * Builder for BasePassengerCompartment.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 400;
        private float maxHealth = 100.0f;
        private int passengerCapacity = 2;
        private boolean hasArtificialGravity = false;
        private boolean hasSleepingQuarters = false;
        private boolean hasEmergencyMedical = false;
        private CompartmentType compartmentType = CompartmentType.BASIC;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.displayName = displayName;
        }
        
        /**
         * Sets the tier.
         * @param tier The tier level (1-3)
         * @return This builder
         */
        public Builder tier(int tier) {
            this.tier = Math.max(1, Math.min(3, tier));
            return this;
        }
        
        /**
         * Sets the mass.
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the max health.
         * @param maxHealth The max health
         * @return This builder
         */
        public Builder maxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
            return this;
        }
        
        /**
         * Sets the passenger capacity.
         * @param passengerCapacity The passenger capacity
         * @return This builder
         */
        public Builder passengerCapacity(int passengerCapacity) {
            this.passengerCapacity = passengerCapacity;
            return this;
        }
        
        /**
         * Sets whether the compartment has artificial gravity.
         * @param hasArtificialGravity True if has artificial gravity
         * @return This builder
         */
        public Builder artificialGravity(boolean hasArtificialGravity) {
            this.hasArtificialGravity = hasArtificialGravity;
            return this;
        }
        
        /**
         * Sets whether the compartment has sleeping quarters.
         * @param hasSleepingQuarters True if has sleeping quarters
         * @return This builder
         */
        public Builder sleepingQuarters(boolean hasSleepingQuarters) {
            this.hasSleepingQuarters = hasSleepingQuarters;
            return this;
        }
        
        /**
         * Sets whether the compartment has emergency medical facilities.
         * @param hasEmergencyMedical True if has emergency medical
         * @return This builder
         */
        public Builder emergencyMedical(boolean hasEmergencyMedical) {
            this.hasEmergencyMedical = hasEmergencyMedical;
            return this;
        }
        
        /**
         * Sets the compartment type.
         * @param compartmentType The compartment type
         * @return This builder
         */
        public Builder compartmentType(CompartmentType compartmentType) {
            this.compartmentType = compartmentType;
            return this;
        }
        
        /**
         * Builds the passenger compartment.
         * @return A new BasePassengerCompartment
         */
        public BasePassengerCompartment build() {
            return new BasePassengerCompartment(this);
        }
    }
}