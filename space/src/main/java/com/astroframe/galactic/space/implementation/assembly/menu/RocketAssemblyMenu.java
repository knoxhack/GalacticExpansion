package com.astroframe.galactic.space.implementation.assembly.menu;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import com.astroframe.galactic.space.implementation.common.RocketDataProvider;
import com.astroframe.galactic.space.implementation.component.ComponentValidator;
import com.astroframe.galactic.space.registry.SpaceMenus;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu for the Rocket Assembly Table.
 * Manages the user interface for rocket assembly.
 */
public class RocketAssemblyMenu extends AbstractContainerMenu {
    
    // Constants
    private static final int INVENTORY_START = 9; // After component slots
    private static final int INVENTORY_END = 36; // End of player inventory slots
    private static final int HOTBAR_START = 36;
    private static final int HOTBAR_END = 45;
    
    // The block entity
    private final RocketAssemblyTableBlockEntity blockEntity;
    
    // The rocket data provider (interface-based approach to avoid circular dependencies)
    private final RocketDataProvider rocketDataProvider;
    
    // Component slots (3x3 grid)
    private static final int COMPONENT_SLOTS = 9;
    
    // The container level access
    private final ContainerLevelAccess access;
    
    // Temporary inventory for when we don't have a block entity
    private final Container tempInventory;
    
    // Component validator
    private final ComponentValidator validator;
    
    /**
     * Creates a new rocket assembly menu.
     * Used on the server side.
     * 
     * @param containerId The container ID
     * @param playerInventory The player inventory
     * @param blockEntity The block entity
     * @param access The container level access
     */
    public RocketAssemblyMenu(int containerId, Inventory playerInventory, 
                              RocketAssemblyTableBlockEntity blockEntity, 
                              ContainerLevelAccess access) {
        super(SpaceMenus.ROCKET_ASSEMBLY_MENU.get(), containerId);
        
        this.blockEntity = blockEntity;
        this.rocketDataProvider = blockEntity; // Cast to interface
        this.access = access;
        this.tempInventory = null;
        this.validator = new ComponentValidator();
        
        // Add component slots (3x3 grid)
        addComponentSlots(blockEntity);
        
        // Add player inventory
        addPlayerInventory(playerInventory);
        
        // Add player hotbar
        addPlayerHotbar(playerInventory);
    }
    
    /**
     * Creates a new rocket assembly menu.
     * Used on the client side.
     * 
     * @param containerId The container ID
     * @param playerInventory The player inventory
     * @param data The additional data from the server
     */
    public RocketAssemblyMenu(int containerId, Inventory playerInventory, FriendlyByteBuf data) {
        super(SpaceMenus.ROCKET_ASSEMBLY_MENU.get(), containerId);
        
        // Get block entity from world
        this.tempInventory = new SimpleContainer(COMPONENT_SLOTS);
        this.blockEntity = getBlockEntity(playerInventory, data);
        this.rocketDataProvider = this.blockEntity; // Cast to interface
        this.access = this.blockEntity != null ? 
                ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()) : 
                ContainerLevelAccess.NULL;
        this.validator = new ComponentValidator();
        
        // Add component slots
        if (blockEntity != null) {
            addComponentSlots(blockEntity);
        } else {
            addComponentSlots(tempInventory);
        }
        
        // Add player inventory
        addPlayerInventory(playerInventory);
        
