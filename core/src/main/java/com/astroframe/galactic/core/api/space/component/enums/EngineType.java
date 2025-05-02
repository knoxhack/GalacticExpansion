package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Enum for different types of rocket engines.
 */
public enum EngineType {
    /**
     * Chemical engines use chemical reactions to produce thrust.
     * Good for atmospheric flight.
     */
    CHEMICAL,
    
    /**
     * Ion engines use electricity to accelerate ions for thrust.
     * Very efficient but low thrust, good for space.
     */
    ION,
    
    /**
     * Nuclear engines use nuclear reactions to produce thrust.
     * High power and efficiency, but heavy and dangerous.
     */
    NUCLEAR,
    
    /**
     * Fusion engines use fusion reactions to produce thrust.
     * Very high power and efficiency, but very advanced.
     */
    FUSION,
    
    /**
     * Warp engines bend space-time to allow FTL travel.
     * Requires exotic materials and enormous power.
     */
    WARP,
    
    /**
     * Antimatter engines use matter-antimatter reactions.
     * Extremely powerful but extremely dangerous.
     */
    ANTIMATTER,
    
    /**
     * Solid fuel engines use solid propellant.
     * Simple and reliable, but not throttleable.
     */
    SOLID_FUEL,
    
    /**
     * Liquid fuel engines use liquid propellants.
     * Throttleable and efficient, but complex.
     */
    LIQUID_FUEL,
    
    /**
     * Hybrid engines use both solid and liquid propellants.
     * Balance of simplicity and performance.
     */
    HYBRID
}