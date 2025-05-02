package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 * Updated for NeoForge 1.21.5
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
        // Implementation for NeoForge 1.21.5
        // Need to use a matrix for position
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        consumer.vertex(matrix, x, y, z);
        consumer.color(red, green, blue, alpha);
        consumer.normal(0, 1, 0);
        consumer.endVertex();
    }
}