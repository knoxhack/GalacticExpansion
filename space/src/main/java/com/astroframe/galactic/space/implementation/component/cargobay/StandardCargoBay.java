package com.astroframe.galactic.space.implementation.component.cargobay;

import com.astroframe.galactic.core.api.space.component.ICargoBay;
import com.astroframe.galactic.core.api.space.component.RocketComponentType;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

/**
 * A standard cargo bay component for rockets.
 * Provides basic storage capabilities for items during space travel.
 */
public class StandardCargoBay implements ICargoBay {
    
    private static final String DEFAULT_NAME = "Standard Cargo Bay";
    private static final String DEFAULT_DESCRIPTION = "A basic cargo bay for storing items during space travel.";
    private static final int DEFAULT_TIER = 1;
    private static final int DEFAULT_MASS = 150;
    private static final int DEFAULT_MAX_DURABILITY = 200;
    private static final int DEFAULT_MAX_SLOTS = 9;
    private static final int DEFAULT_MAX_CAPACITY = 1000; // in kg
    
    private final ResourceLocation id;
    private String name;
    private String description;
    private int tier;
    private int mass;
    private int maxDurability;
    private int currentDurability;
    private int maxSlots;
    private int maxCapacity;
    private boolean securityFeatures;
    private boolean environmentControl;
    private boolean automatedLoading;
    private List<ItemStack> items;
    private Vec3 position;
    
    /**
     * Creates a standard cargo bay with default values.
     * 
     * @param id The unique identifier
     */
    public StandardCargoBay(ResourceLocation id) {
        this.id = id;
        this.name = DEFAULT_NAME;
        this.description = DEFAULT_DESCRIPTION;
        this.tier = DEFAULT_TIER;
        this.mass = DEFAULT_MASS;
        this.maxDurability = DEFAULT_MAX_DURABILITY;
        this.currentDurability = maxDurability;
        this.maxSlots = DEFAULT_MAX_SLOTS;
        this.maxCapacity = DEFAULT_MAX_CAPACITY;
        this.securityFeatures = false;
        this.environmentControl = false;
        this.automatedLoading = false;
        this.items = new ArrayList<>(maxSlots);
        this.position = new Vec3(0, 0, 0);
    }
    
    /**
     * Creates a standard cargo bay with custom values.
     * 
     * @param id The unique identifier
     * @param tier The technology tier (1-3)
     * @param name The display name
     * @param description The description
     */
    public StandardCargoBay(ResourceLocation id, int tier, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        
        // Scale properties based on tier
        this.mass = DEFAULT_MASS + (tier - 1) * 50; // Higher tier is heavier (more reinforced)
        this.maxDurability = DEFAULT_MAX_DURABILITY + (tier - 1) * 100; // Higher tier is more durable
        this.currentDurability = maxDurability;
        this.maxSlots = DEFAULT_MAX_SLOTS * tier; // Higher tier has more slots
        this.maxCapacity = DEFAULT_MAX_CAPACITY * tier; // Higher tier has more capacity
        
        // Advanced features based on tier
        this.securityFeatures = (tier >= 2);
        this.environmentControl = (tier >= 2);
        this.automatedLoading = (tier >= 3);
        
        this.items = new ArrayList<>(maxSlots);
        this.position = new Vec3(0, 0, 0);
    }
    
