package com.example.modapi.energy;

import com.astroframe.galactic.energy.GalacticEnergy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compatibility class that redirects to the new GalacticEnergy module.
 * This class exists only for backward compatibility with existing code.
 * All new code should use GalacticEnergy directly.
 * 
 * @deprecated Use {@link com.astroframe.galactic.energy.GalacticEnergy} instead
 */
@Deprecated
public class ModApiEnergy {
    public static final String MOD_ID = GalacticEnergy.MOD_ID;
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    private static final ModApiEnergy instance = new ModApiEnergy();
    
    /**
     * Constructor for the compatibility layer.
     */
    private ModApiEnergy() {
        LOGGER.warn("ModApiEnergy is deprecated, use GalacticEnergy instead");
    }
    
    /**
     * Get the singleton instance of this compatibility layer.
     * 
     * @return The singleton instance
     * @deprecated Use {@link com.astroframe.galactic.energy.GalacticEnergy} instead
     */
    @Deprecated
    public static ModApiEnergy getInstance() {
        return instance;
    }
}
