package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICommandModule;
import com.astroframe.galactic.core.api.space.component.enums.ComponentType;
import com.astroframe.galactic.core.api.space.component.enums.CommandModuleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of a rocket command module.
 */
public class CommandModuleImpl implements ICommandModule {
    private final ResourceLocation id;
    private final Component displayName;
    private final int tier;
    private final int mass;
    private final float maxHealth;
    private final float navigationAccuracy;
    private final int computerTier;
    private final int scanningRange;
    private final int autopilotLevel;
    private final boolean hasEmergencyTeleport;
    private final int basicLifeSupportCapacity;
    private final CommandModuleType commandModuleType;
    
    /**
     * Creates a new command module.
     */
    public CommandModuleImpl(
            ResourceLocation id,
            Component displayName,
            int tier,
            int mass,
            float maxHealth,
            float navigationAccuracy,
            int computerTier,
            int scanningRange,
            int autopilotLevel,
            boolean hasEmergencyTeleport,
            int basicLifeSupportCapacity,
            CommandModuleType commandModuleType) {
        this.id = id;
        this.displayName = displayName;
        this.tier = tier;
        this.mass = mass;
        this.maxHealth = maxHealth;
        this.navigationAccuracy = navigationAccuracy;
        this.computerTier = computerTier;
        this.scanningRange = scanningRange;
        this.autopilotLevel = autopilotLevel;
        this.hasEmergencyTeleport = hasEmergencyTeleport;
        this.basicLifeSupportCapacity = basicLifeSupportCapacity;
        this.commandModuleType = commandModuleType;
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
        return ComponentType.COMMAND_MODULE;
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
    public int getNavigationLevel() {
        return (int)(navigationAccuracy * 10); // Convert to 1-10 scale
    }
    
    @Override
    public int getCrewCapacity() {
        return basicLifeSupportCapacity; // Use the basic life support capacity as crew capacity
    }
    
    @Override
    public int getComputingPower() {
        return computerTier;
    }
    
    @Override
    public int getCommunicationRange() {
        return scanningRange; // Use scanning range as communication range
    }
    
    /**
     * Gets the autopilot level of this command module.
     * @return The autopilot level
     */
    public int getAutopilotLevel() {
        return autopilotLevel;
    }
    
    /**
     * Checks if this command module has emergency teleport.
     * @return True if emergency teleport is available
     */
    public boolean hasEmergencyTeleport() {
        return hasEmergencyTeleport;
    }

    @Override
    public CommandModuleType getCommandModuleType() {
        return commandModuleType;
    }

    @Override
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.translatable("tooltip.galactic-space.tier", tier));
        tooltip.add(Component.translatable("tooltip.galactic-space.command_module.type", commandModuleType.name()));
        
        if (detailed) {
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.navigation", navigationAccuracy));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.computer", computerTier));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.scanning", scanningRange));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.autopilot", autopilotLevel));
            tooltip.add(Component.translatable("tooltip.galactic-space.command_module.life_support", basicLifeSupportCapacity));
            
            if (hasEmergencyTeleport) {
                tooltip.add(Component.translatable("tooltip.galactic-space.command_module.emergency_teleport"));
            }
        }
        
        return tooltip;
    }
}