package jeresources.util;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;

public class Font {
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private boolean isSmall;
    private static final float SCALING = 0.75F;

    private Font(boolean small) {
        this.isSmall = small;
    }

    public void print(Object o, int x, int y) {
        doTransform(x, y);
        Minecraft.getInstance().fontRenderer.drawString(String.valueOf(o), 0, 0, 8);
        GlStateManager.popMatrix();
    }

    public void print(Object o, int x, int y, int color) {
        doTransform(x, y);
        Minecraft.getInstance().fontRenderer.drawString(String.valueOf(o), 0, 0, color);
        GlStateManager.popMatrix();
    }

    public void print(Object o, int x, int y, int color, boolean shadow) {
        doTransform(x, y);
        if (shadow) {
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(String.valueOf(o), 0, 0, color);
        } else {
            Minecraft.getInstance().fontRenderer.drawString(String.valueOf(o), 0, 0, color);
        }
        GlStateManager.popMatrix();
    }

    public int getStringWidth(String string) {
        int width = Minecraft.getInstance().fontRenderer.getStringWidth(string);
        return (int)(isSmall ? width * SCALING : width);
    }

    private void doTransform(int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.translatef(x, y, 0);
        if (isSmall) {
            GlStateManager.scalef(SCALING, SCALING, 1);
        }
    }
}
