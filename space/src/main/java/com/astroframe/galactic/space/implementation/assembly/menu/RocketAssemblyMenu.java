package com.astroframe.galactic.space.implementation.assembly.menu;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.space.SpaceModule;
import com.astroframe.galactic.space.implementation.assembly.RocketAssemblyTableBlockEntity;
import com.astroframe.galactic.space.implementation.component.ComponentValidator;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Menu for the Rocket Assembly Table.
 * Allows users to configure rocket components.
 */
public class RocketAssemblyMenu extends AbstractContainerMenu {
    // Constants for slot indices
    public static final int COMMAND_MODULE_SLOT = 0;
    public static final int ENGINE_SLOT = 1;
    public static final int FUEL_TANK_SLOT = 2;
    public static final int SHIELD_SLOT = 3;
    public static final int LIFE_SUPPORT_SLOT = 4;
    public static final int COMPONENT_SLOTS_START = 5;
    public static final int COMPONENT_SLOTS_COUNT = 9;
    public static final int INVENTORY_SLOT_START = COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT;
    public static final int INVENTORY_SLOT_COUNT = 36; // Player inventory (27) + hotbar (9)
    public static final int TOTAL_SLOTS = INVENTORY_SLOT_START + INVENTORY_SLOT_COUNT;
    
    // The block entity for this menu
    private final RocketAssemblyTableBlockEntity blockEntity;
    private final Level level;
    private final ContainerLevelAccess access;
    
    // Containers for the menu
    private final Container componentContainer;
    
    // Validation status
    private boolean isValid = false;
    private List<String> validationErrors = new ArrayList<>();
    
    /**
     * Creates a menu from a packet buffer.
     * Used on the client side.
     *
     * @param id The menu ID
     * @param inventory The player inventory
     * @param buf The packet buffer
     */
    public RocketAssemblyMenu(int id, @NotNull Inventory inventory, FriendlyByteBuf buf) {
        this(id, inventory, inventory.player.level().getBlockEntity(buf.readBlockPos()), 
                ContainerLevelAccess.NULL);
    }
    
