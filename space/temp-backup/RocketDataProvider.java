package com.astroframe.galactic.space.implementation.common;

import com.astroframe.galactic.core.api.space.IRocket;

/**
 * Interface for entities that can provide rocket data.
 * This is used to decouple implementations and avoid circular dependencies.
 */
public interface RocketDataProvider {
    /**
     * Gets the rocket data to display.
     *
     * @return The rocket data
     */
    IRocket getRocketData();
}