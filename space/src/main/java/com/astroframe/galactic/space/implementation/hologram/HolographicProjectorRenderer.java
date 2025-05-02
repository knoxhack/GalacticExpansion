package com.astroframe.galactic.space.implementation.hologram;

import com.astroframe.galactic.core.api.space.IRocket;
import com.astroframe.galactic.core.api.space.component.IRocketComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import org.joml.Matrix4f;

/**
 * Renderer for the Holographic Projector block entity.
 * Displays the holographic projection of a rocket based on components.
 */
public class HolographicProjectorRenderer implements BlockEntityRenderer<HolographicProjectorBlockEntity> {
    
    // Base location for hologram textures
    private static final ResourceLocation HOLOGRAM_BASE_TEXTURE = 
            new ResourceLocation("galactic", "textures/hologram/base.png");
    
    // Hologram colors
    private static final float HOLOGRAM_RED = 0.0F;
    private static final float HOLOGRAM_GREEN = 0.8F;
    private static final float HOLOGRAM_BLUE = 1.0F;
    private static final float HOLOGRAM_ALPHA = 0.7F;
    
    /**
     * Constructor for the renderer.
     *
     * @param context The renderer provider context
     */
    public HolographicProjectorRenderer(BlockEntityRendererProvider.Context context) {
        // Renderer setup
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
        
        // Apply scaling to fit the hologram in the display area
        float scale = 0.5F;
        poseStack.scale(scale, scale, scale);
        
        // Save the matrix state
        Matrix4f pose = poseStack.last().pose();
        
        // Start direct rendering with our own buffer
        Minecraft.getInstance().getTextureManager().bindForSetup(HOLOGRAM_BASE_TEXTURE);
        
        // Draw base platform and parts
        BufferBuilder buffer = Tesselator.getInstance().getBuilder();
        
        // Simple lines for the hologram base
        renderHologramBase(poseStack, buffer, packedLight);
        
        // If we have rocket data, render the rocket components
        if (rocketData != null) {
            renderRocketComponents(rocketData, poseStack, buffer, packedLight);
        } else {
            // Render placeholder if no rocket data available
            renderPlaceholderRocket(poseStack, buffer, packedLight);
        }
        
        // Add holographic scan lines
        renderScanLines(poseStack, buffer, packedLight, angle);
        
        poseStack.popPose();
    }
    
    /**
     * Renders the base platform of the hologram.
     *
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     * @param packedLight The packed light
     */
    private void renderHologramBase(PoseStack poseStack, BufferBuilder buffer, int packedLight) {
        poseStack.pushPose();
        
        // Move to base position
        poseStack.translate(0, -0.4, 0);
        poseStack.scale(1.5F, 0.1F, 1.5F);
        
        // Create a circular platform with lines
        float centerY = 0.0F;
        int segments = 16;
        float radius = 0.5F;
        
        // Render a simple circle of lines at the base
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Draw a circular pattern
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * ((i + 1) % segments) / segments);
            
            float x1 = (float) (radius * Math.cos(angle1));
            float z1 = (float) (radius * Math.sin(angle1));
            
            float x2 = (float) (radius * Math.cos(angle2));
            float z2 = (float) (radius * Math.sin(angle2));
            
