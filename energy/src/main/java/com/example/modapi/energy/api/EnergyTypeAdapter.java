package com.example.modapi.energy.api;

/**
 * Adapter for converting between old and new energy types.
 * This facilitates compatibility between the old and new APIs.
 * 
 * @deprecated Use the new energy API directly with {@link com.astroframe.galactic.energy.api.EnergyType}
 */
@Deprecated
public class EnergyTypeAdapter {
    
    /**
     * Converts an old energy type to a new energy type.
     * 
     * @param oldType The old energy type
     * @return The equivalent new energy type
     */
    public static com.astroframe.galactic.energy.api.EnergyType toNewType(EnergyType oldType) {
        // Get the ID from the old type and create a new type with the same ID
        return com.astroframe.galactic.energy.api.EnergyType.byId(oldType.getId());
    }
    
    /**
     * Converts a new energy type to an old energy type.
     * 
     * @param newType The new energy type
     * @return The equivalent old energy type
     */
    public static EnergyType toOldType(com.astroframe.galactic.energy.api.EnergyType newType) {
        // Get the ID from the new type and create an old type with the same ID
        return EnergyType.byId(newType.getId());
    }
}