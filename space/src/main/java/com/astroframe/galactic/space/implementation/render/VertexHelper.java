package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector4f;

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
        // Correctly use the VertexConsumer API for NeoForge 1.21.5
        
        // Start adding a new vertex
        consumer.vertex(x, y, z);
        // Set the color of the vertex
        consumer.color(red, green, blue, alpha);
        // Set the normal vector of the vertex
        consumer.normal(0.0F, 1.0F, 0.0F);
        // Set the UV texture coordinates
        consumer.uv(0.0F, 0.0F);
        // Set the overlay (used for entity rendering effects)
        consumer.overlayCoords(0, 0);
        // Set the light map coordinates (used for dynamic lighting)
        consumer.uv2(0, 0);
        // Finish the vertex
        consumer.endVertex();
    }
    
    /**
     * Creates a vertex with position, color, and using a transform matrix.
     * 
     * @param consumer The vertex consumer
     * @param matrix The transform matrix
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void addColoredVertex(VertexConsumer consumer, Matrix4f matrix, float x, float y, float z, 
                                      float red, float green, float blue, float alpha) {
        // Transform the vertex position using the matrix
        Vector4f pos = new Vector4f(x, y, z, 1.0F);
        pos.mul(matrix);
        
        // Add the transformed vertex
        consumer.vertex(pos.x(), pos.y(), pos.z());
        consumer.color(red, green, blue, alpha);
        consumer.normal(0.0F, 1.0F, 0.0F);
        consumer.uv(0.0F, 0.0F);
        consumer.overlayCoords(0, 0);
        consumer.uv2(0, 0);
        consumer.endVertex();
    }
}