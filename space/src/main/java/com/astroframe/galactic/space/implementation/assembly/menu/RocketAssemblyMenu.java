package com.astroframe.galactic.space.implementation.assembly.menu;

import com.astroframe.galactic.core.api.space.IRocket;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * Menu class for the Rocket Assembly Table.
 * This handles the inventory and slots for assembling rockets.
 */
public class RocketAssemblyMenu extends AbstractContainerMenu {
    private final ContainerLevelAccess access;
    private final RocketAssemblyTableBlockEntity blockEntity;
    private final Level level;
    private final Player player;

    // Slot indices
    private static final int FIRST_COMPONENT_SLOT = 0;
    private static final int COMPONENT_SLOTS = 9; // 3x3 grid for components
    private static final int FIRST_PLAYER_SLOT = COMPONENT_SLOTS;
    private static final int PLAYER_SLOTS = 36; // Player inventory + hotbar
    
    // Inventory for component slots
    private final Container componentInventory;
    
    // Component validation
    private final ComponentValidator validator;
    private final List<String> validationErrors;
    
    /**
     * Creates a rocket assembly menu for server side.
     * 
     * @param id The menu ID
     * @param inventory The player inventory
     * @param blockEntity The block entity
     * @param access Access to the level
     */
    public RocketAssemblyMenu(int id, Inventory inventory, RocketAssemblyTableBlockEntity blockEntity, ContainerLevelAccess access) {
        super(SpaceMenus.ROCKET_ASSEMBLY_MENU.get(), id);
        this.access = access;
        this.blockEntity = blockEntity;
        this.level = inventory.player.level();
        this.player = inventory.player;
        this.componentInventory = blockEntity.getComponentInventory();
        this.validator = new ComponentValidator();
        this.validationErrors = new ArrayList<>();
        
        // Add component slots (3x3 grid)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                int x = 30 + col * 30;
                int y = 20 + row * 30;
                addSlot(new ComponentSlot(this.componentInventory, index, x, y));
            }
        }
        
        // Add player inventory slots
        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);
        
        // Sync blockEntity data to client
        if (blockEntity.hasRocket()) {
            // Initialize with rocket data
            updateDisplayInfo();
        }
    }
    
    /**
     * Creates a rocket assembly menu for client side.
     * 
     * @param id The menu ID
     * @param inventory The player inventory
     * @param data The packet buffer
     */
    public RocketAssemblyMenu(int id, Inventory inventory, FriendlyByteBuf data) {
        this(id, inventory, getBlockEntity(inventory, data), ContainerLevelAccess.NULL);
    }
    
    /**
     * Extracts the block entity from the network buffer.
     * 
     * @param inventory The player inventory
     * @param data The packet buffer 
     * @return The rocket assembly table block entity
     */
    private static RocketAssemblyTableBlockEntity getBlockEntity(Inventory inventory, FriendlyByteBuf data) {
        BlockPos pos = data.readBlockPos();
        Level level = inventory.player.level();
        BlockEntity be = level.getBlockEntity(pos);
        
        if (be instanceof RocketAssemblyTableBlockEntity) {
            return (RocketAssemblyTableBlockEntity) be;
        }
        
        // Create a dummy inventory if something went wrong
        return null;
    }
    
    /**
     * Adds the player inventory slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerInventory(Inventory playerInventory) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                int x = 8 + col * 18;
                int y = 86 + row * 18; // Below the component slots
                int index = col + row * 9 + 9; // Skip hotbar
                addSlot(new Slot(playerInventory, index, x, y));
            }
        }
    }
    
    /**
     * Adds the player hotbar slots to the menu.
     * 
     * @param playerInventory The player inventory
     */
    private void addPlayerHotbar(Inventory playerInventory) {
        for (int col = 0; col < 9; col++) {
            int x = 8 + col * 18;
            int y = 144; // Below player inventory
            addSlot(new Slot(playerInventory, col, x, y));
        }
    }
    
    /**
     * Called to handle item clicks within the container.
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            stackCopy = stack.copy();
            
            if (index < COMPONENT_SLOTS) {
                // Move from component grid to player inventory
                if (!moveItemStackTo(stack, FIRST_PLAYER_SLOT, FIRST_PLAYER_SLOT + PLAYER_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // Move from player inventory to component grid if it's a component
                if (isRocketComponent(stack)) {
                    if (!moveItemStackTo(stack, FIRST_COMPONENT_SLOT, FIRST_COMPONENT_SLOT + COMPONENT_SLOTS, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    // Move between player inventory and hotbar
                    if (index < FIRST_PLAYER_SLOT + 27) {
                        // Move from inventory to hotbar
                        if (!moveItemStackTo(stack, FIRST_PLAYER_SLOT + 27, FIRST_PLAYER_SLOT + PLAYER_SLOTS, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Move from hotbar to inventory
                        if (!moveItemStackTo(stack, FIRST_PLAYER_SLOT, FIRST_PLAYER_SLOT + 27, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
            
            if (stack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (stack.getCount() == stackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, stack);
        }
        
        return stackCopy;
    }
    
    /**
     * Check if the menu is still valid.
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, blockEntity.getBlockState().getBlock());
    }
    
    /**
     * Updates rocket preview and validation status.
     * Called when component inventory changes.
     */
    public void updateDisplayInfo() {
        if (level.isClientSide()) {
            return;
        }
        
        if (blockEntity != null) {
            List<IRocketComponent> components = getComponentsFromInventory();
            blockEntity.updateRocketComponents(components);
            
            // Validate components
            boolean valid = validator.validate(components, validationErrors);
            blockEntity.setRocketValid(valid);
            
            // Update the block entity's validation error list
            blockEntity.setValidationErrors(validationErrors);
            
            // Notify client of block entity update
            blockEntity.setChanged();
            blockEntity.syncToClient();
        }
    }
    
    /**
     * Gets the current rocket components from the inventory.
     * 
     * @return List of rocket components
     */
    private List<IRocketComponent> getComponentsFromInventory() {
        List<IRocketComponent> components = new ArrayList<>();
        
        for (int i = 0; i < componentInventory.getContainerSize(); i++) {
            ItemStack stack = componentInventory.getItem(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IRocketComponent) {
                components.add((IRocketComponent) stack.getItem());
            }
        }
        
        return components;
    }
    
    /**
     * Checks if an item is a rocket component.
     * 
     * @param stack The item stack to check
     * @return true if the item is a rocket component
     */
    private boolean isRocketComponent(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof IRocketComponent;
    }
    
    /**
     * Gets the validation errors from the last validation.
     * 
     * @return List of validation error messages
     */
    public List<String> getValidationErrors() {
        return validationErrors;
    }
    
    /**
     * Test if the rocket is valid and can be launched.
     * 
     * @return true if the rocket is valid
     */
    public boolean isRocketValid() {
        return blockEntity.isRocketValid();
    }
    
    /**
     * Attempts to launch the rocket.
     * Called on server side only.
     * 
     * @param player The player who is launching
     * @return true if launch was successful
     */
    public boolean launchRocket(Player player) {
        if (!(player instanceof ServerPlayer) || !isRocketValid()) {
            return false;
        }
        
        // Here we would connect to the space travel system
        // For now, just confirm the rocket is valid
        return blockEntity.isRocketValid();
    }
    
    /**
     * Custom slot class for rocket components.
     */
    private class ComponentSlot extends Slot {
        public ComponentSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }
        
        @Override
        public boolean mayPlace(ItemStack stack) {
            return isRocketComponent(stack);
        }
        
        @Override
        public void setChanged() {
            super.setChanged();
            updateDisplayInfo();
        }
    }
}