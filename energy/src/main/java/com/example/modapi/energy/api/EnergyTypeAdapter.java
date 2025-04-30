package com.example.modapi.energy.api;

/**
 * Adapter class to convert between old and new energy types.
 * This provides compatibility between the old and new APIs.
 */
public class EnergyTypeAdapter {
    
    /**
     * Convert an old energy type to a new energy type.
     * 
     * @param oldType The old energy type
     * @return The new energy type
     */
    public static com.astroframe.galactic.energy.api.EnergyType toNewType(EnergyType oldType) {
        if (oldType == EnergyType.FORGE_ENERGY || oldType == EnergyType.REDSTONE_FLUX) {
            return com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL;
        } else if (oldType == EnergyType.MODAPI_ENERGY) {
            return com.astroframe.galactic.energy.api.EnergyType.QUANTUM;
        } else {
            return com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL;
        }
    }
    
    /**
     * Convert a new energy type to an old energy type.
     * 
     * @param newType The new energy type
     * @return The old energy type
     */
    public static EnergyType toOldType(com.astroframe.galactic.energy.api.EnergyType newType) {
        if (newType == com.astroframe.galactic.energy.api.EnergyType.ELECTRICAL) {
            return EnergyType.FORGE_ENERGY;
        } else if (newType == com.astroframe.galactic.energy.api.EnergyType.THERMAL) {
            return EnergyType.REDSTONE_FLUX;
        } else if (newType == com.astroframe.galactic.energy.api.EnergyType.QUANTUM) {
            return EnergyType.MODAPI_ENERGY;
        } else {
            return EnergyType.FORGE_ENERGY;
        }
    }
}