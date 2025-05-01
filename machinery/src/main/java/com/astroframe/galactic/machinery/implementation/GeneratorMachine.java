package com.astroframe.galactic.machinery.implementation;

import com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.machinery.api.MachineType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * A machine that generates energy.
 * This is a specialized machine that adds energy to its energy storage over time.
 */
public class GeneratorMachine extends BaseMachine {
    
    private final int energyProduced;
    private final int maxFuel;
    private int currentFuel;
    
    /**
     * Construct a new generator machine.
     * 
     * @param name The name of the generator
     * @param energyStorage The energy storage for this generator
     * @param efficiency The efficiency of this generator (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one generation cycle
     * @param energyProduced The amount of energy produced per cycle
     * @param maxFuel The maximum amount of fuel this generator can hold
     */
    public GeneratorMachine(String name, EnergyStorage energyStorage, 
                           float efficiency, int processingTime,
                           int energyProduced, int maxFuel) {
        super(name, MachineType.GENERATOR, energyStorage, efficiency, processingTime, 0);
        this.energyProduced = Math.max(1, energyProduced);
        this.maxFuel = Math.max(1, maxFuel);
        this.currentFuel = 0;
    }
    
    /**
     * Add fuel to the generator.
     * 
     * @param amount The amount of fuel to add
     * @return The amount of fuel that was actually added
     */
    public int addFuel(int amount) {
        int fuelToAdd = Math.min(amount, maxFuel - currentFuel);
        currentFuel += fuelToAdd;
        return fuelToAdd;
    }
    
    /**
     * Get the current amount of fuel in the generator.
     * 
     * @return The current fuel amount
     */
    public int getCurrentFuel() {
        return currentFuel;
    }
    
    /**
     * Get the maximum amount of fuel the generator can hold.
     * 
     * @return The maximum fuel capacity
     */
    public int getMaxFuel() {
        return maxFuel;
    }
    
    /**
     * Get the amount of energy produced per cycle.
     * 
     * @return The energy produced
     */
    public int getEnergyProduced() {
        return energyProduced;
    }
    
    @Override
    public int getEnergyConsumption() {
        // Generators produce energy, they don't consume it
        return 0;
    }
    
    @Override
    public int getMaxEnergyOutput() {
        // Generators can output energy at the rate they produce it
        return energyProduced;
    }
    
    @Override
    public int getMaxEnergyInput() {
        // Generators don't receive energy from external sources
        return 0;
    }
    
    @Override
    protected boolean canStartProcessing() {
        if (energyStorage == null) {
            return false;
        }
        
        // Check if we have fuel and if the energy storage isn't full
        return currentFuel > 0 && energyStorage.canReceive();
    }
    
    @Override
    protected void completeProcessing() {
        if (energyStorage != null) {
            // Add energy to storage based on efficiency
            int actualEnergyProduced = Math.round(energyProduced * efficiency);
            energyStorage.receiveEnergy(actualEnergyProduced, false);
        }
        
        // Consume fuel
        currentFuel--;
    }
    
    @Override
    protected boolean canContinueProcessing() {
        if (energyStorage == null) {
            return false;
        }
        
        // Check if we have fuel and if the energy storage isn't full
        return currentFuel > 0 && energyStorage.canReceive();
    }
    
    @Override
    public void tick(Level level, BlockPos pos) {
        if (!active) {
            // Auto-start if we have fuel and can generate
            if (canStartProcessing()) {
                start();
            } else {
                return;
            }
        }
        
        // Check if we can still generate
        if (!canContinueProcessing()) {
            stop();
            return;
        }
        
        // Update progress
        float progressPerTick = 1.0f / (processingTime / efficiency);
        progress += progressPerTick;
        
        // Check if processing is complete
        if (progress >= 1.0f) {
            completeProcessing();
            progress = 0.0f;
            
            // Check if we should continue processing
            if (!canContinueProcessing()) {
                stop();
            }
        }
    }
    
    // For backward compatibility
    @Override
    public void tick() {
        // This is now a stub - the real logic is in tick(Level, BlockPos)
    }
}