package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket command module components.
 * The command module serves as the brain of the rocket, containing
 * navigation systems, communications, and flight control mechanisms.
 */
public interface ICommandModule extends IRocketComponent {
    
    /**
     * Gets the navigation capability level.
     * Higher values provide better pathfinding to distant destinations.
     * @return The navigation capability level (1-10)
     */
    int getNavigationLevel();
    
    /**
     * Gets the maximum crew capacity of this command module.
     * @return The maximum number of crew members
     */
    int getCrewCapacity();
    
    /**
     * Gets the computing power of this command module.
     * Higher values allow for more advanced flight maneuvers and automation.
     * @return The computing power level (1-10)
     */
    int getComputingPower();
    
    /**
     * Gets the communication range of this command module.
     * Higher values allow for communication with more distant positions.
     * @return The communication range in arbitrary units
     */
    int getCommunicationRange();
    
    /**
     * Gets the command module type.
     * @return The command module type
     */
    CommandModuleType getCommandModuleType();
    
    /**
     * Enum representing the different types of command modules.
     */
    enum CommandModuleType {
        BASIC("Basic"),           // Minimal functionality, early-game option
        ADVANCED("Advanced"),     // Mid-tier performance and capabilities
        AUTOMATED("Automated"),   // Advanced with automated systems
        MILITARY("Military"),     // Combat-oriented command systems
        SCIENTIFIC("Scientific"); // Research-oriented with advanced sensors
        
        private final String displayName;
        
        CommandModuleType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.COMMAND_MODULE;
    }
}