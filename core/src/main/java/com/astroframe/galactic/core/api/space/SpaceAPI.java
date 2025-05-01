package com.astroframe.galactic.core.api.space;

/**
 * API access point for space-related functionality.
 * This class provides static methods to access the Space API.
 */
public class SpaceAPI {
    private static ISpaceTravelManager spaceTravelManager;
    
    /**
     * Sets the space travel manager implementation.
     * This should be called by the Space module during initialization.
     * 
     * @param manager The space travel manager implementation
     */
    public static void setSpaceTravelManager(ISpaceTravelManager manager) {
        spaceTravelManager = manager;
    }
    
    /**
     * Gets the space travel manager.
     * 
     * @return The space travel manager
     * @throws IllegalStateException if the space travel manager has not been set
     */
    public static ISpaceTravelManager getSpaceTravelManager() {
        if (spaceTravelManager == null) {
            throw new IllegalStateException("Space Travel Manager has not been initialized");
        }
        return spaceTravelManager;
    }
    
    /**
     * Checks if the space travel manager has been initialized.
     * 
     * @return true if the space travel manager has been set
     */
    public static boolean isSpaceTravelManagerInitialized() {
        return spaceTravelManager != null;
    }
}