package jeresources.util;

import jeresources.api.render.ColourHelper;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.compatibility.MobRegistryImpl;
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
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import org.lwjgl.opengl.GL11;

public class RenderHelper
{
    public static void drawArrow(double xBegin, double yBegin, double xEnd, double yEnd, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
        GL11.glLineWidth(scale * 1.3F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
        double angle = Math.atan2(yEnd - yBegin, xEnd - xBegin) * 180.0 / Math.PI;
        GlStateManager.pushMatrix();
        GlStateManager.translate(xEnd, yEnd, 0.0);
        GlStateManager.rotate((float) angle, 0.0F, 0.0F, 1.0F);
        GlStateManager.scale(scale, scale, 1.0);
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2d(3.0, 0.0);
        GL11.glVertex2d(0.0, -1.5);
        GL11.glVertex2d(0.0, 1.5);
        GL11.glEnd();
        GlStateManager.popMatrix();
    }

    public static void drawLine(double xBegin, double yBegin, double xEnd, double yEnd, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
        GL11.glLineWidth(scale * 1.3F);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2d(xBegin, yBegin);
        GL11.glVertex2d(xEnd, yEnd);
        GL11.glEnd();
    }

    public static void drawPoint(double x, double y, int color)
    {
        Minecraft mc = Minecraft.getMinecraft();
        int scale = new ScaledResolution(mc).getScaleFactor();
        GlStateManager.color(ColourHelper.getRed(color), ColourHelper.getGreen(color), ColourHelper.getBlue(color));
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
        IMobRenderHook.RenderInfo renderInfo = MobRegistryImpl.applyRenderHooks(entityLivingBase, new IMobRenderHook.RenderInfo(x, y, scale, yaw, pitch));
        x = renderInfo.x;
        y = renderInfo.y;
        scale = renderInfo.scale;
        yaw = renderInfo.yaw;
        pitch = renderInfo.pitch;
        GlStateManager.rotate(-((float) Math.atan((double) (pitch / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entityLivingBase.renderYawOffset = (float) Math.atan((double) (yaw / 40.0F)) * 20.0F;
        entityLivingBase.rotationYaw = (float) Math.atan((double) (yaw / 40.0F)) * 40.0F;
        entityLivingBase.rotationPitch = -((float) Math.atan((double) (pitch / 40.0F))) * 20.0F;
        entityLivingBase.rotationYawHead = entityLivingBase.rotationYaw;
        entityLivingBase.prevRotationYawHead = entityLivingBase.rotationYaw;
        GlStateManager.translate(0.0F, entityLivingBase.getYOffset(), 0.0F);
        getRenderManager().setPlayerViewY(180.0F);
        //String temp = BossStatus.bossName;
        getRenderManager().doRenderEntity(entityLivingBase, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        //BossStatus.bossName = temp;
        entityLivingBase.renderYawOffset = renderYawOffset;
        entityLivingBase.rotationYaw = rotationYaw;
        entityLivingBase.rotationPitch = rotationPitch;
        entityLivingBase.prevRotationYawHead = prevRotationYawHead;
        entityLivingBase.rotationYawHead = rotationYawHead;
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
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
        mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        mc.getBlockRendererDispatcher().renderBlockBrightness(block, 1.0F);
        GlStateManager.popMatrix();
        net.minecraft.client.renderer.RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
    }

    public static void scissor(Minecraft mc, int guiWidth, int guiHeight, float x, float y, float w, float h)
    {
        int scale = new ScaledResolution(mc).getScaleFactor();
        x *= scale;
        y *= scale;
        w *= scale;
        h *= scale;
        float guiScaledWidth = (guiWidth * scale);
        float guiScaledHeight = (guiHeight * scale);
        long guiLeft = Math.round((mc.displayWidth - guiScaledWidth) / 2.0F);
        long guiTop = Math.round((mc.displayHeight + guiScaledHeight) / 2.0F);
        int scissorX = Math.round(guiLeft + x - scale);
        int scissorY = Math.round(guiTop - h - y);
        int scissorW = Math.round(w);
        int scissorH = Math.round(h);
        IScissorHook.ScissorInfo scissorInfo = MobRegistryImpl.applyScissorHooks(new IScissorHook.ScissorInfo(scissorX, scissorY, scissorW, scissorH));
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(scissorInfo.x, scissorInfo.y, scissorInfo.width, scissorInfo.height);
    }

    public static void stopScissor()
    {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void drawTexture(int x, int y, int u, int v, int width, int height, ResourceLocation resource)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(resource);
        GuiUtils.drawTexturedModalRect(x, y, u, v, width, height, 0);
    }

    private static RenderManager getRenderManager()
    {
        return Minecraft.getMinecraft().getRenderManager();
    }
}
