package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ISpaceTravelManager;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.registry.SpaceBodies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Manages space travel between celestial bodies.
 * This class handles rocket travel, fuel consumption, and player teleportation.
 */
public class SpaceTravelManager implements ISpaceTravelManager {
    
    private final Map<UUID, ICelestialBody> playerDestinations = new HashMap<>();
    private final Map<UUID, Long> travelTimes = new HashMap<>();
    private final Map<UUID, Set<ResourceLocation>> discoveredBodies = new ConcurrentHashMap<>();
    private final Map<ResourceLocation, ICelestialBody> registeredBodies = new ConcurrentHashMap<>();
    
    /**
     * Launches a rocket to a destination.
     *
     * @param player The player operating the rocket
     * @param rocket The rocket to launch
     * @param destination The celestial body destination
     * @return True if launch was successful
     */
    public boolean launchRocket(Player player, IRocket rocket, ICelestialBody destination) {
        if (player == null || rocket == null || destination == null) {
            return false;
        }
        
        // Check if rocket has enough fuel
        if (rocket.getFuelLevel() < calculateRequiredFuel(rocket, destination)) {
            if (player instanceof ServerPlayer serverPlayer) {
                // Notify player about insufficient fuel
                serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal("Not enough fuel to reach " + destination.getName()), 
                    false
                );
            }
            return false;
        }
        
        // Check if rocket meets tier requirements
        if (rocket.getTier() < destination.getRequiredTier()) {
            if (player instanceof ServerPlayer serverPlayer) {
                // Notify player about insufficient rocket tier
                serverPlayer.displayClientMessage(
                    net.minecraft.network.chat.Component.literal(
                        "Rocket tier " + rocket.getTier() + " is not sufficient to reach " +
                        destination.getName() + " (requires tier " + destination.getRequiredTier() + ")"
                    ),
                    false
                );
            }
            return false;
        }
        
        // Store player's destination for the travel manager to handle
        playerDestinations.put(player.getUUID(), destination);
        
        // Calculate travel time based on distance and rocket speed
        long travelTimeMillis = calculateTravelTime(rocket, destination);
        travelTimes.put(player.getUUID(), System.currentTimeMillis() + travelTimeMillis);
        
        // Consume fuel
        rocket.consumeFuel(calculateRequiredFuel(rocket, destination));
        
        // Log the launch
        GalacticSpace.LOGGER.info("Player {} launched rocket to {} (arrival in {} ms)", 
            player.getName().getString(), destination.getName(), travelTimeMillis);
        
