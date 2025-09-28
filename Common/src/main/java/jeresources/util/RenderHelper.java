package jeresources.util;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import jeresources.api.render.IMobRenderHook;
import jeresources.compatibility.api.MobRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.*;

import java.lang.Math;

public class RenderHelper {
    public static void drawLine(GuiGraphics guiGraphics, int xBegin, int yBegin, int xEnd, int yEnd, int color) {
        xEnd += xBegin == xEnd ? 1 : 0;
        yEnd += yBegin == yEnd ? 1 : 0;
        guiGraphics.fill(xBegin, yBegin, xEnd, yEnd, color);
    }

    public static void renderEntity(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        PoseStack mobPoseStack = new PoseStack();
        ScreenRectangle screenRectangle = new ScreenRectangle(x1, y1, x2 - x1, y2 - y1).transformAxisAligned(guiGraphics.pose());
        IMobRenderHook.RenderInfo renderInfo = MobRegistryImpl.applyRenderHooks(mobPoseStack, livingEntity, new IMobRenderHook.RenderInfo(0, 0, scale, yaw, pitch));
        int x = renderInfo.x;
        int y = renderInfo.y;
        mobPoseStack.translate(x, y, 0);
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
        mobPoseStack.translate(0.0F, livingEntity.getBbHeight() / 2, 0.0F);
        Vector3f translationVec = new Vector3f();
        mobPoseStack.last().pose().getTranslation(translationVec);
        Quaternionf rotationQuat = new Quaternionf();
        mobPoseStack.last().pose().getUnnormalizedRotation(rotationQuat);
        Quaternionf cameraQuat = (new Quaternionf()).rotateZ((float)Math.PI);
        cameraQuat.mul(new Quaternionf().rotateY((float)Math.PI));
        rotationQuat.mul(cameraQuat);
        InventoryScreen.renderEntityInInventory(guiGraphics, screenRectangle.left(), screenRectangle.top(), screenRectangle.right(), screenRectangle.bottom(), (float) scale, translationVec, rotationQuat, cameraQuat, livingEntity);
    }

    public static void renderChest(GuiGraphics guiGraphics, float x, float y, float rotate, float scale, float lidAngle) {
        // RenderType rendertype = RenderType.guiTextured(Resources.Vanilla.CHEST);
        // VertexConsumer buffer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(rendertype);
        // TODO: Reimplement
        // ChestModel modelchest = new ChestModel();

        PoseStack poseStack = new PoseStack();
        // RenderSystem.enableRescaleNormal();
        // RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.translate(x, y, 50.0F);
        poseStack.mulPose(new Quaternionf(-160.0F, 1.0F, 0.0F, 0.0F));
        poseStack.scale(scale, -scale, -scale);
        poseStack.translate(0.5F, 0.5F, 0.5F);
        poseStack.mulPose(new Quaternionf(rotate, 0.0F, 1.0F, 0.0F));
        poseStack.translate(-0.5F, -0.5F, -0.5F);

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
    }

    public static void renderBlock(GuiGraphics guiGraphics, BlockState block, float x, float y, float z, float rotate, float scale) {
        Minecraft mc = Minecraft.getInstance();
        PoseStack poseStack = new PoseStack();
        poseStack.translate(x, y, z);
        poseStack.scale(-scale, -scale, -scale);
        poseStack.translate(-0.5F, -0.5F, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(-30F));
        poseStack.translate(0.5F, 0, -0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotate));
        poseStack.translate(-0.5F, 0, 0.5F);

        poseStack.pushPose();
        poseStack.translate(0, 0, -1);

        MultiBufferSource.BufferSource bufferSource = mc.renderBuffers().bufferSource();
        mc.getBlockRenderer().renderSingleBlock(block, poseStack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY);
        bufferSource.endBatch();
    }

    public static void drawTexture(GuiGraphics guiGraphics, ResourceLocation resource, int x, int y, int u, int v, int width, int height) {
        drawTexturedModalRect(guiGraphics, resource, x, y, u, v, width, height);
    }

    public static void drawTexturedModalRect(GuiGraphics guiGraphics, ResourceLocation resource, int x, int y, int u, int v, int width, int height) {
        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED,
                resource,
                x,
                y,
                u,
                v,
                width,
                height,
                256,
                256
        );
    }
}
