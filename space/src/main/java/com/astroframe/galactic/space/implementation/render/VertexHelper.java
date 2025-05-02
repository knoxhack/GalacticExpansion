package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Matrix4f;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;

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
        // In NeoForge 1.21.5, we need to use a different approach to add vertices
        // Create a buffer builder and add the vertex data manually
        if (consumer instanceof BufferBuilder builder) {
            // Add position transformed by matrix
            float transformedX = matrix.m00() * x + matrix.m01() * y + matrix.m02() * z + matrix.m03();
            float transformedY = matrix.m10() * x + matrix.m11() * y + matrix.m12() * z + matrix.m13();
            float transformedZ = matrix.m20() * x + matrix.m21() * y + matrix.m22() * z + matrix.m23();
            
            builder.vertex(transformedX, transformedY, transformedZ);
            builder.color(red, green, blue, alpha);
            builder.normal(0.0F, 1.0F, 0.0F);
            builder.uv(0.0F, 0.0F);
            builder.uv2(15728880); // Full brightness
            builder.endVertex();
        } else {
            // Fallback for non-BufferBuilder consumers
            consumer.vertex(x, y, z);
            consumer.color(red, green, blue, alpha);
            consumer.endVertex();
        }
    }
}