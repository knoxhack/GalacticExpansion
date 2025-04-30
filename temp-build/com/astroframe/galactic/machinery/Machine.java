package com.astroframe.galactic.machinery;

import com.astroframe.galactic.core.SimpleCore;
import com.astroframe.galactic.energy.EnergySystem;

/**
 * A simple machine implementation for the Galactic Expansion mod.
 * This is a placeholder for demonstration purposes.
 */
public class Machine {
    
    private static final String VERSION = "1.0.0";
    
    private final String name;
    private final EnergySystem energySystem;
    private boolean running;
    
    /**
     * Create a new machine with the specified name and energy capacity.
     * 
     * @param name The machine name
     * @param energyCapacity The energy capacity of the machine
     */
    public Machine(String name, int energyCapacity) {
        this.name = name;
        this.energySystem = new EnergySystem(energyCapacity);
        this.running = false;
    }
    
    /**
     * Get the version of the machinery module.
     * 
     * @return The version string
     */
    public static String getVersion() {
        return VERSION;
    }
    
    /**
     * Get the machine name.
     * 
     * @return The machine name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the machine's energy system.
     * 
     * @return The energy system
     */
    public EnergySystem getEnergySystem() {
        return energySystem;
    }
    
    /**
     * Check if the machine is running.
     * 
     * @return True if the machine is running, false otherwise
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Start the machine.
     * 
     * @return True if the machine was started, false otherwise
     */
    public boolean start() {
        if (energySystem.getEnergy() > 0) {
            running = true;
            System.out.println("Started machine: " + name);
            return true;
        } else {
            System.out.println("Cannot start machine: " + name + " (no energy)");
            return false;
        }
    }
    
    /**
     * Stop the machine.
     */
    public void stop() {
        running = false;
        System.out.println("Stopped machine: " + name);
    }
    
    /**
     * Process a work cycle, consuming energy.
     * 
     * @param energyCost The energy cost of processing
     * @return True if processing was successful, false if not enough energy
     */
    public boolean process(int energyCost) {
        if (!running) {
            System.out.println("Cannot process: machine is not running");
            return false;
        }
        
        int extracted = energySystem.extractEnergy(energyCost);
        if (extracted >= energyCost) {
            System.out.println("Machine " + name + " processed work cycle (cost: " + energyCost + ")");
            return true;
        } else {
            System.out.println("Machine " + name + " failed to process (insufficient energy)");
            energySystem.addEnergy(extracted); // refund the energy
            return false;
        }
    }
    
    /**
     * Initialize the machinery module.
     */
    public static void initialize() {
        System.out.println("Initializing Galactic Expansion Machinery v" + VERSION);
        System.out.println("Core version: " + SimpleCore.getVersion());
        System.out.println("Energy system version: " + EnergySystem.getVersion());
    }
    
    /**
     * Main method for testing purposes only.
     */
    public static void main(String[] args) {
        SimpleCore.initialize();
        EnergySystem.initialize();
        initialize();
        
        Machine machine = new Machine("TestMachine", 1000);
        machine.getEnergySystem().addEnergy(500);
        
        System.out.println("Machine: " + machine.getName());
        System.out.println("Energy: " + machine.getEnergySystem().getEnergy() + " / " + 
                           machine.getEnergySystem().getCapacity());
        
        machine.start();
        machine.process(200);
        
        System.out.println("Energy after processing: " + 
                           machine.getEnergySystem().getEnergy() + " / " + 
                           machine.getEnergySystem().getCapacity());
        
        machine.stop();
        
        System.out.println("Galactic Expansion Machinery is ready!");
    }
}