    /**
     * Creates a menu for the rocket assembly table.
     *
     * @param id The menu ID
     * @param inventory The player inventory
     * @param blockEntity The block entity
     * @param access The container access
     */
    public RocketAssemblyMenu(int id, Inventory inventory, BlockEntity blockEntity, ContainerLevelAccess access) {
        super(SpaceModule.ROCKET_ASSEMBLY_MENU.get(), id);
        
        // Store references
        this.blockEntity = (RocketAssemblyTableBlockEntity) blockEntity;
        this.level = inventory.player.level();
        this.access = access;
        
        // Set up the container for components
        this.componentContainer = new SimpleContainer(COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT);
        
        // Add component slots
        // Command module slot
        addSlot(new ComponentSlot(componentContainer, COMMAND_MODULE_SLOT, 80, 20, this, RocketComponentType.COCKPIT));
        
        // Engine slot
        addSlot(new ComponentSlot(componentContainer, ENGINE_SLOT, 80, 140, this, RocketComponentType.ENGINE));
        
        // Fuel tank slot
        addSlot(new ComponentSlot(componentContainer, FUEL_TANK_SLOT, 80, 100, this, RocketComponentType.FUEL_TANK));
        
        // Shield slot
        addSlot(new ComponentSlot(componentContainer, SHIELD_SLOT, 20, 80, this, RocketComponentType.SHIELDING));
        
        // Life support slot
        addSlot(new ComponentSlot(componentContainer, LIFE_SUPPORT_SLOT, 140, 80, this, RocketComponentType.LIFE_SUPPORT));
        
        // Additional component slots in a grid at the bottom
        int startX = 44;
        int startY = 180;
        int slotSize = 18;
        
        for (int i = 0; i < COMPONENT_SLOTS_COUNT; i++) {
            int row = i / 3;
            int col = i % 3;
            int x = startX + col * slotSize;
            int y = startY + row * slotSize;
            
            addSlot(new ComponentSlot(componentContainer, COMPONENT_SLOTS_START + i, x, y, this, null));
        }
        
        // Add player inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, 232 + row * 18));
            }
        }
        
        // Add player hotbar slots
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(inventory, i, 8 + i * 18, 290));
        }
        
        // Load current components from the block entity
        loadComponentsFromBlockEntity();
        
        // Validate the current configuration
        validateRocket();
    }
    
    /**
     * Loads components from the block entity.
     */
    private void loadComponentsFromBlockEntity() {
        if (blockEntity != null) {
            List<ItemStack> components = blockEntity.getComponentStacks();
            
            // Load each component into the corresponding slot
            for (int i = 0; i < components.size() && i < COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT; i++) {
                componentContainer.setItem(i, components.get(i));
            }
        }
    }
    
    /**
     * Saves components to the block entity.
     */
    private void saveComponentsToBlockEntity() {
        if (blockEntity != null) {
            List<ItemStack> components = new ArrayList<>();
            
            // Get each component from the slots
            for (int i = 0; i < COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT; i++) {
                components.add(componentContainer.getItem(i));
            }
            
            // Save to the block entity
            blockEntity.setComponentStacks(components);
            
            // Create a new rocket instance from the components
            IRocket rocket = createRocketFromComponents();
            
            // Set the rocket in the block entity
            if (rocket != null) {
                blockEntity.setRocket(rocket);
            }
        }
    }
    
    /**
     * Creates a rocket from the components in the menu.
     *
     * @return The rocket, or null if invalid
     */
    private IRocket createRocketFromComponents() {
        // Start with a new modular rocket
        ModularRocket rocket = new ModularRocket();
        
        // Add components from each slot
        for (int i = 0; i < COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT; i++) {
            ItemStack stack = componentContainer.getItem(i);
            
            if (!stack.isEmpty()) {
                // Try to get the component from the registry
                Optional<IRocketComponent> component = getComponentFromItem(stack.getItem());
                
                if (component.isPresent()) {
                    // Get the slot position for component placement
                    Vec3 position = getPositionForSlot(i);
                    
                    // Set the position on the component
                    component.get().setPosition(position);
                    
                    // Add to the rocket
                    rocket.addComponent(component.get());
                }
            }
        }
        
        // Validate the rocket
        if (validateRocket()) {
            return rocket;
        } else {
            return null;
        }
    }
    
    /**
     * Gets the position for a component based on slot index.
     *
     * @param slotIndex The slot index
     * @return The position vector
     */
    private Vec3 getPositionForSlot(int slotIndex) {
        // Calculate positions based on slot type
        switch (slotIndex) {
            case COMMAND_MODULE_SLOT:
                return new Vec3(0, 2, 0); // Command module at the top
            case ENGINE_SLOT:
                return new Vec3(0, -2, 0); // Engines at the bottom
            case FUEL_TANK_SLOT:
                return new Vec3(0, 0, 0); // Fuel tank in the center
            case SHIELD_SLOT:
                return new Vec3(-1, 0, 0); // Shield on the left
            case LIFE_SUPPORT_SLOT:
                return new Vec3(1, 0, 0); // Life support on the right
            default:
                // Additional components in a grid pattern
                if (slotIndex >= COMPONENT_SLOTS_START) {
                    int index = slotIndex - COMPONENT_SLOTS_START;
                    int row = index / 3;
                    int col = index % 3;
                    return new Vec3(col - 1, -1, row - 1);
                }
                
                // Default position if none of the above
                return new Vec3(0, 0, 0);
        }
    }
    
    /**
     * Gets a component from an item.
     *
     * @param item The item
     * @return The component, or empty if not a component
     */
    public Optional<IRocketComponent> getComponentFromItem(Item item) {
        // TODO: Implement component lookup from item registry
        // For now, this is a placeholder that will be replaced with actual
        // component lookup logic when component items are implemented
        return Optional.empty();
    }
    
    /**
     * Validates the current rocket configuration.
     *
     * @return true if valid
     */
    public boolean validateRocket() {
        // Clear previous validation
        validationErrors.clear();
        
        // Get all components
        List<IRocketComponent> components = new ArrayList<>();
        
        for (int i = 0; i < COMPONENT_SLOTS_START + COMPONENT_SLOTS_COUNT; i++) {
            ItemStack stack = componentContainer.getItem(i);
            
            if (!stack.isEmpty()) {
                // Try to get the component from the registry
                Optional<IRocketComponent> component = getComponentFromItem(stack.getItem());
                
                if (component.isPresent()) {
                    components.add(component.get());
                }
            }
        }
        
        // Validate the components
        ComponentValidator validator = new ComponentValidator();
        isValid = validator.validate(components, validationErrors);
        
        // Update the block entity with validation status
        if (blockEntity != null) {
            blockEntity.setValidationStatus(isValid, validationErrors);
        }
        
        return isValid;
    }
    
    /**
     * Gets whether the current configuration is valid.
     *
     * @return true if valid
     */
    public boolean isValid() {
        return isValid;
    }
    
    /**
     * Gets the validation errors.
     *
     * @return The validation errors
     */
    public List<String> getValidationErrors() {
        return validationErrors;
    }
    
    /**
     * Called when the container is closed.
     *
     * @param player The player
     */
    @Override
    public void removed(Player player) {
        super.removed(player);
        
        // Save components to the block entity
        saveComponentsToBlockEntity();
    }
    
    /**
     * Gets whether the player can use the container.
     *
     * @param player The player
     * @return true if the player can use
     */
    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, SpaceModule.ROCKET_ASSEMBLY_TABLE.get());
    }
    
    /**
     * Handles shifting an item between inventory and container.
     *
     * @param player The player
     * @param slotIndex The slot index
     * @return The result
     */
    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        
        if (slot != null && slot.hasItem()) {
            ItemStack stackInSlot = slot.getItem();
            result = stackInSlot.copy();
            
            // If we're moving from component slots to inventory
            if (slotIndex < INVENTORY_SLOT_START) {
                if (!moveItemStackTo(stackInSlot, INVENTORY_SLOT_START, TOTAL_SLOTS, true)) {
                    return ItemStack.EMPTY;
                }
            }
            // If we're moving from inventory to component slots
            else {
                // Try to find the appropriate component slot type
                Optional<IRocketComponent> component = getComponentFromItem(stackInSlot.getItem());
                
                if (component.isPresent()) {
                    RocketComponentType type = component.get().getType();
                    int targetSlot = getSlotForComponentType(type);
                    
                    if (targetSlot >= 0 && !this.slots.get(targetSlot).hasItem()) {
                        // Move to the specific slot for this component type
                        if (!moveItemStackTo(stackInSlot, targetSlot, targetSlot + 1, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        // Try general component slots
                        if (!moveItemStackTo(stackInSlot, COMPONENT_SLOTS_START, INVENTORY_SLOT_START, false)) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                
                // Validate after any change
                validateRocket();
            }
            
            if (stackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (stackInSlot.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }
            
            slot.onTake(player, stackInSlot);
        }
        
        return result;
    }
    
    /**
     * Gets the slot index for a component type.
     *
     * @param type The component type
     * @return The slot index, or -1 if no specific slot
     */
    private int getSlotForComponentType(RocketComponentType type) {
        return switch (type) {
            case COCKPIT -> COMMAND_MODULE_SLOT;
            case ENGINE -> ENGINE_SLOT;
            case FUEL_TANK -> FUEL_TANK_SLOT;
            case SHIELDING -> SHIELD_SLOT;
            case LIFE_SUPPORT -> LIFE_SUPPORT_SLOT;
            default -> -1;
        };
    }
    
    /**
     * A specialized slot that only accepts specific component types.
     */
    public static class ComponentSlot extends Slot {
        private final RocketAssemblyMenu menu;
        private final RocketComponentType allowedType;
        
        /**
         * Creates a component slot.
         *
         * @param container The container
         * @param index The slot index
         * @param x The X position
         * @param y The Y position
         * @param menu The parent menu
         * @param allowedType The allowed component type, or null for any
         */
        public ComponentSlot(Container container, int index, int x, int y, RocketAssemblyMenu menu, RocketComponentType allowedType) {
            super(container, index, x, y);
            this.menu = menu;
            this.allowedType = allowedType;
        }
        
        /**
         * Checks if an item stack can be placed in this slot.
         *
         * @param stack The item stack
         * @return true if allowed
         */
        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            // If no type restriction, accept any component
            if (allowedType == null) {
                return isComponentItem(stack);
            }
            
            // Otherwise, check if the component matches the allowed type
            Optional<IRocketComponent> component = menu.getComponentFromItem(stack.getItem());
            
            if (component.isPresent()) {
                return component.get().getType() == allowedType;
            }
            
            return false;
        }
        
        /**
         * Checks if an item stack is a rocket component.
         *
         * @param stack The item stack
         * @return true if a component
         */
        private boolean isComponentItem(ItemStack stack) {
            return menu.getComponentFromItem(stack.getItem()).isPresent();
        }
    }
}