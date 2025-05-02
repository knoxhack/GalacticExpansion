package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;

/**
 * Renderer for the Holographic Projector block entity.
 * Displays the holographic projection of a rocket based on components.
 */
public class HolographicProjectorRenderer implements BlockEntityRenderer<HolographicProjectorBlockEntity> {
    
    // Hologram colors
    private static final float HOLOGRAM_RED = 0.0F;
    private static final float HOLOGRAM_GREEN = 0.8F;
    private static final float HOLOGRAM_BLUE = 1.0F;
    private static final float HOLOGRAM_ALPHA = 0.7F;
    
    // Texture for lines
    private static final ResourceLocation HOLOGRAM_TEXTURE = 
            new ResourceLocation("galactic", "textures/hologram/hologram.png");
    
    /**
     * Constructor for the renderer.
     *
     * @param context The renderer provider context
     */
    public HolographicProjectorRenderer(BlockEntityRendererProvider.Context context) {
        // No initialization needed
    }
    
    /**
     * Renders the holographic projection.
     *
     * @param blockEntity The holographic projector block entity
     * @param partialTick The partial tick time
     * @param poseStack The pose stack
     * @param bufferSource The buffer source
     * @param packedLight The packed light
     * @param packedOverlay The packed overlay
     */
    @Override
    public void render(HolographicProjectorBlockEntity blockEntity, float partialTick, PoseStack poseStack, 
                     MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        // Only render if the projector is active
        if (!blockEntity.isActive()) {
            return;
        }
        
        // Get the rocket data
        IRocket rocketData = blockEntity.getRocketData();
        
        // Get the rotation angle with smooth interpolation for animation
        float angle = blockEntity.getRotationAngle() + partialTick * 0.5F;
        
        // Setup the rendering pose
        poseStack.pushPose();
        
        // Position the hologram above the projector
        poseStack.translate(0.5D, 1.0D, 0.5D);
        
        // Apply the rotation for the spinning effect
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
        
        // Get a line renderer
        VertexConsumer lines = bufferSource.getBuffer(RenderType.lines());
        
        // Render base platform
        renderHologramBase(poseStack, lines);
        
        // If we have rocket data, render the rocket components
        if (rocketData != null) {
            renderRocketComponents(rocketData, poseStack, lines);
        } else {
            // Render placeholder if no rocket data available
            renderPlaceholderRocket(poseStack, lines);
        }
        
        // Add scan lines
        float time = (Minecraft.getInstance().level.getGameTime() % 80) / 80.0F;
        float scanHeight = Mth.sin(time * (float) Math.PI * 2) * 1.5F;
        renderScanLines(poseStack, lines, scanHeight);
        
        poseStack.popPose();
    }
    
    /**
     * Renders the base platform of the hologram.
     *
     * @param poseStack The pose stack
     * @param lines The line renderer
     */
    private void renderHologramBase(PoseStack poseStack, VertexConsumer lines) {
        poseStack.pushPose();
        
        // Move to base position
        poseStack.translate(0, -0.4, 0);
        poseStack.scale(1.0F, 0.02F, 1.0F);
        
        // Create a circular platform
        renderCircle(poseStack, lines, 0.5f, 16);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a circle in the XZ plane.
     *
     * @param poseStack The pose stack
     * @param lines The line renderer
     * @param radius The radius of the circle
     * @param segments The number of segments to use
     */
    private void renderCircle(PoseStack poseStack, VertexConsumer lines, float radius, int segments) {
        Matrix4f pose = poseStack.last().pose();
        
        // Draw a circle with lines
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * ((i + 1) % segments) / segments);
            
            float x1 = (float) (radius * Math.cos(angle1));
            float z1 = (float) (radius * Math.sin(angle1));
            
            float x2 = (float) (radius * Math.cos(angle2));
            float z2 = (float) (radius * Math.sin(angle2));
            
            // Draw line for the circle
            drawLine(pose, lines, x1, 0.0f, z1, x2, 0.0f, z2);
            
            // Draw line from center
            Vector3f normal = new Vector3f(0, 1, 0);
            
            Vector4f center = new Vector4f(0, 0.0f, 0, 1.0f);
            Vector4f edge = new Vector4f(x1, 0.0f, z1, 1.0f);
            
            center.mul(pose);
            edge.mul(pose);
            
            lines.vertex(center.x(), center.y(), center.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
                 
            lines.vertex(edge.x(), edge.y(), edge.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, 0.3f * HOLOGRAM_ALPHA)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
        }
    }
    
