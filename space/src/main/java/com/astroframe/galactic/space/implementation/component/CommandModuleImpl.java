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
    private CommandModuleImpl(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = maxDurability;
        this.computingPower = builder.computingPower;
        this.sensorStrength = builder.sensorStrength;
        this.navigationAccuracy = builder.navigationAccuracy;
        this.crewCapacity = builder.crewCapacity;
        this.hasAdvancedLifeSupport = builder.hasAdvancedLifeSupport;
        this.hasAutomatedLanding = builder.hasAutomatedLanding;
        this.hasEmergencyEvacuation = builder.hasEmergencyEvacuation;
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
        
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Crew Capacity: " + crewCapacity));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Navigation Accuracy: " + Math.round(navigationAccuracy * 100) + "%"));
            tooltip.add(Component.literal("Computing Power: " + computingPower));
            tooltip.add(Component.literal("Sensor Strength: " + sensorStrength));
            tooltip.add(Component.literal("Advanced Life Support: " + (hasAdvancedLifeSupport ? "Yes" : "No")));
            tooltip.add(Component.literal("Automated Landing: " + (hasAutomatedLanding ? "Yes" : "No")));
            tooltip.add(Component.literal("Emergency Evacuation: " + (hasEmergencyEvacuation ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + currentDurability + " / " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for CommandModuleImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Command Module";
        private String description = "A command module for controlling the rocket.";
        private int tier = 1;
        private int mass = 400;
        private int maxDurability = 100;
        private int computingPower = 100;
        private int sensorStrength = 100;
        private float navigationAccuracy = 0.8f;
        private int crewCapacity = 2;
        private boolean hasAdvancedLifeSupport = false;
        private boolean hasAutomatedLanding = false;
        private boolean hasEmergencyEvacuation = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        /**
         * Sets the name.
         * @param name The name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Sets the description.
         * @param description The description
         * @return This builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
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
         * Sets the max durability.
         * @param maxDurability The max durability
         * @return This builder
         */
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }
        
        /**
         * Sets the computing power.
         * @param computingPower The computing power
         * @return This builder
         */
        public Builder computingPower(int computingPower) {
            this.computingPower = computingPower;
            return this;
        }
        
        /**
         * Sets the sensor strength.
         * @param sensorStrength The sensor strength
         * @return This builder
         */
        public Builder sensorStrength(int sensorStrength) {
            this.sensorStrength = sensorStrength;
            return this;
        }
        
        /**
         * Sets the navigation accuracy.
         * @param navigationAccuracy The navigation accuracy (0.0-1.0)
         * @return This builder
         */
        public Builder navigationAccuracy(float navigationAccuracy) {
            this.navigationAccuracy = Math.max(0.0f, Math.min(1.0f, navigationAccuracy));
            return this;
        }
        
        /**
         * Sets the crew capacity.
         * @param crewCapacity The crew capacity
         * @return This builder
         */
        public Builder crewCapacity(int crewCapacity) {
            this.crewCapacity = crewCapacity;
            return this;
        }
        
        /**
         * Sets whether this module has advanced life support.
         * @param hasAdvancedLifeSupport True if has advanced life support
         * @return This builder
         */
        public Builder advancedLifeSupport(boolean hasAdvancedLifeSupport) {
            this.hasAdvancedLifeSupport = hasAdvancedLifeSupport;
            return this;
        }
        
        /**
         * Sets whether this module has automated landing.
         * @param hasAutomatedLanding True if has automated landing
         * @return This builder
         */
        public Builder automatedLanding(boolean hasAutomatedLanding) {
            this.hasAutomatedLanding = hasAutomatedLanding;
            return this;
        }
        
        /**
         * Sets whether this module has emergency evacuation.
         * @param hasEmergencyEvacuation True if has emergency evacuation
         * @return This builder
         */
        public Builder emergencyEvacuation(boolean hasEmergencyEvacuation) {
            this.hasEmergencyEvacuation = hasEmergencyEvacuation;
            return this;
        }
        
        /**
         * Builds the command module.
         * @return A new CommandModuleImpl
         */
        public CommandModuleImpl build() {
            return new CommandModuleImpl(this);
        }
    }
}