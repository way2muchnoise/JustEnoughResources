package jeresources.util;

import com.mojang.blaze3d.platform.GLX;
import com.mojang.blaze3d.platform.GlStateManager;
import jeresources.api.render.ColourHelper;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.compatibility.MobRegistryImpl;
import jeresources.reference.Resources;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.model.ChestModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class RenderHelper {
    public static void drawArrow(double xBegin, double yBegin, double xEnd, double yEnd, int color) {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.mainWindow.getGuiScaleFactor();
        GlStateManager.color3f(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
        GL11.glLineWidth((float)(scale * 1.3F));
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
        double angle = Math.atan2(yEnd - yBegin, xEnd - xBegin) * 180.0 / Math.PI;
        GlStateManager.pushMatrix();
        GlStateManager.translated(xEnd, yEnd, 0.0);
        GlStateManager.rotatef((float) angle, 0.0F, 0.0F, 1.0F);
        GlStateManager.scaled(scale, scale, 1.0);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2d(3.0, 0.0);
        GL11.glVertex2d(0.0, -1.5);
        GL11.glVertex2d(0.0, 1.5);
        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawLine(double xBegin, double yBegin, double xEnd, double yEnd, int color) {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.mainWindow.getGuiScaleFactor();
        GlStateManager.color3f(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
        GL11.glLineWidth((float)(scale * 1.3F));
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
    }

    public static void drawPoint(double x, double y, int color) {
        Minecraft mc = Minecraft.getInstance();
        double scale = mc.mainWindow.getGuiScaleFactor();
        GlStateManager.color3f(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
        GL11.glPointSize((float)(scale * 1.3F));
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void renderEntity(int x, int y, double scale, double yaw, double pitch, LivingEntity livingEntity) {
        if (livingEntity.world == null) livingEntity.world = Minecraft.getInstance().world;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 50.0F);
        GlStateManager.scaled(-scale, scale, scale);
        GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float renderYawOffset = livingEntity.renderYawOffset;
        float rotationYaw = livingEntity.rotationYaw;
        float rotationPitch = livingEntity.rotationPitch;
        float prevRotationYawHead = livingEntity.prevRotationYawHead;
        float rotationYawHead = livingEntity.rotationYawHead;
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        IMobRenderHook.RenderInfo renderInfo = MobRegistryImpl.applyRenderHooks(livingEntity, new IMobRenderHook.RenderInfo(x, y, scale, yaw, pitch));
        x = renderInfo.x;
        y = renderInfo.y;
        scale = renderInfo.scale;
        yaw = renderInfo.yaw;
        pitch = renderInfo.pitch;
        GlStateManager.rotatef(-((float) Math.atan((pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        livingEntity.renderYawOffset = (float) Math.atan(yaw / 40.0F) * 20.0F;
        livingEntity.rotationYaw = (float) Math.atan(yaw / 40.0F) * 40.0F;
        livingEntity.rotationPitch = -((float) Math.atan(pitch / 40.0F)) * 20.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
        GlStateManager.translated(0.0F, livingEntity.getYOffset(), 0.0F);
        getRenderManager().setPlayerViewY(180.0F);
        getRenderManager().renderEntity(livingEntity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        livingEntity.renderYawOffset = renderYawOffset;
        livingEntity.rotationYaw = rotationYaw;
        livingEntity.rotationPitch = rotationPitch;
        livingEntity.prevRotationYawHead = prevRotationYawHead;
        livingEntity.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        // GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.activeTexture(GLX.GL_TEXTURE1);
        GlStateManager.disableTexture();
        // GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.activeTexture(GLX.GL_TEXTURE0);
    }

    public static void renderChest(float x, float y, float rotate, float scale, float lidAngle) {
        Minecraft.getInstance().getTextureManager().bindTexture(Resources.Vanilla.CHEST);
        ChestModel modelchest = new ChestModel();

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef(x, y, 50.0F);
        GlStateManager.rotatef(-160.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scalef(scale, -scale, -scale);
        GlStateManager.translatef(0.5F, 0.5F, 0.5F);
        GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);

        float lidAngleF = lidAngle / 180;
        lidAngleF = 1.0F - lidAngleF;
        lidAngleF = 1.0F - lidAngleF * lidAngleF * lidAngleF;
        modelchest.getLid().rotateAngleX = -(lidAngleF * (float) Math.PI / 2.0F);
        modelchest.field_78233_c.offsetX += 0.1F;
        modelchest.field_78233_c.offsetZ += 0.12F; // chestKnob
        modelchest.field_78232_b.offsetX -= 0.755F; // chestBelow
        modelchest.field_78232_b.offsetY -= 0.4F; // chestBelow
        modelchest.field_78232_b.offsetZ -= 0.9F; // chestBelow
        modelchest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    public static void renderBlock(BlockState block, float x, float y, float z, float rotate, float scale) {
        Minecraft mc = Minecraft.getInstance();
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.rotatef(-30.0F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 50.0F + z);
        GlStateManager.rotatef(20.0F, 1.0F, 0.0F, 0.0F);
        scale *= 50;
        GlStateManager.scalef(scale, -scale, -scale);
        GlStateManager.translatef(0.5F, 0.5F, 0.5F);
        GlStateManager.rotatef(rotate, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        mc.getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        mc.getBlockRendererDispatcher().renderBlockBrightness(block, 1.0F);
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    public static void scissor(int x, int y, int w, int h) {
        double scale = Minecraft.getInstance().mainWindow.getGuiScaleFactor();
        double[] xyzTranslation = getGLTranslation(scale);
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        int scissorX = Math.round(Math.round(xyzTranslation[0] + x));
        int scissorY = Math.round(Math.round(Minecraft.getInstance().mainWindow.getHeight() - y - h - xyzTranslation[1]));
        int scissorW = Math.round(w);
        int scissorH = Math.round(h);
        IScissorHook.ScissorInfo scissorInfo = MobRegistryImpl.applyScissorHooks(new IScissorHook.ScissorInfo(scissorX, scissorY, scissorW, scissorH));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorInfo.x, scissorInfo.y, scissorInfo.width, scissorInfo.height);
    }

    public static void stopScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void drawTexture(int x, int y, int u, int v, int width, int height, ResourceLocation resource) {
        Minecraft.getInstance().getTextureManager().bindTexture(resource);
        GuiUtils.drawTexturedModalRect(x, y, u, v, width, height, 0);
    }

    private static EntityRendererManager getRenderManager() {
        return Minecraft.getInstance().getRenderManager();
    }

    public static double[] getGLTranslation(double scale) {
        FloatBuffer buf = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloatv(GL11.GL_MODELVIEW_MATRIX, buf);
        buf.rewind();
        Matrix4f mat = new Matrix4f();
        mat.read(buf);
        // { x, y, z }
        return new double[] { mat.get(0, 3) * scale, mat.get(1, 3) * scale, mat.get(2, 3) * scale };
    }
}