    /**
     * Renders the rocket components from the rocket data.
     *
     * @param rocket The rocket data
     * @param poseStack The pose stack
     * @param lines The line renderer
     */
    private void renderRocketComponents(IRocket rocket, PoseStack poseStack, VertexConsumer lines) {
        if (rocket == null) return;
        
        poseStack.pushPose();
        
        // Adjust position for rocket components
        poseStack.translate(0, 0.5, 0);
        
        // Get all components from the rocket
        List<IRocketComponent> components = rocket.getAllComponents();
        
        // Render each component
        for (IRocketComponent component : components) {
            renderRocketComponent(component, poseStack, lines);
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders a single rocket component.
     *
     * @param component The rocket component
     * @param poseStack The pose stack
     * @param lines The line renderer
     */
    private void renderRocketComponent(IRocketComponent component, PoseStack poseStack, VertexConsumer lines) {
        if (component == null) return;
        
        poseStack.pushPose();
        
        // Get component position and size
        Vec3 position = component.getPosition();
        Vec3 size = component.getSize();
        
        // Translate to component position
        poseStack.translate(position.x, position.y, position.z);
        
        // Scale based on component size
        poseStack.scale((float)size.x, (float)size.y, (float)size.z);
        
        // Render a box for the component
        renderBox(poseStack, lines);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a box centered at the origin with size 1x1x1.
     *
     * @param poseStack The pose stack
     * @param lines The line renderer
     */
    private void renderBox(PoseStack poseStack, VertexConsumer lines) {
        Matrix4f pose = poseStack.last().pose();
        
        // Draw 12 edges of a 1x1x1 cube centered at the origin
        // Bottom face
        drawLine(pose, lines, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f);
        drawLine(pose, lines, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f);
        drawLine(pose, lines, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f);
        drawLine(pose, lines, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f);
        
        // Top face
        drawLine(pose, lines, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f);
        drawLine(pose, lines, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f);
        drawLine(pose, lines, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f);
        drawLine(pose, lines, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f);
        
        // Connecting edges
        drawLine(pose, lines, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f);
        drawLine(pose, lines, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f);
        drawLine(pose, lines, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f);
        drawLine(pose, lines, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f);
    }
    
    /**
     * Draws a line between two points.
     *
     * @param pose The matrix pose
     * @param lines The line renderer
     * @param x1 Start X
     * @param y1 Start Y
     * @param z1 Start Z
     * @param x2 End X
     * @param y2 End Y
     * @param z2 End Z
     */
    private void drawLine(Matrix4f pose, VertexConsumer lines, float x1, float y1, float z1, float x2, float y2, float z2) {
        Vector3f normal = new Vector3f(0, 1, 0);
        
        Vector4f pos1 = new Vector4f(x1, y1, z1, 1.0f);
        Vector4f pos2 = new Vector4f(x2, y2, z2, 1.0f);
        
        pos1.mul(pose);
        pos2.mul(pose);
        
        lines.vertex(pos1.x(), pos1.y(), pos1.z())
             .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
             .normal(normal.x(), normal.y(), normal.z())
             .endVertex();
             
        lines.vertex(pos2.x(), pos2.y(), pos2.z())
             .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
             .normal(normal.x(), normal.y(), normal.z())
             .endVertex();
    }
    
    /**
     * Renders a placeholder rocket when no rocket data is available.
     *
     * @param poseStack The pose stack
     * @param lines The line renderer
     */
    private void renderPlaceholderRocket(PoseStack poseStack, VertexConsumer lines) {
        poseStack.pushPose();
        
        // Adjust position for placeholder
        poseStack.translate(0, 0.5, 0);
        
        // Body
        poseStack.pushPose();
        poseStack.scale(0.3F, 1.0F, 0.3F);
        renderBox(poseStack, lines);
        poseStack.popPose();
        
        // Nose cone
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        poseStack.scale(0.2F, 0.3F, 0.2F);
        renderBox(poseStack, lines);
        poseStack.popPose();
        
        // Fins
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0, -0.4, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
            poseStack.translate(0.2, 0, 0);
            poseStack.scale(0.1F, 0.3F, 0.2F);
            renderBox(poseStack, lines);
            poseStack.popPose();
        }
        
        poseStack.popPose();
    }
    
    /**
     * Renders holographic scan lines for added effect.
     *
     * @param poseStack The pose stack
     * @param lines The line renderer
     * @param scanHeight The height of the scan effect
     */
    private void renderScanLines(PoseStack poseStack, VertexConsumer lines, float scanHeight) {
        poseStack.pushPose();
        
        // Position the scan plane
        poseStack.translate(0, scanHeight, 0);
        
        // Render a grid of lines
        Matrix4f pose = poseStack.last().pose();
        float size = 1.0F;
        int gridSize = 4;
        float step = size * 2 / gridSize;
        
        // Render the grid with lower alpha
        float scanAlpha = 0.3F * HOLOGRAM_ALPHA;
        
        // Horizontal lines
        for (int i = 0; i <= gridSize; i++) {
            float z = -size + i * step;
            Vector3f normal = new Vector3f(0, 1, 0);
            
            Vector4f start = new Vector4f(-size, 0.0f, z, 1.0f);
            Vector4f end = new Vector4f(size, 0.0f, z, 1.0f);
            
            start.mul(pose);
            end.mul(pose);
            
            lines.vertex(start.x(), start.y(), start.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
                 
            lines.vertex(end.x(), end.y(), end.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
        }
        
        // Vertical lines
        for (int i = 0; i <= gridSize; i++) {
            float x = -size + i * step;
            Vector3f normal = new Vector3f(0, 1, 0);
            
            Vector4f start = new Vector4f(x, 0.0f, -size, 1.0f);
            Vector4f end = new Vector4f(x, 0.0f, size, 1.0f);
            
            start.mul(pose);
            end.mul(pose);
            
            lines.vertex(start.x(), start.y(), start.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
                 
            lines.vertex(end.x(), end.y(), end.z())
                 .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                 .normal(normal.x(), normal.y(), normal.z())
                 .endVertex();
        }
        
        poseStack.popPose();
    }
}