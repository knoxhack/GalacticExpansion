package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket shield components.
 */
public interface IShield extends IRocketComponent {
    
    /**
     * Gets the maximum impact absorption capacity of this shield.
     * Higher values protect against larger space debris impacts.
     * @return The impact protection level
     */
    float getImpactProtection();
    
    /**
     * Gets the heat resistance of this shield.
     * Higher values provide better protection during atmospheric entry.
     * @return The heat resistance
     */
    float getHeatResistance();
    
    /**
     * Gets the radiation shielding effectiveness.
     * Higher values protect against cosmic radiation.
     * @return The radiation shielding level
     */
    float getRadiationShielding();
    
    /**
     * Gets the shield type.
     * @return The shield type
     */
    ShieldType getShieldType();
    
    /**
     * Gets the energy consumption of this shield if it's powered.
     * Only applicable to energy shields.
     * @return The energy consumption per tick, or 0 if passive
     */
    int getEnergyConsumption();
    
    /**
     * Checks if this shield is regenerative.
     * @return true if the shield can repair itself over time
     */
    boolean isRegenerative();
    
    /**
     * Gets the coverage percentage of this shield.
     * Not all shields provide 100% coverage.
     * @return The coverage percentage (0-100)
     */
    float getCoveragePercentage();
    
    /**
     * Enum representing the different types of shields.
     */
    enum ShieldType {
        PHYSICAL,       // Metal plating, no energy required
        THERMAL,        // Heat shields for atmospheric entry
        MAGNETIC,       // Energy-based protection against charged particles
        DEFLECTOR,      // Energy-based protection against physical impacts
        QUANTUM         // Advanced energy shield with regenerative capabilities
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.SHIELD;
    }
}