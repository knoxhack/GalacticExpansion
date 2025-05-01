package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket command modules.
 */
public interface ICommandModule extends IRocketComponent {
    
    /**
     * Gets the computing power of this command module.
     * Higher tier modules have more computing power.
     * @return The computing power
     */
    int getComputingPower();
    
    /**
     * Gets the sensor strength of this command module.
     * Higher sensor strength allows detection of more distant celestial bodies.
     * @return The sensor strength
     */
    int getSensorStrength();
    
    /**
     * Gets the navigation accuracy of this command module.
     * Higher accuracy means more precise landings.
     * @return The navigation accuracy
     */
    float getNavigationAccuracy();
    
    /**
     * Gets the number of crew this command module can hold.
     * @return The crew capacity
     */
    int getCrewCapacity();
    
    /**
     * Whether this command module has advanced life support systems.
     * @return true if the module has advanced life support
     */
    boolean hasAdvancedLifeSupport();
    
    /**
     * Whether this command module has automated landing systems.
     * @return true if the module has automated landing
     */
    boolean hasAutomatedLanding();
    
    /**
     * Whether this command module has emergency evacuation systems.
     * @return true if the module has emergency evacuation
     */
    boolean hasEmergencyEvacuation();
}