            // Outer circle
            buffer.vertex(poseStack.last().pose(), x1, centerY, z1)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            buffer.vertex(poseStack.last().pose(), x2, centerY, z2)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            // Spokes from center
            buffer.vertex(poseStack.last().pose(), 0, centerY, 0)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            buffer.vertex(poseStack.last().pose(), x1, centerY, z1)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, 0.3F * HOLOGRAM_ALPHA)
                  .endVertex();
        }
        
        Tesselator.getInstance().end();
        
        poseStack.popPose();
    }
    
    /**
     * Renders the rocket components from the rocket data.
     *
     * @param rocket The rocket data
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     * @param packedLight The packed light
     */
    private void renderRocketComponents(IRocket rocket, PoseStack poseStack, BufferBuilder buffer, int packedLight) {
        poseStack.pushPose();
        
        // Adjust position for rocket components
        poseStack.translate(0, 0.5, 0);
        
        // Get all components from the rocket
        List<IRocketComponent> components = rocket.getAllComponents();
        
        // Start batch rendering of lines
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Render each component
        for (IRocketComponent component : components) {
            renderRocketComponent(component, poseStack, buffer, packedLight);
        }
        
        Tesselator.getInstance().end();
        
        poseStack.popPose();
    }
    
    /**
     * Renders a single rocket component.
     *
     * @param component The rocket component
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     * @param packedLight The packed light
     */
    private void renderRocketComponent(IRocketComponent component, PoseStack poseStack, BufferBuilder buffer, int packedLight) {
        poseStack.pushPose();
        
        // Get component position in the rocket
        Vec3 position = component.getPosition();
        
        // Translate to component position
        poseStack.translate(position.x, position.y, position.z);
        
        // Scale based on component size
        Vec3 size = component.getSize();
        poseStack.scale((float) size.x, (float) size.y, (float) size.z);
        
        // Render a holographic representation of the component
        // Here we just render a simple box for each component
        renderHolographicBox(poseStack, buffer);
        
        poseStack.popPose();
    }
    
    /**
     * Renders a placeholder rocket when no rocket data is available.
     *
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     * @param packedLight The packed light
     */
    private void renderPlaceholderRocket(PoseStack poseStack, BufferBuilder buffer, int packedLight) {
        poseStack.pushPose();
        
        // Adjust position for placeholder
        poseStack.translate(0, 0.5, 0);
        
        // Start line batch
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Render a basic rocket shape
        // Body
        poseStack.pushPose();
        poseStack.scale(0.3F, 1.0F, 0.3F);
        renderHolographicBox(poseStack, buffer);
        poseStack.popPose();
        
        // Nose cone
        poseStack.pushPose();
        poseStack.translate(0, 0.75, 0);
        poseStack.scale(0.3F, 0.5F, 0.3F);
        renderHolographicCone(poseStack, buffer);
        poseStack.popPose();
        
        // Fins
        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.translate(0, -0.4, 0);
            poseStack.mulPose(Axis.YP.rotationDegrees(90 * i));
            poseStack.translate(0.31, 0, 0);
            poseStack.scale(0.1F, 0.3F, 0.3F);
            renderHolographicBox(poseStack, buffer);
            poseStack.popPose();
        }
        
        Tesselator.getInstance().end();
        
        poseStack.popPose();
    }
    
    /**
     * Renders a holographic box shape.
     *
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     */
    private void renderHolographicBox(PoseStack poseStack, BufferBuilder buffer) {
        Matrix4f pose = poseStack.last().pose();
        
        // Define vertices of a unit cube centered at the origin
        float[][] vertices = {
            // Front face
            {-0.5F, -0.5F, 0.5F}, {0.5F, -0.5F, 0.5F}, {0.5F, 0.5F, 0.5F}, {-0.5F, 0.5F, 0.5F},
            // Back face
            {-0.5F, -0.5F, -0.5F}, {0.5F, -0.5F, -0.5F}, {0.5F, 0.5F, -0.5F}, {-0.5F, 0.5F, -0.5F}
        };
        
        // Define edges of the cube
        int[][] edges = {
            // Bottom face
            {0, 1}, {1, 5}, {5, 4}, {4, 0},
            // Top face
            {3, 2}, {2, 6}, {6, 7}, {7, 3},
            // Connect bottom to top
            {0, 3}, {1, 2}, {5, 6}, {4, 7}
        };
        
        // Draw each edge
        for (int[] edge : edges) {
            buffer.vertex(pose, vertices[edge[0]][0], vertices[edge[0]][1], vertices[edge[0]][2])
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            buffer.vertex(pose, vertices[edge[1]][0], vertices[edge[1]][1], vertices[edge[1]][2])
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
        }
    }
    
    /**
     * Renders a holographic cone shape.
     *
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     */
    private void renderHolographicCone(PoseStack poseStack, BufferBuilder buffer) {
        Matrix4f pose = poseStack.last().pose();
        
        // Number of segments for the cone base
        int segments = 12;
        float radius = 0.5F;
        
        // Apex of the cone
        float apexX = 0.0F;
        float apexY = 0.5F;
        float apexZ = 0.0F;
        
        // Draw lines from the base to the apex
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2 * Math.PI * i / segments);
            float angle2 = (float) (2 * Math.PI * ((i + 1) % segments) / segments);
            
            float x1 = (float) (radius * Math.cos(angle));
            float z1 = (float) (radius * Math.sin(angle));
            
            float x2 = (float) (radius * Math.cos(angle2));
            float z2 = (float) (radius * Math.sin(angle2));
            
            // Base point 1 to apex
            buffer.vertex(pose, x1, -0.5F, z1)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            buffer.vertex(pose, apexX, apexY, apexZ)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            // Base edge
            buffer.vertex(pose, x1, -0.5F, z1)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
            
            buffer.vertex(pose, x2, -0.5F, z2)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, HOLOGRAM_ALPHA)
                  .endVertex();
        }
    }
    
    /**
     * Renders holographic scan lines for added effect.
     *
     * @param poseStack The pose stack
     * @param buffer The vertex buffer
     * @param packedLight The packed light
     * @param rotationAngle The current rotation angle
     */
    private void renderScanLines(PoseStack poseStack, BufferBuilder buffer, int packedLight, float rotationAngle) {
        poseStack.pushPose();
        
        // Scan lines move up and down
        float time = (Minecraft.getInstance().level.getGameTime() % 80) / 80.0F;
        float scanHeight = Mth.sin(time * (float) Math.PI * 2) * 1.5F;
        
        poseStack.translate(0, scanHeight, 0);
        
        // Make scan line transparent
        float scanAlpha = 0.3F * HOLOGRAM_ALPHA;
        
        // Render a horizontal scan plane with a grid
        Matrix4f pose = poseStack.last().pose();
        float size = 1.0F;
        
        // Start rendering lines
        buffer.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        
        // Draw a grid of lines for the scan effect
        int gridSize = 4;
        float step = size * 2 / gridSize;
        
        // Horizontal lines
        for (int i = 0; i <= gridSize; i++) {
            float y = 0;
            float z = -size + i * step;
            
            buffer.vertex(pose, -size, y, z)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                  .endVertex();
            
            buffer.vertex(pose, size, y, z)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                  .endVertex();
        }
        
        // Vertical lines
        for (int i = 0; i <= gridSize; i++) {
            float y = 0;
            float x = -size + i * step;
            
            buffer.vertex(pose, x, y, -size)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                  .endVertex();
            
            buffer.vertex(pose, x, y, size)
                  .color(HOLOGRAM_RED, HOLOGRAM_GREEN, HOLOGRAM_BLUE, scanAlpha)
                  .endVertex();
        }
        
        Tesselator.getInstance().end();
        
        poseStack.popPose();
    }
}