package com.astroframe.galactic.energy.config;

/**
 * Configuration for the energy module.
 * Contains settings for energy networks, transfer rates, and storage limits.
 */
public class EnergyConfig {

    // Energy network settings
    private NetworkSettings networkSettings = new NetworkSettings();
    
    // Energy storage settings
    private StorageSettings storageSettings = new StorageSettings();
    
    // Display and conversion settings
    private DisplaySettings displaySettings = new DisplaySettings();
    
    /**
     * Get the network settings.
     * 
     * @return The network settings
     */
    public NetworkSettings getNetworkSettings() {
        return networkSettings;
    }
    
    /**
     * Get the storage settings.
     * 
     * @return The storage settings
     */
    public StorageSettings getStorageSettings() {
        return storageSettings;
    }
    
    /**
     * Get the display settings.
     * 
     * @return The display settings
     */
    public DisplaySettings getDisplaySettings() {
        return displaySettings;
    }
    
    /**
     * Settings for energy networks.
     */
    public static class NetworkSettings {
        // Maximum distance for energy transport
        private int maxTransportDistance = 16;
        
        // Transfer rate per tick for machine connections
        private int machineTransferRate = 100;
        
        // Transfer rate per tick for cable connections
        private int cableTransferRate = 200;
        
        // Whether to use caching for network paths
        private boolean usePathCaching = true;
        
        // Whether to optimize for unloaded chunks
        private boolean optimizeForUnloadedChunks = true;
        
        // Path cache expiry time in seconds
        private int pathCacheExpirySeconds = 300;
        
        /**
         * Get the maximum transport distance.
         * 
         * @return The maximum transport distance
         */
        public int getMaxTransportDistance() {
            return maxTransportDistance;
        }
        
        /**
         * Set the maximum transport distance.
         * 
         * @param maxTransportDistance The new maximum transport distance
         */
        public void setMaxTransportDistance(int maxTransportDistance) {
            this.maxTransportDistance = maxTransportDistance;
        }
        
        /**
         * Get the machine transfer rate.
         * 
         * @return The machine transfer rate
         */
        public int getMachineTransferRate() {
            return machineTransferRate;
        }
        
        /**
         * Set the machine transfer rate.
         * 
         * @param machineTransferRate The new machine transfer rate
         */
        public void setMachineTransferRate(int machineTransferRate) {
            this.machineTransferRate = machineTransferRate;
        }
        
        /**
         * Get the cable transfer rate.
         * 
         * @return The cable transfer rate
         */
        public int getCableTransferRate() {
            return cableTransferRate;
        }
        
        /**
         * Set the cable transfer rate.
         * 
         * @param cableTransferRate The new cable transfer rate
         */
        public void setCableTransferRate(int cableTransferRate) {
            this.cableTransferRate = cableTransferRate;
        }
        
        /**
         * Check if path caching is enabled.
         * 
         * @return true if path caching is enabled, false otherwise
         */
        public boolean isUsePathCaching() {
            return usePathCaching;
        }
        
        /**
         * Set whether to use path caching.
         * 
         * @param usePathCaching Whether to use path caching
         */
        public void setUsePathCaching(boolean usePathCaching) {
            this.usePathCaching = usePathCaching;
        }
        
        /**
         * Check if optimization for unloaded chunks is enabled.
         * 
         * @return true if optimization is enabled, false otherwise
         */
        public boolean isOptimizeForUnloadedChunks() {
            return optimizeForUnloadedChunks;
        }
        
        /**
         * Set whether to optimize for unloaded chunks.
         * 
         * @param optimizeForUnloadedChunks Whether to optimize for unloaded chunks
         */
        public void setOptimizeForUnloadedChunks(boolean optimizeForUnloadedChunks) {
            this.optimizeForUnloadedChunks = optimizeForUnloadedChunks;
        }
        
        /**
         * Get the path cache expiry time in seconds.
         * 
         * @return The path cache expiry time
         */
        public int getPathCacheExpirySeconds() {
            return pathCacheExpirySeconds;
        }
        
        /**
         * Set the path cache expiry time in seconds.
         * 
         * @param pathCacheExpirySeconds The new path cache expiry time
         */
        public void setPathCacheExpirySeconds(int pathCacheExpirySeconds) {
            this.pathCacheExpirySeconds = pathCacheExpirySeconds;
        }
    }
    
    /**
     * Settings for energy storage.
     */
    public static class StorageSettings {
        // Default capacities for different tiers
        private int basicCapacity = 10000;
        private int advancedCapacity = 50000;
        private int eliteCapacity = 250000;
        private int ultimateCapacity = 1000000;
        
        // Default transfer rates for different tiers
        private int basicTransferRate = 100;
        private int advancedTransferRate = 500;
        private int eliteTransferRate = 2500;
        private int ultimateTransferRate = 10000;
        
        // Explosion risk when overcharged (set to 0 to disable)
        private double explosionRisk = 0.1;
        
        /**
         * Get the basic capacity.
         * 
         * @return The basic capacity
         */
        public int getBasicCapacity() {
            return basicCapacity;
        }
        
        /**
         * Set the basic capacity.
         * 
         * @param basicCapacity The new basic capacity
         */
        public void setBasicCapacity(int basicCapacity) {
            this.basicCapacity = basicCapacity;
        }
        
        /**
         * Get the advanced capacity.
         * 
         * @return The advanced capacity
         */
        public int getAdvancedCapacity() {
            return advancedCapacity;
        }
        
