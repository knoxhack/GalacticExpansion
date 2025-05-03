package com.astroframe.galactic.core.api.space;

/**
 * Temperature range categories for celestial bodies
 */
public enum TemperatureRange {
    EXTREME_COLD,    // Below -100°C
    VERY_COLD,       // -100°C to -50°C
    COLD,            // -50°C to 0°C
    TEMPERATE,       // 0°C to 30°C
    HOT,             // 30°C to 100°C
    VERY_HOT,        // 100°C to 500°C
    EXTREME_HOT      // Above 500°C
}