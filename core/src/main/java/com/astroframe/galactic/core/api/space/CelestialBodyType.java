package com.astroframe.galactic.core.api.space;

/**
 * Celestial body type categories.
 */
public enum CelestialBodyType {
    PLANET,
    MOON,
    ASTEROID,
    COMET,
    STAR,
    SPACE_STATION,
    DWARF_PLANET,
    ARTIFICIAL,
    OTHER;
    
    /**
     * Determines if this celestial body type typically orbits another.
     * 
     * @return true if this body type typically orbits another body
     */
    public boolean isOrbital() {
        return this == MOON || this == SPACE_STATION;
    }
    
    /**
     * Determines if this celestial body type is artificial (made by intelligent beings).
     * 
     * @return true if this body type is artificial
     */
    public boolean isArtificial() {
        return this == SPACE_STATION || this == ARTIFICIAL;
    }
    
    /**
     * Gets a readable name for the type.
     * 
     * @return A formatted name for this type
     */
    public String getDisplayName() {
        return switch(this) {
            case PLANET -> "Planet";
            case MOON -> "Moon";
            case ASTEROID -> "Asteroid";
            case COMET -> "Comet";
            case STAR -> "Star";
            case SPACE_STATION -> "Space Station";
            case DWARF_PLANET -> "Dwarf Planet";
            case ARTIFICIAL -> "Artificial Structure";
            case OTHER -> "Unknown";
        };
    }
}