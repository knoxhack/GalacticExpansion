package com.astroframe.galactic.machinery.implementation;

import com.astroframe.galactic.core.api.energy.IEnergyHandler.EnergyUnit;
import com.astroframe.galactic.energy.api.EnergyStorage;
import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.machinery.api.MachineType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

/**
 * A machine that processes items.
 * This is a specialized machine that transforms input items into output items using energy.
 */
public class ProcessorMachine extends BaseMachine {
    
    private final int inputSlots;
    private final int outputSlots;
    private Object[] inputs;  // Placeholder for actual item instances
    private Object[] outputs; // Placeholder for actual item instances
    private Supplier<Boolean> recipeValidator;
    private Runnable recipeProcessor;
    
    /**
     * Construct a new processor machine.
     * 
     * @param name The name of the processor
     * @param energyStorage The energy storage for this processor
     * @param efficiency The efficiency of this processor (0.0 to 1.0)
     * @param processingTime The base time (in ticks) it takes to complete one processing cycle
     * @param energyPerTick The amount of energy consumed per tick while active
     * @param inputSlots The number of input slots
     * @param outputSlots The number of output slots
     */
    public ProcessorMachine(String name, EnergyStorage energyStorage, 
                           float efficiency, int processingTime, int energyPerTick,
                           int inputSlots, int outputSlots) {
        super(name, MachineType.PROCESSOR, energyStorage, efficiency, processingTime, energyPerTick);
        this.inputSlots = Math.max(1, inputSlots);
        this.outputSlots = Math.max(1, outputSlots);
        this.inputs = new Object[inputSlots];
        this.outputs = new Object[outputSlots];
        
        // Default recipe validator (always fails)
        this.recipeValidator = () -> false;
        
        // Default recipe processor (does nothing)
        this.recipeProcessor = () -> {};
    }
    
    @Override
    public int getEnergyConsumption() {
        // Return the energy per tick value stored in the BaseMachine class
        return energyPerTick;
    }
    
    @Override
    public int getMaxEnergyOutput() {
        // Processors don't output energy
        return 0;
    }
    
    @Override
    public int getMaxEnergyInput() {
        // Processors need to receive energy to operate
        // Default to twice the consumption rate to allow for some buffer
        return energyPerTick * 2;
    }
    
    /**
     * Set a recipe validator function.
     * This function should check if the current inputs can be processed.
     * 
     * @param validator A function that returns true if the inputs can be processed
     */
    public void setRecipeValidator(Supplier<Boolean> validator) {
        this.recipeValidator = validator != null ? validator : () -> false;
    }
    
    /**
     * Set a recipe processor function.
     * This function should consume inputs and produce outputs.
     * 
     * @param processor A function that processes the inputs into outputs
     */
    public void setRecipeProcessor(Runnable processor) {
        this.recipeProcessor = processor != null ? processor : () -> {};
    }
    
    /**
     * Get the input slots for this processor.
     * 
     * @return The number of input slots
     */
    public int getInputSlots() {
        return inputSlots;
    }
    
    /**
     * Get the output slots for this processor.
     * 
     * @return The number of output slots
     */
    public int getOutputSlots() {
        return outputSlots;
    }
    
    /**
     * Get the inputs array.
     * Note: This is a placeholder; in a real implementation, this would return a collection of items.
     * 
     * @return The inputs array
     */
    public Object[] getInputs() {
        return inputs;
    }
    
    /**
     * Get the outputs array.
     * Note: This is a placeholder; in a real implementation, this would return a collection of items.
     * 
     * @return The outputs array
     */
    public Object[] getOutputs() {
        return outputs;
    }
    
    @Override
    protected boolean canStartProcessing() {
        if (energyStorage != null && energyStorage.getEnergy() < energyPerTick) {
            return false;
        }
        
        // Use the recipe validator to check if we can process the current inputs
        return recipeValidator.get();
    }
    
    @Override
    protected void completeProcessing() {
        // Process the recipe
        recipeProcessor.run();
    }
    
    @Override
    protected boolean canContinueProcessing() {
        if (energyStorage != null && energyStorage.getEnergy() < energyPerTick) {
            return false;
        }
        
        // Use the recipe validator to check if we can process the current inputs
        return recipeValidator.get();
    }
    
    @Override
    public void tick(Level level, BlockPos pos) {
        // Delegate to parent implementation
        super.tick(level, pos);
    }
    
    // For backward compatibility
    @Override
    public void tick() {
        // This is now a stub - the real logic is in tick(Level, BlockPos)
    }
    
    /**
     * Whether this machine can receive energy.
     * Processors need to receive energy to operate.
     * 
     * @return True since processors need to receive energy
     */
    @Override
    public boolean canReceive() {
        return true;
    }
    
    /**
     * Whether this machine can extract energy.
     * Processors consume energy, they don't produce it.
     * 
     * @return False since processors don't extract energy
     */
    @Override
    public boolean canExtract() {
        return false;
    }
    
    @Override
    public EnergyUnit getEnergyUnit() {
        return EnergyUnit.GALACTIC_ENERGY_UNIT;
    }
}