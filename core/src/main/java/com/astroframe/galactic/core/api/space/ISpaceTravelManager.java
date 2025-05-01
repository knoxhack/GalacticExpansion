package com.astroframe.galactic.core.api.space;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for managing space travel between celestial bodies.
 */
public interface ISpaceTravelManager {
    
    /**
     * Initiates travel from the current celestial body to a destination.
     * 
     * @param player The player traveling
     * @param destination The destination celestial body
     * @param vehicleEntityId The ID of the vehicle entity
     * @return A travel ticket ID, or empty if travel could not be initiated
     */
    Optional<UUID> startTravel(ServerPlayer player, ICelestialBody destination, UUID vehicleEntityId);
    
    /**
     * Aborts an ongoing space travel.
     * 
     * @param travelTicketId The travel ticket ID
     * @return Whether the abortion was successful
     */
    boolean abortTravel(UUID travelTicketId);
    
    /**
     * Checks if a player is currently traveling in space.
     * 
     * @param player The player to check
     * @return Whether the player is traveling
     */
    boolean isPlayerTraveling(ServerPlayer player);
    
    /**
     * Gets the current travel progress for a player.
     * 
     * @param player The player to check
     * @return The travel progress from 0.0 to 1.0, or -1.0 if not traveling
     */
    float getTravelProgress(ServerPlayer player);
    
    /**
     * Gets the remaining travel time in ticks.
     * 
     * @param travelTicketId The travel ticket ID
     * @return The remaining time in ticks, or -1 if not found
     */
    long getRemainingTravelTime(UUID travelTicketId);
    
    /**
     * Gets the current location of a player in space.
     * 
     * @param player The player to check
     * @return The current celestial body, or the body being traveled to if in transit
     */
    ICelestialBody getPlayerLocation(ServerPlayer player);
    
    /**
     * Gets all active travel tickets.
     * 
     * @return A list of all active travel tickets
     */
    List<TravelTicket> getAllActiveTickets();
    
    /**
     * Gets a travel ticket by its ID.
     * 
     * @param ticketId The ticket ID
     * @return The travel ticket, or empty if not found
     */
    Optional<TravelTicket> getTicket(UUID ticketId);
    
    /**
     * Gets all space vehicles that can travel to a celestial body.
     * 
     * @param destination The destination celestial body
     * @return A list of vehicle registry names that can reach the destination
     */
    List<ResourceLocation> getVehiclesCapableOfReaching(ICelestialBody destination);
    
    /**
     * Checks if a vehicle can reach a celestial body.
     * 
     * @param vehicleId The vehicle registry name
     * @param destination The destination celestial body
     * @return Whether the vehicle can reach the destination
     */
    boolean canVehicleReach(ResourceLocation vehicleId, ICelestialBody destination);
    
    /**
     * Gets the minimum vehicle tier required to reach a celestial body.
     * 
     * @param destination The destination celestial body
     * @return The minimum vehicle tier required
     */
    int getMinimumVehicleTierForDestination(ICelestialBody destination);
    
    /**
     * Represents a space travel ticket with all relevant information.
     */
    interface TravelTicket {
        /**
         * Gets the unique identifier for this ticket.
         * 
         * @return The ticket ID
         */
        UUID getTicketId();
        
        /**
         * Gets the player who is traveling.
         * 
         * @return The player's UUID
         */
        UUID getPlayerUUID();
        
        /**
         * Gets the vehicle entity ID.
         * 
         * @return The vehicle entity ID
         */
        UUID getVehicleEntityId();
        
        /**
         * Gets the origin celestial body.
         * 
         * @return The origin
         */
        ICelestialBody getOrigin();
        
        /**
         * Gets the destination celestial body.
         * 
         * @return The destination
         */
        ICelestialBody getDestination();
        
        /**
         * Gets the departure time in game ticks.
         * 
         * @return The departure time
         */
        long getDepartureTime();
        
        /**
         * Gets the arrival time in game ticks.
         * 
         * @return The arrival time
         */
        long getArrivalTime();
        
        /**
         * Gets the current progress of the journey.
         * 
         * @return A value from 0.0 to 1.0
         */
        float getProgress();
        
        /**
         * Gets all entities that are part of this travel ticket.
         * 
         * @return A list of entity UUIDs
         */
        List<UUID> getIncludedEntities();
        
        /**
         * Adds an entity to this travel ticket.
         * 
         * @param entityId The entity UUID
         */
        void addEntity(UUID entityId);
        
        /**
         * Gets the status of this travel ticket.
         * 
         * @return The ticket status
         */
        TravelStatus getStatus();
    }
    
    /**
     * Status of a travel ticket.
     */
    enum TravelStatus {
        SCHEDULED,
        IN_PROGRESS,
        COMPLETED,
        ABORTED,
        FAILED
    }
}