package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket shield components.
 * Shields protect the rocket from environmental hazards and damage.
 */
public interface IShield extends IRocketComponent {
    
    /**
     * Gets the maximum shield strength.
     * Higher values provide more protection.
     * @return The maximum shield strength
     */
    float getMaxShieldStrength();
    
    /**
     * Gets the shield regeneration rate.
     * Higher values mean faster shield regeneration after taking damage.
     * @return The shield regeneration rate in strength per second
     */
    float getRegenerationRate();
    
    /**
     * Gets the shield type.
     * @return The shield type
     */
    ShieldType getShieldType();
    
    /**
     * Gets the energy consumption of this shield when active.
     * @return The energy consumption rate
     */
    int getEnergyConsumption();
    
    /**
     * Gets the heat resistance of this shield.
     * Higher values provide better protection during atmospheric entry.
     * @return The heat resistance level (1-10)
     */
    int getHeatResistance();
    
    /**
     * Gets the impact resistance of this shield.
     * Higher values provide better protection against physical impacts.
     * @return The impact resistance level (1-10)
     */
    int getImpactResistance();
    
    /**
     * Enum representing the different types of shields.
     */
    enum ShieldType {
        HEAT_SHIELD("Heat Shield"),           // Protects from atmospheric entry heat
        IMPACT_SHIELD("Impact Shield"),       // Protects from physical impacts
        RADIATION_SHIELD("Radiation Shield"), // Protects from cosmic radiation
        ENERGY_SHIELD("Energy Shield"),       // Advanced shield using energy field
        COMPOSITE("Composite Shield");        // Balanced protection against multiple threats
        
        private final String displayName;
        
        ShieldType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.SHIELD;
    }
}