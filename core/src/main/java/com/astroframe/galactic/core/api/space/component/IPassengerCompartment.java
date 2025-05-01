package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket passenger compartment components.
 * Passenger compartments provide accommodation for players and NPCs.
 */
public interface IPassengerCompartment extends IRocketComponent {
    
    /**
     * Gets the maximum passenger capacity.
     * @return The number of passenger slots
     */
    int getPassengerCapacity();
    
    /**
     * Gets the comfort level of this passenger compartment.
     * Higher values provide buffs to passengers during space travel.
     * @return The comfort level (1-10)
     */
    int getComfortLevel();
    
    /**
     * Gets the life support duration provided by this compartment.
     * Measured in minutes of game time.
     * @return The life support duration in minutes
     */
    int getLifeSupportDuration();
    
    /**
     * Gets the passenger compartment type.
     * @return The passenger compartment type
     */
    CompartmentType getCompartmentType();
    
    /**
     * Gets the radiation protection level.
     * Higher values reduce radiation damage to passengers.
     * @return The radiation protection level (1-10)
     */
    int getRadiationProtection();
    
    /**
     * Checks if this compartment has artificial gravity.
     * @return true if the compartment has artificial gravity
     */
    boolean hasArtificialGravity();
    
    /**
     * Enum representing the different types of passenger compartments.
     */
    enum CompartmentType {
        BASIC("Basic"),                   // Minimal accommodations
        STANDARD("Standard"),             // Average comfort and amenities
        LUXURY("Luxury"),                 // High comfort, provides buffs
        CRYOSLEEP("Cryosleep"),           // For long-duration missions
        RESEARCH("Research"),             // Contains scientific equipment
        MILITARY("Military Transport");   // Optimized for troop transport
        
        private final String displayName;
        
        CompartmentType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.PASSENGER_COMPARTMENT;
    }
}