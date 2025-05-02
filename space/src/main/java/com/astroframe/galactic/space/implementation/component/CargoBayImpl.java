package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import com.astroframe.galactic.core.api.common.ItemStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of a rocket cargo bay component.
 */
public class CargoBayImpl implements ICargoBay {
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int storageCapacity;
    private final boolean hasVacuumSeal;
    private final boolean hasTemperatureRegulation;
    private final boolean hasRadiationShielding;
    private final Map<Integer, ItemStack> contents;
    
    /**
     * Creates a new cargo bay.
     */
    public CargoBayImpl(
            ResourceLocation id,
            String name,
            String description,
            int tier,
            int mass,
            int maxDurability,
            int storageCapacity,
            boolean hasVacuumSeal,
            boolean hasTemperatureRegulation,
            boolean hasRadiationShielding) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.storageCapacity = storageCapacity;
        this.hasVacuumSeal = hasVacuumSeal;
        this.hasTemperatureRegulation = hasTemperatureRegulation;
        this.hasRadiationShielding = hasRadiationShielding;
        this.contents = new HashMap<>();
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int getTier() {
        return tier;
    }
    
    @Override
    public RocketComponentType getType() {
        return RocketComponentType.STORAGE;
    }

    @Override
    public int getMass() {
        return mass;
    }
    
    @Override
    public int getMaxDurability() {
        return maxDurability;
    }
    
    @Override
    public int getCurrentDurability() {
        return currentDurability;
    }
    
    @Override
    public void damage(int amount) {
        currentDurability = Math.max(0, currentDurability - amount);
    }
    
