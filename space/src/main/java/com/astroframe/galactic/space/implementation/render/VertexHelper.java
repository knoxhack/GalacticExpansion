package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.VertexConsumer;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 */
public class VertexHelper {
    
    /**
     * Adds a vertex to the vertex consumer with the given position and color.
     * This handles API differences between Minecraft versions.
     * 
     * @param consumer The vertex consumer
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer, float x, float y, float z, 
                                       float red, float green, float blue, float alpha) {
        // In NeoForge 1.21.5, use individual methods for setting vertex data
        consumer.addVertex(x, y, z, red, green, blue, alpha, 0, 0, 0, 0, 1, 0, 0);
    }
}