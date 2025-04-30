package com.astroframe.galactic.core.api;

import com.astroframe.galactic.core.registry.annotation.RegistryScanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Abstract implementation of ModuleIntegration that provides common functionality.
 * Modules can extend this class to easily integrate with the Galactic Expansion Core.
 */
public abstract class AbstractModuleIntegration implements ModuleIntegration {
    
    private final String modId;
    private final String moduleName;
    private final Logger logger;
    private final RegistryScanner registryScanner;
    
    /**
     * Create a new module integration.
     * 
     * @param modId The mod ID of the module
     * @param moduleName The name of the module
     */
    protected AbstractModuleIntegration(String modId, String moduleName) {
        this.modId = modId;
        this.moduleName = moduleName;
        this.logger = LogManager.getLogger(moduleName);
        this.registryScanner = new RegistryScanner(modId);
        
        // Map type to registry associations specific to this module
        configureRegistryMappings(registryScanner);
        
        logger.info("Initializing {} module integration", moduleName);
    }
    
    /**
     * Configure type to registry mappings for the registry scanner.
     * Subclasses should override this method to add their own mappings.
     * 
     * @param scanner The registry scanner to configure
     */
    protected void configureRegistryMappings(RegistryScanner scanner) {
        // Default implementation does nothing
        // Subclasses should override to add their own mappings
    }
    
    @Override
    public String getModId() {
        return modId;
    }
    
    @Override
    public String getModuleName() {
        return moduleName;
    }
    
    @Override
    public RegistryScanner createRegistryScanner() {
        return registryScanner;
    }
    
    /**
     * Get the logger for this module.
     * 
     * @return The module's logger
     */
    public Logger getLogger() {
        return logger;
    }
    
    /**
     * Log a message at the info level.
     * 
     * @param message The message to log
     * @param params The message parameters
     */
    public void info(String message, Object... params) {
        logger.info(message, params);
    }
    
    /**
     * Log a message at the debug level.
     * 
     * @param message The message to log
     * @param params The message parameters
     */
    public void debug(String message, Object... params) {
        logger.debug(message, params);
    }
    
    /**
     * Log a message at the warn level.
     * 
     * @param message The message to log
     * @param params The message parameters
     */
    public void warn(String message, Object... params) {
        logger.warn(message, params);
    }
    
    /**
     * Log a message at the error level.
     * 
     * @param message The message to log
     * @param params The message parameters
     */
    public void error(String message, Object... params) {
        logger.error(message, params);
    }
}