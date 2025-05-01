package com.astroframe.galactic.core.api.space.component;

/**
 * Interface for rocket cargo bay components.
 * Cargo bays provide storage space for items and resources.
 */
public interface ICargoBay extends IRocketComponent {
    
    /**
     * Gets the storage capacity of this cargo bay in inventory slots.
     * @return The number of inventory slots
     */
    int getStorageCapacity();
    
    /**
     * Gets the maximum item stack size for this cargo bay.
     * @return The maximum stack size
     */
    int getMaxStackSize();
    
    /**
     * Gets the specialized storage type for this cargo bay.
     * @return The cargo bay specialized type
     */
    CargoBayType getCargoBayType();
    
    /**
     * Gets the automation level of this cargo bay.
     * Higher values allow for more automated loading/unloading.
     * @return The automation level (0-10)
     */
    int getAutomationLevel();
    
    /**
     * Checks if this cargo bay has refrigeration capabilities.
     * @return true if the cargo bay has refrigeration
     */
    boolean hasRefrigeration();
    
    /**
     * Checks if this cargo bay has radiation shielding.
     * @return true if the cargo bay has radiation shielding
     */
    boolean hasRadiationShielding();
    
    /**
     * Enum representing the different specialized cargo bay types.
     */
    enum CargoBayType {
        STANDARD("Standard"),       // General purpose storage
        BULK("Bulk Storage"),       // Optimized for large quantities of the same item
        FLUID("Fluid Storage"),     // For storing liquids
        SPECIMEN("Specimen"),       // For biological samples
        EQUIPMENT("Equipment"),     // For tools and specialized equipment
        HAZARDOUS("Hazardous");     // For dangerous materials
        
        private final String displayName;
        
        CargoBayType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return this.displayName;
        }
    }
    
    @Override
    default ComponentType getType() {
        return ComponentType.CARGO_BAY;
    }
}