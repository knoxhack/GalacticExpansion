package com.astroframe.galactic.example;

import com.astroframe.galactic.core.SimpleCore;
import com.astroframe.galactic.energy.EnergySystem;
import com.astroframe.galactic.machinery.Machine;

/**
 * An example mod that demonstrates the use of the Galactic Expansion API.
 * This is a placeholder for demonstration purposes.
 */
public class ExampleMod {
    
    private static final String VERSION = "1.0.0";
    
    /**
     * Get the version of the example mod.
     * 
     * @return The version string
     */
    public static String getVersion() {
        return VERSION;
    }
    
    /**
     * Initialize the example mod.
     */
    public static void initialize() {
        System.out.println("Initializing Galactic Expansion Example Mod v" + VERSION);
        System.out.println("Core version: " + SimpleCore.getVersion());
        System.out.println("Energy system version: " + EnergySystem.getVersion());
        System.out.println("Machinery version: " + Machine.getVersion());
    }
    
    /**
     * Create a solar panel machine.
     * 
     * @return A new solar panel machine
     */
    public static Machine createSolarPanel() {
        Machine solarPanel = new Machine("Solar Panel", 2000);
        solarPanel.getEnergySystem().addEnergy(500);
        return solarPanel;
    }
    
    /**
     * Create an electric furnace machine.
     * 
     * @return A new electric furnace machine
     */
    public static Machine createElectricFurnace() {
        return new Machine("Electric Furnace", 1000);
    }
    
    /**
     * Main method for testing purposes only.
     */
    public static void main(String[] args) {
        // Initialize all modules
        SimpleCore.initialize();
        EnergySystem.initialize();
        Machine.initialize();
        initialize();
        
        // Create machines
        Machine solarPanel = createSolarPanel();
        Machine furnace = createElectricFurnace();
        
        // Start the solar panel
        solarPanel.start();
        
        // Transfer energy from solar panel to furnace
        int transferAmount = 300;
        int extracted = solarPanel.getEnergySystem().extractEnergy(transferAmount);
        furnace.getEnergySystem().addEnergy(extracted);
        
        System.out.println("Transferred " + extracted + " energy from " + 
                           solarPanel.getName() + " to " + furnace.getName());
        
        // Start the furnace and process
        furnace.start();
        furnace.process(150);
        
        // Display final energy levels
        System.out.println(solarPanel.getName() + " energy: " + 
                           solarPanel.getEnergySystem().getEnergy() + " / " + 
                           solarPanel.getEnergySystem().getCapacity());
        
        System.out.println(furnace.getName() + " energy: " + 
                           furnace.getEnergySystem().getEnergy() + " / " + 
                           furnace.getEnergySystem().getCapacity());
        
        // Stop machines
        solarPanel.stop();
        furnace.stop();
        
        System.out.println("Galactic Expansion Example Mod is ready!");
    }
}