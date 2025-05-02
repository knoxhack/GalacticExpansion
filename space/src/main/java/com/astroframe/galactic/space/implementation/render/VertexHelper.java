package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
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
        // Create a default matrix
        Matrix4f matrix = new Matrix4f().identity();
        
        // Forward to the matrix-based method
        addColoredVertex(consumer, matrix, x, y, z, red, green, blue, alpha);
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
        // Basic vertex data
        // NeoForge 1.21.5 compatible approach
        // Some vertex consumers don't support method chaining, so call each method separately
        consumer.vertex(matrix, x, y, z);
        consumer.color(red, green, blue, alpha);
        consumer.normal(0.0F, 1.0F, 0.0F);
        consumer.uv(0.0F, 0.0F);
        consumer.overlayCoords(0, 0);
        consumer.uv2(15728880); // Full brightness
        consumer.endVertex();
    }
}