package com.astroframe.galactic.core;

/**
 * A simple core class for the Galactic Expansion mod.
 * This is a placeholder for demonstration purposes.
 */
public class SimpleCore {
    
    private static final String VERSION = "1.0.0";
    
    /**
     * Get the version of the Galactic Expansion mod.
     * 
     * @return The version string
     */
    public static String getVersion() {
        return VERSION;
    }
    
    /**
     * Initialize the core module.
     */
    public static void initialize() {
        System.out.println("Initializing Galactic Expansion Core v" + VERSION);
    }
    
    /**
     * Main method for testing purposes only.
     */
    public static void main(String[] args) {
        initialize();
        System.out.println("Galactic Expansion Core is ready!");
    }
}