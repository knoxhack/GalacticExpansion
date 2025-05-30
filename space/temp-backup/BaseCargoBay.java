package com.astroframe.galactic.space.implementation.component;

import com.astroframe.galactic.core.api.common.ItemStack;
import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the ICargoBay interface.
 * Handles storage of items in a rocket with additional features like vacuum sealing.
 */
public class BaseCargoBay implements ICargoBay {
    
    private final ResourceLocation id;
    private final String name;
    private final String description;
    private final int tier;
    private final int mass;
    private final int maxDurability;
    private int currentDurability;
    private final int storageCapacity;
    private final Map<Integer, ItemStack> contents;
    private final boolean hasVacuumSeal;
    private final boolean hasTemperatureRegulation;
    private final boolean hasRadiationShielding;
    private final boolean hasEmpShielding;
    
    private BaseCargoBay(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.description = builder.description;
        this.tier = builder.tier;
        this.mass = builder.mass;
        this.maxDurability = builder.maxDurability;
        this.currentDurability = this.maxDurability;
        this.storageCapacity = builder.storageCapacity;
        this.contents = new HashMap<>();
        this.hasVacuumSeal = builder.hasVacuumSeal;
        this.hasTemperatureRegulation = builder.hasTemperatureRegulation;
        this.hasRadiationShielding = builder.hasRadiationShielding;
        this.hasEmpShielding = builder.hasEmpShielding;
    }
    
    public ResourceLocation getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getTier() {
        return tier;
    }
    
    public RocketComponentType getType() {
        return RocketComponentType.STORAGE;
    }
    
    public int getMass() {
        // Base mass plus the mass of contents (simplified)
        int contentsMass = 0;
        for (ItemStack stack : contents.values()) {
            if (!stack.isEmpty()) {
                contentsMass += stack.getCount();
            }
        }
        return mass + contentsMass / 10; // Approximate mass calculation
    }
    
    public int getMaxDurability() {
        return maxDurability;
    }
    
    public int getCurrentDurability() {
        return currentDurability;
    }
    
    public void damage(int amount) {
        currentDurability = Math.max(0, currentDurability - amount);
        
        // If severely damaged and no vacuum seal, items might be lost
        if (currentDurability < maxDurability * 0.25 && !hasVacuumSeal) {
            // Simulate item loss (random item removal)
            if (!contents.isEmpty()) {
                // Find a random slot from the available keys, or use 0 as fallback
                int randomSlot = 0;
                if (!contents.isEmpty()) {
                    Integer[] slots = contents.keySet().toArray(new Integer[0]);
                    randomSlot = slots[(int)(Math.random() * slots.length)];
                }
                
                ItemStack stack = contents.get(randomSlot);
                if (stack != null && !stack.isEmpty()) {
                    int lostAmount = Math.max(1, stack.getCount() / 4);
                    stack.shrink(lostAmount);
                    
                    if (stack.isEmpty()) {
                        contents.remove(randomSlot);
                    }
                }
            }
        }
    }
    
    public void repair(int amount) {
        currentDurability = Math.min(maxDurability, currentDurability + amount);
    }
    
    @Override
    public int getMaxSlots() {
        return storageCapacity;
    }
    
    public Map<Integer, ItemStack> getContents() {
        // Return a copy of the contents to prevent direct modification
        Map<Integer, ItemStack> contentsCopy = new HashMap<>();
        for (Map.Entry<Integer, ItemStack> entry : contents.entrySet()) {
            contentsCopy.put(entry.getKey(), entry.getValue().copy());
        }
        return contentsCopy;
    }
    
