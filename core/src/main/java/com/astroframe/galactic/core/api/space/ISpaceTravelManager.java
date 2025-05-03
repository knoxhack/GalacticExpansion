package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * Interface for managing space travel between planets and dimensions.
 */
public interface ISpaceTravelManager {

    /**
     * Initiates travel to the specified celestial body.
     * @param player The player traveling
     * @param destination The celestial body to travel to
     * @return true if the journey was successfully started
     */
    boolean travelTo(ServerPlayer player, ICelestialBody destination);
    
    /**
     * Gets all discovered celestial bodies for a player.
     * @param player The player
     * @return A list of discovered celestial bodies
     */
    List<ICelestialBody> getDiscoveredCelestialBodies(Player player);
    
    /**
     * Checks if a player has discovered a celestial body.
     * @param player The player
     * @param body The celestial body
     * @return true if discovered
     */
    boolean hasDiscovered(Player player, ICelestialBody body);
    
    /**
     * Discovers a celestial body for a player.
     * @param player The player
     * @param body The celestial body to discover
     */
    void discoverCelestialBody(Player player, ICelestialBody body);
    
    /**
     * Gets the current celestial body the player is on.
     * @param player The player
     * @return The current celestial body, or null if in an unrelated dimension
     */
    ICelestialBody getCurrentCelestialBody(Player player);
    
    /**
     * Registers a new celestial body with the manager.
     * @param body The celestial body to register
     */
    void registerCelestialBody(ICelestialBody body);
    
    /**
     * Gets a celestial body by its ID.
     * @param id The celestial body ID
     * @return The celestial body, or null if not found
     */
    ICelestialBody getCelestialBody(ResourceLocation id);
    
    /**
     * Gets all registered celestial bodies.
     * @return A list of all celestial bodies
     */
    List<ICelestialBody> getAllCelestialBodies();
    
    /**
     * Calculates the fuel required to travel between two celestial bodies.
     * @param origin The origin celestial body
     * @param destination The destination celestial body
     * @return The amount of fuel required
     */
    int calculateFuelRequired(ICelestialBody origin, ICelestialBody destination);
    
    /**
     * Checks if a player can travel to the specified celestial body.
     * @param player The player
     * @param destination The destination
     * @return true if travel is possible
     */
    boolean canTravelTo(Player player, ICelestialBody destination);
}