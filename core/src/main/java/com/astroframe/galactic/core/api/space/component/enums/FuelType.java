package com.astroframe.galactic.core.api.space.component.enums;

/**
 * Enum for different types of rocket fuels.
 */
public enum FuelType {
    /**
     * Standard chemical rocket fuel.
     * Medium energy density, relatively safe.
     */
    CHEMICAL,
    
    /**
     * Hydrogen fuel, typically used in fuel cells or certain engines.
     * Clean burning but low energy density.
     */
    HYDROGEN,
    
    /**
     * Nuclear fuel, used in nuclear thermal rockets.
     * Very high energy density, but dangerous.
     */
    NUCLEAR,
    
    /**
     * Electric "fuel" - not really a fuel, but a power source.
     * Used by ion engines and similar.
     */
    ELECTRIC,
    
    /**
     * Fusion fuel, typically hydrogen isotopes.
     * Extremely high energy density, advanced technology.
     */
    FUSION,
    
    /**
     * Antimatter, the most energy-dense "fuel" possible.
     * Extremely dangerous, requires special containment.
     */
    ANTIMATTER,
    
    /**
     * Exotic matter with negative energy density.
     * Theoretical, used for warp drives or similar.
     */
    EXOTIC,
    
    /**
     * Solid fuel, cannot be throttled once ignited.
     * Simple and reliable.
     */
    SOLID,
    
    /**
     * Liquid fuel, can be throttled.
     * More complex but more versatile than solid.
     */
    LIQUID,
    
    /**
     * Biofuel, renewable but lower energy density.
     * Environmentally friendly alternative.
     */
    BIOFUEL,
    
    /**
     * Oxidizer component for chemical rockets.
     * Not a fuel itself, but required for combustion.
     */
    OXIDIZER
}