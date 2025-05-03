package com.astroframe.galactic.space.util;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.registry.SpaceBodies;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Random;

/**
 * Utility methods for space-related operations.
 */
public class SpaceUtil {
    private static final Random RANDOM = new Random();
    
    /**
     * Attempts to transport a player to a celestial body.
     * 
     * @param player The player
     * @param body The destination celestial body
     * @param rocket The rocket being used
     * @return true if travel was successful
     */
    public static boolean travelTo(ServerPlayer player, ICelestialBody body, IRocket rocket) {
        // Check if the rocket can reach the destination
        if (!rocket.canReach(body)) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.not_enough_fuel", body.getName()));
            return false;
        }
        
        // Start the launch sequence
        player.sendSystemMessage(Component.translatable("message.galactic-space.travel_start", body.getName()));
        
        // In a full implementation, this would teleport to a dimension
        // For now, just return true and consume fuel
        player.sendSystemMessage(Component.translatable("message.galactic-space.travel_complete", body.getName()));
        
        // Mark the body as discovered for this player
        GalacticSpace.getSpaceTravelManager().discoverCelestialBody(player, body);
        
        return true;
    }
    
    /**
     * Gets a random celestial body appropriate for the given rocket tier.
     * 
     * @param tier The rocket tier
     * @return A celestial body, or null if none are available
     */
    public static ICelestialBody getRandomReachableCelestialBody(int tier) {
        var bodies = SpaceBodies.getAll().stream()
                .filter(body -> body.getRocketTierRequired() <= tier)
                .toList();
        
        if (bodies.isEmpty()) {
            return null;
        }
        
        return bodies.get(RANDOM.nextInt(bodies.size()));
    }
    
    /**
     * Calculates the amount of fuel needed to reach a celestial body.
     * 
     * @param body The destination body
     * @return The amount of fuel required
     */
    public static int getFuelRequiredForTravel(ICelestialBody body) {
        return body.getDistanceFromHome() * 10;
    }
}