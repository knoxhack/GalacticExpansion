package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.ShieldType;

/**
 * Interface for rocket shields.
 */
public interface IShield extends IRocketComponent {
    
    /**
     * Gets the maximum shield strength.
     * @return The maximum shield strength
     */
    float getMaxShieldStrength();
    
    /**
     * Gets the shield regeneration rate per second.
     * @return The regeneration rate
     */
    float getRegenerationRate();
    
    /**
     * Gets the impact resistance level (1-10).
     * @return The impact resistance
     */
    int getImpactResistance();
    
    /**
     * Checks if this shield provides radiation protection.
     * @return True if radiation shielded
     */
    boolean isRadiationShielded();
    
    /**
     * Checks if this shield provides EMP protection.
     * @return True if EMP shielded
     */
    boolean isEMPShielded();
    
    /**
     * Checks if this shield provides thermal protection.
     * @return True if thermal shielded
     */
    boolean isThermalShielded();
    
    /**
     * Gets the type of this shield.
     * @return The shield type
     */
    ShieldType getShieldType();
}