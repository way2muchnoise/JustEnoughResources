package jeresources.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.compatibility.api.MobRegistryImpl;
import jeresources.reference.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

public class RenderHelper {
    public static void drawLine(GuiGraphics guiGraphics, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        xEnd += xBegin == xEnd ? 1 : 0;
        yEnd += yBegin == yEnd ? 1 : 0;
        guiGraphics.fill(xBegin, yBegin, xEnd, yEnd, color);
    }

    public static void renderEntity(GuiGraphics guiGraphics, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushPose();
        modelViewStack.mulPoseMatrix(guiGraphics.pose().last().pose());
        modelViewStack.translate(x, y, 50.0F);
        modelViewStack.scale((float) -scale, (float) scale, (float) scale);
        PoseStack mobPoseStack = new PoseStack();
        mobPoseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        IMobRenderHook.RenderInfo renderInfo = MobRegistryImpl.applyRenderHooks(livingEntity, new IMobRenderHook.RenderInfo(x, y, scale, yaw, pitch));
        x = renderInfo.x;
        y = renderInfo.y;
        scale = renderInfo.scale;
        yaw = renderInfo.yaw;
        pitch = renderInfo.pitch;
        mobPoseStack.mulPose(Axis.XN.rotationDegrees(((float) Math.atan((pitch / 40.0F))) * 20.0F));
        livingEntity.yo = (float) Math.atan(yaw / 40.0F) * 20.0F;
        float yRot = (float) Math.atan(yaw / 40.0F) * 40.0F;
        float xRot = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        livingEntity.setYRot(yRot);
        livingEntity.setYRot(yRot);
        livingEntity.setXRot(xRot);
        livingEntity.yHeadRot = yRot;
        livingEntity.yHeadRotO = yRot;
        mobPoseStack.translate(0.0F, livingEntity.getY(), 0.0F);
        RenderSystem.applyModelViewMatrix();
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        entityRenderDispatcher.setRenderShadow(false);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, mobPoseStack, bufferSource, 15728880);
        });
        bufferSource.endBatch();
        entityRenderDispatcher.setRenderShadow(true);
        modelViewStack.popPose();
        RenderSystem.applyModelViewMatrix();
    }

    public static void renderChest(GuiGraphics guiGraphics, float x, float y, float rotate, float scale, float lidAngle) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, Resources.Vanilla.CHEST);
        // TODO: Reimplement
        // ChestModel modelchest = new ChestModel();

        guiGraphics.pose().pushPose();
        // RenderSystem.enableRescaleNormal();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.pose().translate(x, y, 50.0F);
        guiGraphics.pose().mulPose(new Quaternionf(-160.0F, 1.0F, 0.0F, 0.0F));
        guiGraphics.pose().scale(scale, -scale, -scale);
        guiGraphics.pose().translate(0.5F, 0.5F, 0.5F);
        guiGraphics.pose().mulPose(new Quaternionf(rotate, 0.0F, 1.0F, 0.0F));
        guiGraphics.pose().translate(-0.5F, -0.5F, -0.5F);

        float lidAngleF = lidAngle / 180;
        lidAngleF = 1.0F - lidAngleF;
        lidAngleF = 1.0F - lidAngleF * lidAngleF * lidAngleF;
        // modelchest.getLid().rotateAngleX = -(lidAngleF * (float) Math.PI / 2.0F);
        // modelchest.field_78233_c.offsetX += 0.1F;
        // modelchest.field_78233_c.offsetZ += 0.12F; // chestKnob
        // modelchest.field_78232_b.offsetX -= 0.755F; // chestBelow
        // modelchest.field_78232_b.offsetY -= 0.4F; // chestBelow
        // modelchest.field_78232_b.offsetZ -= 0.9F; // chestBelow
        // modelchest.renderAll();
        // RenderSystem.disableRescaleNormal();
        guiGraphics.pose().popPose();
    }

    public static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float rotate, float scale) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x, y, z);
        guiGraphics.pose().scale(-scale, -scale, -scale);
        guiGraphics.pose().translate(-0.5F, -0.5F, 0);
        guiGraphics.pose().mulPose(Axis.XP.rotationDegrees(-30F));
        guiGraphics.pose().translate(0.5F, 0, -0.5F);
        guiGraphics.pose().mulPose(Axis.YP.rotationDegrees(rotate));
        guiGraphics.pose().translate(-0.5F, 0, 0.5F);

        guiGraphics.pose().pushPose();
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        guiGraphics.pose().translate(0, 0, -1);

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        mc.getBlockRenderer().renderSingleBlock(block, guiGraphics.pose(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        bufferSource.endBatch();
        guiGraphics.pose().popPose();

        guiGraphics.pose().popPose();
    }

    public static void scissor(GuiGraphics guiGraphics, int x, int y, int w, int h) {
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        double[] xyzTranslation = getGLTranslation(guiGraphics, scale);
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        int scissorX = Math.round(Math.round(xyzTranslation[0] + x));
        int scissorY = Math.round(Math.round(Minecraft.getInstance().getWindow().getHeight() - y - xyzTranslation[1]));
        int scissorW = Math.round(w - x);
        int scissorH = Math.round(h - y);
        IScissorHook.ScissorInfo scissorInfo = MobRegistryImpl.applyScissorHooks(new IScissorHook.ScissorInfo(scissorX, scissorY, scissorW, scissorH));
        RenderSystem.enableScissor(scissorInfo.x, scissorInfo.y, Math.max(0, scissorInfo.width), Math.max(0, scissorInfo.height));
    }

    public static void stopScissor() {
        RenderSystem.disableScissor();
    }

    public static void drawTexture(GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height, ResourceLocation resource) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, resource);
        drawTexturedModalRect(guiGraphics, x, y, u, v, width, height, 0);
    }

    public static double[] getGLTranslation(GuiGraphics guiGraphics, double scale) {
        Matrix4f matrix = guiGraphics.pose().last().pose();
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        matrix.set(buf);
        // { x, y, z }
        return new double[] { buf.get(getIndexFloatBuffer(0,3)) * scale, buf.get(getIndexFloatBuffer(1, 3)) * scale, buf.get(getIndexFloatBuffer(2, 3)) * scale };
    }

    private static int getIndexFloatBuffer(int x, int y) {
        return y * 4 + x;
    }

    public static double getGuiScaleFactor() {
        return Minecraft.getInstance().getWindow().getGuiScale();
    }

    public static void drawTexturedModalRect(GuiGraphics guiGraphics, int x, int y, int u, int v, int width, int height, float zLevel)
    {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder wr = tesselator.getBuilder();
        wr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        wr.vertex(matrix, x        , y + height, zLevel).uv( u          * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y + height, zLevel).uv((u + width) * uScale, ((v + height) * vScale)).endVertex();
        wr.vertex(matrix, x + width, y         , zLevel).uv((u + width) * uScale, ( v           * vScale)).endVertex();
        wr.vertex(matrix, x        , y         , zLevel).uv( u          * uScale, ( v           * vScale)).endVertex();
        tesselator.end();
    }
}
