package com.astroframe.galactic.core.api.space;

import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

/**
 * Represents radiation levels for celestial bodies.
 * Used for categorizing planets and moons by their radiation hazards.
 */
public enum RadiationLevel {
    NONE("None", 0.0f, 0.1f, ChatFormatting.GREEN),
    LOW("Low", 0.1f, 1.0f, ChatFormatting.GREEN),
    MODERATE("Moderate", 1.0f, 5.0f, ChatFormatting.YELLOW),
    HIGH("High", 5.0f, 20.0f, ChatFormatting.GOLD),
    EXTREME("Extreme", 20.0f, 100.0f, ChatFormatting.RED),
    DEADLY("Deadly", 100.0f, Float.MAX_VALUE, ChatFormatting.DARK_RED);

    private final String name;
    private final float minLevel;
    private final float maxLevel;
    private final ChatFormatting textColor;

    RadiationLevel(String name, float minLevel, float maxLevel, ChatFormatting textColor) {
        this.name = name;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.textColor = textColor;
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
    
    public ChatFormatting getTextColor() {
        return textColor;
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
    
    /**
     * Converts the radiation level to a displayable component
     * with appropriate color coding
     * 
     * @return A text component with formatting
     */
    public Component getDisplayComponent() {
        return Component.literal(getName()).withStyle(textColor);
    }
    
    /**
     * Determines if the radiation level is safe for humans
     * without protective equipment
     * 
     * @return True if radiation is safe for humans
     */
    public boolean isSafe() {
        return this == NONE || this == LOW;
    }
    
    /**
     * Determines if the radiation level is deadly and will
     * quickly kill unprotected humans
     * 
     * @return True if the radiation will cause rapid death
     */
    public boolean isLethal() {
        return this == EXTREME || this == DEADLY;
    }
    
    /**
     * Returns the appropriate tier of radiation protection
     * needed to safely withstand this radiation level
     * 
     * @return Protection tier needed (0-3)
     */
    public int getProtectionTierRequired() {
        return switch(this) {
            case NONE, LOW -> 0;
            case MODERATE -> 1;
            case HIGH -> 2;
            case EXTREME, DEADLY -> 3;
        };
    }
    
    /**
     * Gets the appropriate radiation level from an ordinal value, with bounds checking.
     * 
     * @param ordinal The ordinal value to convert
     * @return The radiation level, or NONE if the ordinal is invalid
     */
    public static RadiationLevel fromOrdinal(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            return NONE; // Default to none if out of range
        }
        return values()[ordinal];
    }
    
    /**
     * Gets a radiation level from a string name.
     * 
     * @param name The name to convert
     * @return The radiation level, or NONE if not found
     */
    public static RadiationLevel fromName(String name) {
        if (name == null || name.isEmpty()) {
            return NONE;
        }
        
        for (RadiationLevel level : values()) {
            if (level.getName().equalsIgnoreCase(name)) {
                return level;
            }
        }
        return NONE;
    }
}