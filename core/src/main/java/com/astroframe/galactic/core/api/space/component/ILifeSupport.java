package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket life support components.
 * Life support systems provide oxygen, temperature control, and other necessities
 * for crew survival in the harsh environment of space.
 */
public interface ILifeSupport extends IRocketComponent {
    
    /**
     * Gets the life support system's oxygen generation rate.
     * @return The oxygen generation rate in units per minute
     */
    float getOxygenGenerationRate();
    
    /**
     * Gets the maximum crew capacity this life support system can sustain.
     * @return The maximum crew capacity
     */
    int getMaxCrewCapacity();
    
    /**
     * Gets the life support system type.
     * @return The life support type
     */
    LifeSupportType getLifeSupportType();
    
    /**
     * Gets the energy consumption of this life support system.
     * @return The energy consumption rate
     */
    int getEnergyConsumption();
    
    /**
     * Gets the redundancy level of this life support system.
     * Higher values mean the system continues working even when partially damaged.
     * @return The redundancy level (1-5)
     */
    int getRedundancyLevel();
    
    /**
     * Gets the toxin filtering capability.
     * Higher values provide better protection against environmental toxins.
     * @return The toxin filtering capability (1-10)
     */
    int getToxinFiltering();
    
    /**
     * Enum representing the different types of life support systems.
     */
    enum LifeSupportType {
        BASIC("Basic"),                 // Minimal life support for short trips
        CHEMICAL("Chemical"),           // Standard chemical-based system
        BIOLOGICAL("Biological"),       // Advanced system using plants and algae
        RECYCLING("Recycling"),         // High-efficiency system with recycling
        QUANTUM("Quantum");             // End-game system with matter conversion
        
        private final String displayName;
        
        LifeSupportType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.LIFE_SUPPORT;
    }
}