        /**
         * Set the advanced capacity.
         * 
         * @param advancedCapacity The new advanced capacity
         */
        public void setAdvancedCapacity(int advancedCapacity) {
            this.advancedCapacity = advancedCapacity;
        }
        
        /**
         * Get the elite capacity.
         * 
         * @return The elite capacity
         */
        public int getEliteCapacity() {
            return eliteCapacity;
        }
        
        /**
         * Set the elite capacity.
         * 
         * @param eliteCapacity The new elite capacity
         */
        public void setEliteCapacity(int eliteCapacity) {
            this.eliteCapacity = eliteCapacity;
        }
        
        /**
         * Get the ultimate capacity.
         * 
         * @return The ultimate capacity
         */
        public int getUltimateCapacity() {
            return ultimateCapacity;
        }
        
        /**
         * Set the ultimate capacity.
         * 
         * @param ultimateCapacity The new ultimate capacity
         */
        public void setUltimateCapacity(int ultimateCapacity) {
            this.ultimateCapacity = ultimateCapacity;
        }
        
        /**
         * Get the basic transfer rate.
         * 
         * @return The basic transfer rate
         */
        public int getBasicTransferRate() {
            return basicTransferRate;
        }
        
        /**
         * Set the basic transfer rate.
         * 
         * @param basicTransferRate The new basic transfer rate
         */
        public void setBasicTransferRate(int basicTransferRate) {
            this.basicTransferRate = basicTransferRate;
        }
        
        /**
         * Get the advanced transfer rate.
         * 
         * @return The advanced transfer rate
         */
        public int getAdvancedTransferRate() {
            return advancedTransferRate;
        }
        
        /**
         * Set the advanced transfer rate.
         * 
         * @param advancedTransferRate The new advanced transfer rate
         */
        public void setAdvancedTransferRate(int advancedTransferRate) {
            this.advancedTransferRate = advancedTransferRate;
        }
        
        /**
         * Get the elite transfer rate.
         * 
         * @return The elite transfer rate
         */
        public int getEliteTransferRate() {
            return eliteTransferRate;
        }
        
        /**
         * Set the elite transfer rate.
         * 
         * @param eliteTransferRate The new elite transfer rate
         */
        public void setEliteTransferRate(int eliteTransferRate) {
            this.eliteTransferRate = eliteTransferRate;
        }
        
        /**
         * Get the ultimate transfer rate.
         * 
         * @return The ultimate transfer rate
         */
        public int getUltimateTransferRate() {
            return ultimateTransferRate;
        }
        
        /**
         * Set the ultimate transfer rate.
         * 
         * @param ultimateTransferRate The new ultimate transfer rate
         */
        public void setUltimateTransferRate(int ultimateTransferRate) {
            this.ultimateTransferRate = ultimateTransferRate;
        }
        
        /**
         * Get the explosion risk when overcharged.
         * 
         * @return The explosion risk
         */
        public double getExplosionRisk() {
            return explosionRisk;
        }
        
        /**
         * Set the explosion risk when overcharged.
         * 
         * @param explosionRisk The new explosion risk
         */
        public void setExplosionRisk(double explosionRisk) {
            this.explosionRisk = explosionRisk;
        }
    }
    
    /**
     * Settings for energy display and unit conversion.
     */
    public static class DisplaySettings {
        // Display unit (FE, J, RF, etc.)
        private String displayUnit = "FE";
        
        // Conversion factors from internal units to display units
        private double conversionFactor = 1.0;
        
        // Display format for energy values
        private String displayFormat = "%.1f %s";
        
        // Show tooltips with energy information
        private boolean showTooltips = true;
        
        // Show energy bar on machine GUIs
        private boolean showEnergyBar = true;
        
        /**
         * Get the display unit.
         * 
         * @return The display unit
         */
        public String getDisplayUnit() {
            return displayUnit;
        }
        
        /**
         * Set the display unit.
         * 
         * @param displayUnit The new display unit
         */
        public void setDisplayUnit(String displayUnit) {
            this.displayUnit = displayUnit;
        }
        
        /**
         * Get the conversion factor.
         * 
         * @return The conversion factor
         */
        public double getConversionFactor() {
            return conversionFactor;
        }
        
        /**
         * Set the conversion factor.
         * 
         * @param conversionFactor The new conversion factor
         */
        public void setConversionFactor(double conversionFactor) {
            this.conversionFactor = conversionFactor;
        }
        
        /**
         * Get the display format.
         * 
         * @return The display format
         */
        public String getDisplayFormat() {
            return displayFormat;
        }
        
        /**
         * Set the display format.
         * 
         * @param displayFormat The new display format
         */
        public void setDisplayFormat(String displayFormat) {
            this.displayFormat = displayFormat;
        }
        
        /**
         * Check if tooltips should be shown.
         * 
         * @return true if tooltips should be shown, false otherwise
         */
        public boolean isShowTooltips() {
            return showTooltips;
        }
        
        /**
         * Set whether tooltips should be shown.
         * 
         * @param showTooltips Whether tooltips should be shown
         */
        public void setShowTooltips(boolean showTooltips) {
            this.showTooltips = showTooltips;
        }
        
        /**
         * Check if the energy bar should be shown on machine GUIs.
         * 
         * @return true if the energy bar should be shown, false otherwise
         */
        public boolean isShowEnergyBar() {
            return showEnergyBar;
        }
        
        /**
         * Set whether the energy bar should be shown on machine GUIs.
         * 
         * @param showEnergyBar Whether the energy bar should be shown
         */
        public void setShowEnergyBar(boolean showEnergyBar) {
            this.showEnergyBar = showEnergyBar;
        }
    }
}