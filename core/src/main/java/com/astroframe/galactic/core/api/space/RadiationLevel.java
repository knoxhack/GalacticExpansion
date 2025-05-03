package com.astroframe.galactic.core.api.space;

/**
 * Represents radiation levels for celestial bodies.
 * Used for categorizing planets and moons by their radiation hazards.
 */
public enum RadiationLevel {
    NONE("None", 0.0f, 0.1f),
    LOW("Low", 0.1f, 1.0f),
    MODERATE("Moderate", 1.0f, 5.0f),
    HIGH("High", 5.0f, 20.0f),
    EXTREME("Extreme", 20.0f, 100.0f),
    DEADLY("Deadly", 100.0f, Float.MAX_VALUE);

    private final String name;
    private final float minLevel;
    private final float maxLevel;

    RadiationLevel(String name, float minLevel, float maxLevel) {
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
    }

    public String getName() {
        return name;
    }

    public float getMinLevel() {
        return minLevel;
    }

    public float getMaxLevel() {
        return maxLevel;
    }

    /**
     * Gets a radiation level from a radiation value.
     *
     * @param value The radiation value in standard units
     * @return The appropriate radiation level
     */
    public static RadiationLevel fromValue(float value) {
        for (RadiationLevel level : values()) {
            if (value >= level.minLevel && value < level.maxLevel) {
                return level;
            }
        }
        return value < NONE.minLevel ? NONE : DEADLY;
    }
}