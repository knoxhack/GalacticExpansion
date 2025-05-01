package com.astroframe.galactic.core.api.energy;

/**
 * Interface for objects that wrap an IEnergyHandler.
 * This is useful for adapting between different energy systems or for providing
 * a specific view of an energy handler.
 */
public interface IEnergyHandlerWrapper extends IEnergyHandler {
    
    /**
     * Gets the underlying energy handler that this wrapper is wrapping.
     * 
     * @return The wrapped energy handler
     */
    IEnergyHandler getWrappedHandler();
    
    /**
     * Helper method to safely unwrap nested energy handler wrappers to find
     * a specific type of energy handler.
     * 
     * @param <T> The type of energy handler to unwrap to
     * @param wrapper The wrapper to unwrap
     * @param handlerClass The class of the handler to find
     * @return The unwrapped handler, or null if not found
     */
    static <T extends IEnergyHandler> T unwrapTo(IEnergyHandler wrapper, Class<T> handlerClass) {
        if (handlerClass.isInstance(wrapper)) {
            return handlerClass.cast(wrapper);
        }
        
        // Recursively unwrap until we find the requested type
        IEnergyHandler current = wrapper;
        while (current instanceof IEnergyHandlerWrapper handlerWrapper) {
            current = handlerWrapper.getWrappedHandler();
            if (handlerClass.isInstance(current)) {
                return handlerClass.cast(current);
            }
        }
        
        return null;
    }
}