    /**
     * Creates a fully customized cargo bay.
     * 
     * @param id The unique identifier
     * @param name The display name
     * @param description The description
     * @param tier The technology tier
     * @param mass The mass in kg
     * @param maxDurability The maximum durability
     * @param maxSlots The maximum number of slots
     * @param maxCapacity The maximum weight capacity in kg
     * @param securityFeatures Whether it has security features
     * @param environmentControl Whether it has environment control
     * @param automatedLoading Whether it has automated loading
     */
    public StandardCargoBay(ResourceLocation id, String name, String description, int tier, int mass,
                          int maxDurability, int maxSlots, int maxCapacity,
                          boolean securityFeatures, boolean environmentControl, boolean automatedLoading) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.mass = mass;
        this.maxDurability = maxDurability;
        this.currentDurability = maxDurability;
        this.maxSlots = maxSlots;
        this.maxCapacity = maxCapacity;
        this.securityFeatures = securityFeatures;
        this.environmentControl = environmentControl;
        this.automatedLoading = automatedLoading;
        this.items = new ArrayList<>(maxSlots);
        this.position = new Vec3(0, 0, 0);
    }
    
    @Override
    public ResourceLocation getId() {
        return id;
    }
    
    @Override
    public RocketComponentType getType() {
        return RocketComponentType.CARGO_BAY;
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
    public int getMass() {
        int totalMass = mass;
        
        // Add mass of stored items
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                totalMass += calculateItemWeight(stack);
            }
        }
        
        return totalMass;
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
    
    @Override
    public int getMaxSlots() {
        return maxSlots;
    }
    
    @Override
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    @Override
    public int getCurrentUsedCapacity() {
        int usedCapacity = 0;
        
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                usedCapacity += calculateItemWeight(stack);
            }
        }
        
        return usedCapacity;
    }
    
    @Override
    public List<ItemStack> getItems() {
        return new ArrayList<>(items); // Return a copy to prevent external modification
    }
    
    @Override
    public boolean addItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        
        // Check if there's enough space (slots and weight capacity)
        if (items.size() >= maxSlots) {
            return false;
        }
        
        float itemWeight = calculateItemWeight(stack);
        if (getCurrentUsedCapacity() + itemWeight > maxCapacity) {
            return false;
        }
        
        // Add the item to storage
        items.add(stack.copy()); // Store a copy to prevent external modification
        return true;
    }
    
    @Override
    public ItemStack removeItem(int index) {
        if (index < 0 || index >= items.size()) {
            return ItemStack.EMPTY;
        }
        
        return items.remove(index);
    }
    
    @Override
    public boolean hasSecurityFeatures() {
        return securityFeatures;
    }
    
    @Override
    public boolean hasEnvironmentControl() {
        return environmentControl;
    }
    
    @Override
    public boolean hasAutomatedLoading() {
        return automatedLoading;
    }
    
    @Override
    public Vec3 getPosition() {
        return position;
    }
    
    @Override
    public void setPosition(Vec3 position) {
        this.position = position;
    }
    
    @Override
    public Vec3 getSize() {
        // Cargo bays are typically rectangular
        return new Vec3(1.0, 0.8, 0.8);
    }
    
    @Override
    public float calculateItemWeight(ItemStack stack) {
        if (stack.isEmpty()) {
            return 0.0f;
        }
        
        // Enhanced weight calculation that takes item type into account
        float baseWeight = stack.getCount() * 0.5f; // Default: 0.5kg per item
        
        // Apply multipliers for certain item types (example logic)
        // In a real implementation, this would be more sophisticated
        if (stack.getItem().getDescriptionId().contains("block")) {
            // Blocks are heavier
            baseWeight *= 2.0f;
        } else if (stack.getItem().getDescriptionId().contains("ore")) {
            // Ores are very heavy
            baseWeight *= 3.0f;
        } else if (stack.getItem().getDescriptionId().contains("ingot")) {
            // Ingots are somewhat heavy
            baseWeight *= 1.5f;
        }
        
        return baseWeight;
    }
    
    @Override
    public void save(CompoundTag tag) {
        // Save basic properties manually for NeoForge 1.21.5 compatibility
        tag.putString("ID", getId().toString());
        tag.putString("Type", getType().name());
        tag.putString("Name", getName());
        tag.putString("Description", getDescription());
        tag.putInt("Tier", getTier());
        tag.putInt("Mass", mass); // Save base mass, not including items
        tag.putInt("MaxDurability", getMaxDurability());
        tag.putInt("CurrentDurability", getCurrentDurability());
        
        // Save position if component has a non-default position
        Vec3 pos = getPosition();
        if (pos.x != 0 || pos.y != 0 || pos.z != 0) {
            tag.putDouble("PosX", pos.x);
            tag.putDouble("PosY", pos.y);
            tag.putDouble("PosZ", pos.z);
        }
        
        // Save cargo bay specific properties
        tag.putInt("MaxSlots", maxSlots);
        tag.putInt("MaxCapacity", maxCapacity);
        tag.putBoolean("SecurityFeatures", securityFeatures);
        tag.putBoolean("EnvironmentControl", environmentControl);
        tag.putBoolean("AutomatedLoading", automatedLoading);
        
        // Save stored items
        ListTag itemsList = new ListTag();
        for (ItemStack stack : items) {
            CompoundTag itemTag = new CompoundTag();
            stack.save(itemTag);
            itemsList.add(itemTag);
        }
        tag.put("Items", itemsList);
    }
    
    @Override
    public void load(CompoundTag tag) {
        // Load common properties manually for NeoForge 1.21.5 compatibility
        if (tag.contains("PosX") && tag.contains("PosY") && tag.contains("PosZ")) {
            double x = tag.getDouble("PosX").orElse(0.0);
            double y = tag.getDouble("PosY").orElse(0.0);
            double z = tag.getDouble("PosZ").orElse(0.0);
            setPosition(new Vec3(x, y, z));
        }
        
        // Load cargo bay specific properties
        if (tag.contains("MaxSlots")) {
            this.maxSlots = tag.getInt("MaxSlots").orElse(DEFAULT_MAX_SLOTS);
        }
        if (tag.contains("MaxCapacity")) {
            this.maxCapacity = tag.getInt("MaxCapacity").orElse(DEFAULT_MAX_CAPACITY);
        }
        if (tag.contains("SecurityFeatures")) {
            this.securityFeatures = tag.getBoolean("SecurityFeatures").orElse(false);
        }
        if (tag.contains("EnvironmentControl")) {
            this.environmentControl = tag.getBoolean("EnvironmentControl").orElse(false);
        }
        if (tag.contains("AutomatedLoading")) {
            this.automatedLoading = tag.getBoolean("AutomatedLoading").orElse(false);
        }
        
        // Load stored items
        this.items.clear();
        if (tag.contains("Items")) {
            ListTag itemsList = tag.getList("Items").orElse(new ListTag());
            for (int i = 0; i < itemsList.size(); i++) {
                CompoundTag itemTag = itemsList.getCompound(i);
                ItemStack stack = ItemStack.of(itemTag);
                if (!stack.isEmpty()) {
                    this.items.add(stack);
                }
            }
        }
    }
}