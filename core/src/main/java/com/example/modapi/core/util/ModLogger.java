package com.example.modapi.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for logging messages to the console.
 * Wraps the SLF4J logger with convenience methods.
 */
public class ModLogger {
    private final Logger logger;
    
    /**
     * Constructor for ModLogger.
     * 
     * @param modId The mod ID to use in log messages
     */
    public ModLogger(String modId) {
        this.logger = LoggerFactory.getLogger(modId);
    }
    
    /**
     * Logs an info message.
     * 
     * @param message The message to log
     */
    public void info(String message) {
        logger.info(message);
    }
    
    /**
     * Logs a warning message.
     * 
     * @param message The message to log
     */
    public void warn(String message) {
        logger.warn(message);
    }
    
    /**
     * Logs an error message.
     * 
     * @param message The message to log
     */
    public void error(String message) {
        logger.error(message);
    }
    
    /**
     * Logs an error message with an exception.
     * 
     * @param message The message to log
     * @param throwable The exception to log
     */
    public void error(String message, Throwable throwable) {
        logger.error(message, throwable);
    }
    
    /**
     * Logs a debug message.
     * 
     * @param message The message to log
     */
    public void debug(String message) {
        logger.debug(message);
    }
    
    /**
     * Gets the SLF4J logger.
     * 
     * @return The logger
     */
    public Logger getLogger() {
        return logger;
    }
}
