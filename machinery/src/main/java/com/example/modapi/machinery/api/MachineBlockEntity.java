package com.example.modapi.machinery.api;

import com.example.modapi.energy.api.EnergyStorage;
import com.example.modapi.energy.api.EnergyUnit;
import com.example.modapi.energy.api.IEnergyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Base block entity class for machines.
 * Implements common machine functionality including energy storage and basic inventory.
 */
public abstract class MachineBlockEntity extends BlockEntity implements Machine {
    
    protected final EnergyStorage energyStorage;
    protected final NonNullList<ItemStack> inventory;
    protected int processingTime;
    protected int processingTimeTotal;
    protected boolean isActive;
    
    /**
     * Constructor for MachineBlockEntity.
     * 
     * @param type The block entity type
     * @param pos The block position
     * @param state The block state
     */
    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.energyStorage = createEnergyStorage();
        this.inventory = NonNullList.withSize(getInventorySize(), ItemStack.EMPTY);
        this.processingTime = 0;
        this.processingTimeTotal = getDefaultProcessingTime();
        this.isActive = false;
    }
    
    /**
     * Creates the energy storage for this machine.
     * 
     * @return A new energy storage
     */
    protected EnergyStorage createEnergyStorage() {
        return new EnergyStorage(getMaxEnergyStored(), getMaxEnergyInput(), getMaxEnergyOutput(), 0, EnergyUnit.FORGE_ENERGY);
    }
    
    /**
     * Server-side tick for the machine.
     * 
     * @param level The world
     * @param pos The block position
     * @param state The block state
     */
    public void serverTick(Level level, BlockPos pos, BlockState state) {
        boolean wasActive = isActive;
        
        // Machine logic
        tick(level, pos);
        
        // Update active state if changed
        if (wasActive != isActive) {
            MachineBlock.setActive(isActive, level, pos);
            setChanged();
        }
    }
    
    @Override
    public void tick(Level level, BlockPos pos) {
        if (level.isClientSide) {
            return;
        }
        
        boolean canProcess = canProcess();
        
        if (canProcess && consumeEnergy()) {
            isActive = true;
            
            // Increment processing time
            processingTime++;
            
            if (processingTime >= processingTimeTotal) {
                processingTime = 0;
                processItem();
            }
        } else {
            isActive = false;
            
            // Reset progress if can't process
            if (!canProcess && processingTime > 0) {
                processingTime = 0;
            }
        }
        
        setChanged();
    }
    
    /**
     * Called when a player interacts with the machine.
     * 
     * @param state The block state
     * @param level The world
     * @param pos The block position
     * @param player The player
     * @param hand The hand used
     * @param hit The hit result
     * @return The interaction result
     */
    public InteractionResult onBlockActivated(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        // Default implementation - override to open GUI, etc.
        return InteractionResult.PASS;
    }
    
    /**
     * Drops the machine's inventory contents in the world.
     * 
     * @param level The world
     * @param pos The block position
     */
    public void dropContents(Level level, BlockPos pos) {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
            }
        }
    }
    
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        
        // Save inventory
        ContainerHelper.saveAllItems(tag, this.inventory);
        
        // Save energy
        tag.put("Energy", energyStorage.serializeNBT(new CompoundTag()));
        
        // Save processing state
        tag.putInt("ProcessingTime", processingTime);
        tag.putInt("ProcessingTimeTotal", processingTimeTotal);
        tag.putBoolean("IsActive", isActive);
    }
    
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        
        // Load inventory
        ContainerHelper.loadAllItems(tag, this.inventory);
        
        // Load energy
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(tag.getCompound("Energy"));
        }
        
        // Load processing state
        processingTime = tag.getInt("ProcessingTime");
        processingTimeTotal = tag.getInt("ProcessingTimeTotal");
        isActive = tag.getBoolean("IsActive");
    }
    
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
    
    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }
    
    /**
     * Gets the size of the machine's inventory.
     * 
     * @return The inventory size
     */
    public abstract int getInventorySize();
    
    /**
     * Gets the default processing time for this machine.
     * 
     * @return The processing time
     */
    public abstract int getDefaultProcessingTime();
    
    /**
     * Checks if the machine can process its current inputs.
     * 
     * @return True if the machine can process
     */
    public abstract boolean canProcess();
    
    /**
     * Processes the current inputs, consuming them and producing outputs.
     */
    public abstract void processItem();
    
    @Override
    public boolean isActive() {
        return isActive;
    }
    
    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }
    
    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }
    
    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }
    
    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }
    
    @Override
    public boolean canExtract() {
        return energyStorage.canExtract();
    }
    
    @Override
    public boolean canReceive() {
        return energyStorage.canReceive();
    }
    
    @Override
    public EnergyUnit getEnergyUnit() {
        return energyStorage.getEnergyUnit();
    }
    
    /**
     * Gets the processing progress as a percentage.
     * 
     * @return The progress from 0.0 to 1.0
     */
    public float getProgress() {
        if (processingTimeTotal <= 0) return 0;
        return (float) processingTime / processingTimeTotal;
    }
    
    /**
     * Gets the item handler capability for this machine.
     * 
     * @param direction The direction to get the capability from
     * @return The item handler or null
     */
    public IItemHandler getItemHandler(Direction direction) {
        // To be implemented by subclasses with inventory
        return null;
    }
}
