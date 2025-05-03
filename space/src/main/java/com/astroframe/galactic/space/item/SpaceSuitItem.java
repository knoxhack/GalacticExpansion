package com.astroframe.galactic.space.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import java.util.Optional;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.common.util.Lazy;

/**
 * Space suit armor item with special properties for space survival.
 */
public class SpaceSuitItem extends ArmorItem {
    
    private static final CustomArmorMaterial MATERIAL = new CustomArmorMaterial();
    private final Type type;
    
    /**
     * Create a new space suit item.
     *
     * @param type The armor type (helmet, chestplate, etc.)
     * @param properties The item properties
     */
    public SpaceSuitItem(Type type, Properties properties) {
        super(MATERIAL, type, properties);
        this.type = type;
    }
    
    // Enchantment behavior methods
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    public int getEnchantmentValue() {
        return 15;
    }
    
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        // In NeoForge 1.21.5, access the enchantment registry using BuiltInRegistries and ResourceKey
        ResourceLocation enchLocation = null;
        try {
            // Get the location directly from the registry in NeoForge 1.21.5
            // For NeoForge 1.21.5, we need to use ForgeRegistries since BuiltInRegistries.ENCHANTMENT doesn't exist
            enchLocation = net.neoforged.neoforge.registries.ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
        } catch (Exception e) {
            // If all else fails, just return default value
            return false;
        }
        
        if (enchLocation != null) {
            String path = enchLocation.getPath();
            
            // For helmet-specific enchantments (helmet is type 0 in NeoForge 1.21.5)
            if (this.type.getSlot() == EquipmentSlot.HEAD && 
                (path.equals("respiration") || path.equals("aqua_affinity"))) {
                return true;
            }
        }
        
        // Use basic item compatibility check
        return enchantment.canEnchant(stack);
    }
    
    /**
     * Checks if a player is wearing a full space suit.
     *
     * @param player The player to check
     * @return True if wearing a full space suit
     */
    public static boolean hasFullSpaceSuit(Player player) {
        // Check all armor equipment slots
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            // Skip non-armor slots
            if (!slot.isArmor()) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            // Check if empty or not a SpaceSuitItem by item registry ID
            if (stack.isEmpty() || !isSpaceSuit(stack)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Checks if an ItemStack is a space suit item
     * 
     * @param stack The item stack to check
     * @return True if it's a space suit
     */
    private static boolean isSpaceSuit(ItemStack stack) {
        if (stack.isEmpty()) return false;
        
        Item item = stack.getItem();
        // In NeoForge 1.21.5, we use ForgeRegistries instead of BuiltInRegistries for consistency
        ResourceLocation itemId = net.neoforged.neoforge.registries.ForgeRegistries.ITEMS.getKey(item);
        return itemId != null && itemId.getPath().contains("space_suit");
    }
    
    /**
     * Get the minimum tier of any piece of space suit armor.
     * Returns 0 if no space suit is worn.
     *
     * @param player The player to check
     * @return The minimum tier (1-3) or 0 if no suit
     */
    public static int getMinimumSuitTier(Player player) {
        int minTier = 3; // Start with maximum
        boolean hasSuit = false;
        
        // Check each armor slot
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            // Skip non-armor slots 
            if (!slot.isArmor()) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && isSpaceSuit(stack)) {
                hasSuit = true;
                int tier = 1; // Default to tier 1
                minTier = Math.min(minTier, tier);
            }
        }
        
        return hasSuit ? minTier : 0;
    }
    
    /**
     * Checks if a full space suit is being worn by comparing armor items.
     *
     * @param helmet The helmet item stack
     * @param chestplate The chestplate item stack
     * @param leggings The leggings item stack
     * @param boots The boots item stack
     * @return Whether a full space suit is being worn
     */
    public static boolean isFullSpaceSuit(ItemStack helmet, ItemStack chestplate, 
            ItemStack leggings, ItemStack boots) {
        
        return !helmet.isEmpty() && isSpaceSuit(helmet) && 
                !chestplate.isEmpty() && isSpaceSuit(chestplate) &&
                !leggings.isEmpty() && isSpaceSuit(leggings) && 
                !boots.isEmpty() && isSpaceSuit(boots);
    }
    
    /**
     * Custom armor material implementation for the space suit.
     */
    private static class CustomArmorMaterial implements ArmorMaterial {
        
        private static final int[] DURABILITY_PER_SLOT = new int[]{13, 15, 16, 11};
        private static final int[] PROTECTION_PER_SLOT = new int[]{3, 6, 8, 3};
        private final Lazy<Ingredient> repairMaterial = Lazy.of(() -> Ingredient.of(Items.IRON_INGOT));
        
        public int getDurabilityForType(Type type) {
            return DURABILITY_PER_SLOT[type.getSlot().getIndex()] * 25;
        }
        
        public int getDefenseForType(Type type) {
            return PROTECTION_PER_SLOT[type.getSlot().getIndex()];
        }
        
        public int getEnchantmentValue() {
            return 15;
        }
        
        public net.minecraft.core.Holder<SoundEvent> getEquipSound() {
            // In NeoForge 1.21.5, SoundEvents are already Holders so we need to extract and rewrap
            return net.minecraft.core.Holder.direct(SoundEvents.ARMOR_EQUIP_IRON.value());
        }
        
        public Ingredient getRepairIngredient() {
            return repairMaterial.get();
        }
        
        public String getName() {
            return "space_suit";
        }
        
        public float getToughness() {
            return 2.0F;
        }
        
        public float getKnockbackResistance() {
            return 0.0F;
        }
    }
}