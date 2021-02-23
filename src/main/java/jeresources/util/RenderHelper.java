package jeresources.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import jeresources.api.render.ColorHelper;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.compatibility.MobRegistryImpl;
import jeresources.reference.Resources;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fml.client.gui.GuiUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class RenderHelper {
    public static void drawArrow(double xBegin, double yBegin, double xEnd, double yEnd, int color) {
        double scale = getGuiScaleFactor();
        ColorHelper.setColor3f(color);
        GL11.glLineWidth((float)(scale * 1.3F));
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
        double angle = Math.atan2(yEnd - yBegin, xEnd - xBegin) * 180.0 / Math.PI;
        RenderSystem.pushMatrix();
        RenderSystem.translated(xEnd, yEnd, 0.0);
        RenderSystem.rotatef((float) angle, 0.0F, 0.0F, 1.0F);
        RenderSystem.scaled(scale, scale, 1.0);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2d(3.0, 0.0);
        GL11.glVertex2d(0.0, -1.5);
        GL11.glVertex2d(0.0, 1.5);
        GL11.glEnd();
        RenderSystem.popMatrix();
    }

    public static void drawLine(MatrixStack matrixStack, double xBegin, double yBegin, double xEnd, double yEnd, int color) {
        ColorHelper.setColor3f(color);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        GL11.glLineWidth((float)(getGuiScaleFactor() * 1.3F));
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
        RenderSystem.popMatrix();
    }

    public static void drawPoint(double x, double y, int color) {
        ColorHelper.setColor3f(color);
        GL11.glPointSize((float)(Minecraft.getInstance().getMainWindow().getGuiScaleFactor() * 1.3F));
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void renderEntity(MatrixStack matrixStack, int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        if (livingEntity.world == null) livingEntity.world = Minecraft.getInstance().world;
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        RenderSystem.translatef(x, y, 50.0F);
        RenderSystem.scalef((float) -scale, (float) scale, (float) scale);
        MatrixStack mobMatrix = new MatrixStack();
        mobMatrix.rotate(Vector3f.ZP.rotationDegrees(180.0F));
        IMobRenderHook.RenderInfo renderInfo = MobRegistryImpl.applyRenderHooks(livingEntity, new IMobRenderHook.RenderInfo(x, y, scale, yaw, pitch));
        x = renderInfo.x;
        y = renderInfo.y;
        scale = renderInfo.scale;
        yaw = renderInfo.yaw;
        pitch = renderInfo.pitch;
        RenderSystem.rotatef(((float) Math.atan((pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        livingEntity.renderYawOffset = (float) Math.atan(yaw / 40.0F) * 20.0F;
        livingEntity.rotationYaw = (float) Math.atan(yaw / 40.0F) * 40.0F;
        livingEntity.rotationPitch = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
        mobMatrix.translate(0.0F, livingEntity.getYOffset(), 0.0F);
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl renderTypeBuffer = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        RenderSystem.runAsFancy(() -> {
            entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, mobMatrix, renderTypeBuffer, 15728880);
        });
        renderTypeBuffer.finish();
        entityrenderermanager.setRenderShadow(true);
        RenderSystem.popMatrix();
    }

    public static void renderChest(MatrixStack matrixStack, float x, float y, float rotate, float scale, float lidAngle) {
        Minecraft.getInstance().getTextureManager().bindTexture(Resources.Vanilla.CHEST);
        // TODO: Reimplement
        // ChestModel modelchest = new ChestModel();

        matrixStack.push();
        RenderSystem.enableRescaleNormal();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.translate(x, y, 50.0F);
        matrixStack.rotate(new Quaternion(-160.0F, 1.0F, 0.0F, 0.0F));
        matrixStack.scale(scale, -scale, -scale);
        matrixStack.translate(0.5F, 0.5F, 0.5F);
        matrixStack.rotate(new Quaternion(rotate, 0.0F, 1.0F, 0.0F));
        matrixStack.translate(-0.5F, -0.5F, -0.5F);

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
        RenderSystem.disableRescaleNormal();
        matrixStack.pop();
    }

    public static void renderBlock(MatrixStack matrixStack, BlockState block, float x, float y, float z, float rotate, float scale) {
        Minecraft mc = Minecraft.getInstance();
        matrixStack.push();
        matrixStack.translate(x, y, z);
        matrixStack.scale(-scale, -scale, -scale);
        matrixStack.translate(-0.5F, -0.5F, 0);
        matrixStack.rotate(Vector3f.XP.rotationDegrees(-30F));
        matrixStack.translate(0.5F, 0, -0.5F);
        matrixStack.rotate(Vector3f.YP.rotationDegrees(rotate));
        matrixStack.translate(-0.5F, 0, 0.5F);

        matrixStack.push();
        RenderSystem.color4f(1F, 1F, 1F, 1F);
        matrixStack.translate(0, 0, -1);

        mc.getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
        IRenderTypeBuffer.Impl buffers = mc.getRenderTypeBuffers().getBufferSource();
        mc.getBlockRendererDispatcher().renderBlock(block, matrixStack, buffers, 15728880, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        buffers.finish();
        matrixStack.pop();

        matrixStack.pop();
    }

    public static void scissor(MatrixStack matrixStack, int x, int y, int w, int h) {
        double scale = Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
        double[] xyzTranslation = getGLTranslation(matrixStack, scale);
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        int scissorX = Math.round(Math.round(xyzTranslation[0] + x));
        int scissorY = Math.round(Math.round(Minecraft.getInstance().getMainWindow().getHeight() - y - h - xyzTranslation[1]));
        int scissorW = Math.round(w);
        int scissorH = Math.round(h);
        IScissorHook.ScissorInfo scissorInfo = MobRegistryImpl.applyScissorHooks(new IScissorHook.ScissorInfo(scissorX, scissorY, scissorW, scissorH));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorInfo.x, scissorInfo.y, scissorInfo.width, scissorInfo.height);
    }

    public static void stopScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void drawTexture(MatrixStack matrixStack, int x, int y, int u, int v, int width, int height, ResourceLocation resource) {
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        GuiUtils.drawTexturedModalRect(x, y, u, v, width, height, 0);
        RenderSystem.popMatrix();
    }

    public static double[] getGLTranslation(MatrixStack matrixStack, double scale) {
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        matrix.write(buf);
        // { x, y, z }
        return new double[] { buf.get(getIndexFloatBuffer(0,3)) * scale, buf.get(getIndexFloatBuffer(1, 3)) * scale, buf.get(getIndexFloatBuffer(2, 3)) * scale };
    }

    private static int getIndexFloatBuffer(int x, int y) {
        return y * 4 + x;
    }

    public static double getGuiScaleFactor() {
        return Minecraft.getInstance().getMainWindow().getGuiScaleFactor();
    }
}
