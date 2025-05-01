package com.astroframe.galactic.space.implementation;

import com.astroframe.galactic.core.api.space.ICelestialBody;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ISpaceTravelManager;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import com.astroframe.galactic.space.dimension.SpaceStationTeleporter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.TickEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;

import java.util.*;
import net.minecraft.resources.ResourceLocation;

/**
 * Manages travel between celestial bodies.
 * This class coordinates rockets, players, and dimension teleportation.
 */
@Mod.EventBusSubscriber(modid = GalacticSpace.MOD_ID)
public class SpaceTravelManager implements ISpaceTravelManager {
    private final Map<UUID, Set<ICelestialBody>> discoveredBodies = new HashMap<>();
    private final Map<ResourceLocation, ICelestialBody> registeredBodies = new HashMap<>();
    private boolean isInitialized = false;
    
    /**
     * Initialization logic.
     */
    public void initialize() {
        if (isInitialized) {
            return;
        }
        
        GalacticSpace.LOGGER.info("Initializing Space Travel Manager");
        
        // Register default celestial bodies
        registerCelestialBody(SpaceBodies.EARTH);
        registerCelestialBody(SpaceBodies.SPACE_STATION);
        
        isInitialized = true;
    }
    
    /**
     * Server tick event handler to update space travel.
     *
     * @param event The tick event
     */
    @SubscribeEvent
    public static void onServerTick(net.neoforged.bus.api.Event event) {
        // Update rocket launch sequences on each tick
        RocketLaunchController.updateLaunches();
    }
    
    /**
     * Initiates travel to a celestial body.
     * This method is used when a player uses a rocket to travel to a celestial body.
     *
     * @param player The player
     * @param destination The destination celestial body
     * @param rocket The rocket being used
     * @return true if the travel initiation was successful
     */
    public boolean initiateTravelWithRocket(ServerPlayer player, ICelestialBody destination, ModularRocket rocket) {
        // Check if the rocket can reach the destination
        if (!rocket.canReach(destination)) {
            player.sendSystemMessage(Component.translatable("message.galactic-space.rocket_insufficient")
                                    .withStyle(net.minecraft.ChatFormatting.RED));
            return false;
        }
        
        // Start the launch sequence using the RocketLaunchController
        return RocketLaunchController.startLaunch(player, rocket, destination);
    }
    
    /**
     * Teleports a player to a celestial body.
     * This is a direct teleport without a rocket, primarily used for commands or testing.
     *
     * @param player The player
     * @param destination The destination celestial body
     * @return true if the teleportation was successful
     */
    public boolean travelTo(ServerPlayer player, ICelestialBody destination) {
        // Right now we only support travel to space station
        if (destination == SpaceBodies.SPACE_STATION) {
            return SpaceStationTeleporter.teleportToSpaceStation(player);
        } else if (destination == SpaceBodies.EARTH) {
            return SpaceStationTeleporter.teleportToOverworld(player);
        } else {
            // Unsupported destination
            player.sendSystemMessage(Component.translatable("message.galactic-space.destination_not_implemented")
                                    .withStyle(net.minecraft.ChatFormatting.RED));
            return false;
        }
    }
    
    /**
     * Checks if a player is currently in a specific celestial body's dimension.
     *
     * @param player The player
     * @param body The celestial body
     * @return true if the player is in the celestial body's dimension
     */
    public boolean isPlayerAt(ServerPlayer player, ICelestialBody body) {
        ServerLevel level = player.serverLevel();
        
        if (body == SpaceBodies.SPACE_STATION) {
            return SpaceStationDimension.isSpaceStation(level);
        } else if (body == SpaceBodies.EARTH) {
            return level.dimension().equals(Level.OVERWORLD);
        }
        
        return false;
    }
    
    /**
     * Implementation of the interface method - handles any Player type
     * Discovers a celestial body for a player, adds it to their discovered bodies list.
     */
    @Override
    public void discoverCelestialBody(Player player, ICelestialBody body) {
        if (player instanceof ServerPlayer serverPlayer) {
            discoverCelestialBodyInternal(serverPlayer, body);
        }
    }
    
    /**
     * Internal implementation that works with ServerPlayer
     */
    private void discoverCelestialBodyInternal(ServerPlayer player, ICelestialBody body) {
        UUID playerId = player.getUUID();
        
        // Initialize set if this is the player's first discovery
        if (!discoveredBodies.containsKey(playerId)) {
            discoveredBodies.put(playerId, new HashSet<>());
        }
        
        // Add the body to the player's discoveries
        if (discoveredBodies.get(playerId).add(body)) {
            // If it's a new discovery, notify the player
            player.sendSystemMessage(Component.translatable("message.galactic-space.discovered_body", 
                                                         body.getName())
                                   .withStyle(net.minecraft.ChatFormatting.GREEN));
            
            // TODO: Trigger advancements when the body is discovered
        }
    }
    