    public ItemStack addItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        // First try to find a slot with the same item
        for (int i = 0; i < storageCapacity; i++) {
            ItemStack existingStack = contents.get(i);
            if (existingStack != null && !existingStack.isEmpty() && 
                    existingStack.isSameItemSameTags(stack) &&
                    existingStack.getCount() < existingStack.getMaxStackSize()) {
                
                int spaceAvailable = existingStack.getMaxStackSize() - existingStack.getCount();
                int amountToAdd = Math.min(stack.getCount(), spaceAvailable);
                
                existingStack.grow(amountToAdd);
                
                // Return remaining items
                if (amountToAdd < stack.getCount()) {
                    ItemStack remaining = stack.copy();
                    remaining.setCount(stack.getCount() - amountToAdd);
                    return remaining;
                } else {
                    return ItemStack.EMPTY;
                }
            }
        }
        
        // If we can't stack with existing items, find an empty slot
        for (int i = 0; i < storageCapacity; i++) {
            if (!contents.containsKey(i)) {
                contents.put(i, stack);
                return ItemStack.EMPTY;
            }
        }
        
        // If we can't add the item, return the original stack
        return stack;
    }
    
    public ItemStack takeItem(int slotIndex, int amount) {
        if (slotIndex < 0 || slotIndex >= storageCapacity) {
            return ItemStack.EMPTY;
        }
        
        ItemStack stack = contents.get(slotIndex);
        if (stack == null || stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        
        int amountToTake = Math.min(amount, stack.getCount());
        ItemStack result = stack.copy();
        result.setCount(amountToTake);
        
        stack.shrink(amountToTake);
        
        if (stack.isEmpty()) {
            contents.remove(slotIndex);
        }
        
        return result;
    }
    
    @Override
    public int getMaxCapacity() {
        // Maximum weight capacity in grams (500g per slot)
        return storageCapacity * 500;
    }
    
    @Override
    public int getCurrentUsedCapacity() {
        // Calculate the current used capacity
        float usedCapacity = 0.0f;
        for (ItemStack stack : contents.values()) {
            if (stack != null && !stack.isEmpty()) {
                // Using a simple weight calculation (0.05kg per item)
                usedCapacity += stack.getCount() * 0.05f;
            }
        }
        // Convert from kg to g and return as int
        return (int)(usedCapacity * 1000);
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
    
    @Override
    public boolean hasAutomatedLoading() {
        // By default, this implementation doesn't support automated loading
        return false;
    }
    
    @Override
    public boolean hasEnvironmentControl() {
        // Environment control corresponds to temperature regulation
        return hasTemperatureRegulation;
    }
    
    @Override
    public boolean hasSecurityFeatures() {
        // Security features include EMP shielding
        return hasEmpShielding;
    }
    
    @Override
    public List<net.minecraft.world.item.ItemStack> getItems() {
        // Convert our internal ItemStacks to Minecraft ItemStacks
        List<net.minecraft.world.item.ItemStack> result = new ArrayList<>();
        for (ItemStack stack : contents.values()) {
            if (stack != null && !stack.isEmpty()) {
                result.add(stack.toMinecraft());
            }
        }
        return result;
    }
    
    @Override
    public boolean addItem(net.minecraft.world.item.ItemStack mcStack) {
        if (mcStack.isEmpty()) {
            return true; // Empty stack is always "added" successfully
        }
        
        // Convert Minecraft ItemStack to our internal ItemStack
        ItemStack stack = ItemStack.fromMinecraft(mcStack);
        
        // Try to add the item
        ItemStack remaining = addItem(stack);
        
        // If nothing remains, the item was added successfully
        if (remaining.isEmpty()) {
            // Consume the original stack
            mcStack.setCount(0);
            return true;
        }
        
        // Item couldn't be added completely
        mcStack.setCount(remaining.getCount());
        return false;
    }
    
    @Override
    public net.minecraft.world.item.ItemStack removeItem(int index) {
        if (index < 0 || index >= storageCapacity || !contents.containsKey(index)) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        
        // Get the item and remove it
        ItemStack stack = contents.remove(index);
        if (stack == null || stack.isEmpty()) {
            return net.minecraft.world.item.ItemStack.EMPTY;
        }
        
        // Convert to Minecraft ItemStack
        return stack.toMinecraft();
    }
    
    /**
     * Whether this cargo bay has EMP shielding.
     * @return true if the cargo bay has EMP shielding
     */
    public boolean hasEmpShielding() {
        return hasEmpShielding;
    }
    
    /**
     * Gets the number of filled slots in this cargo bay.
     * @return The number of filled slots
     */
    public int getFilledSlots() {
        return contents.size();
    }
    
    /**
     * Gets a list of tooltip lines for this component.
     * @param detailed Whether to include detailed information
     * @return The tooltip lines
     */
    public List<Component> getTooltip(boolean detailed) {
        List<Component> tooltip = new ArrayList<>();
        tooltip.add(Component.literal(name));
        tooltip.add(Component.literal("Tier: " + tier));
        tooltip.add(Component.literal("Storage: " + getFilledSlots() + "/" + storageCapacity));
        
        if (detailed) {
            tooltip.add(Component.literal("Vacuum Seal: " + (hasVacuumSeal ? "Yes" : "No")));
            tooltip.add(Component.literal("Temperature Regulation: " + (hasTemperatureRegulation ? "Yes" : "No")));
            tooltip.add(Component.literal("Radiation Shielding: " + (hasRadiationShielding ? "Yes" : "No")));
            tooltip.add(Component.literal("EMP Shielding: " + (hasEmpShielding ? "Yes" : "No")));
            tooltip.add(Component.literal("Mass (Empty): " + mass));
            tooltip.add(Component.literal("Mass (Current): " + getMass()));
            tooltip.add(Component.literal("Durability: " + currentDurability + "/" + maxDurability));
        }
        
        return tooltip;
    }
    
    /**
     * Builder for BaseCargoBay.
     */
    public static class Builder {
        private final ResourceLocation id;
        private String name = "Cargo Bay";
        private String description = "A bay for storing cargo on a rocket.";
        private int tier = 1;
        private int mass = 200;
        private int maxDurability = 100;
        private int storageCapacity = 9;
        private boolean hasVacuumSeal = false;
        private boolean hasTemperatureRegulation = false;
        private boolean hasRadiationShielding = false;
        private boolean hasEmpShielding = false;
        
        /**
         * Creates a new builder with required parameters.
         * @param id The component ID
         * @param displayName The display name component
         */
        public Builder(ResourceLocation id, Component displayName) {
            this.id = id;
            this.name = displayName.getString();
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
         * @param storageCapacity The storage capacity
         * @return This builder
         */
        public Builder storageCapacity(int storageCapacity) {
            this.storageCapacity = storageCapacity;
            return this;
        }
        
        /**
         * Sets whether the cargo bay has a vacuum seal.
         * @param hasVacuumSeal True if the cargo bay has a vacuum seal
         * @return This builder
         */
        public Builder vacuumSealed(boolean hasVacuumSeal) {
            this.hasVacuumSeal = hasVacuumSeal;
            return this;
        }
        
        /**
         * Sets whether the cargo bay has temperature regulation.
         * @param hasTemperatureRegulation True if the cargo bay has temperature regulation
         * @return This builder
         */
        public Builder climateControlled(boolean hasTemperatureRegulation) {
            this.hasTemperatureRegulation = hasTemperatureRegulation;
            return this;
        }
        
        /**
         * Sets whether the cargo bay has radiation shielding.
         * @param hasRadiationShielding True if the cargo bay has radiation shielding
         * @return This builder
         */
        public Builder radiationShielded(boolean hasRadiationShielding) {
            this.hasRadiationShielding = hasRadiationShielding;
            return this;
        }
        
        /**
         * Sets whether the cargo bay has EMP shielding.
         * @param hasEmpShielding True if the cargo bay has EMP shielding
         * @return This builder
         */
        public Builder empShielded(boolean hasEmpShielding) {
            this.hasEmpShielding = hasEmpShielding;
            return this;
        }
        
        /**
         * Builds the cargo bay.
         * @return A new BaseCargoBay
         */
        public BaseCargoBay build() {
            return new BaseCargoBay(this);
        }
    }
}