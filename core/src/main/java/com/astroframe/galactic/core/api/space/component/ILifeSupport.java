package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket life support components.
 */
public interface ILifeSupport extends IRocketComponent {
    
    /**
     * Gets the maximum number of passengers this life support system can sustain.
     * @return The passenger capacity
     */
    int getSupportCapacity();
    
    /**
     * Gets the oxygen generation rate of this life support system.
     * Higher values sustain more passengers for longer.
     * @return The oxygen generation rate
     */
    float getOxygenGenerationRate();
    
    /**
     * Gets the water recycling efficiency.
     * Higher values reduce water consumption during long journeys.
     * @return The water recycling efficiency (0-1)
     */
    float getWaterRecyclingEfficiency();
    
    /**
     * Gets the food production capability.
     * Higher values reduce food requirements during long journeys.
     * @return The food production rate
     */
    float getFoodProductionRate();
    
    /**
     * Gets the waste processing efficiency.
     * Higher values improve environmental quality during long journeys.
     * @return The waste processing efficiency (0-1)
     */
    float getWasteProcessingEfficiency();
    
    /**
     * Gets the radiation filtration level.
     * Higher values reduce radiation exposure effects.
     * @return The radiation filtration level
     */
    float getRadiationFiltration();
    
    /**
     * Gets the life support type.
     * @return The life support type
     */
    LifeSupportType getLifeSupportType();
    
    /**
     * Gets the backup duration of this life support system.
     * How long the system can operate if primary systems fail.
     * @return The backup duration in minutes
     */
    int getBackupDuration();
    
    /**
     * Enum representing the different types of life support systems.
     */
    enum LifeSupportType {
        BASIC,          // Minimal oxygen generation only
        STANDARD,       // Oxygen and water recycling
        ADVANCED,       // Complete environmental control
        BIOREGENERATIVE // Self-sustaining biosphere system
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.LIFE_SUPPORT;
    }
}