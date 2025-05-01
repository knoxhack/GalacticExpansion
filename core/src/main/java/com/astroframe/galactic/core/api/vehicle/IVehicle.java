package com.astroframe.galactic.core.api.vehicle;

import com.astroframe.galactic.core.api.energy.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Interface for vehicles that can be driven, flown, or sailed.
 * This is a central concept for the Vehicles module.
 */
public interface IVehicle {
    
    /**
     * Gets the unique identifier for this vehicle.
     * 
     * @return The vehicle UUID
     */
    UUID getVehicleId();
    
    /**
     * Gets the type of this vehicle.
     * 
     * @return The vehicle type
     */
    VehicleType getVehicleType();
    
    /**
     * Gets the entity representation of this vehicle.
     * 
     * @return The vehicle entity
     */
    Entity getEntity();
    
    /**
     * Gets the current driver of this vehicle.
     * 
     * @return The driver, or null if unoccupied
     */
    Player getDriver();
    
    /**
     * Sets the driver of this vehicle.
     * 
     * @param player The new driver
     * @return Whether the driver was set successfully
     */
    boolean setDriver(Player player);
    
    /**
     * Gets all passengers in this vehicle.
     * 
     * @return A list of passenger entities
     */
    List<Entity> getPassengers();
    
    /**
     * Gets the maximum number of passengers this vehicle can hold.
     * 
     * @return The maximum passenger count
     */
    int getMaxPassengers();
    
    /**
     * Adds a passenger to this vehicle.
     * 
     * @param passenger The passenger to add
     * @return Whether the passenger was added successfully
     */
    boolean addPassenger(Entity passenger);
    
    /**
     * Removes a passenger from this vehicle.
     * 
     * @param passenger The passenger to remove
     * @return Whether the passenger was removed successfully
     */
    boolean removePassenger(Entity passenger);
    
    /**
     * Gets the current speed of this vehicle.
     * 
     * @return The speed in blocks per tick
     */
    double getSpeed();
    
    /**
     * Gets the maximum speed of this vehicle.
     * 
     * @return The maximum speed in blocks per tick
     */
    double getMaxSpeed();
    
    /**
     * Sets the speed of this vehicle.
     * 
     * @param speed The new speed in blocks per tick
     */
    void setSpeed(double speed);
    
    /**
     * Gets the acceleration rate of this vehicle.
     * 
     * @return The acceleration in blocks per tick squared
     */
    double getAcceleration();
    
    /**
     * Gets the deceleration rate of this vehicle.
     * 
     * @return The deceleration in blocks per tick squared
     */
    double getDeceleration();
    
    /**
     * Gets the current fuel level.
     * 
     * @return The fuel level as a value from 0.0 to 1.0
     */
    float getFuelLevel();
    
    /**
     * Gets the fuel capacity.
     * 
     * @return The fuel capacity in units
     */
    int getFuelCapacity();
    
    /**
     * Adds fuel to this vehicle.
     * 
     * @param amount The amount to add
     * @return The amount that was actually added
     */
    int addFuel(int amount);
    
    /**
     * Consumes fuel from this vehicle.
     * 
     * @param amount The amount to consume
     * @return Whether sufficient fuel was available and consumed
     */
    boolean consumeFuel(int amount);
    
    /**
     * Gets the energy handler for this vehicle if it's electric.
     * 
     * @return The energy handler, or null if not electric
     */
    IEnergyHandler getEnergyHandler();
    
    /**
     * Gets the health of this vehicle.
     * 
     * @return The health as a value from 0.0 to 1.0
     */
    float getHealth();
    
    /**
     * Gets the maximum health of this vehicle.
     * 
     * @return The maximum health
     */
    float getMaxHealth();
    
    /**
     * Sets the health of this vehicle.
     * 
     * @param health The new health value
     */
    void setHealth(float health);
    
    /**
     * Damages this vehicle.
     * 
     * @param amount The amount of damage
     * @param source The source of damage, or null if unspecified
     * @return Whether damage was applied
     */
    boolean damage(float amount, Entity source);
    
    /**
     * Repairs this vehicle.
     * 
     * @param amount The amount to repair
     * @return Whether the repair was successful
     */
    boolean repair(float amount);
    
    /**
     * Gets the current position of this vehicle.
     * 
     * @return The position vector
     */
    Vec3 getPosition();
    
    /**
     * Gets the current rotation of this vehicle.
     * 
     * @return An array of [pitch, yaw, roll] in degrees
     */
    float[] getRotation();
    
    /**
     * Gets the installed modules on this vehicle.
     * 
     * @return A set of installed module IDs
     */
    Set<UUID> getInstalledModules();
    
    /**
     * Installs a module on this vehicle.
     * 
     * @param moduleId The module ID to install
     * @return Whether the installation was successful
     */
    boolean installModule(UUID moduleId);
    
    /**
     * Uninstalls a module from this vehicle.
     * 
     * @param moduleId The module ID to uninstall
     * @return Whether the uninstallation was successful
     */
    boolean uninstallModule(UUID moduleId);
    
    /**
     * Starts the vehicle's engine.
     * 
     * @return Whether the engine started successfully
     */
    boolean startEngine();
    
    /**
     * Stops the vehicle's engine.
     */
    void stopEngine();
    
    /**
     * Checks if the vehicle's engine is running.
     * 
     * @return Whether the engine is running
     */
    boolean isEngineRunning();
    
    /**
     * Teleports this vehicle to a specific position.
     * 
     * @param level The target level
     * @param pos The target position
     * @return Whether the teleportation was successful
     */
    boolean teleport(Level level, BlockPos pos);
    
    /**
     * Saves this vehicle's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads this vehicle's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Types of vehicles.
     */
    enum VehicleType {
        /** Ground vehicles that drive on land */
        GROUND,
        /** Air vehicles that fly */
        AIR,
        /** Water vehicles that sail or submarine */
        WATER,
        /** Space vehicles that can travel to other celestial bodies */
        SPACE,
        /** Amphibious vehicles that can travel on both land and water */
        AMPHIBIOUS,
        /** Hover vehicles that float above the ground */
        HOVER,
        /** Submersible vehicles that can travel underwater */
        SUBMERSIBLE,
        /** Multi-terrain vehicles that can handle various environments */
        MULTI_TERRAIN
    }
}