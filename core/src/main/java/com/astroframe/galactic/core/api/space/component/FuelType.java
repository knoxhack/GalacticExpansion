package com.astroframe.galactic.core.api.space.component;

/**
 * Enum representing different types of rocket fuel.
 */
public enum FuelType {
    /**
     * Chemical fuel, most basic type (Tier 1).
     */
    CHEMICAL(1, "Chemical", 1.0),
    
    /**
     * Ion propulsion fuel, more efficient but less thrust (Tier 2).
     */
    ION(2, "Ion", 2.5),
    
    /**
     * Antimatter fuel, highest efficiency and thrust (Tier 3).
     */
    ANTIMATTER(3, "Antimatter", 5.0);
    
    private final int tier;
    private final String displayName;
    private final double efficiencyMultiplier;
    
    /**
     * Creates a new fuel type.
     *
     * @param tier The tier level of this fuel type
     * @param displayName The display name
     * @param efficiencyMultiplier The efficiency multiplier
     */
    FuelType(int tier, String displayName, double efficiencyMultiplier) {
        this.tier = tier;
        this.displayName = displayName;
        this.efficiencyMultiplier = efficiencyMultiplier;
    }
    
    /**
     * Gets the tier level of this fuel type.
     *
     * @return The tier level
     */
    public int getTier() {
        return tier;
    }
    
    /**
     * Gets the display name of this fuel type.
     *
     * @return The display name
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the efficiency multiplier for this fuel type.
     *
     * @return The efficiency multiplier
     */
    public double getEfficiencyMultiplier() {
        return efficiencyMultiplier;
    }
    
    /**
     * Gets a fuel type by its tier level.
     *
     * @param tier The tier level
     * @return The fuel type, or CHEMICAL if not found
     */
    public static FuelType getByTier(int tier) {
        for (FuelType type : values()) {
            if (type.getTier() == tier) {
                return type;
            }
        }
        return CHEMICAL; // Default to chemical
    }
}