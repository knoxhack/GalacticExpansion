package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket command module components.
 * The command module serves as the control center of the rocket.
 */
public interface ICommandModule extends IRocketComponent {
    
    /**
     * Gets the navigation accuracy of this command module.
     * Higher values provide more precise navigation.
     * @return The navigation accuracy
     */
    float getNavigationAccuracy();
    
    /**
     * Gets the computer tier of this command module.
     * Higher tiers enable more advanced navigation capabilities.
     * @return The computer tier
     */
    int getComputerTier();
    
    /**
     * Gets the scanning range of this command module.
     * Determines how far the rocket can detect celestial bodies.
     * @return The scanning range in arbitrary units
     */
    int getScanningRange();
    
    /**
     * Gets the autopilot capability level of this command module.
     * Higher levels enable more autonomous flight capabilities.
     * @return The autopilot level
     */
    int getAutopilotLevel();
    
    /**
     * Checks if this command module has emergency teleportation capabilities.
     * @return true if emergency teleportation is available
     */
    boolean hasEmergencyTeleport();
    
    /**
     * Gets the basic life support capacity of this command module.
     * Represents the number of players it can support without additional life support systems.
     * @return The life support capacity
     */
    int getBasicLifeSupportCapacity();
    
    /**
     * Enum representing the different types of command modules.
     */
    enum CommandModuleType {
        BASIC,          // Manual control only
        STANDARD,       // Basic autopilot capabilities
        ADVANCED,       // Advanced navigation and autopilot
        QUANTUM         // Cutting-edge with emergency teleportation
    }
    
    /**
     * Gets the command module type.
     * @return The command module type
     */
    CommandModuleType getCommandModuleType();
    
    @Override
    default ComponentType getType() {
        return ComponentType.COMMAND_MODULE;
    }
}