package com.astroframe.galactic.machinery.api;

import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Base interface for machinery in the Galactic Expansion mod.
 * Machines are advanced blocks that can process items and energy.
 */
public interface Machine {
    /**
     * Get the name of the machine.
     * This is used for registration and display purposes.
     * 
     * @return The machine name
     */
    String getName();
    
    /**
     * Get the energy storage for this machine.
     * 
     * @return An Optional containing the energy storage, or empty if this machine doesn't use energy
     */
    Optional<EnergyStorage> getEnergyStorage();
    
    /**
     * Get the acceptable energy types for this machine.
     * 
     * @return A list of energy types this machine can work with
     */
    List<EnergyType> getAcceptableEnergyTypes();
    
    /**
     * Called once per tick to update the machine's state.
     * This is where processing logic should be implemented.
     */
    void tick();
    
    /**
     * Check if the machine is currently active (processing).
     * 
     * @return true if the machine is active, false otherwise
     */
    boolean isActive();
    
    /**
     * Start the machine if it's not already running.
     * 
     * @return true if the machine was started, false if it was already running or couldn't be started
     */
    boolean start();
    
    /**
     * Stop the machine if it's running.
     * 
     * @return true if the machine was stopped, false if it wasn't running
     */
    boolean stop();
    
    /**
     * Get the current progress of the processing operation.
     * 
     * @return A value between 0.0 and 1.0 representing the progress, or 0.0 if inactive
     */
    float getProgress();
    
    /**
     * Get the efficiency of this machine.
     * Efficiency affects energy consumption and processing speed.
     * 
     * @return A value between 0.0 and 1.0 representing the efficiency
     */
    float getEfficiency();
}