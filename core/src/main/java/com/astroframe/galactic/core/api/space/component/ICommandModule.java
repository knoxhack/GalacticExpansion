package com.astroframe.galactic.core.api.space.component;

import com.astroframe.galactic.core.api.space.component.enums.CommandModuleType;

/**
 * Interface for rocket command modules.
 */
public interface ICommandModule extends IRocketComponent {
    
    /**
     * Gets the navigation level of this command module (1-10).
     * @return The navigation level
     */
    int getNavigationLevel();
    
    /**
     * Gets the crew capacity of this command module.
     * @return The crew capacity
     */
    int getCrewCapacity();
    
    /**
     * Gets the computing power of this command module (1-10).
     * @return The computing power
     */
    int getComputingPower();
    
    /**
     * Gets the communication range of this command module.
     * @return The communication range
     */
    int getCommunicationRange();
    
    /**
     * Gets the type of this command module.
     * @return The command module type
     */
    CommandModuleType getCommandModuleType();
}