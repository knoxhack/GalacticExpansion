package com.astroframe.galactic.space.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Item representing a rocket that can be deployed.
 */
public class RocketItem extends Item {
    private final int tier;
    
    /**
     * Creates a new rocket item.
     * @param tier The rocket tier
     * @param properties The item properties
     */
    public RocketItem(int tier, Properties properties) {
        super(properties);
        this.tier = tier;
    }
    
    /**
     * Gets the tier of this rocket.
     * @return The rocket tier
     */
    public int getTier() {
        return tier;
    }
    
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.galactic-space.rocket.tier", tier));
        
        // Display appropriate capabilities based on tier
        switch (tier) {
            case 1:
                tooltip.add(Component.translatable("item.galactic-space.rocket.destination.moon"));
                break;
            case 2:
                tooltip.add(Component.translatable("item.galactic-space.rocket.destination.mars"));
                break;
            case 3:
                tooltip.add(Component.translatable("item.galactic-space.rocket.destination.deepspace"));
                break;
            default:
                break;
        }
    }
}