    @Override
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
    }

    public int getStorageCapacity() {
        return storageCapacity;
    }
    
    public Map<Integer, ItemStack> getContents() {
        return new HashMap<>(contents);
    }
    
    /**
     * Calculate the weight of an item stack in kg
     * @param item The item stack to calculate
     * @return The weight in kg
     */
    @Override
    public float calculateItemWeight(net.minecraft.world.item.ItemStack item) {
        // Default weight calculation (0.05kg per item)
        if (item.isEmpty()) {
            return 0.0f;
        }
        
        return item.getCount() * 0.05f;
    }
    
    public ItemStack addItem(ItemStack stack) {
        // If the stack is empty, return it
        if (stack.isEmpty()) {
            return stack;
        }
        
        // Try to merge with existing stacks first
        ItemStack remainingStack = stack.copy();
        for (int i = 0; i < storageCapacity; i++) {
            if (contents.containsKey(i)) {
                ItemStack existing = contents.get(i);
                if (existing.isSameItemSameTags(remainingStack)) {
                    int spaceAvailable = existing.getMaxStackSize() - existing.getCount();
                    if (spaceAvailable > 0) {
                        int amountToAdd = Math.min(spaceAvailable, remainingStack.getCount());
                        existing.grow(amountToAdd);
                        remainingStack.shrink(amountToAdd);
                        
                        if (remainingStack.isEmpty()) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            }
        }
        
        // Try to add to empty slots
        for (int i = 0; i < storageCapacity; i++) {
            if (!contents.containsKey(i) || contents.get(i).isEmpty()) {
                if (remainingStack.getCount() <= remainingStack.getMaxStackSize()) {
                    contents.put(i, remainingStack.copy());
                    return ItemStack.EMPTY;
                } else {
                    ItemStack splitStack = remainingStack.copy();
                    splitStack.setCount(splitStack.getMaxStackSize());
                    remainingStack.shrink(splitStack.getMaxStackSize());
                    contents.put(i, splitStack);
                    
                    if (remainingStack.isEmpty()) {
                        return ItemStack.EMPTY;
                    }
                }
            }
        }
        
        return remainingStack;
    }
    
    public ItemStack takeItem(int slotIndex, int amount) {
        if (slotIndex < 0 || slotIndex >= storageCapacity || !contents.containsKey(slotIndex)) {
            return ItemStack.EMPTY;
        }
        
        ItemStack stack = contents.get(slotIndex);
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        int actualAmount = Math.min(amount, stack.getCount());
        ItemStack result = stack.copy();
        result.setCount(actualAmount);
        
        stack.shrink(actualAmount);
        if (stack.isEmpty()) {
            contents.remove(slotIndex);
        }
        
        return result;
    }
    
    public boolean hasVacuumSeal() {
        return hasVacuumSeal;
    }
    
    public boolean hasTemperatureRegulation() {
        return hasTemperatureRegulation;
    }
    
    public boolean hasRadiationShielding() {
        return hasRadiationShielding;
    }
    
    public boolean hasAutomatedLoading() {
        // Implementation for NeoForge 1.21.5 compatibility
        // By default, this cargo bay doesn't have automated loading
        return false;
    }
    
    public boolean hasEnvironmentControl() {
        // In NeoForge 1.21.5, environment control corresponds to temperature regulation
        return hasTemperatureRegulation;
    }
    
    @Override
    public boolean hasSecurityFeatures() {
        // In NeoForge 1.21.5, security features include radiation shielding
        return hasRadiationShielding;
    }
    
    @Override
    public boolean addItem(net.minecraft.world.item.ItemStack mcStack) {
        // If the stack is empty, return true (operation successful, nothing to add)
        if (mcStack.isEmpty()) {
            return true;
        }
        
        // Create our internal item representation by wrapping the Minecraft ItemStack
        com.astroframe.galactic.core.api.common.ItemStack stack = 
            com.astroframe.galactic.core.api.common.ItemStack.fromMinecraft(mcStack);
        
        // Check if there is an available slot
        for (int i = 0; i < storageCapacity; i++) {
            if (!contents.containsKey(i)) {
                // Found empty slot, add the item
                contents.put(i, stack);
                // Consume the minecraft stack
                mcStack.setCount(0);
                return true;
            }
        }
        
        // No slots available
        return false;
    }
    
    @Override
    public List<net.minecraft.world.item.ItemStack> getItems() {
        // Create a list of Minecraft ItemStacks from our internal ItemStacks
        List<net.minecraft.world.item.ItemStack> result = new ArrayList<>();
        
        // Convert each internal ItemStack to a Minecraft ItemStack
        for (com.astroframe.galactic.core.api.common.ItemStack stack : contents.values()) {
            if (stack != null && !stack.isEmpty()) {
                // Get the Minecraft ItemStack directly from our wrapper
                net.minecraft.world.item.ItemStack mcStack = stack.toMinecraft();
                result.add(mcStack);
            }
        }
        
        return result;
    }
    
    @Override
    public net.minecraft.world.item.ItemStack removeItem(int index) {
        // NeoForge 1.21.5 compatibility implementation
        // Check if the index is valid and the slot has an item
        if (index < 0 || index >= storageCapacity || !contents.containsKey(index)) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        
        // Get the item and remove it from the contents
        com.astroframe.galactic.core.api.common.ItemStack removed = contents.get(index);
        contents.remove(index);
        
        // Convert our custom ItemStack to Minecraft's ItemStack
        if (removed == null || removed.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        
        // Convert our custom ItemStack to Minecraft ItemStack
        net.minecraft.world.item.ItemStack mcStack = removed.toMinecraft();
        
        return mcStack;
    }
    
    @Override
    public int getMaxSlots() {
        return storageCapacity;
    }
    
    @Override
    public int getMaxCapacity() {
        // Approximate maximum weight capacity (500g per slot)
        return storageCapacity * 500;
    }
    
    @Override
    public int getCurrentUsedCapacity() {
        // Calculate the current used capacity
        float usedCapacity = 0.0f;
        for (com.astroframe.galactic.core.api.common.ItemStack stack : contents.values()) {
            if (stack != null && !stack.isEmpty()) {
                usedCapacity += calculateItemWeight(stack.toMinecraft());
            }
        }
        // Convert from kg to g and return as int
        return (int)(usedCapacity * 1000);
    }

    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Capacity: " + storageCapacity + " slots"));
        tooltip.add(Component.literal("Tier: " + tier));
        
        if (detailed) {
            tooltip.add(Component.literal("Vacuum Seal: " + (hasVacuumSeal ? "Yes" : "No")));
            tooltip.add(Component.literal("Temperature Control: " + (hasTemperatureRegulation ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Shielding: " + (hasRadiationShielding ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass: " + mass));
            tooltip.add(Component.literal("Durability: " + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for CargoBayImpl.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Cargo Bay";
        private String description = "A storage compartment for cargo.";
        private int tier = 1;
        private int mass = 250;
        private int maxDurability = 100;
        private int storageCapacity = 27;
        private boolean hasVacuumSeal = false;
        private boolean hasTemperatureRegulation = false;
        private boolean hasRadiationShielding = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         */
        public Builder(ResourceLocation id) {
            this.id = id;
        }
        
        /**
         * Sets the name.
         * @param name The name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        
        /**
         * Sets the description.
         * @param description The description
         * @return This builder
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        
        /**
         * Sets the tier.
         * @param tier The tier level (1-3)
         * @return This builder
         */
        public Builder tier(int tier) {
            this.tier = Math.max(1, Math.min(3, tier));
            return this;
        }
        
        /**
         * Sets the mass.
         * @param mass The mass
         * @return This builder
         */
        public Builder mass(int mass) {
            this.mass = mass;
            return this;
        }
        
        /**
         * Sets the max durability.
         * @param maxDurability The max durability
         * @return This builder
         */
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }
        
        /**
         * Sets the storage capacity.
         * @param storageCapacity The storage capacity in slots
         * @return This builder
         */
        public Builder storageCapacity(int storageCapacity) {
            this.storageCapacity = storageCapacity;
            return this;
        }
        
        /**
         * Sets whether this cargo bay has vacuum sealing.
         * @param hasVacuumSeal True if has vacuum sealing
         * @return This builder
         */
        public Builder vacuumSeal(boolean hasVacuumSeal) {
            this.hasVacuumSeal = hasVacuumSeal;
            return this;
        }
        
        /**
         * Sets whether this cargo bay has temperature regulation.
         * @param hasTemperatureRegulation True if has temperature regulation
         * @return This builder
         */
        public Builder temperatureRegulation(boolean hasTemperatureRegulation) {
            this.hasTemperatureRegulation = hasTemperatureRegulation;
            return this;
        }
        
        /**
         * Sets whether this cargo bay has radiation shielding.
         * @param hasRadiationShielding True if has radiation shielding
         * @return This builder
         */
        public Builder radiationShielding(boolean hasRadiationShielding) {
            this.hasRadiationShielding = hasRadiationShielding;
            return this;
        }
        
        /**
         * Builds the cargo bay.
         * @return A new CargoBayImpl
         */
        public CargoBayImpl build() {
            return new CargoBayImpl(
                    id, name, description, tier, mass, maxDurability,
                    storageCapacity, hasVacuumSeal, hasTemperatureRegulation, hasRadiationShielding
            );
        }
    }
}