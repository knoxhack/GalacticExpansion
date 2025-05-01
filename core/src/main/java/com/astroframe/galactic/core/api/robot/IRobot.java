package com.astroframe.galactic.core.api.robot;

import com.astroframe.galactic.core.api.energy.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Interface for programmable robots.
 * These robots can perform automated tasks and be programmed with custom behaviors.
 */
public interface IRobot {
    
    /**
     * Gets the unique identifier for this robot.
     * 
     * @return The robot UUID
     */
    UUID getRobotId();
    
    /**
     * Gets the robot's current position.
     * 
     * @return The current position
     */
    BlockPos getPosition();
    
    /**
     * Gets the robot's current facing direction.
     * 
     * @return The facing direction
     */
    Direction getFacing();
    
    /**
     * Sets the robot's facing direction.
     * 
     * @param facing The new facing direction
     */
    void setFacing(Direction facing);
    
    /**
     * Gets the current level the robot is in.
     * 
     * @return The world level
     */
    Level getLevel();
    
    /**
     * Gets the entity representation of this robot.
     * 
     * @return The robot entity if it exists as an entity
     */
    Optional<Entity> getEntity();
    
    /**
     * Gets the robot's energy handler.
     * 
     * @return The energy handler
     */
    IEnergyHandler getEnergyHandler();
    
    /**
     * Gets the type of this robot.
     * 
     * @return The robot type
     */
    RobotType getRobotType();
    
    /**
     * Gets the currently running program.
     * 
     * @return The running program, or null if none
     */
    IRobotProgram getCurrentProgram();
    
    /**
     * Sets the current program.
     * 
     * @param program The program to run
     * @return Whether the program was set successfully
     */
    boolean setProgram(IRobotProgram program);
    
    /**
     * Gets all available programs for this robot.
     * 
     * @return A list of available programs
     */
    List<IRobotProgram> getAvailablePrograms();
    
    /**
     * Executes a command on this robot.
     * 
     * @param command The command to execute
     * @param args Command arguments
     * @return Whether the command executed successfully
     */
    boolean executeCommand(String command, String... args);
    
    /**
     * Moves the robot in a direction.
     * 
     * @param direction The direction to move in
     * @return Whether the movement was successful
     */
    boolean move(Direction direction);
    
    /**
     * Interacts with a block in the specified direction.
     * 
     * @param direction The direction to interact in
     * @return Whether the interaction was successful
     */
    boolean interact(Direction direction);
    
    /**
     * Saves the robot's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads the robot's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
    
    /**
     * Types of robots, each with different capabilities.
     */
    enum RobotType {
        /** Mining robots can break blocks and collect resources */
        MINING,
        /** Combat robots can fight and defend */
        COMBAT,
        /** Farming robots can plant, harvest, and tend crops */
        FARMING,
        /** Builder robots can place blocks according to blueprints */
        BUILDER,
        /** Transport robots can move items between containers */
        TRANSPORT,
        /** Scout robots can explore and map terrain */
        SCOUT,
        /** Utility robots can perform miscellaneous tasks */
        UTILITY,
        /** Advanced robots can perform multiple roles */
        ADVANCED
    }
}