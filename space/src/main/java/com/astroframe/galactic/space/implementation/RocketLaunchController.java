package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.IRocketEngine;
import com.astroframe.galactic.space.GalacticSpace;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for rocket launches.
 * This class handles the launch sequence and coordinates with the SpaceTravelManager.
 */
public class RocketLaunchController {
    // Map to track which players are in a launch sequence
    private static final Map<UUID, LaunchState> playerLaunchStates = new HashMap<>();
    
    // Launch sequence timing (in ticks)
    private static final int COUNTDOWN_TIME = 100; // 5 seconds (20 ticks per second)
    private static final int LAUNCH_TIME = 60;     // 3 seconds
    private static final int TRAVEL_TIME = 200;    // 10 seconds
    
    /**
     * Initiates a launch sequence for a player.
     *
     * @param player The player
     * @param rocket The rocket being launched
     * @param destination The destination celestial body
     * @return true if the launch sequence was started
     */
    public static boolean startLaunch(ServerPlayer player, ModularRocket rocket, ICelestialBody destination) {
        // Check if player is already in a launch
        if (playerLaunchStates.containsKey(player.getUUID())) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.already_launching")
                                     .withStyle(ChatFormatting.RED));
            return false;
        }
        
        // Check if the rocket can reach the destination
        if (!canReachDestination(rocket, destination)) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.rocket_insufficient")
                                     .withStyle(ChatFormatting.RED));
            return false;
        }
        
        // Check if player has necessary equipment (spacesuit)
        if (!hasRequiredEquipment(player, destination)) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.missing_equipment")
                                     .withStyle(ChatFormatting.RED));
            return false;
        }
        
        // Start launch sequence
        playerLaunchStates.put(player.getUUID(), new LaunchState(rocket, destination, COUNTDOWN_TIME));
        
        // Send initial message
        player.sendSystemMessage(Component.translatable("message.galactic-space.launch_sequence_started")
                               .withStyle(ChatFormatting.GREEN));
        
        // Set rocket status to launching
        if (rocket instanceof IRocket) {
            ((IRocket) rocket).setStatus(IRocket.RocketStatus.LAUNCHING);
        }
        
        return true;
    }
    
    /**
     * Updates the launch sequence for all players.
     * This should be called every server tick.
     */
    public static void updateLaunches() {
        playerLaunchStates.entrySet().removeIf(entry -> {
            UUID playerId = entry.getKey();
            LaunchState state = entry.getValue();
            
            // Get the player
            ServerPlayer player = GalacticSpace.getServer().getPlayerList().getPlayer(playerId);
            if (player == null) {
                // Player is offline, cancel launch
                return true;
            }
            
            // Update the launch state
            state.timeRemaining--;
            
            // Handle countdown messages
            if (state.phase == LaunchPhase.COUNTDOWN) {
                // Show countdown messages at specific intervals
                if (state.timeRemaining <= 60 && state.timeRemaining % 20 == 0) {
                    int seconds = state.timeRemaining / 20;
                    player.sendSystemMessage(Component.translatable("message.galactic-space.countdown", seconds)
                                           .withStyle(ChatFormatting.GOLD));
                }
                
                // Transition to launch phase when countdown completes
                if (state.timeRemaining <= 0) {
                    state.phase = LaunchPhase.LAUNCHING;
                    state.timeRemaining = LAUNCH_TIME;
                    player.sendSystemMessage(Component.translatable("message.galactic-space.launch_initiated")
                                           .withStyle(ChatFormatting.GOLD));
                    
                    // Set rocket status to in flight
                    if (state.rocket instanceof IRocket) {
                        ((IRocket) state.rocket).setStatus(IRocket.RocketStatus.IN_FLIGHT);
                    }
                    
                    // Play launch sound
                    player.playSound(net.minecraft.sounds.SoundEvents.GENERIC_EXPLODE.value(), 1.0F, 0.5F);
                }
            }
            // Handle launch phase
            else if (state.phase == LaunchPhase.LAUNCHING) {
                // Transition to travel phase when launch completes
                if (state.timeRemaining <= 0) {
                    state.phase = LaunchPhase.TRAVELING;
                    state.timeRemaining = TRAVEL_TIME;
                    player.sendSystemMessage(Component.translatable("message.galactic-space.traveling_to", 
                                           state.destination.getName())
                                           .withStyle(ChatFormatting.AQUA));
                }
            }
            // Handle travel phase
            else if (state.phase == LaunchPhase.TRAVELING) {
                // Complete travel when travel phase ends
                if (state.timeRemaining <= 0) {
                    // Complete the travel
                    completeTravel(player, state.rocket, state.destination);
                    
                    // Remove this launch state
                    return true;
                }
            }
            
            return false;
        });
    }
    
    /**
     * Completes travel to the destination.
     *
     * @param player The player
     * @param rocket The rocket
     * @param destination The destination
     */
    private static void completeTravel(ServerPlayer player, ModularRocket rocket, ICelestialBody destination) {
        // Set rocket status to landing
        if (rocket instanceof IRocket) {
            ((IRocket) rocket).setStatus(IRocket.RocketStatus.LANDING);
        }
        
        // Send arrival message
        player.sendSystemMessage(Component.translatable("message.galactic-space.arrived_at", 
                               destination.getName())
                               .withStyle(ChatFormatting.GREEN));
        
        // Use the space travel manager to teleport the player
        boolean success = GalacticSpace.getSpaceTravelManager().travelTo(player, destination);
        
        if (success) {
            // Mark destination as discovered
            GalacticSpace.getSpaceTravelManager().discoverCelestialBody(player, destination);
            
            // Set rocket status to ready
            if (rocket instanceof IRocket) {
                ((IRocket) rocket).setStatus(IRocket.RocketStatus.READY_FOR_LAUNCH);
            }
        } else {
            player.sendSystemMessage(Component.translatable("message.galactic-space.travel_failed")
                                   .withStyle(ChatFormatting.RED));
            
            // Set rocket status to crashed
            if (rocket instanceof IRocket) {
                ((IRocket) rocket).setStatus(IRocket.RocketStatus.CRASHED);
                ((IRocket) rocket).damage(30); // Apply damage to the rocket
            }
        }
    }
    
    /**
     * Checks if the player has the necessary equipment for the destination.
     *
     * @param player The player
     * @param destination The destination
     * @return true if player has required equipment
     */
    private static boolean hasRequiredEquipment(Player player, ICelestialBody destination) {
        // For now, assume players always have the required equipment
        // This will be expanded with spacesuit implementation
        return true;
    }
    
    /**
     * Checks if a rocket can reach the destination.
     *
     * @param rocket The rocket
     * @param destination The destination
     * @return true if the rocket can reach the destination
     */
    private static boolean canReachDestination(ModularRocket rocket, ICelestialBody destination) {
        // Check rocket tier requirement
        IRocketEngine engine = rocket.getEngine();
        if (engine == null) {
            return false;
        }
        
        int engineTier = engine.getTier();
        if (engineTier < destination.getRocketTierRequired()) {
            return false;
        }
        
        // Check if rocket has enough fuel
        // For now, just check if it has an engine
        return true;
    }
    
    /**
     * Cancels a launch sequence for a player.
     *
     * @param player The player
     */
    public static void cancelLaunch(Player player) {
        LaunchState state = playerLaunchStates.remove(player.getUUID());
        if (state != null) {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.sendSystemMessage(Component.translatable("message.galactic-space.launch_aborted")
                                     .withStyle(ChatFormatting.RED));
            }
            
            // Set rocket status back to ready
            if (state.rocket instanceof IRocket) {
                ((IRocket) state.rocket).setStatus(IRocket.RocketStatus.READY_FOR_LAUNCH);
            }
        }
    }
    
    /**
     * Checks if a player is currently in a launch sequence.
     *
     * @param player The player
     * @return true if player is launching
     */
    public static boolean isLaunching(Player player) {
        return playerLaunchStates.containsKey(player.getUUID());
    }
    
    /**
     * Gets the current launch state for a player.
     *
     * @param player The player
     * @return The launch state, or null if not launching
     */
    public static LaunchState getLaunchState(Player player) {
        return playerLaunchStates.get(player.getUUID());
    }
    
    /**
     * Enum representing the phases of a launch sequence.
     */
    public enum LaunchPhase {
        COUNTDOWN,
        LAUNCHING,
        TRAVELING
    }
    
    /**
     * Class representing the state of a launch sequence.
     */
    public static class LaunchState {
        private final ModularRocket rocket;
        private final ICelestialBody destination;
        private LaunchPhase phase;
        private int timeRemaining;
        
        public LaunchState(ModularRocket rocket, ICelestialBody destination, int countdownTime) {
            this.rocket = rocket;
            this.destination = destination;
            this.phase = LaunchPhase.COUNTDOWN;
            this.timeRemaining = countdownTime;
        }
        
        public ModularRocket getRocket() {
            return rocket;
        }
        
        public ICelestialBody getDestination() {
            return destination;
        }
        
        public LaunchPhase getPhase() {
            return phase;
        }
        
        public int getTimeRemaining() {
            return timeRemaining;
        }
    }
}