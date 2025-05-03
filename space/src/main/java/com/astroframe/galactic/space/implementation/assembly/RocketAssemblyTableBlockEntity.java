package com.astroframe.galactic.space.implementation.assembly;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;
import com.astroframe.galactic.space.implementation.common.RocketDataProvider;
import com.astroframe.galactic.space.registry.SpaceBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Block entity for the Rocket Assembly Table
 * Handles component storage and rocket assembly
 */
public class RocketAssemblyTableBlockEntity extends BlockEntity 
        implements Container, MenuProvider, RocketDataProvider {
    
    // The components inventory (3x3 grid)
    private NonNullList<ItemStack> components = NonNullList.withSize(9, ItemStack.EMPTY);
    
    // Storage of rocket data as NBT
    private CompoundTag rocketDataTag = new CompoundTag();
    
    // Cache of the built rocket
    private IRocket cachedRocket = null;
    
    /**
     * Creates a new rocket assembly table block entity.
     *
     * @param pos The position
     * @param state The block state
     */
    public RocketAssemblyTableBlockEntity(BlockPos pos, BlockState state) {
        super(SpaceBlockEntities.ROCKET_ASSEMBLY_TABLE.get(), pos, state);
    }
    
    /**
     * Tick method called each game tick.
     * Can be used for processing, animations, etc.
     *
     * @param level The level
     * @param pos The position
     * @param state The block state
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }
        
        // If rocket is null, try to rebuild it
        if (cachedRocket == null) {
            rebuildRocket();
        }
    }
    
    /**
     * Creates the menu for this block entity.
     * 
     * @param containerId The container ID
     * @param playerInventory The player inventory
     * @param player The player
     * @return The menu
     */
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RocketAssemblyMenu(containerId, playerInventory, this, 
                ContainerLevelAccess.create(level, worldPosition));
    }
    
    /**
     * Gets the display name for this block entity.
     * 
     * @return The display name
     */
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.galacticspace.rocket_assembly_table");
    }
    
    /**
     * Gets the size of the component inventory.
     * 
     * @return The inventory size
     */
    @Override
    public int getContainerSize() {
        return components.size();
    }
    
    /**
     * Checks if the inventory is empty.
     * 
     * @return true if empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : components) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Gets the item at the specified slot.
     * 
     * @param slot The slot
     * @return The item stack
     */
    @Override
    public ItemStack getItem(int slot) {
        if (slot >= 0 && slot < components.size()) {
            return components.get(slot);
        }
        return ItemStack.EMPTY;
    }
    
    /**
     * Removes items from the specified slot.
     * 
     * @param slot The slot
     * @param count The count to remove
     * @return The removed items
     */
    @Override
    public ItemStack removeItem(int slot, int count) {
        ItemStack stack = ContainerHelper.removeItem(components, slot, count);
        if (!stack.isEmpty()) {
            setChanged();
            rebuildRocket();
        }
        return stack;
    }
    
    /**
     * Removes all items from the specified slot.
     * 
     * @param slot The slot
     * @return The removed items
     */
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        if (slot >= 0 && slot < components.size()) {
            ItemStack stack = components.get(slot);
            components.set(slot, ItemStack.EMPTY);
            setChanged();
            rebuildRocket();
            return stack;
        }
        return ItemStack.EMPTY;
    }
    
    /**
     * Sets the item in the specified slot.
     * 
     * @param slot The slot
     * @param stack The item stack
     */
    @Override
    public void setItem(int slot, ItemStack stack) {
        if (slot >= 0 && slot < components.size()) {
            components.set(slot, stack);
            setChanged();
            rebuildRocket();
        }
    }
    
    /**
     * Marks the inventory as changed.
     */
    @Override
    public void setChanged() {
        super.setChanged();
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }
    
    /**
     * Checks if the player can still use the container.
     * 
     * @param player The player
     * @return true if the player can use the container, false otherwise
     */
    @Override
    public boolean stillValid(Player player) {
        return level.getBlockEntity(worldPosition) == this && 
                player.distanceToSqr(worldPosition.getX() + 0.5, 
                        worldPosition.getY() + 0.5, 
                        worldPosition.getZ() + 0.5) <= 64;
    }
    
    /**
     * Clears the inventory.
     */
    @Override
    public void clearContent() {
        components.clear();
        for (int i = 0; i < components.size(); i++) {
            components.set(i, ItemStack.EMPTY);
        }
        setChanged();
        rebuildRocket();
    }
    
    /**
     * Builds/rebuilds the rocket from components.
     */
    private void rebuildRocket() {
        // Clear current rocket
        cachedRocket = null;
        
        // Check if we have any components
        boolean hasComponents = false;
        for (ItemStack stack : components) {
            if (!stack.isEmpty()) {
                hasComponents = true;
                break;
            }
        }
        
        if (!hasComponents) {
            // No components, no rocket
            rocketDataTag = new CompoundTag();
            return;
        }
        
        // For now, just create a simple rocket with the basic stats
        // In a full implementation, this would build from component properties
        try {
            cachedRocket = new ModularRocket();
            // Save to NBT
            rocketDataTag = new CompoundTag();
            CompoundTag rocketTag = new CompoundTag();
            rocketTag.putString("id", "modular_rocket");
            rocketTag.putInt("tier", 1);
            rocketTag.putFloat("fuel", 0.0f);
            rocketTag.putFloat("mass", 1000.0f);
            rocketTag.putFloat("thrust", 5000.0f);
            
            // Save components
            ListTag componentsTag = new ListTag();
            for (ItemStack stack : components) {
                if (!stack.isEmpty()) {
                    // In NeoForge 1.21.5, ItemStack.save requires a Provider parameter
                    // We'll create a temporary tag and use built-in registry
                    CompoundTag componentTag = stack.save(new CompoundTag(), 
                        net.minecraft.core.registries.BuiltInRegistries.ITEM);
                    componentsTag.add(componentTag);
                }
            }
            rocketTag.put("components", componentsTag);
            
            rocketDataTag.put("rocket", rocketTag);
        } catch (Exception e) {
            // Handle errors gracefully
            cachedRocket = null;
            rocketDataTag = new CompoundTag();
        }
    }
    
    /**
     * Saves data to NBT.
     * 
     * @param tag The tag to save to
     */
    /**
     * Save data implementation for NeoForge 1.21.5 compatibility.
     * This method is called by the parent class saveAdditional method.
     * 
     * @param tag The tag to save to
     */
    @Override
    protected void saveData(CompoundTag tag) {
        // Save components - need to use Provider with NeoForge 1.21.5
        ContainerHelper.saveAllItems(tag, components, net.minecraft.core.registries.BuiltInRegistries.ITEM);
        
        // Save rocket data
        if (rocketDataTag != null && !rocketDataTag.isEmpty()) {
            tag.put("RocketData", rocketDataTag);
        }
    }
    
    /**
     * Load data implementation for NeoForge 1.21.5 compatibility.
     * This method is called by the parent class loadAdditional method.
     * 
     * @param tag The tag to load from
     */
    @Override
    protected void loadData(CompoundTag tag) {
        // Load components with proper Provider parameter for NeoForge 1.21.5
        components = NonNullList.withSize(9, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(tag, components, net.minecraft.core.registries.BuiltInRegistries.ITEM);
        
        // Load rocket data
        if (tag.contains("RocketData")) {
            rocketDataTag = tag.getCompound("RocketData").orElse(new CompoundTag());
        } else {
            rocketDataTag = new CompoundTag();
        }
        
        // Force rocket rebuild
        cachedRocket = null;
    }
    
    // ---- RocketDataProvider Implementation ----
    
    /**
     * Gets the rocket data tag.
     * 
     * @return The rocket data tag
     */
    @Override
    public CompoundTag getRocketDataTag() {
        return rocketDataTag;
    }
    
    /**
     * Sets the rocket data tag.
     * 
     * @param tag The rocket data tag
     */
    @Override
    public void setRocketDataTag(CompoundTag tag) {
        this.rocketDataTag = tag;
        cachedRocket = null; // Force rebuild next tick
        setChanged();
    }
    
    /**
     * Gets the rocket instance.
     * 
     * @return The rocket instance
     */
    @Override
    public IRocket getRocket() {
        if (cachedRocket == null) {
            rebuildRocket();
        }
        return cachedRocket;
    }
    
    /**
     * Gets all component stacks.
     * 
     * @return The component stacks
     */
    @Override
    public List<ItemStack> getComponentStacks() {
        List<ItemStack> result = new ArrayList<>();
        for (ItemStack stack : components) {
            if (!stack.isEmpty()) {
                result.add(stack.copy());
            }
        }
        return result;
    }
    
    /**
     * Checks if there is a complete rocket.
     * 
     * @return true if there is a complete rocket, false otherwise
     */
    @Override
    public boolean hasCompleteRocket() {
        return cachedRocket != null;
    }
}