package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket shields.
 */
public interface IShield extends IRocketComponent {
    
    /**
     * Gets the maximum shield strength this shield can provide.
     * @return The maximum shield strength
     */
    int getMaxShieldStrength();
    
    /**
     * Gets the current shield strength.
     * @return The current shield strength
     */
    int getCurrentShieldStrength();
    
    /**
     * Gets the shield's impact resistance (1-10).
     * Higher values provide better protection against physical impacts.
     * @return The impact resistance
     */
    int getImpactResistance();
    
    /**
     * Gets the shield's heat resistance (1-10).
     * Higher values provide better protection against heat.
     * @return The heat resistance
     */
    int getHeatResistance();
    
    /**
     * Gets the shield's radiation resistance (1-10).
     * Higher values provide better protection against radiation.
     * @return The radiation resistance
     */
    int getRadiationResistance();
    
    /**
     * Applies damage to the shield.
     * @param amount The amount of damage to apply
     * @return The amount of damage that penetrated the shield
     */
    int applyDamage(int amount);
    
    /**
     * Regenerates shield strength.
     * @param amount The amount to regenerate
     * @return The amount actually regenerated
     */
    int regenerate(int amount);
    
    /**
     * Whether this shield is currently active.
     * @return true if the shield is active
     */
    boolean isActive();
    
    /**
     * Activates or deactivates this shield.
     * @param active Whether to activate or deactivate
     */
    void setActive(boolean active);
}