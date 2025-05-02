package com.astroframe.galactic.space.implementation.assembly;

import com.astroframe.galactic.core.api.block.BlockEntityBase;
import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.ModularRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.astroframe.galactic.space.SpaceModule;
import com.astroframe.galactic.space.implementation.assembly.menu.RocketAssemblyMenu;
import com.astroframe.galactic.space.implementation.hologram.HolographicProjectorBlockEntity;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * BlockEntity for the rocket assembly table.
 * Manages rocket components, their configuration, and interaction with
 * holographic projectors.
 * Implements the RocketAssemblyTableProvider interface for integration with projectors.
 */
public class RocketAssemblyTableBlockEntity extends BlockEntityBase
        implements Container, MenuProvider, HolographicProjectorBlockEntity.RocketAssemblyTableProvider {
    
    // Maximum number of components the rocket can have
    private static final int MAX_COMPONENTS = 27;
    
    // Component inventory (items representing components)
    private final NonNullList<ItemStack> components = NonNullList.withSize(MAX_COMPONENTS, ItemStack.EMPTY);
    
    // The assembled rocket data
    private ModularRocket rocketData = new ModularRocket();
    
    // Linked projector positions
    private final List<BlockPos> linkedProjectors = new ArrayList<>();
    
    /**
     * Creates a new rocket assembly table block entity.
     *
     * @param pos The position
     * @param state The block state
     */
    public RocketAssemblyTableBlockEntity(BlockPos pos, BlockState state) {
        super(SpaceModule.ROCKET_ASSEMBLY_TABLE_BLOCK_ENTITY.get(), pos, state);
    }
    
    /**
     * Called every tick to update the table's state.
     *
     * @param level The level
     * @param pos The position
     * @param state The block state
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        if (!level.isClientSide) {
            // Update linked projectors if the rocket data has changed
            updateLinkedProjectors();
        }
    }
    
    /**
     * Updates any linked holographic projectors with the current rocket data.
     */
    private void updateLinkedProjectors() {
        if (level == null) return;
        
        // First, clean up any invalid linked projectors
        linkedProjectors.removeIf(projPos -> {
            if (level.getBlockEntity(projPos) instanceof HolographicProjectorBlockEntity) {
                return false; // Keep it
            }
            return true; // Remove it
        });
        
        // Then update all linked projectors with the current rocket data
        for (BlockPos projPos : linkedProjectors) {
            if (level.getBlockEntity(projPos) instanceof HolographicProjectorBlockEntity projector) {
                projector.setRocketData(rocketData);
            }
        }
    }
    
    /**
     * Adds a projector to the linked projectors list.
     *
     * @param projectorPos The projector position
     */
    public void linkProjector(BlockPos projectorPos) {
        if (!linkedProjectors.contains(projectorPos)) {
            linkedProjectors.add(projectorPos);
            setChanged();
        }
    }
    
    /**
     * Removes a projector from the linked projectors list.
     *
     * @param projectorPos The projector position
     */
    public void unlinkProjector(BlockPos projectorPos) {
        if (linkedProjectors.remove(projectorPos)) {
            setChanged();
        }
    }
    
    /**
     * Adds a component to the rocket.
     *
     * @param component The component to add
     * @return True if the component was added successfully
     */
    public boolean addRocketComponent(IRocketComponent component) {
        if (rocketData.addComponent(component)) {
            setChanged();
            return true;
        }
        return false;
    }
    
    /**
     * Removes a component from the rocket.
     *
     * @param componentId The component ID to remove
     * @return True if the component was removed successfully
     */
    public boolean removeRocketComponent(String componentId) {
        if (rocketData.removeComponent(componentId)) {
            setChanged();
            return true;
        }
        return false;
    }
    
    /**
     * Gets the current rocket data.
     *
     * @return The rocket data
     */
    @Override
    public IRocket getRocketData() {
        return rocketData;
    }
    
    /**
     * Loads data from NBT.
     *
     * @param tag The tag to load from
     */
    @Override
    protected void loadData(CompoundTag tag) {
        // Load the component inventory
        ContainerHelper.loadAllItems(tag, components);
        
        // Load the rocket data
        if (tag.contains("RocketData")) {
            CompoundTag rocketTag = tag.getCompound("RocketData");
            rocketData.deserialize(rocketTag);
        }
        
        // Load linked projectors
        if (tag.contains("LinkedProjectors", Tag.TAG_LIST)) {
            ListTag projectorList = tag.getList("LinkedProjectors", Tag.TAG_COMPOUND);
            linkedProjectors.clear();
            
            for (int i = 0; i < projectorList.size(); i++) {
                CompoundTag posTag = projectorList.getCompound(i);
                int x = posTag.getInt("X").orElse(0);
                int y = posTag.getInt("Y").orElse(0);
                int z = posTag.getInt("Z").orElse(0);
                linkedProjectors.add(new BlockPos(x, y, z));
            }
        }
    }
    
    /**
     * Saves data to NBT.
     *
     * @param tag The tag to save to
     */
    @Override
    protected void saveData(CompoundTag tag) {
        // Save the component inventory
        ContainerHelper.saveAllItems(tag, components);
        
        // Save the rocket data
        CompoundTag rocketTag = new CompoundTag();
        rocketData.serialize(rocketTag);
        tag.put("RocketData", rocketTag);
        
        // Save linked projectors
        ListTag projectorList = new ListTag();
        for (BlockPos pos : linkedProjectors) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("X", pos.getX());
            posTag.putInt("Y", pos.getY());
            posTag.putInt("Z", pos.getZ());
            projectorList.add(posTag);
        }
        tag.put("LinkedProjectors", projectorList);
    }
    
    // Container implementation
    
    @Override
    public int getContainerSize() {
        return components.size();
    }
    
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : components) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public ItemStack getItem(int slot) {
        return components.get(slot);
    }
    
    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack result = ContainerHelper.removeItem(components, slot, amount);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }
    
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack result = ContainerHelper.takeItem(components, slot);
        if (!result.isEmpty()) {
            setChanged();
        }
        return result;
    }
    
    @Override
    public void setItem(int slot, ItemStack stack) {
        components.set(slot, stack);
        setChanged();
    }
    
    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }
    
    @Override
    public void clearContent() {
        components.clear();
        setChanged();
    }
    
    // Menu provider implementation
    
    @Override
    public Component getDisplayName() {
        return Component.translatable("container.galactic.rocket_assembly_table");
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new RocketAssemblyMenu(containerId, playerInventory, this, 
                net.minecraft.world.inventory.ContainerLevelAccess.create(level, worldPosition));
    }
    
    /**
     * Gets all component stacks for the menu.
     * 
     * @return The list of component stacks
     */
    public List<ItemStack> getComponentStacks() {
        return components;
    }
    
    /**
     * Sets the component stacks from the menu.
     * 
     * @param stacks The new component stacks
     */
    public void setComponentStacks(List<ItemStack> stacks) {
        for (int i = 0; i < Math.min(stacks.size(), components.size()); i++) {
            components.set(i, stacks.get(i));
        }
        setChanged();
    }
    
    /**
     * Sets the rocket data.
     * 
     * @param rocket The new rocket data
     */
    public void setRocket(IRocket rocket) {
        if (rocket instanceof ModularRocket modularRocket) {
            this.rocketData = modularRocket;
            setChanged();
            updateLinkedProjectors();
        }
    }
    
    /**
     * Sets the validation status.
     * 
     * @param isValid Whether the rocket is valid
     * @param errors The validation errors
     */
    public void setValidationStatus(boolean isValid, List<String> errors) {
        // For now, we just log the validation status
        // In the future, we'll save this to display in the UI
        if (!isValid && level != null && !level.isClientSide) {
            System.out.println("Rocket validation failed with errors: " + errors);
        }
    }
}