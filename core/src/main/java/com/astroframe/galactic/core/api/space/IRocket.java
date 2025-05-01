package com.astroframe.galactic.core.api.space;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.nbt.CompoundTag;

/**
 * Interface for rocket entities that can travel between celestial bodies.
 */
public interface IRocket {
    
    /**
     * Gets the current tier of this rocket.
     * @return The rocket tier (1-3)
     */
    int getTier();
    
    /**
     * Gets the maximum fuel capacity of this rocket.
     * @return The fuel capacity
     */
    int getFuelCapacity();
    
    /**
     * Gets the current amount of fuel in this rocket.
     * @return The current fuel level
     */
    int getFuelLevel();
    
    /**
     * Adds fuel to the rocket.
     * @param amount The amount to add
     * @return The amount actually added
     */
    int addFuel(int amount);
    
    /**
     * Gets the maximum payload capacity of this rocket in inventory slots.
     * @return The payload capacity
     */
    int getPayloadCapacity();
    
    /**
     * Gets the maximum passenger capacity of this rocket.
     * @return The passenger capacity
     */
    int getPassengerCapacity();
    
    /**
     * Gets the current passengers in this rocket.
     * @return A list of passengers
     */
    List<Player> getPassengers();
    
    /**
     * Adds a passenger to this rocket.
     * @param player The player to add
     * @return true if the player was added successfully
     */
    boolean addPassenger(Player player);
    
    /**
     * Removes a passenger from this rocket.
     * @param player The player to remove
     */
    void removePassenger(Player player);
    
    /**
     * Gets the cargo contents of this rocket.
     * @return A map of slot index to item stack
     */
    Map<Integer, ItemStack> getCargo();
    
    /**
     * Adds an item to the cargo.
     * @param stack The item stack to add
     * @return The remaining items that couldn't be added, or empty if all were added
     */
    ItemStack addCargo(ItemStack stack);
    
    /**
     * Checks if this rocket can reach the specified celestial body.
     * @param body The celestial body
     * @return true if the rocket has sufficient tier and fuel
     */
    boolean canReach(ICelestialBody body);
    
    /**
     * Launches the rocket to the specified celestial body.
     * @param destination The destination
     * @return true if launch was successful
     */
    boolean launch(ICelestialBody destination);
    
    /**
     * Gets the status of this rocket.
     * @return The current status
     */
    RocketStatus getStatus();
    
    /**
     * Sets the status of this rocket.
     * @param status The new status
     */
    void setStatus(RocketStatus status);
    
    /**
     * Enum representing the possible statuses of a rocket.
     */
    enum RocketStatus {
        BUILDING,
        READY_FOR_LAUNCH,
        LAUNCHING,
        IN_FLIGHT,
        LANDING,
        CRASHED
    }
    
    /**
     * Gets the current health of the rocket as a percentage.
     * @return The health percentage (0-100)
     */
    float getHealth();
    
    /**
     * Damages the rocket.
     * @param amount The amount of damage to apply
     */
    void damage(float amount);
    
    /**
     * Repairs the rocket.
     * @param amount The amount to repair
     */
    void repair(float amount);
    
    /**
     * Checks if this rocket has a component of the specified type.
     * @param type The component type to check for
     * @return true if the rocket has the component
     */
    boolean hasComponent(RocketComponentType type);
    
    /**
     * Saves rocket data to an NBT tag.
     * @param tag The tag to save to
     */
    default void saveToTag(CompoundTag tag) {
        // Default implementation does nothing
    }
    
    /**
     * Creates a rocket from an NBT tag.
     * @param tag The tag to load from
     * @return The rocket, or null if invalid
     */
    static IRocket fromTag(CompoundTag tag) {
        // Default implementation returns null
        return null;
    }
}