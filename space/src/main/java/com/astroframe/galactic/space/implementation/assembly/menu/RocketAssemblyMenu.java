package com.astroframe.galactic.space.implementation.assembly.menu;

import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
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
     * Gets the block entity from the world.
     * 
     * @param inventory The player inventory
     * @param data The additional data from the server
     * @return The block entity, or null if not found
     */
    private RocketAssemblyTableBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf data) {
        BlockEntity blockEntity = inventory.player.level().getBlockEntity(data.readBlockPos());
        
        if (blockEntity instanceof RocketAssemblyTableBlockEntity) {
            return (RocketAssemblyTableBlockEntity) blockEntity;
        }
        
        return null;
    }
    
    /**
     * Adds the component slots to the menu.
     * 
     * @param container The container to add slots for
     */
    private void addComponentSlots(Container container) {
        // Add 3x3 grid of component slots
        int slotIndex = 0;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new ComponentSlot(container, slotIndex++, 44 + col * 24, 20 + row * 24));
            }
        }
    }
    
    /**
     * Adds the player inventory slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerInventory(Inventory playerInventory) {
        // Add player inventory (3 rows x 9 columns)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }
    
    /**
     * Adds the player hotbar slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        // Add hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
    
    /**
     * Validates the rocket components.
     * 
     * @return True if the rocket is valid
     */
    public boolean validateRocket() {
        if (blockEntity == null) {
            return false;
        }
        
        // Get the rocket components
        List<IRocketComponent> components = new ArrayList<>();
        
        // We'd normally extract components from items here
        // For now, use the components in the rocket data
        components.addAll(blockEntity.getRocketData().getAllComponents());
        
        // Validate components using the validator
        List<String> errors = new ArrayList<>();
        boolean valid = validator.validateComponents(components, errors);
        
        // Update the block entity
        blockEntity.setValidationStatus(valid, errors);
        
        return valid;
    }
    
    /**
     * Called when the menu is closed.
     * 
     * @param player The player
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
        
        // Update validation status when menu is closed
        if (!player.level().isClientSide && player instanceof ServerPlayer) {
            validateRocket();
            blockEntity.syncToClient();
        }
    }
    
    /**
     * Checks if the player can still use the menu.
     * 
     * @param player The player
     * @return True if the player can still use the menu
     */
    @Override
    public boolean stillValid(Player player) {
        if (blockEntity != null) {
            return stillValid(access, player, blockEntity.getBlockState().getBlock());
        }
        return false;
    }
    
    /**
     * Moves an item between inventory slots.
     * 
     * @param player The player
     * @param sourceSlotIndex The source slot index
     * @return The remaining item stack
     */
    @Override
    public ItemStack quickMoveStack(Player player, int sourceSlotIndex) {
        Slot sourceSlot = slots.get(sourceSlotIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack sourceStackCopy = sourceStack.copy();
        
        // Moving from component grid to player inventory
        if (sourceSlotIndex < INVENTORY_START) {
            if (!moveItemStackTo(sourceStack, INVENTORY_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        }
        // Moving from player inventory to component grid
        else if (sourceSlotIndex < HOTBAR_END) {
            // Check if this is a component
            if (isRocketComponentItem(sourceStack)) {
                if (!moveItemStackTo(sourceStack, 0, INVENTORY_START, false)) {
                    return ItemStack.EMPTY;
                }
            }
            // Move between player inventory and hotbar
            else if (sourceSlotIndex < INVENTORY_END) {
                if (!moveItemStackTo(sourceStack, HOTBAR_START, HOTBAR_END, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (sourceSlotIndex < HOTBAR_END && !moveItemStackTo(sourceStack, INVENTORY_START, INVENTORY_END, false)) {
                return ItemStack.EMPTY;
            }
        }
        
        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        
        sourceSlot.onTake(player, sourceStack);
        return sourceStackCopy;
    }
    
    /**
     * Checks if an item is a rocket component.
     * 
     * @param stack The item stack
     * @return True if the item is a rocket component
     */
    private boolean isRocketComponentItem(ItemStack stack) {
        // In a full implementation, we'd check if the item is a rocket component
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