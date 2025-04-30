package com.example.modapi.machinery.common;

import com.example.modapi.machinery.api.Machine;
import com.example.modapi.machinery.api.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Basic implementation of a processing machine.
 * Processes input items into output items using energy.
 */
public abstract class ProcessingMachine extends MachineBlockEntity {
    
    /**
     * Constructor for ProcessingMachine.
     * 
     * @param type The block entity type
     * @param pos The block position
     * @param state The block state
     */
    public ProcessingMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    
    @Override
    public int getInventorySize() {
        return 2; // Input and output slots
    }
    
    @Override
    public boolean canProcess() {
        ItemStack inputStack = inventory.get(0);
        ItemStack outputStack = inventory.get(1);
        
        // Check if there's an input
        if (inputStack.isEmpty()) {
            return false;
        }
        
        // Get the processing result
        ItemStack resultStack = getProcessingResult(inputStack);
        
        // Check if there's a valid result
        if (resultStack.isEmpty()) {
            return false;
        }
        
        // Check if the output slot can accept the result
        if (!outputStack.isEmpty()) {
            if (!outputStack.is(resultStack.getItem())) {
                return false;
            }
            
            int resultCount = outputStack.getCount() + resultStack.getCount();
            return resultCount <= outputStack.getMaxStackSize();
        }
        
        return true;
    }
    
    @Override
    public void processItem() {
        if (canProcess()) {
            ItemStack inputStack = inventory.get(0);
            ItemStack outputStack = inventory.get(1);
            ItemStack resultStack = getProcessingResult(inputStack);
            
            // Consume input
            inputStack.shrink(1);
            
            // Add output
            if (outputStack.isEmpty()) {
                inventory.set(1, resultStack.copy());
            } else {
                outputStack.grow(resultStack.getCount());
            }
        }
    }
    
    /**
     * Gets the result of processing an input item.
     * 
     * @param input The input item
     * @return The processing result
     */
    protected abstract ItemStack getProcessingResult(ItemStack input);
    
    /**
     * Basic tier processing machine.
     */
    public static class Basic extends ProcessingMachine {
        
        /**
         * Constructor for Basic processing machine.
         */
        public Basic() {
            this(null, BlockPos.ZERO, null);
        }
        
        /**
         * Constructor for Basic processing machine.
         * 
         * @param type The block entity type
         * @param pos The block position
         * @param state The block state
         */
        public Basic(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }
        
        @Override
        public String getMachineId() {
            return "basic_processor";
        }
        
        @Override
        public String getMachineName() {
            return "Basic Processor";
        }
        
        @Override
        public int getMachineTier() {
            return 1;
        }
        
        @Override
        public int getDefaultProcessingTime() {
            return 100;
        }
        
        @Override
        public int getMaxEnergyStored() {
            return 10000;
        }
        
        @Override
        public int getMaxEnergyInput() {
            return 100;
        }
        
        @Override
        public int getMaxEnergyOutput() {
            return 0;
        }
        
        @Override
        public int getEnergyConsumption() {
            return 20;
        }
        
        @Override
        protected ItemStack getProcessingResult(ItemStack input) {
            // Simple example: stone -> cobblestone
            if (input.is(net.minecraft.world.level.block.Blocks.STONE.asItem())) {
                return new ItemStack(net.minecraft.world.level.block.Blocks.COBBLESTONE);
            }
            
            return ItemStack.EMPTY;
        }
    }
    
    /**
     * Advanced tier processing machine.
     */
    public static class Advanced extends ProcessingMachine {
        
        /**
         * Constructor for Advanced processing machine.
         */
        public Advanced() {
            this(null, BlockPos.ZERO, null);
        }
        
        /**
         * Constructor for Advanced processing machine.
         * 
         * @param type The block entity type
         * @param pos The block position
         * @param state The block state
         */
        public Advanced(BlockEntityType<?> type, BlockPos pos, BlockState state) {
            super(type, pos, state);
        }
        
        @Override
        public String getMachineId() {
            return "advanced_processor";
        }
        
        @Override
        public String getMachineName() {
            return "Advanced Processor";
        }
        
        @Override
        public int getMachineTier() {
            return 2;
        }
        
        @Override
        public int getDefaultProcessingTime() {
            return 50;
        }
        
        @Override
        public int getMaxEnergyStored() {
            return 50000;
        }
        
        @Override
        public int getMaxEnergyInput() {
            return 500;
        }
        
        @Override
        public int getMaxEnergyOutput() {
            return 0;
        }
        
        @Override
        public int getEnergyConsumption() {
            return 40;
        }
        
        @Override
        protected ItemStack getProcessingResult(ItemStack input) {
            // More efficient processing: 1 input -> 2 output
            if (input.is(net.minecraft.world.level.block.Blocks.STONE.asItem())) {
                return new ItemStack(net.minecraft.world.level.block.Blocks.COBBLESTONE, 2);
            }
            
            return ItemStack.EMPTY;
        }
        
        @Override
        public int getInventorySize() {
            return 4; // More inventory slots for advanced machine
        }
    }
}
