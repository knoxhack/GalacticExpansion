package com.astroframe.galactic.machinery.blockentity;

import com.astroframe.galactic.machinery.api.MachineBlockEntity;
import com.astroframe.galactic.machinery.api.MachineType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Block entity implementation for the Assembler machine.
 */
public class AssemblerBlockEntity extends MachineBlockEntity {

    /**
     * Creates a new Assembler block entity.
     *
     * @param pos The position
     * @param state The block state
     */
    public AssemblerBlockEntity(BlockPos pos, BlockState state) {
        super(MachineryBlockEntities.ASSEMBLER.get(), pos, state);
    }
    
    /**
     * Called when the block is activated (right-clicked).
     *
     * @param state The block state
     * @param level The level
     * @param pos The position
     * @param player The player
     * @param hand The hand
     * @param hit The hit result
     * @return The interaction result
     */
    @Override
    public InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // For now, just return success to acknowledge the interaction
        return InteractionResult.SUCCESS;
    }
    
    /**
     * Called every tick to update the block entity.
     *
     * @param level The level
     * @param pos The position
     * @param state The block state
     */
    @Override
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        // No processing logic yet
    }
    
    /**
     * Process an item in this machine.
     * This is the main processing logic for the machine.
     */
    @Override
    public void processItem() {
        // No processing logic yet
    }
    
    /**
     * Gets the default processing time for this machine.
     *
     * @return The default processing time in ticks
     */
    @Override
    public int getDefaultProcessingTime() {
        return 200; // 10 seconds
    }
    
    /**
     * Gets the size of the inventory.
     *
     * @return The inventory size
     */
    @Override
    public int getInventorySize() {
        return 9; // 3x3 crafting grid
    }
    
    /**
     * Gets the maximum energy this machine can store.
     *
     * @return The maximum energy
     */
    @Override
    public int getMaxEnergy() {
        return 10000;
    }
    
    /**
     * Gets the maximum energy input rate.
     *
     * @return The maximum energy input rate
     */
    @Override
    public int getMaxEnergyInput() {
        return 100;
    }
    
    /**
     * Gets the maximum energy output rate.
     *
     * @return The maximum energy output rate
     */
    @Override
    public int getMaxEnergyOutput() {
        return 0; // This machine does not output energy
    }
    
    /**
     * Gets the energy consumption rate per tick.
     *
     * @return The energy consumption rate
     */
    @Override
    public int getEnergyConsumption() {
        return 10;
    }
    
    /**
     * Gets the type of this machine.
     *
     * @return The machine type
     */
    public MachineType getMachineType() {
        return MachineType.PROCESSOR;
    }
    
    /**
     * Checks if the machine can process its current inputs.
     *
     * @return True if the machine can process
     */
    @Override
    public boolean canProcess() {
        // Basic implementation - we'll expand this later
        return true;
    }
    
    /**
     * Stops the machine's processing.
     * Required by the Machine interface.
     * 
     * @return True if the machine was stopped successfully
     */
    @Override
    public boolean stop() {
        // Stop processing
        this.isActive = false;
        this.processingTime = 0;
        return true;
    }
}