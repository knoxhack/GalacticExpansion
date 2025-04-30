package com.astroframe.galactic.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.neoforged.fml.loading.FMLPaths;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Manages configuration for the Galactic Expansion mod and its modules.
 * This centralized configuration system handles loading, saving, and accessing
 * configuration data for all modules in a unified way.
 */
public class ConfigManager {

    private static final Logger LOGGER = LogManager.getLogger(ConfigManager.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Map<String, Object> CONFIGS = new HashMap<>();
    private static final Map<String, Supplier<Object>> DEFAULT_CONFIGS = new HashMap<>();
    
    /**
     * Registers a configuration class with the manager.
     * 
     * @param <T> The type of configuration class
     * @param modId The ID of the mod or module this config belongs to
     * @param configClass The class of the configuration
     * @param defaultSupplier A supplier that provides the default configuration
     */
    public static <T> void register(String modId, Class<T> configClass, Supplier<T> defaultSupplier) {
        DEFAULT_CONFIGS.put(modId, defaultSupplier::get);
        load(modId, configClass);
    }
    
    /**
     * Loads a configuration from disk.
     * 
     * @param <T> The type of configuration class
     * @param modId The ID of the mod or module
     * @param configClass The class of the configuration
     */
    public static <T> void load(String modId, Class<T> configClass) {
        Path configDir = FMLPaths.CONFIGDIR.get();
        Path configFile = configDir.resolve(modId + ".json");
        
        // Create default config if it doesn't exist
        if (!Files.exists(configFile)) {
            T defaultConfig = configClass.cast(DEFAULT_CONFIGS.get(modId).get());
            CONFIGS.put(modId, defaultConfig);
            save(modId);
            return;
        }
        
        // Load the config
        try (BufferedReader reader = Files.newBufferedReader(configFile)) {
            T config = GSON.fromJson(reader, configClass);
            CONFIGS.put(modId, config);
        } catch (IOException e) {
            LOGGER.error("Failed to load config for " + modId, e);
            resetToDefault(modId);
        } catch (JsonSyntaxException e) {
            LOGGER.error("Invalid config syntax for " + modId + ", resetting to default", e);
            resetToDefault(modId);
        }
    }
    
    /**
     * Saves a configuration to disk.
     * 
     * @param modId The ID of the mod or module
     */
    public static void save(String modId) {
        if (!CONFIGS.containsKey(modId)) {
            LOGGER.warn("Attempted to save non-existent config for " + modId);
            return;
        }
        
        Path configDir = FMLPaths.CONFIGDIR.get();
        Path configFile = configDir.resolve(modId + ".json");
        
        try {
            if (!Files.exists(configDir)) {
                Files.createDirectories(configDir);
            }
            
            try (BufferedWriter writer = Files.newBufferedWriter(configFile)) {
                GSON.toJson(CONFIGS.get(modId), writer);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save config for " + modId, e);
        }
    }
    
    /**
     * Gets a configuration.
     * 
     * @param <T> The type of configuration class
     * @param modId The ID of the mod or module
     * @param configClass The class of the configuration
     * @return The configuration, or null if it doesn't exist
     */
    public static <T> T get(String modId, Class<T> configClass) {
        if (!CONFIGS.containsKey(modId)) {
            return null;
        }
        
        return configClass.cast(CONFIGS.get(modId));
    }
    
    /**
     * Resets a configuration to its default values.
     * 
     * @param modId The ID of the mod or module
     */
    public static void resetToDefault(String modId) {
        if (!DEFAULT_CONFIGS.containsKey(modId)) {
            LOGGER.warn("Attempted to reset non-existent config for " + modId);
            return;
        }
        
        CONFIGS.put(modId, DEFAULT_CONFIGS.get(modId).get());
        save(modId);
    }
    
    /**
     * Reloads all configurations from disk.
     */
    public static void reloadAll() {
        for (String modId : DEFAULT_CONFIGS.keySet()) {
            Object config = CONFIGS.get(modId);
            if (config != null) {
                load(modId, config.getClass());
            }
        }
    }
}