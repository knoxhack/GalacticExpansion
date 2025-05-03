package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * Controller for launching rockets into space.
 */
public class RocketLaunchController {

    private final ServerPlayer player;
    private final IRocket rocket;
    private boolean launchInProgress = false;
    private int launchCountdown = -1;
    private Component cannotLaunchReason = null;
    
    /**
     * Create a new rocket launch controller.
     *
     * @param player The player launching the rocket
     * @param rocket The rocket being launched
     */
    public RocketLaunchController(ServerPlayer player, IRocket rocket) {
        this.player = player;
        this.rocket = rocket;
    }
    
    /**
     * Check if the rocket can be launched.
     *
     * @return true if the rocket can be launched
     */
    public boolean canLaunch() {
        // Already launching
        if (launchInProgress) {
            cannotLaunchReason = Component.translatable("message.galactic-space.launch_already_in_progress");
            return false;
        }
        
        // Check if player is in a valid position to launch
        if (!isValidLaunchPosition()) {
            cannotLaunchReason = Component.translatable("message.galactic-space.invalid_launch_position");
            return false;
        }
        
        // Check if the rocket is valid for launch
        if (!rocket.isValid()) {
            cannotLaunchReason = Component.translatable("message.galactic-space.invalid_rocket_configuration");
            return false;
        }
        
        // Check if the rocket has sufficient fuel
        if (rocket.getFuelLevel() < getRocketMinimumFuelRequired()) {
            cannotLaunchReason = Component.translatable("message.galactic-space.insufficient_fuel");
            return false;
        }
        
        // Check for minimum component requirements
        if (!hasRequiredComponents()) {
            cannotLaunchReason = Component.translatable("message.galactic-space.missing_required_components");
            return false;
        }
        
        // All checks passed
        cannotLaunchReason = null;
        return true;
    }
    
    /**
     * Get the reason why the rocket cannot be launched.
     *
     * @return The reason component
     */
    public Component getCannotLaunchReason() {
        return cannotLaunchReason != null ? 
                cannotLaunchReason : 
                Component.translatable("message.galactic-space.unknown_launch_error");
    }
    
    /**
     * Start the launch sequence.
     */
    public void startLaunchSequence() {
        if (!canLaunch()) {
            return;
        }
        
        launchInProgress = true;
        launchCountdown = 5; // 5 second countdown
        
        // Send initial countdown message
        player.displayClientMessage(
                Component.translatable("message.galactic-space.launch_countdown_started"), 
                false);
        
        // In a real implementation, we would schedule tasks for the countdown
        // and actual launch. For now, we'll just simulate completing the launch.
        completeLaunch();
    }
    
    /**
     * Complete the launch process.
     */
    private void completeLaunch() {
        // In a real implementation, this would be called after the countdown completes
        
        // Consume fuel
        rocket.setFuelLevel(rocket.getFuelLevel() - getRocketFuelConsumption());
        
        // Apply wear to components
        applyComponentWear();
        
        // Send success message
        player.displayClientMessage(
                Component.translatable("message.galactic-space.launch_successful"), 
                false);
        
        // Log the launch
        GalacticSpace.LOGGER.info("Player {} launched a Tier {} rocket", 
                player.getName().getString(), 
                rocket.getTier());
        
        // Reset launch state
        launchInProgress = false;
        launchCountdown = -1;
    }
    
    /**
     * Checks if the player is in a valid position for launching.
     *
     * @return true if the position is valid
     */
    private boolean isValidLaunchPosition() {
        // For now, just check if they're on the ground
        return !player.isInWater() && !player.isInLava() && player.isOnGround();
    }
    
    /**
     * Get the minimum fuel required for launch.
     *
     * @return The minimum fuel amount
     */
    private int getRocketMinimumFuelRequired() {
        return 100; // Basic requirement
    }
    
    /**
     * Get the fuel consumed during launch.
     *
     * @return The fuel consumption amount
     */
    private int getRocketFuelConsumption() {
        return 50; // Basic consumption
    }
    
    /**
     * Checks if the rocket has all required components.
     *
     * @return true if all required components are present
     */
    private boolean hasRequiredComponents() {
        // Must have at minimum an engine, cockpit and fuel tank
        return rocket.hasComponent(RocketComponentType.ENGINE) && 
               rocket.hasComponent(RocketComponentType.COCKPIT) && 
               rocket.hasComponent(RocketComponentType.FUEL_TANK);
    }
    
    /**
     * Apply wear to rocket components from launch.
     */
    private void applyComponentWear() {
        // Apply wear to components based on their type
        rocket.getComponents().forEach(component -> {
            int wear = switch (component.getType()) {
                case ENGINE -> 10; // Engines wear the most during launch
                case FUEL_TANK -> 5;
                default -> 2; // Other components wear less
            };
            
            component.damage(wear);
        });
    }
}