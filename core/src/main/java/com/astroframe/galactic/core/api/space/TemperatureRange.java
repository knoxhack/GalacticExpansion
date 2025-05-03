package com.astroframe.galactic.core.api.space;

import net.minecraft.network.chat.Component;

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
    
    /**
     * Converts the temperature range to a displayable component
     * 
     * @return A text component containing the formatted name
     */
    public Component getDisplayComponent() {
        return Component.literal(getName());
    }
    
    /**
     * Checks if this temperature range is habitable for humans without special equipment
     * 
     * @return True if humans can survive in this temperature range
     */
    public boolean isHabitable() {
        return this == TEMPERATE || this == COLD;
    }
    
    /**
     * Gets the average temperature in this range.
     * 
     * @return The average temperature in Celsius
     */
    public float getAverageTemperature() {
        return (minTemp + maxTemp) / 2.0f;
    }
    
    /**
     * Gets the appropriate temperature range from an ordinal value, with bounds checking.
     * 
     * @param ordinal The ordinal value to convert
     * @return The temperature range, or TEMPERATE if the ordinal is invalid
     */
    public static TemperatureRange fromOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            return TEMPERATE; // Default to temperate if out of range
        }
        return values()[ordinal];
    }
    
    /**
     * Gets a temperature range from a string name.
     * 
     * @param name The name to convert
     * @return The temperature range, or TEMPERATE if not found
     */
    public static TemperatureRange fromName(String name) {
        if (name == null || name.isEmpty()) {
            return TEMPERATE;
        }
        
        for (TemperatureRange range : values()) {
            if (range.getName().equalsIgnoreCase(name)) {
                return range;
            }
        }
        return TEMPERATE;
    }
}