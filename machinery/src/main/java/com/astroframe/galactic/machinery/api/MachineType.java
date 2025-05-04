package com.astroframe.galactic.machinery.api;

/**
 * Defines the different types of machines in the mod.
 * This allows for specialized behavior based on machine category.
 */
public enum MachineType {
    /**
     * Machines that generate energy from fuel or other sources.
     */
    GENERATOR,
    
    /**
     * Machines that process materials (crushing, smelting, etc.).
     */
    PROCESSOR,
    
    /**
     * Machines that store energy.
     */
    STORAGE,
    
    /**
     * Machines that transmit energy.
     */
    TRANSMISSION,
    
    /**
     * Specialized machinery for automated assembly.
     */
    ASSEMBLY,
    
    /**
     * Specialized machinery for resource extraction.
     */
    EXTRACTOR,
    
    /**
     * Specialized machinery for research and development.
     */
    RESEARCH;
}