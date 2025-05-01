package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.common.ItemStack;

import java.util.Map;

/**
 * Interface for rocket cargo bays.
 */
public interface ICargoBay extends IRocketComponent {
    
    /**
     * Gets the number of storage slots this cargo bay provides.
     * @return The storage capacity
     */
    int getStorageCapacity();
    
    /**
     * Gets the cargo contents of this bay.
     * @return A map of slot index to item stack
     */
    Map<Integer, ItemStack> getContents();
    
    /**
     * Adds an item to this cargo bay.
     * @param stack The item stack to add
     * @return The remaining items that couldn't be added, or empty if all were added
     */
    ItemStack addItem(ItemStack stack);
    
    /**
     * Takes an item from this cargo bay.
     * @param slotIndex The slot to take from
     * @param amount The number of items to take
     * @return The items taken
     */
    ItemStack takeItem(int slotIndex, int amount);
    
    /**
     * Whether this cargo bay has vacuum sealing.
     * @return true if the cargo bay has vacuum sealing
     */
    boolean hasVacuumSeal();
    
    /**
     * Whether this cargo bay has temperature regulation.
     * @return true if the cargo bay has temperature regulation
     */
    boolean hasTemperatureRegulation();
    
    /**
     * Whether this cargo bay has radiation shielding.
     * @return true if the cargo bay has radiation shielding
     */
    boolean hasRadiationShielding();
}