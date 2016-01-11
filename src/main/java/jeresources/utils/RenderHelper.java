package jeresources.utils;

import jeresources.api.utils.ColorHelper;
import jeresources.reference.Resources;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.Point;
import java.awt.Dimension;

public class RenderHelper
{
    public static void drawArrow(double xBegin, double yBegin, double xEnd, double yEnd, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color));
        GL11.glLineWidth(scale * 1.3F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
        double angle = Math.atan2(yEnd - yBegin, xEnd - xBegin) * 180.0 / Math.PI;
        GlStateManager.pushMatrix();
        GlStateManager.translate(xEnd, yEnd, 0.0);
        GlStateManager.rotate((float)angle, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(scale, scale, 1.0);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2d(3.0, 0.0);
        GL11.glVertex2d(0.0, -1.5);
        GL11.glVertex2d(0.0, 1.5);
        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawLine(double x1, double y1, double x2, double y2, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color));
        GL11.glLineWidth(scale * 1.3F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(x1, y1);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
    }

    public static void drawPoint(double x, double y, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColorHelper.getRed(color), ColorHelper.getGreen(color), ColorHelper.getBlue(color));
        GL11.glPointSize(scale * 1.3F);
        GL11.glBegin(GL11.GL_POINTS);
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void renderEntity(int x, int y, float scale, float yaw, float pitch, EntityLivingBase entityLivingBase)
    {
        if (entityLivingBase.worldObj == null) entityLivingBase.worldObj = Minecraft.getMinecraft().theWorld;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        float renderYawOffset = entityLivingBase.renderYawOffset;
        float rotationYaw = entityLivingBase.rotationYaw;
        float rotationPitch = entityLivingBase.rotationPitch;
        float prevRotationYawHead = entityLivingBase.prevRotationYawHead;
        float rotationYawHead = entityLivingBase.rotationYawHead;
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        if (entityLivingBase instanceof EntityDragon || entityLivingBase instanceof EntityBat)
        {
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            if (entityLivingBase instanceof EntityDragon)
            {
                pitch = -pitch-80;
                GlStateManager.rotate(yaw < 90 ? (yaw < -90 ? 90 : -yaw) : -90, 0.0F, 1.0F, 0.0F);
            }
            else pitch = -pitch;
        }
        if (entityLivingBase instanceof EntityGuardian && ((EntityGuardian) entityLivingBase).isElder())
        {
            GlStateManager.scale(0.6F, 0.6F, 0.6F);
        }
        if (entityLivingBase instanceof EntitySquid)
        {
            GlStateManager.rotate(50.0F, 1.0F, 0.0F, 0.0F);
        }
        GlStateManager.rotate(-((float) Math.atan((double) (pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entityLivingBase.renderYawOffset = (float) Math.atan((double) (yaw / 40.0F)) * 20.0F;
        entityLivingBase.rotationYaw = (float) Math.atan((double) (yaw / 40.0F)) * 40.0F;
        entityLivingBase.rotationPitch = -((float) Math.atan((double) (pitch / 40.0F))) * 20.0F;
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
        GlStateManager.translate(0.0F, entityLivingBase.getYOffset(), 0.0F);
        getRenderManager().setPlayerViewY(180.0F);
        String temp = BossStatus.bossName;
        getRenderManager().renderEntityWithPosYaw(entityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        BossStatus.bossName = temp;
        entityLivingBase.renderYawOffset = renderYawOffset;
        entityLivingBase.rotationYaw = rotationYaw;
        entityLivingBase.rotationPitch = rotationPitch;
        entityLivingBase.prevRotationYawHead = prevRotationYawHead;
        entityLivingBase.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    public static void renderChest(float x, float y, float rotate, float scale, float lidAngle)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.Vanilla.CHEST);
        ModelChest modelchest = new ModelChest();

        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate(x, y, 50.0F);
        GlStateManager.rotate(-160.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(scale, -scale, -scale);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        float lidAngleF = lidAngle / 180;
        lidAngleF = 1.0F - lidAngleF;
        lidAngleF = 1.0F - lidAngleF * lidAngleF * lidAngleF;
        modelchest.chestLid.rotateAngleX = -(lidAngleF * (float) Math.PI / 2.0F);
        modelchest.chestKnob.offsetX += 0.1F;
        modelchest.chestKnob.offsetZ += 0.12F;
        modelchest.chestBelow.offsetX -= 0.755F;
        modelchest.chestBelow.offsetY -= 0.4F;
        modelchest.chestBelow.offsetZ -= 0.9F;
        modelchest.renderAll();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    public static void renderBlock(IBlockState block, float x, float y, float z, float rotate, float scale)
    {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();
        GlStateManager.rotate(-30.0F, 0.0F, 1.0F, 0.0F);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 50.0F + z);
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        scale *= 50;
        GlStateManager.scale(scale, -scale, -scale);
        GlStateManager.translate(0.5F, 0.5F, 0.5F);
        GlStateManager.rotate(rotate, 0.0F, 1.0F, 0.0F);
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);
        mc.getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        mc.getBlockRendererDispatcher().renderBlockBrightness(block, 1.0F);
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    public static void scissor(Minecraft mc, int guiWidth, int guiHeight, float x, float y, int w, int h)
    {
        int scale = new ScaledResolution(mc).getScaleFactor();
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        float guiScaledWidth = (guiWidth * scale);
        float guiScaledHeight = (guiHeight * scale);
        float guiLeft = ((mc.displayWidth / 2) - guiScaledWidth / 2);
        float guiTop = ((mc.displayHeight / 2) + guiScaledHeight / 2);
        int scissorX = Math.round((guiLeft + x));
        int scissorY = Math.round(((guiTop - h) - y));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorX, scissorY, w, h);
    }

    public static void stopScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    private static RenderManager getRenderManager()
    {
        return Minecraft.getMinecraft().getRenderManager();
    }

    public static Dimension displaySize() {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution res = new ScaledResolution(mc);
        return new Dimension(res.getScaledWidth(), res.getScaledHeight());
    }

    public static Dimension displayRes() {
        Minecraft mc = Minecraft.getMinecraft();
        return new Dimension(mc.displayWidth, mc.displayHeight);
    }

    public static Point getMousePosition(int eventX, int eventY) {
        Dimension size = displaySize();
        Dimension res = displayRes();
        return new Point(eventX * size.width / res.width, size.height - eventY * size.height / res.height - 1);
    }

    public static Point getMousePosition() {
        return getMousePosition(Mouse.getX(), Mouse.getY());
    }

    public static float getTop(Minecraft mc, int height)
    {
        return (mc.currentScreen.height - height) / 2;
    }

    public static float getLeft(Minecraft mc, int width)
    {
        return (mc.currentScreen.width - width) / 2;
    }
}
