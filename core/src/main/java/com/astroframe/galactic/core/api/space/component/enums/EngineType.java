package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Types of rocket engines available in the game.
 */
public enum EngineType {
    /**
     * Generic chemical rocket engine - used as a compatibility constant.
     */
    CHEMICAL,
    
    /**
     * Chemical rocket engine using liquid propellants.
     */
    CHEMICAL_LIQUID,
    
    /**
     * Chemical rocket engine using solid propellants.
     */
    CHEMICAL_SOLID,
    
    /**
     * Ion thruster technology with high efficiency but low thrust.
     */
    ION,
    
    /**
     * Plasma-based propulsion engine.
     */
    PLASMA,
    
    /**
     * Nuclear thermal propulsion system.
     */
    NUCLEAR_THERMAL,
    
    /**
     * Fusion rocket with high thrust and efficiency.
     */
    FUSION,
    
    /**
     * Exotic matter drive for advanced space travel.
     */
    EXOTIC_MATTER,
    
    /**
     * Antimatter rocket with extremely high energy density.
     */
    ANTIMATTER;
    
    /**
     * Get the thrust multiplier for this engine type.
     * @return The thrust multiplier
     */
    public float getThrustMultiplier() {
        return switch (this) {
            case CHEMICAL -> 1.0f; // Same as chemical liquid
            case CHEMICAL_LIQUID -> 1.0f;
            case CHEMICAL_SOLID -> 1.2f;
            case ION -> 0.7f;
            case PLASMA -> 1.3f;
            case NUCLEAR_THERMAL -> 1.5f;
            case FUSION -> 2.0f;
            case EXOTIC_MATTER -> 2.5f;
            case ANTIMATTER -> 3.0f;
        };
    }
    
    /**
     * Get the fuel efficiency multiplier for this engine type.
     * @return The efficiency multiplier
     */
    public float getEfficiencyMultiplier() {
        return switch (this) {
            case CHEMICAL -> 1.0f; // Same as chemical liquid
            case CHEMICAL_LIQUID -> 1.0f;
            case CHEMICAL_SOLID -> 0.8f;
            case ION -> 3.0f;
            case PLASMA -> 1.5f;
            case NUCLEAR_THERMAL -> 2.0f;
            case FUSION -> 2.5f;
            case EXOTIC_MATTER -> 3.5f;
            case ANTIMATTER -> 5.0f;
        };
    }
    
    /**
     * Get the technology tier required for this engine type.
     * @return The technology tier
     */
    public int getTechTier() {
        return switch (this) {
            case CHEMICAL -> 1; // Same as chemical liquid
            case CHEMICAL_LIQUID -> 1;
            case CHEMICAL_SOLID -> 1;
            case ION -> 2;
            case PLASMA -> 2;
            case NUCLEAR_THERMAL -> 3;
            case FUSION -> 4;
            case EXOTIC_MATTER -> 5;
            case ANTIMATTER -> 5;
        };
    }
}