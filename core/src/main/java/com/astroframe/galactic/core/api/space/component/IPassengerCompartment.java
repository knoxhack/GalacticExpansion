package com.astroframe.galactic.core.api.space.component;

import net.minecraft.world.entity.player.Player;

import java.util.List;

/**
 * Interface for rocket passenger compartments.
 */
public interface IPassengerCompartment extends IRocketComponent {
    
    /**
     * Gets the number of passengers this compartment can hold.
     * @return The passenger capacity
     */
    int getPassengerCapacity();
    
    /**
     * Gets the current passengers in this compartment.
     * @return A list of passengers
     */
    List<Player> getPassengers();
    
    /**
     * Adds a passenger to this compartment.
     * @param player The player to add
     * @return true if the player was added successfully
     */
    boolean addPassenger(Player player);
    
    /**
     * Removes a passenger from this compartment.
     * @param player The player to remove
     */
    void removePassenger(Player player);
    
    /**
     * Gets the comfort level of this compartment (1-10).
     * Higher comfort reduces passenger fatigue during long journeys.
     * @return The comfort level
     */
    int getComfortLevel();
    
    /**
     * Whether this compartment has life support systems.
     * @return true if the compartment has life support
     */
    boolean hasLifeSupport();
    
    /**
     * Whether this compartment has gravity simulation.
     * @return true if the compartment has gravity simulation
     */
    boolean hasGravitySimulation();
    
    /**
     * Whether this compartment has radiation shielding.
     * @return true if the compartment has radiation shielding
     */
    boolean hasRadiationShielding();
}