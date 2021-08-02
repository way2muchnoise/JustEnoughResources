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
        Minecraft.getInstance().font.draw(matrixStack, String.valueOf(o), 0, 0, 8);
        matrixStack.popPose();
    }

    public void print(MatrixStack matrixStack, Object o, int x, int y, int color) {
        doTransform(matrixStack, x, y);
        Minecraft.getInstance().font.draw(matrixStack, String.valueOf(o), 0, 0, color);
        matrixStack.popPose();
    }

    public void print(MatrixStack matrixStack, Object o, int x, int y, int color, boolean shadow) {
        doTransform(matrixStack, x, y);
        if (shadow) {
            Minecraft.getInstance().font.drawShadow(matrixStack, String.valueOf(o), 0, 0, color);
        } else {
            Minecraft.getInstance().font.draw(matrixStack, String.valueOf(o), 0, 0, color);
        }
        matrixStack.popPose();
    }

    public int getStringWidth(String string) {
        int width = Minecraft.getInstance().font.width(string);
        return (int)(isSmall ? width * SCALING : width);
    }

    private void doTransform(MatrixStack matrixStack, int x, int y) {
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        if (isSmall) {
            matrixStack.scale(SCALING, SCALING, 1);
        }
    }
}
