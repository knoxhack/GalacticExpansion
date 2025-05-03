package com.astroframe.galactic.space.implementation.assembly.menu;

import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import com.astroframe.galactic.space.implementation.common.RocketDataProvider;
import com.astroframe.galactic.space.registry.SpaceBlocks;
import com.astroframe.galactic.space.registry.SpaceMenus;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Menu for the rocket assembly table.
 */
public class RocketAssemblyMenu extends AbstractContainerMenu {
    
    private final Container container;
    private final RocketDataProvider rocketDataProvider;
    private final ContainerLevelAccess access;
    
    // Store component validation state
    private boolean componentsValid = false;
    
    /**
     * Creates a new rocket assembly menu (server-side).
     *
     * @param containerId The container ID
     * @param playerInventory The player inventory
     * @param container The container
     * @param access The level access
     */
    public RocketAssemblyMenu(int containerId, Inventory playerInventory, 
                             Container container, ContainerLevelAccess access) {
        super(SpaceMenus.ROCKET_ASSEMBLY.get(), containerId);
        this.container = container;
        this.access = access;
        
        // Set the rocket data provider
        if (container instanceof RocketDataProvider) {
            rocketDataProvider = (RocketDataProvider) container;
        } else {
            rocketDataProvider = null;
        }
        
        // Add component slots (3x3 grid)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                addSlot(new ComponentSlot(container, col + row * 3, 30 + col * 18, 17 + row * 18));
            }
        }
        
        // Add player inventory slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
        
        // Add player hotbar slots
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }
    
    /**
     * Creates a new rocket assembly menu (client-side).
     * 
     * @param containerId The container ID
     * @param playerInventory The player inventory
     * @param extraData Extra data from the server
     */
    public RocketAssemblyMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, new SimpleContainer(9), 
                ContainerLevelAccess.NULL);
    }
    
    /**
     * Special slot class for rocket components.
     */
    private static class ComponentSlot extends Slot {
        
        /**
         * Creates a new component slot.
         * 
         * @param container The container
         * @param slot The slot index
         * @param x The x position
         * @param y The y position
         */
        public ComponentSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }
        
        /**
         * Determines if an item is valid for this slot.
         * This is where component validation logic would go.
         * 
         * @param stack The item stack
         * @return true if valid, false otherwise
         */
        @Override
        public boolean mayPlace(ItemStack stack) {
            // For now, all items are valid
            // In a full implementation, this would check component compatibility
            return true;
        }
    }
    
    /**
     * Checks if the player can use this menu.
     * 
     * @param player The player
     * @return true if can use, false otherwise
     */
    @Override
    public boolean stillValid(Player player) {
        if (access == ContainerLevelAccess.NULL) {
            return true; // Client side is always valid
        }
        
        // Menu is valid if the player is close to the block
        return stillValid(access, player, SpaceBlocks.ROCKET_ASSEMBLY_TABLE.get());
    }
    
    /**
     * Handles quick move (shift-click) behavior.
     * 
     * @param player The player
     * @param index The slot index
     * @return The item stack result
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();
            
            if (index < 9) {
                // Move from component slots to player inventory
                if (!moveItemStackTo(slotStack, 9, 45, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemstack);
            } else {
                // Move from player inventory to component slots
                if (!moveItemStackTo(slotStack, 0, 9, false)) {
                    if (index < 36) {
                        // Move to hotbar
                        if (!moveItemStackTo(slotStack, 36, 45, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!moveItemStackTo(slotStack, 9, 36, false)) {
                        // Move to main inventory
                        return ItemStack.EMPTY;
                    }
                }
            }
            
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, slotStack);
        }
        
        return itemstack;
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
     * Validates the components in the menu.
     * 
     * @return true if valid, false otherwise
     */
    public boolean validateComponents() {
        if (rocketDataProvider == null) {
            return false;
        }
        
        // For now, just check if there are any components
        boolean hasComponents = false;
        for (int i = 0; i < 9; i++) {
            if (!container.getItem(i).isEmpty()) {
                hasComponents = true;
                break;
            }
        }
        
        componentsValid = hasComponents;
        return componentsValid;
    }
    
    /**
     * Checks if the components are valid.
     * 
     * @return true if valid, false otherwise
     */
    public boolean areComponentsValid() {
        return validateComponents();
    }
}