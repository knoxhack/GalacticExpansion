package com.example.modapi.core.api;

/**
 * Interface representing a module in the mod API.
 * Modules are separate components that can be registered with the core mod.
 */
public interface Module {

    /**
     * Gets the unique ID of this module.
     * 
     * @return The module ID
     */
    String getModuleId();
    
    /**
     * Gets the display name of this module.
     * 
     * @return The module display name
     */
    String getModuleName();
    
    /**
     * Gets the module version.
     * 
     * @return The module version
     */
    String getModuleVersion();
    
    /**
     * Called to register this module's content with the registry.
     * 
     * @param registry The mod registry
     */
    void registerContent(ModRegistry registry);
    
    /**
     * Gets the dependency modules required by this module.
     * 
     * @return An array of module IDs this module depends on
     */
    default String[] getDependencies() {
        return new String[0];
    }
    
    /**
     * Called during the initialization phase.
     * Good place to set up event handlers or other initialization tasks.
     */
    default void initialize() {
        // Default implementation does nothing
    }
    
    /**
     * Called during the post-initialization phase.
     * Good place to interact with other mods after they've initialized.
     */
    default void postInitialize() {
        // Default implementation does nothing
    }
}
