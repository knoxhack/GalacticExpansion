package com.astroframe.galactic.space.implementation.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * Helper class for vertex rendering compatibility across Minecraft versions.
 * Completely rewritten for NeoForge 1.21.5
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
     * This is a compatibility layer for NeoForge 1.21.5
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
        // Transform the vertex by the matrix
        Vector4f pos = new Vector4f(x, y, z, 1.0F);
        pos.mul(matrix);
        
        // Use a simpler approach to add vertices for NeoForge 1.21.5
        // Just add position, color, normal, and UV coordinates separately
        consumer.vertex(matrix, x, y, z);
        consumer.color(red, green, blue, alpha);
        consumer.normal(0.0F, 1.0F, 0.0F);
        consumer.uv(0.0F, 0.0F);
        consumer.overlayCoords(OverlayTexture.NO_OVERLAY);
        consumer.uv2(15728880); // Full brightness
        consumer.endVertex();
    }
    
    /**
     * Draw a colored line between two points
     * 
     * @param builder The vertex consumer
     * @param matrix The transform matrix
     * @param x1 First point X
     * @param y1 First point Y 
     * @param z1 First point Z
     * @param x2 Second point X
     * @param y2 Second point Y
     * @param z2 Second point Z
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void drawLine(VertexConsumer builder, Matrix4f matrix, 
                              float x1, float y1, float z1, 
                              float x2, float y2, float z2,
                              float red, float green, float blue, float alpha) {
        // Draw a line from point 1 to point 2
        addColoredVertex(builder, matrix, x1, y1, z1, red, green, blue, alpha);
        addColoredVertex(builder, matrix, x2, y2, z2, red, green, blue, alpha);
    }
    
    /**
     * Draw a colored rectangle at the specified position
     * 
     * @param consumer The vertex consumer
     * @param matrix The transform matrix
     * @param x The x position
     * @param y The y position
     * @param z The z position
     * @param width The width
     * @param height The height
     * @param red The red component (0.0-1.0)
     * @param green The green component (0.0-1.0)
     * @param blue The blue component (0.0-1.0)
     * @param alpha The alpha component (0.0-1.0)
     */
    public static void drawRect(VertexConsumer consumer, Matrix4f matrix, 
                             float x, float y, float z, float width, float height,
                             float red, float green, float blue, float alpha) {
        // Draw a rectangle from the bottom left
        addColoredVertex(consumer, matrix, x, y, z, red, green, blue, alpha);
        addColoredVertex(consumer, matrix, x + width, y, z, red, green, blue, alpha);
        addColoredVertex(consumer, matrix, x + width, y + height, z, red, green, blue, alpha);
        addColoredVertex(consumer, matrix, x, y + height, z, red, green, blue, alpha);
    }
}