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
import net.minecraft.world.item.enchantment.Enchantments;
import net.neoforged.neoforge.common.util.Lazy;

/**
 * Space suit armor item with special properties for space survival.
 */
public class SpaceSuitItem extends ArmorItem {
    
    private static final CustomArmorMaterial MATERIAL = new CustomArmorMaterial();
    
    /**
     * Create a new space suit item.
     *
     * @param type The armor type (helmet, chestplate, etc.)
     * @param properties The item properties
     */
    public SpaceSuitItem(ArmorItem.Type type, Properties properties) {
        super(MATERIAL, type, properties);
    }
    
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
    
    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 15;
    }
    
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        if (enchantment == Enchantments.RESPIRATION && getType() == ArmorItem.Type.HELMET) {
            return true;
        }
        
        if (enchantment == Enchantments.AQUA_AFFINITY && getType() == ArmorItem.Type.HELMET) {
            return true;
        }
        
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
    
    /**
     * Checks if a player is wearing a full space suit.
     *
     * @param player The player to check
     * @return True if wearing a full space suit
     */
    public static boolean hasFullSpaceSuit(Player player) {
        // Check each armor slot
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            if (stack.isEmpty() || !(stack.getItem().getClass().equals(SpaceSuitItem.class))) {
                return false;
            }
        }
        return true;
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
            if (slot.getType() != EquipmentSlot.Type.ARMOR) continue;
            
            ItemStack stack = player.getItemBySlot(slot);
            if (!stack.isEmpty() && stack.getItem().getClass().equals(SpaceSuitItem.class)) {
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
        
        return !helmet.isEmpty() && helmet.getItem().getClass().equals(SpaceSuitItem.class) && 
                !chestplate.isEmpty() && chestplate.getItem().getClass().equals(SpaceSuitItem.class) &&
                !leggings.isEmpty() && leggings.getItem().getClass().equals(SpaceSuitItem.class) && 
                !boots.isEmpty() && boots.getItem().getClass().equals(SpaceSuitItem.class);
    }
    
    /**
     * Custom armor material implementation for the space suit.
     */
    private static class CustomArmorMaterial implements ArmorMaterial {
        
        private static final int[] DURABILITY_PER_SLOT = new int[]{13, 15, 16, 11};
        private static final int[] PROTECTION_PER_SLOT = new int[]{3, 6, 8, 3};
        private final Lazy<Ingredient> repairMaterial = Lazy.of(() -> Ingredient.of(Items.IRON_INGOT));
        
        public int getDurabilityForType(ArmorItem.Type type) {
            return DURABILITY_PER_SLOT[type.getSlot().getIndex()] * 25;
        }
        
        public int getDefenseForType(ArmorItem.Type type) {
            return PROTECTION_PER_SLOT[type.getSlot().getIndex()];
        }
        
        public int getEnchantmentValue() {
            return 15;
        }
        
        public net.minecraft.core.Holder<SoundEvent> getEquipSound() {
            return net.minecraft.core.registries.BuiltInRegistries.SOUND_EVENT.getHolder(
                   net.minecraft.resources.ResourceKey.create(
                        net.minecraft.core.registries.Registries.SOUND_EVENT, 
                        SoundEvents.ARMOR_EQUIP_IRON.getLocation()))
                   .orElseThrow();
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