        // Add player hotbar
        addPlayerHotbar(playerInventory);
    }
    
    /**
     * Gets the block entity from the network data.
     * 
     * @param playerInventory The player inventory
     * @param data The additional data from the server
     * @return The block entity
     */
    private RocketAssemblyTableBlockEntity getBlockEntity(Inventory playerInventory, FriendlyByteBuf data) {
        if (data == null) return null;
        
        try {
            // Read block position from extra data
            BlockEntity be = playerInventory.player.level().getBlockEntity(data.readBlockPos());
            
            if (be instanceof RocketAssemblyTableBlockEntity rocketAssemblyTable) {
                return rocketAssemblyTable;
            }
        } catch (Exception e) {
            // Handle parsing errors gracefully
            return null;
        }
        
        return null;
    }
    
    /**
     * Adds the component slots to the menu.
     * 
     * @param container The container to add slots for
     */
    private void addComponentSlots(Container container) {
        // 3x3 grid of component slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new ComponentSlot(container, col + row * 3, 
                        30 + col * 18, 17 + row * 18));
            }
        }
    }
    
    /**
     * Adds the player inventory slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerInventory(Inventory playerInventory) {
        // 3 rows of 9 slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 
                        8 + col * 18, 84 + row * 18));
            }
        }
    }
    
    /**
     * Adds the player hotbar slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        // 1 row of 9 slots
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
    
    /**
     * Called when the menu is closed.
     * 
     * @param player The player
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
        
        // Return items to the player if they close the menu
        if (player instanceof ServerPlayer && blockEntity != null) {
            // Already handled by AbstractContainerMenu
        }
    }
    
    /**
     * Check if the player can still interact with the menu.
     * 
     * @param player The player
     * @return True if the player can still interact with the menu
     */
    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null ? 
                stillValid(access, player, blockEntity.getBlockState().getBlock()) : 
                false;
    }
    
    /**
     * Handle item slot transfers (shift-clicking).
     * 
     * @param player The player
     * @param slotIndex The slot index being moved
     * @return The resulting item stack
     */
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        Slot sourceSlot = slots.get(slotIndex);
        
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack sourceStackCopy = sourceStack.copy();
        
        // Check if the slot is in the component area, player inventory, or hotbar
        if (slotIndex < INVENTORY_START) {
            // Component slot to player inventory or hotbar
            if (!moveItemStackTo(sourceStack, INVENTORY_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (slotIndex < INVENTORY_END) {
            // Player inventory to component slots (if valid) or hotbar
            if (isRocketComponentItem(sourceStack) && 
                    !moveItemStackTo(sourceStack, 0, INVENTORY_START, false)) {
                // If it's a component but can't go into component slots, try hotbar
                if (!moveItemStackTo(sourceStack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Non-component items to hotbar
                if (!moveItemStackTo(sourceStack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        } else if (slotIndex < HOTBAR_END) {
            // Hotbar to component slots (if valid) or player inventory
            if (isRocketComponentItem(sourceStack) && 
                    !moveItemStackTo(sourceStack, 0, INVENTORY_START, false)) {
                // If it's a component but can't go into component slots, try inventory
                if (!moveItemStackTo(sourceStack, INVENTORY_START, INVENTORY_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Non-component items to inventory
                if (!moveItemStackTo(sourceStack, INVENTORY_START, INVENTORY_END, false)) {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        // Handle slot updates
        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        
        sourceSlot.onTake(player, sourceStack);
        return sourceStackCopy;
    }
    
    /**
     * Check if an item is a valid rocket component.
     * 
     * @param stack The item stack
     * @return True if it's a valid rocket component
     */
    private boolean isRocketComponentItem(ItemStack stack) {
        // For now, allow any item as a component
        return true;
    }
    
    /**
     * Gets the block entity.
     * 
     * @return The block entity
     */
    public RocketAssemblyTableBlockEntity getBlockEntity() {
        return blockEntity;
    }
    
    /**
     * Gets the rocket data provider.
     * 
     * @return The rocket data provider
     */
    public RocketDataProvider getRocketDataProvider() {
        return rocketDataProvider;
    }
    
    /**
     * A slot for rocket components.
     */
    private class ComponentSlot extends Slot {
        
        /**
         * Creates a new component slot.
         * 
         * @param container The container
         * @param index The slot index
         * @param x The X position
         * @param y The Y position
         */
        public ComponentSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }
        
        /**
         * Check if the item can be placed in this slot.
         * 
         * @param stack The item stack
         * @return True if the item can be placed in this slot
         */
        @Override
        public boolean mayPlace(ItemStack stack) {
            return isRocketComponentItem(stack);
        }
        
        /**
         * Called when an item is changed in this slot.
         */
        @Override
        public void setChanged() {
            super.setChanged();
            
            // Update the rocket data in the block entity
            if (blockEntity != null) {
                blockEntity.setChanged();
            }
        }
    }
}