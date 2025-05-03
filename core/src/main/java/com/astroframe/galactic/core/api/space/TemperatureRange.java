package com.astroframe.galactic.core.api.space;

/**
 * Represents temperature ranges for celestial bodies.
 * Used for categorizing planets and moons by their surface temperatures.
 */
public enum TemperatureRange {
    FREEZING("Freezing", -200.0f, -50.0f),
    COLD("Cold", -50.0f, 0.0f),
    TEMPERATE("Temperate", 0.0f, 30.0f),
    HOT("Hot", 30.0f, 100.0f),
    EXTREME("Extreme", 100.0f, 500.0f);

    private final String name;
    private final float minTemp;
    private final float maxTemp;

    TemperatureRange(String name, float minTemp, float maxTemp) {
        this.name = name;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
    }

    public String getName() {
        return name;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    /**
     * Gets a temperature range from a temperature value.
     *
     * @param temp The temperature in Celsius
     * @return The appropriate temperature range
     */
    public static TemperatureRange fromTemperature(float temp) {
        for (TemperatureRange range : values()) {
            if (temp >= range.minTemp && temp < range.maxTemp) {
                return range;
            }
        }
        return temp < FREEZING.minTemp ? FREEZING : EXTREME;
    }
}