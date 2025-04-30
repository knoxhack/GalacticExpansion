package com.astroframe.galactic.energy;

import com.astroframe.galactic.core.SimpleCore;

/**
 * A simple energy system for the Galactic Expansion mod.
 * This is a placeholder for demonstration purposes.
 */
public class EnergySystem {
    
    private static final String VERSION = "1.0.0";
    private static final int DEFAULT_CAPACITY = 10000;
    
    private int capacity;
    private int energy;
    
    /**
     * Create a new energy system with the default capacity.
     */
    public EnergySystem() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Create a new energy system with the specified capacity.
     * 
     * @param capacity The maximum energy capacity
     */
    public EnergySystem(int capacity) {
        this.capacity = capacity;
        this.energy = 0;
    }
    
    /**
     * Get the version of the energy system.
     * 
     * @return The version string
     */
    public static String getVersion() {
        return VERSION;
    }
    
    /**
     * Get the current energy level.
     * 
     * @return The current energy level
     */
    public int getEnergy() {
        return energy;
    }
    
    /**
     * Get the maximum energy capacity.
     * 
     * @return The maximum energy capacity
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Add energy to the system.
     * 
     * @param amount The amount of energy to add
     * @return The amount of energy actually added
     */
    public int addEnergy(int amount) {
        int added = Math.min(amount, capacity - energy);
        energy += added;
        return added;
    }
    
    /**
     * Extract energy from the system.
     * 
     * @param amount The amount of energy to extract
     * @return The amount of energy actually extracted
     */
    public int extractEnergy(int amount) {
        int extracted = Math.min(amount, energy);
        energy -= extracted;
        return extracted;
    }
    
    /**
     * Initialize the energy module.
     */
    public static void initialize() {
        System.out.println("Initializing Galactic Expansion Energy System v" + VERSION);
        System.out.println("Core version: " + SimpleCore.getVersion());
    }
    
    /**
     * Main method for testing purposes only.
     */
    public static void main(String[] args) {
        SimpleCore.initialize();
        initialize();
        
        EnergySystem system = new EnergySystem(5000);
        system.addEnergy(3000);
        System.out.println("Energy: " + system.getEnergy() + " / " + system.getCapacity());
        
        int extracted = system.extractEnergy(1000);
        System.out.println("Extracted: " + extracted);
        System.out.println("Energy: " + system.getEnergy() + " / " + system.getCapacity());
        
        System.out.println("Galactic Expansion Energy System is ready!");
    }
}