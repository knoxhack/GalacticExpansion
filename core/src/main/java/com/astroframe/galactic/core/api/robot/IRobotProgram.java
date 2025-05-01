package com.astroframe.galactic.core.api.robot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Map;

/**
 * Interface for robot programs.
 * Programs define the behavior and tasks that robots can perform.
 */
public interface IRobotProgram {
    
    /**
     * Gets the unique identifier for this program.
     * 
     * @return The program ID
     */
    ResourceLocation getProgramId();
    
    /**
     * Gets the display name of this program.
     * 
     * @return The display name
     */
    String getDisplayName();
    
    /**
     * Gets the description of this program.
     * 
     * @return The description
     */
    String getDescription();
    
    /**
     * Gets the author of this program.
     * 
     * @return The author
     */
    String getAuthor();
    
    /**
     * Gets the version of this program.
     * 
     * @return The version
     */
    String getVersion();
    
    /**
     * Checks if this program is compatible with the given robot.
     * 
     * @param robot The robot to check
     * @return Whether the program is compatible
     */
    boolean isCompatibleWith(IRobot robot);
    
    /**
     * Gets the script or code of this program.
     * 
     * @return The program code
     */
    String getProgramCode();
    
    /**
     * Sets the program code.
     * 
     * @param code The new code
     * @return Whether the code was set successfully
     */
    boolean setProgramCode(String code);
    
    /**
     * Initializes the program for a robot.
     * 
     * @param robot The robot to initialize for
     * @return Whether initialization was successful
     */
    boolean initialize(IRobot robot);
    
    /**
     * Updates the program, executing a single tick of logic.
     * 
     * @param robot The robot running the program
     * @return Whether the update was successful
     */
    boolean update(IRobot robot);
    
    /**
     * Terminates the program, cleaning up any resources.
     * 
     * @param robot The robot running the program
     */
    void terminate(IRobot robot);
    
    /**
     * Pauses the program execution.
     * 
     * @param robot The robot running the program
     */
    void pause(IRobot robot);
    
    /**
     * Resumes the program execution.
     * 
     * @param robot The robot running the program
     */
    void resume(IRobot robot);
    
    /**
     * Checks if the program is currently running.
     * 
     * @return Whether the program is running
     */
    boolean isRunning();
    
    /**
     * Checks if the program is currently paused.
     * 
     * @return Whether the program is paused
     */
    boolean isPaused();
    
    /**
     * Gets the required resources for this program to run.
     * 
     * @return A map of resource names to required amounts
     */
    Map<String, Integer> getRequiredResources();
    
    /**
     * Gets the items required for this program to run.
     * 
     * @return A list of required items
     */
    List<ItemStack> getRequiredItems();
    
    /**
     * Saves the program's state to NBT.
     * 
     * @param tag The tag to save to
     * @return The updated tag
     */
    CompoundTag save(CompoundTag tag);
    
    /**
     * Loads the program's state from NBT.
     * 
     * @param tag The tag to load from
     */
    void load(CompoundTag tag);
}