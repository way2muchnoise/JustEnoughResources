package jeresources.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

public class Font {
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private boolean isSmall;
    private static final float SCALING = 0.75F;

    private Font(boolean small) {
        this.isSmall = small;
    }

    public void print(MatrixStack matrixStack, Object o, int x, int y) {
        doTransform(matrixStack, x, y);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, String.valueOf(o), 0, 0, 8);
        matrixStack.pop();
    }

    public void print(MatrixStack matrixStack, Object o, int x, int y, int color) {
        doTransform(matrixStack, x, y);
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, String.valueOf(o), 0, 0, color);
        matrixStack.pop();
    }

    public void print(MatrixStack matrixStack, Object o, int x, int y, int color, boolean shadow) {
        doTransform(matrixStack, x, y);
        if (shadow) {
            Minecraft.getInstance().fontRenderer.drawStringWithShadow(matrixStack, String.valueOf(o), 0, 0, color);
        } else {
            Minecraft.getInstance().fontRenderer.drawString(matrixStack, String.valueOf(o), 0, 0, color);
        }
        matrixStack.pop();
    }

    public int getStringWidth(String string) {
        int width = Minecraft.getInstance().fontRenderer.getStringWidth(string);
        return (int)(isSmall ? width * SCALING : width);
    }

    private void doTransform(MatrixStack matrixStack, int x, int y) {
        matrixStack.push();
        matrixStack.translate(x, y, 0);
        if (isSmall) {
            matrixStack.scale(SCALING, SCALING, 1);
        }
    }
}
