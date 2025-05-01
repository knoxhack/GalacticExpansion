package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket life support systems.
 */
public interface ILifeSupport extends IRocketComponent {
    
    /**
     * Gets the maximum number of crew this life support system can sustain.
     * @return The maximum crew capacity
     */
    int getMaxCrewCapacity();
    
    /**
     * Gets the oxygen generation rate in units per minute.
     * @return The oxygen generation rate
     */
    int getOxygenGenerationRate();
    
    /**
     * Gets the water recycling efficiency (0-1).
     * Higher values mean more water is recycled.
     * @return The water recycling efficiency
     */
    float getWaterRecyclingEfficiency();
    
    /**
     * Gets the food production rate in units per day.
     * @return The food production rate
     */
    int getFoodProductionRate();
    
    /**
     * Gets the waste management efficiency (0-1).
     * Higher values mean more waste is processed.
     * @return The waste management efficiency
     */
    float getWasteManagementEfficiency();
    
    /**
     * Gets the atmospheric quality (0-1).
     * Higher values mean better air quality.
     * @return The atmospheric quality
     */
    float getAtmosphericQuality();
    
    /**
     * Whether this life support system has backup systems.
     * @return true if the system has backups
     */
    boolean hasBackupSystems();
    
    /**
     * Whether this life support system has radiation filtering.
     * @return true if the system has radiation filtering
     */
    boolean hasRadiationFiltering();
    
    /**
     * Whether this life support system can operate in emergency mode.
     * @return true if the system has emergency mode
     */
    boolean hasEmergencyMode();
    
    /**
     * Activates or deactivates emergency mode.
     * @param active Whether to activate emergency mode
     */
    void setEmergencyMode(boolean active);
    
    /**
     * Whether emergency mode is currently active.
     * @return true if emergency mode is active
     */
    boolean isEmergencyModeActive();
}