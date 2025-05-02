package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
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
        // Create a simple identity matrix for position transformation
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        
        // Use the builder pattern methods for vertex specification
        // These are available in all implementations of VertexConsumer
        consumer.vertex(x, y, z);
        consumer.color(red, green, blue, alpha);
        consumer.normal(0.0F, 1.0F, 0.0F);
        consumer.uv(0.0F, 0.0F);  // Add UV coordinates which are required
        consumer.uv2(0, 0);       // Add overlay/lightmap UV
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
        float[] pos = new float[4];
        pos[0] = x;
        pos[1] = y;
        pos[2] = z;
        pos[3] = 1.0F;
        
        matrix.transform(pos);
        
        // Add the transformed vertex
        consumer.vertex(pos[0], pos[1], pos[2]);
        consumer.color(red, green, blue, alpha);
        consumer.normal(0.0F, 1.0F, 0.0F);
        consumer.uv(0.0F, 0.0F);
        consumer.uv2(0, 0); 
        consumer.endVertex();
    }
}