package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.enums.CommandModuleType;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of the ICommandModule interface.
 */
public class BaseCommandModule implements ICommandModule {
    
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final int navigationLevel;
    private final int crewCapacity;
    private final int computingPower;
    private final int communicationRange;
    private final CommandModuleType commandModuleType;
    
    private BaseCommandModule(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxHealth = builder.maxHealth;
        this.navigationLevel = builder.navigationLevel;
        this.crewCapacity = builder.crewCapacity;
        this.computingPower = builder.computingPower;
        this.communicationRange = builder.communicationRange;
        this.commandModuleType = builder.commandModuleType;
    }
    
    public ResourceLocation getId() {
        return id;
    }
    
    public Component getDisplayName() {
        return displayName;
    }
    
    public int getTier() {
        return tier;
    }
    
    public com.astroframe.galactic.core.api.space.component.RocketComponentType getType() {
        return com.astroframe.galactic.core.api.space.component.RocketComponentType.COCKPIT;
    }
    
    public int getMass() {
        return mass;
    }
    
    public float getMaxHealth() {
        return maxHealth;
    }
    
    // This is a non-interfaced method for backward compatibility
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(displayName);
        tooltip.add(Component.literal("Type: " + commandModuleType.getDisplayName()));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Navigation: " + navigationLevel + "/10"));
            tooltip.add(Component.literal("Computing: " + computingPower + "/10"));
            tooltip.add(Component.literal("Crew Capacity: " + crewCapacity));
            tooltip.add(Component.literal("Communication Range: " + communicationRange));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Max Health: " + maxHealth));
        }
        
        return tooltip;
    }
    
    public int getNavigationLevel() {
        return navigationLevel;
    }
    
    public int getCrewCapacity() {
        return crewCapacity;
    }
    
    public int getComputingPower() {
        return computingPower;
    }
    
    public int getCommunicationRange() {
        return communicationRange;
    }
    
    public CommandModuleType getCommandModuleType() {
        return commandModuleType;
    }
    
    // Additional method implementations for ICommandModule
    public int getSensorStrength() {
        return (tier * 3) + navigationLevel; // Derived from tier and navigation level
    }
    
    public float getNavigationAccuracy() {
        return 0.7f + (tier * 0.1f); // Tier-based navigation accuracy
    }
    
    public boolean hasAdvancedLifeSupport() {
        return tier >= 2; // Only tier 2+ has advanced life support
    }
    
    public boolean hasAutomatedLanding() {
        return tier >= 2; // Only tier 2+ has automated landing
    }
    
    public boolean hasEmergencyEvacuation() {
        return tier >= 3; // Only tier 3 has emergency evacuation
    }
    
    // Required implementation of IRocketComponent methods
    public int getMaxDurability() {
        return (int)maxHealth;
    }
    
    public int getCurrentDurability() {
        return (int)maxHealth; // Currently always at max
    }
    
    public void damage(int amount) {
        // No-op until we implement durability
    }
    
    public boolean isBroken() {
        return false; // Default implementation always returns false until we implement durability
    }
    
    public void repair(int amount) {
        // No-op until we implement durability
    }
    
    public String getName() {
        return displayName.getString();
    }
    
    public String getDescription() {
        return "Command Module - " + commandModuleType.getDisplayName() + " - Tier " + tier;
    }
    
    /**
     * Builder for BaseCommandModule.
     */
    public static class Builder {
        private final ResourceLocation id;
        private final Component displayName;
        private int tier = 1;
        private int mass = 500;
        private float maxHealth = 100.0f;
        private int navigationLevel = 1;
        private int crewCapacity = 1;
        private int computingPower = 1;
        private int communicationRange = 100;
        private CommandModuleType commandModuleType = CommandModuleType.BASIC;
        
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
         * Sets the navigation level.
         * @param navigationLevel The navigation level (1-10)
         * @return This builder
         */
        public Builder navigationLevel(int navigationLevel) {
            this.navigationLevel = Math.max(1, Math.min(10, navigationLevel));
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
         * Sets the computing power.
         * @param computingPower The computing power (1-10)
         * @return This builder
         */
        public Builder computingPower(int computingPower) {
            this.computingPower = Math.max(1, Math.min(10, computingPower));
            return this;
        }
        
        /**
         * Sets the communication range.
         * @param communicationRange The communication range
         * @return This builder
         */
        public Builder communicationRange(int communicationRange) {
            this.communicationRange = communicationRange;
            return this;
        }
        
        /**
         * Sets the command module type.
         * @param commandModuleType The command module type
         * @return This builder
         */
        public Builder commandModuleType(CommandModuleType commandModuleType) {
            this.commandModuleType = commandModuleType;
            return this;
        }
        
        /**
         * Builds the command module.
         * @return A new BaseCommandModule
         */
        public BaseCommandModule build() {
            return new BaseCommandModule(this);
        }
    }
}