package com.astroframe.galactic.space.item;

import com.astroframe.galactic.space.GalacticSpace;
import com.astroframe.galactic.space.dimension.SpaceStationDimension;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.living.LivingAttackEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;

/**
 * Space suit armor item, provides protection in space environments.
 */
public abstract class SpaceSuitItem extends ArmorItem {

    // Use a literal ArmorMaterial instance for compatibility
    private static final net.minecraft.world.item.ArmorMaterials MATERIAL = createSpaceSuitMaterial();
    private final int tier;

    /**
     * Create a new space suit item.
     * 
     * @param slot The equipment slot
     * @param tier The tier/level of the space suit (1-3)
     */
    public SpaceSuitItem(EquipmentSlot slot, int tier) {
        super(MATERIAL, slot, new Properties().stacksTo(1).fireResistant().durability(800));
        this.tier = Math.max(1, Math.min(3, tier)); // Clamp between 1-3
    }
    
    /**
     * Gets the tier/level of this space suit piece.
     * 
     * @return The tier (1-3)
     */
    public int getTier() {
        return tier;
    }
    
    // Adds tooltip information
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, 
                              TooltipFlag flag) {
        tooltip.add(Component.translatable("item.galactic-space.space_suit.tier", getTier())
                .withStyle(ChatFormatting.GRAY));
        
        tooltip.add(Component.translatable("item.galactic-space.space_suit.protection")
                .withStyle(ChatFormatting.BLUE));
        
        if (getTier() >= 2) {
            tooltip.add(Component.translatable("item.galactic-space.space_suit.radiation")
                    .withStyle(ChatFormatting.GREEN));
        }
        
        if (getTier() >= 3) {
            tooltip.add(Component.translatable("item.galactic-space.space_suit.temperature")
                    .withStyle(ChatFormatting.RED));
        }
    }
    
    // Updates the item each tick when in inventory
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        super.inventoryTick(stack, level, entity, slotId, isSelected);
        
        // Apply effects when in hostile environments
        if (entity instanceof Player player && entity.tickCount % 20 == 0) {
            // Only apply effects when in space
            if (SpaceStationDimension.isSpaceStation(level)) {
                applySpaceEffects(player);
            }
        }
    }
    
    /**
     * Applies effects to the player based on their space suit protection.
     * 
     * @param player The player
     */
    private void applySpaceEffects(Player player) {
        // Check if player has complete space suit
        if (!hasFullSpaceSuit(player)) {
            // Apply negative effects if not wearing a complete suit
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 1));
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 0));
            
            // Add suffocation if no helmet
            if (!hasHelmet(player)) {
                player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0));
                
                // Damage from no oxygen
                if (player.getRandom().nextInt(10) == 0) {
                    player.hurt(player.damageSources().drown(), 1.0F);
                }
            }
        } else {
            // Apply positive effects for complete suit
            int minTier = getMinimumSuitTier(player);
            
            // Tier 2+ benefits
            if (minTier >= 2) {
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, true, false));
            }
            
            // Tier 3 benefits
            if (minTier >= 3) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 40, 0, true, false));
            }
        }
    }
    
    /**
     * Handles protection from environmental damage.
     * 
     * @param event The damage event
     */
    private void onLivingHurt(LivingDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        
        // Only protect in space environments
        if (!SpaceStationDimension.isSpaceStation(player.level())) {
            return;
        }
        
        // Protect against various damage types based on suit tier
        int minTier = getMinimumSuitTier(player);
        
        if (hasFullSpaceSuit(player)) {
            if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.DROWN)) {
                // Protect from suffocation with any tier
                event.setCanceled(true);
                return;
            }
            
            if (minTier >= 2 && event.getSource().is(net.minecraft.world.damagesource.DamageTypes.MAGIC)) {
                // Tier 2+ protects from radiation (magic damage)
                event.setAmount(event.getAmount() * 0.5f);
            }
            
            if (minTier >= 3) {
                if (event.getSource().is(net.minecraft.world.damagesource.DamageTypes.FREEZE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.IN_FIRE) ||
                    event.getSource().is(net.minecraft.world.damagesource.DamageTypes.ON_FIRE)) {
                    // Tier 3 protects from temperature extremes
                    event.setCanceled(true);
                }
            }
        }
    }
    
    /**
     * Checks if a player has a complete space suit.
     * 
     * @param player The player
     * @return true if wearing a complete space suit
     */
    public static boolean hasFullSpaceSuit(Player player) {
        return hasHelmet(player) && hasChestplate(player) && hasLeggings(player) && hasBoots(player);
    }
    
    /**
     * Checks if a player has a space suit helmet.
     * 
     * @param player The player
     * @return true if wearing a space suit helmet
     */
    public static boolean hasHelmet(Player player) {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() instanceof SpaceSuitItem.Helmet;
    }
    
    /**
     * Checks if a player has a space suit chestplate.
     * 
     * @param player The player
     * @return true if wearing a space suit chestplate
     */
    public static boolean hasChestplate(Player player) {
        return player.getItemBySlot(EquipmentSlot.CHEST).getItem() instanceof SpaceSuitItem.Chestplate;
    }
    
    /**
     * Checks if a player has space suit leggings.
     * 
     * @param player The player
     * @return true if wearing space suit leggings
     */
    public static boolean hasLeggings(Player player) {
        return player.getItemBySlot(EquipmentSlot.LEGS).getItem() instanceof SpaceSuitItem.Leggings;
    }
    
    /**
     * Checks if a player has space suit boots.
     * 
     * @param player The player
     * @return true if wearing space suit boots
     */
    public static boolean hasBoots(Player player) {
        return player.getItemBySlot(EquipmentSlot.FEET).getItem() instanceof SpaceSuitItem.Boots;
    }
    
    /**
     * Gets the minimum tier of all equipped space suit pieces.
     * 
     * @param player The player
     * @return The minimum tier, or 0 if no space suit
     */
    public static int getMinimumSuitTier(Player player) {
        if (!hasFullSpaceSuit(player)) {
            return 0;
        }
        
        int minTier = Integer.MAX_VALUE;
        
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.isArmor()) {
                ItemStack stack = player.getItemBySlot(slot);
                if (stack.getItem() instanceof SpaceSuitItem suit) {
                    minTier = Math.min(minTier, suit.getTier());
                }
            }
        }
        
        return minTier == Integer.MAX_VALUE ? 0 : minTier;
    }
    
    /**
     * Creates a space suit material instance.
     * 
     * @return The armor material instance
     */
    private static net.minecraft.world.item.ArmorMaterials createSpaceSuitMaterial() {
        // Use the IRON material as a base
        return net.minecraft.world.item.ArmorMaterials.IRON;
    }

    /**
     * Space suit material definition (legacy, for reference only).
     */
    private static class SpaceSuitMaterial {
        private static final Map<EquipmentSlot, Integer> DURABILITY_PER_SLOT = new EnumMap<>(EquipmentSlot.class);
        private static final Map<EquipmentSlot, Integer> PROTECTION_PER_SLOT = new EnumMap<>(EquipmentSlot.class);
        
        static {
            DURABILITY_PER_SLOT.put(EquipmentSlot.HEAD, 200);
            DURABILITY_PER_SLOT.put(EquipmentSlot.CHEST, 280);
            DURABILITY_PER_SLOT.put(EquipmentSlot.LEGS, 260);
            DURABILITY_PER_SLOT.put(EquipmentSlot.FEET, 220);
            
            PROTECTION_PER_SLOT.put(EquipmentSlot.HEAD, 3);
            PROTECTION_PER_SLOT.put(EquipmentSlot.CHEST, 6);
            PROTECTION_PER_SLOT.put(EquipmentSlot.LEGS, 5);
            PROTECTION_PER_SLOT.put(EquipmentSlot.FEET, 3);
        }
        
        // In NeoForge 1.21.5, we don't use ArmorItem.Type as it doesn't exist
        // Instead, we use EquipmentSlot directly
        public int getDurabilityForSlot(EquipmentSlot slot) {
            return DURABILITY_PER_SLOT.getOrDefault(slot, 0);
        }
        
        public int getDefenseForSlot(EquipmentSlot slot) {
            return PROTECTION_PER_SLOT.getOrDefault(slot, 0);
        }
        
        public int getEnchantmentValue() {
            return 15;
        }
        
        public net.minecraft.sounds.SoundEvent getEquipSound() {
            return net.minecraft.sounds.SoundEvents.ARMOR_EQUIP_IRON.value();
        }
        
        public Ingredient getRepairIngredient() {
            return Ingredient.of(net.minecraft.world.item.Items.IRON_INGOT);
        }
        
        public String getName() {
            return "space_suit";
        }
        
        public float getToughness() {
            return 2.0F;
        }
        
        public float getKnockbackResistance() {
            return 0.1F;
        }
    }
    
    /**
     * Space suit helmet implementation.
     */
    public static class Helmet extends SpaceSuitItem {
        public Helmet() {
            super(EquipmentSlot.HEAD, 1);
        }
    }
    
    /**
     * Space suit chestplate implementation.
     */
    public static class Chestplate extends SpaceSuitItem {
        public Chestplate() {
            super(EquipmentSlot.CHEST, 1);
        }
    }
    
    /**
     * Space suit leggings implementation.
     */
    public static class Leggings extends SpaceSuitItem {
        public Leggings() {
            super(EquipmentSlot.LEGS, 1);
        }
    }
    
    /**
     * Space suit boots implementation.
     */
    public static class Boots extends SpaceSuitItem {
        public Boots() {
            super(EquipmentSlot.FEET, 1);
        }
    }
}