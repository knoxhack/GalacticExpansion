package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket cargo bays.
 */
public interface ICargoBay extends IRocketComponent {
    
    /**
     * Gets the storage capacity of this cargo bay in slots.
     * @return The storage capacity
     */
    int getStorageCapacity();
    
    /**
     * Checks if this cargo bay is climate controlled.
     * @return True if climate controlled
     */
    boolean isClimateControlled();
    
    /**
     * Checks if this cargo bay is radiation shielded.
     * @return True if radiation shielded
     */
    boolean isRadiationShielded();
    
    /**
     * Checks if this cargo bay is EMP shielded.
     * @return True if EMP shielded
     */
    boolean isEMPShielded();
}