        // Return success
        return true;
    }
    
    /**
     * Gets the destination a player is currently traveling to.
     *
     * @param player The player to check
     * @return The destination celestial body, or empty if not traveling
     */
    public Optional<ICelestialBody> getPlayerDestination(Player player) {
        if (player == null) {
            return Optional.empty();
        }
        
        ICelestialBody destination = playerDestinations.get(player.getUUID());
        return Optional.ofNullable(destination);
    }
    
    /**
     * Checks if a player is currently traveling.
     *
     * @param player The player to check
     * @return True if the player is traveling
     */
    public boolean isPlayerTraveling(Player player) {
        if (player == null) {
            return false;
        }
        
        return playerDestinations.containsKey(player.getUUID());
    }
    
    /**
     * Gets the remaining travel time for a player in milliseconds.
     *
     * @param player The player to check
     * @return Remaining travel time in milliseconds, or -1 if not traveling
     */
    public long getRemainingTravelTime(Player player) {
        if (player == null || !isPlayerTraveling(player)) {
            return -1;
        }
        
        Long arrivalTime = travelTimes.get(player.getUUID());
        if (arrivalTime == null) {
            return -1;
        }
        
        long remaining = arrivalTime - System.currentTimeMillis();
        return Math.max(0, remaining);
    }
    
    /**
     * Calculates the required fuel to reach a destination.
     *
     * @param rocket The rocket to use
     * @param destination The destination celestial body
     * @return Required fuel units
     */
    public int calculateRequiredFuel(IRocket rocket, ICelestialBody destination) {
        if (rocket == null || destination == null) {
            return 0;
        }
        
        // Base fuel cost is distance in AU × 10
        float baseFuel = destination.getDistance() * 10;
        
        // Apply efficiency factor based on rocket tier
        float efficiencyFactor = switch (rocket.getTier()) {
            case 1 -> 1.5f;  // Tier 1 is least efficient
            case 2 -> 1.2f;  // Tier 2 is more efficient
            case 3 -> 1.0f;  // Tier 3 is most efficient
            default -> 2.0f; // Fallback
        };
        
        return Math.round(baseFuel * efficiencyFactor);
    }
    
    /**
     * Calculates travel time to a destination.
     *
     * @param rocket The rocket to use
     * @param destination The destination celestial body
     * @return Travel time in milliseconds
     */
    public long calculateTravelTime(IRocket rocket, ICelestialBody destination) {
        if (rocket == null || destination == null) {
            return 0;
        }
        
        // Base travel time is distance in AU × 10 seconds (in milliseconds)
        float baseTime = destination.getDistance() * 10 * 1000;
        
        // Apply speed factor based on rocket tier
        float speedFactor = switch (rocket.getTier()) {
            case 1 -> 1.0f;  // Tier 1 is slowest
            case 2 -> 0.8f;  // Tier 2 is faster
            case 3 -> 0.6f;  // Tier 3 is fastest
            default -> 1.2f; // Fallback
        };
        
        return Math.round(baseTime * speedFactor);
    }
    
    /**
     * Cancels a player's space travel.
     *
     * @param player The player whose travel to cancel
     * @return True if travel was canceled
     */
    public boolean cancelTravel(Player player) {
        if (player == null || !isPlayerTraveling(player)) {
            return false;
        }
        
        playerDestinations.remove(player.getUUID());
        travelTimes.remove(player.getUUID());
        
        GalacticSpace.LOGGER.info("Canceled space travel for player {}", 
            player.getName().getString());
        
        return true;
    }
    
    /**
     * Completes a player's space travel immediately.
     *
     * @param player The player whose travel to complete
     * @return True if travel was completed
     */
    public boolean completeTravel(Player player) {
        if (player == null || !isPlayerTraveling(player)) {
            return false;
        }
        
        // Set arrival time to now to force immediate completion
        travelTimes.put(player.getUUID(), System.currentTimeMillis());
        
        GalacticSpace.LOGGER.info("Forced completion of space travel for player {}", 
            player.getName().getString());
        
        return true;
    }
    
    /**
     * Process all ongoing space travels.
     * This should be called on server tick.
     */
    public void processTravels() {
        long currentTime = System.currentTimeMillis();
        
        // Create a copy of the keys to avoid concurrent modification
        for (UUID playerUUID : playerDestinations.keySet().toArray(new UUID[0])) {
            Long arrivalTime = travelTimes.get(playerUUID);
            
            if (arrivalTime != null && currentTime >= arrivalTime) {
                // Travel is complete, handle arrival
                handleArrival(playerUUID);
            }
        }
    }
    
    /**
     * Handle player arrival at destination.
     *
     * @param playerUUID The UUID of the arriving player
     */
    private void handleArrival(UUID playerUUID) {
        Player player = findPlayerByUUID(playerUUID);
        if (player == null) {
            // Player may have disconnected, remove from tracking
            playerDestinations.remove(playerUUID);
            travelTimes.remove(playerUUID);
            return;
        }
        
        ICelestialBody destination = playerDestinations.get(playerUUID);
        if (destination == null) {
            return;
        }
        
        // Log arrival
        GalacticSpace.LOGGER.info("Player {} has arrived at {}", 
            player.getName().getString(), destination.getName());
        
        // Teleport player to destination (implementation would depend on how you handle dimensions)
        // This would be implemented based on your dimension system
        
        // Clean up tracking data
        playerDestinations.remove(playerUUID);
        travelTimes.remove(playerUUID);
        
        // Notify player
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(
                net.minecraft.network.chat.Component.literal("You have arrived at " + destination.getName()),
                false
            );
        }
    }
    
    /**
     * Find a player by UUID on the server.
     *
     * @param playerUUID The player UUID
     * @return The player, or null if not found
     */
    private Player findPlayerByUUID(UUID playerUUID) {
        if (GalacticSpace.getServer() == null) {
            return null;
        }
        
        return GalacticSpace.getServer().getPlayerList().getPlayer(playerUUID);
    }

    /**
     * Initiates travel to the specified celestial body.
     *
     * @param player The player traveling
     * @param destination The celestial body to travel to
     * @return true if the journey was successfully started
     */
    public boolean travelTo(ServerPlayer player, ICelestialBody destination) {
        if (player == null || destination == null) {
            return false;
        }
        
        // Check if destination is discovered
        if (!hasDiscovered(player, destination)) {
            player.displayClientMessage(
                net.minecraft.network.chat.Component.literal("You haven't discovered " + destination.getName() + " yet."),
                false
            );
            return false;
        }
        
        // In a real implementation, we'd check for a rocket in the player's inventory
        // or if they're standing on a launchpad, etc.
        // For now, this is a simplified version
        
        // Store destination and calculate arrival time (simplified)
        playerDestinations.put(player.getUUID(), destination);
        // Set a short travel time for now (5 seconds)
        travelTimes.put(player.getUUID(), System.currentTimeMillis() + 5000);
        
        player.displayClientMessage(
            net.minecraft.network.chat.Component.literal("Traveling to " + destination.getName() + "..."),
            false
        );
        
        // Log the travel initiation
        GalacticSpace.LOGGER.info("Player {} is traveling to {} (command-initiated)", 
            player.getName().getString(), destination.getName());
        
        return true;
    }
    
    /**
     * Gets all discovered celestial bodies for a player.
     *
     * @param player The player
     * @return A list of discovered celestial bodies
     */
    @Override
    public List<ICelestialBody> getDiscoveredCelestialBodies(Player player) {
        if (player == null) {
            return Collections.emptyList();
        }
        
        // Get the player's discovered bodies set, creating if needed
        Set<ResourceLocation> discoveredIds = discoveredBodies.computeIfAbsent(
            player.getUUID(), k -> new HashSet<>()
        );
        
        // Convert IDs to celestial bodies
        return discoveredIds.stream()
            .map(this::getCelestialBody)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    /**
     * Checks if a player has discovered a celestial body.
     *
     * @param player The player
     * @param body The celestial body
     * @return true if discovered
     */
    @Override
    public boolean hasDiscovered(Player player, ICelestialBody body) {
        if (player == null || body == null) {
            return false;
        }
        
        // Earth is always discovered
        if (body.getId().toString().equals("galacticspace:earth")) {
            return true;
        }
        
        // Get the player's discovered bodies set
        Set<ResourceLocation> playerDiscovered = discoveredBodies.get(player.getUUID());
        if (playerDiscovered == null) {
            return false;
        }
        
        return playerDiscovered.contains(body.getId());
    }
    
    /**
     * Discovers a celestial body for a player.
     *
     * @param player The player
     * @param body The celestial body to discover
     */
    @Override
    public void discoverCelestialBody(Player player, ICelestialBody body) {
        if (player == null || body == null) {
            return;
        }
        
        // Get or create the player's discovered bodies set
        Set<ResourceLocation> playerDiscovered = discoveredBodies.computeIfAbsent(
            player.getUUID(), k -> new HashSet<>()
        );
        
        // Add the body ID to the discovered set
        playerDiscovered.add(body.getId());
        
        // Notify player
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.displayClientMessage(
                net.minecraft.network.chat.Component.literal("Discovered: " + body.getName()),
                false
            );
        }
        
        GalacticSpace.LOGGER.info("Player {} discovered celestial body {}", 
            player.getName().getString(), body.getName());
    }
    
    /**
     * Gets the current celestial body the player is on.
     *
     * @param player The player
     * @return The current celestial body, or null if in an unrelated dimension
     */
    @Override
    public ICelestialBody getCurrentCelestialBody(Player player) {
        if (player == null) {
            return null;
        }
        
        // Simple implementation for now
        // In a full implementation, this would check the dimension against mapping data
        
        // Default to Earth if in overworld
        if (player.level().dimension() == net.minecraft.world.level.Level.OVERWORLD) {
            return SpaceBodies.EARTH;
        }
        
        // Return null for other dimensions
        return null;
    }
    
    /**
     * Registers a new celestial body with the manager.
     *
     * @param body The celestial body to register
     */
    @Override
    public void registerCelestialBody(ICelestialBody body) {
        if (body == null) {
            return;
        }
        
        registeredBodies.put(body.getId(), body);
        GalacticSpace.LOGGER.info("Registered celestial body: {}", body.getName());
    }
    
    /**
     * Gets a celestial body by its ID.
     *
     * @param id The celestial body ID
     * @return The celestial body, or null if not found
     */
    @Override
    public ICelestialBody getCelestialBody(ResourceLocation id) {
        if (id == null) {
            return null;
        }
        
        return registeredBodies.get(id);
    }
    
    /**
     * Gets all registered celestial bodies.
     *
     * @return A list of all celestial bodies
     */
    @Override
    public List<ICelestialBody> getAllCelestialBodies() {
        return new ArrayList<>(registeredBodies.values());
    }
    
    /**
     * Calculates the fuel required to travel between two celestial bodies.
     *
     * @param origin The origin celestial body
     * @param destination The destination celestial body
     * @return The amount of fuel required
     */
    @Override
    public int calculateFuelRequired(ICelestialBody origin, ICelestialBody destination) {
        if (origin == null || destination == null) {
            return 0;
        }
        
        // Simple implementation: distance difference * 10
        float distanceDiff = Math.abs(destination.getDistance() - origin.getDistance());
        return Math.round(distanceDiff * 10);
    }
    
    /**
     * Checks if a player can travel to the specified celestial body.
     *
     * @param player The player
     * @param destination The destination
     * @return true if travel is possible
     */
    @Override
    public boolean canTravelTo(Player player, ICelestialBody destination) {
        if (player == null || destination == null) {
            return false;
        }
        
        // Must have discovered the destination
        if (!hasDiscovered(player, destination)) {
            return false;
        }
        
        // Check if player is already traveling
        if (isPlayerTraveling(player)) {
            return false;
        }
        
        // In a full implementation, we would check if the player has the right rocket tier,
        // enough fuel, proper space suit, etc.
        
        return true;
    }
}