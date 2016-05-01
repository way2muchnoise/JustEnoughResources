package jeresources.util;

import jeresources.reference.Resources;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.IReloadableResourceManager;

public class Font
{
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private FontRenderer fontRenderer;

    private Font(boolean small)
    {
        Minecraft mc = Minecraft.getMinecraft();
        fontRenderer = new FontRenderer(mc.gameSettings, Resources.Vanilla.FONT, mc.getTextureManager(), small);
        ((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(fontRenderer);
    }

    public void print(Object o, int x, int y)
    {
        fontRenderer.drawString(String.valueOf(o), x, y, 8, false);
    }

    public void print(Object o, int x, int y, int color)
    {
        fontRenderer.drawString(String.valueOf(o), x, y, color, false);
    }

    public void print(Object o, int x, int y, int color, boolean shadow)
    {
        fontRenderer.drawString(String.valueOf(o), x, y, color, shadow);
    }

    public int getStringWidth(String string)
    {
        return fontRenderer.getStringWidth(string);
    }
}