    /**
     * Internal implementation that checks if a player has discovered a celestial body.
     *
     * @param player The server player
     * @param body The celestial body
     * @return true if the player has discovered the body
     */
    private boolean hasDiscoveredInternal(ServerPlayer player, ICelestialBody body) {
        UUID playerId = player.getUUID();
        
        // Earth is always discovered
        if (body == SpaceBodies.EARTH) {
            return true;
        }
        
        // Check if player has discovered the body
        return discoveredBodies.containsKey(playerId) && 
               discoveredBodies.get(playerId).contains(body);
    }
    
    /**
     * Gets a list of celestial bodies discovered by a player.
     *
     * @param player The player
     * @return A list of discovered celestial bodies
     */
    public List<ICelestialBody> getDiscoveredBodies(ServerPlayer player) {
        UUID playerId = player.getUUID();
        
        // Create a list with Earth (always discovered)
        List<ICelestialBody> bodies = new ArrayList<>();
        bodies.add(SpaceBodies.EARTH);
        
        // Add other discovered bodies
        if (discoveredBodies.containsKey(playerId)) {
            bodies.addAll(discoveredBodies.get(playerId));
        }
        
        return bodies;
    }
    
    /**
     * Aborts an in-progress travel sequence.
     *
     * @param player The player
     * @return true if a travel sequence was aborted
     */
    public boolean abortTravel(ServerPlayer player) {
        if (RocketLaunchController.isLaunching(player)) {
            RocketLaunchController.cancelLaunch(player);
            return true;
        }
        return false;
    }
    
    /**
     * Calculates the travel time between two celestial bodies.
     * 
     * @param from Source celestial body
     * @param to Destination celestial body
     * @param rocketTier The tier of the rocket being used
     * @return Travel time in ticks
     */
    public int calculateTravelTime(ICelestialBody from, ICelestialBody to, int rocketTier) {
        // Base travel time based on distance between bodies
        int distance = Math.abs(to.getDistanceFromHome() - from.getDistanceFromHome());
        
        // Base time: 1 second per 10 distance units
        int baseTime = distance * 2;
        
        // Adjust for rocket tier (higher tier = faster travel)
        float tierMultiplier = 1.0f / rocketTier;
        
        // Calculate final time (in ticks, 20 ticks = 1 second)
        return Math.max(60, Math.round(baseTime * tierMultiplier) * 20);
    }
    
    /**
     * Registers a celestial body with the manager.
     *
     * @param body The celestial body to register
     */
    @Override
    public void registerCelestialBody(ICelestialBody body) {
        registeredBodies.put(body.getId(), body);
        GalacticSpace.LOGGER.info("Registered celestial body: {}", body.getName());
    }
    
    /**
     * Gets a celestial body by its ID.
     *
     * @param id The celestial body ID
     * @return The celestial body, or empty if not found
     */
    @Override
    public Optional<ICelestialBody> getCelestialBody(ResourceLocation id) {
        return Optional.ofNullable(registeredBodies.get(id));
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
     * Gets all discovered celestial bodies for a player.
     * 
     * @param player The player
     * @return A list of discovered celestial bodies
     */
    @Override
    public List<ICelestialBody> getDiscoveredCelestialBodies(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            return getDiscoveredBodies(serverPlayer);
        } else {
            // Client-side, just return all bodies
            return getAllCelestialBodies();
        }
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
        if (player instanceof ServerPlayer serverPlayer) {
            return hasDiscoveredInternal(serverPlayer, body);
        } else {
            // On client side, assume discovered
            return true;
        }
    }
    
    // Second implementation of discoverCelestialBody removed to fix ambiguous reference error
    
    /**
     * Calculates the fuel required to travel between two celestial bodies.
     * 
     * @param origin The origin celestial body
     * @param destination The destination celestial body
     * @return The amount of fuel required
     */
    @Override
    public int calculateFuelRequired(ICelestialBody origin, ICelestialBody destination) {
        // Base fuel required based on distance
        int distance = Math.abs(destination.getDistanceFromHome() - origin.getDistanceFromHome());
        
        // Basic formula: 10 fuel per distance unit
        return Math.max(100, distance * 10);
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
        // Currently only Earth and Space Station are implemented
        return destination == SpaceBodies.EARTH || destination == SpaceBodies.SPACE_STATION;
    }
    
    /**
     * Gets the current celestial body the player is on.
     * 
     * @param player The player
     * @return The current celestial body, or empty if in an unrelated dimension
     */
    @Override
    public Optional<ICelestialBody> getCurrentCelestialBody(Player player) {
        // Check for space station dimension
        if (SpaceStationDimension.isSpaceStation(player.level())) {
            return Optional.of(SpaceBodies.SPACE_STATION);
        }
        
        // Check for overworld
        if (player.level().dimension().equals(Level.OVERWORLD)) {
            return Optional.of(SpaceBodies.EARTH);
        }
        
        // Unknown dimension
        return Optional.empty();
    }
}