package com.example.modapi.example.common;

import com.astroframe.galactic.energy.api.EnergyType;
import com.astroframe.galactic.energy.api.IEnergyHandler;
import com.example.modapi.machinery.api.MachineBlock;
import com.example.modapi.machinery.api.MachineBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

import java.util.function.Supplier;

/**
 * Example implementation of a machine that uses the ModApi machinery framework.
 */
public class ExampleMachine {
    
    /**
     * Block implementation for the example machine.
     */
    public static class Block extends MachineBlock {
        /**
         * Constructor for the example machine block.
         * 
         * @param properties The block properties
         */
        public Block(Properties properties) {
            super(properties, () -> null); // Block entity type will be set later
        }
        
        @Override
        public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
            return new BlockEntityImpl(pos, state);
        }
        
        @Override
        public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
            return createMachineTicker(level, state, type);
        }
    }
    
    /**
     * Block entity implementation for the example machine.
     */
    public static class BlockEntityImpl extends MachineBlockEntity implements ICapabilityProvider {
        private final ItemStackHandler itemHandler = new ItemStackHandler(getInventorySize());
        
        /**
         * Constructor for the example machine block entity.
         * 
         * @param pos The block position
         * @param state The block state
         */
        public BlockEntityImpl(BlockPos pos, BlockState state) {
            super(null, pos, state); // Block entity type will be set later
        }
        
        @Override
        public String getMachineId() {
            return "example_machine";
        }
        
        @Override
        public String getMachineName() {
            return "Example Machine";
        }
        
        @Override
        public int getMachineTier() {
            return 1;
        }
        
        @Override
        public int getDefaultProcessingTime() {
            return 80;
        }
        
        @Override
        public int getMaxEnergyStored() {
            return 20000;
        }
        
        @Override
        public int getMaxEnergyInput() {
            return 200;
        }
        
        @Override
        public int getMaxEnergyOutput() {
            return 0;
        }
        
        @Override
        public int getEnergyConsumption() {
            return 10;
        }
        
        @Override
        public InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            if (!level.isClientSide) {
                // Display some machine info to the player
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Energy: " + getEnergyStored() + " / " + getMaxEnergyStored()));
                player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Progress: " + Math.round(getProgress() * 100) + "%"));
                
                // Let the player add energy if holding the energy crystal
                ItemStack heldItem = player.getItemInHand(hand);
                if (heldItem.is(com.example.modapi.example.ModApiExample.ENERGY_CRYSTAL.get())) {
                    receiveEnergy(1000, false);
                    player.sendSystemMessage(net.minecraft.network.chat.Component.literal("Added energy!"));
                    setChanged();
                    return InteractionResult.SUCCESS;
                }
            }
            
            return InteractionResult.CONSUME;
        }
        
        @Override
        public int getInventorySize() {
            return 4;
        }
        
        @Override
        public boolean canProcess() {
            ItemStack input = inventory.get(0);
            return !input.isEmpty() && hasRecipe(input);
        }
        
        @Override
        public void processItem() {
            ItemStack input = inventory.get(0);
            ItemStack output = getProcessingResult(input);
            
            // Consume input
            input.shrink(1);
            
            // Add output
            ItemStack currentOutput = inventory.get(1);
            if (currentOutput.isEmpty()) {
                inventory.set(1, output.copy());
            } else if (currentOutput.is(output.getItem())) {
                currentOutput.grow(output.getCount());
            }
            
            setChanged();
        }
        
        /**
         * Checks if there's a valid recipe for the input.
         * 
         * @param input The input item
         * @return Whether there's a valid recipe
         */
        private boolean hasRecipe(ItemStack input) {
            return !getProcessingResult(input).isEmpty();
        }
        
        /**
         * Gets the processing result for an input item.
         * 
         * @param input The input item
         * @return The processing result
         */
        private ItemStack getProcessingResult(ItemStack input) {
            // Simple example recipe
            if (input.is(net.minecraft.world.level.block.Blocks.IRON_ORE.asItem())) {
                return new ItemStack(net.minecraft.world.item.Items.IRON_INGOT, 2);
            } else if (input.is(net.minecraft.world.level.block.Blocks.GOLD_ORE.asItem())) {
                return new ItemStack(net.minecraft.world.item.Items.GOLD_INGOT, 2);
            }
            
            return ItemStack.EMPTY;
        }
        
        @Override
        public IItemHandler getItemHandler(Direction direction) {
            return new InvWrapper(this);
        }

        /**
         * Gets the capability for energy.
         * 
         * @param context The direction
         * @return The energy handler
         */
        public IEnergyHandler getEnergyHandler(Direction context) {
            return this;
        }
    }
}
