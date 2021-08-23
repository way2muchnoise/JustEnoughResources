package jeresources.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

public class Font {
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private boolean isSmall;
    private static final float SCALING = 0.75F;

    private Font(boolean small) {
        this.isSmall = small;
    }

    public void print(PoseStack poseStack, Object o, int x, int y) {
        doTransform(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, String.valueOf(o), 0, 0, 8);
        poseStack.popPose();
    }

    public void print(PoseStack poseStack, Object o, int x, int y, int color) {
        doTransform(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, String.valueOf(o), 0, 0, color);
        poseStack.popPose();
    }

    public void print(PoseStack poseStack, Object o, int x, int y, int color, boolean shadow) {
        doTransform(poseStack, x, y);
        if (shadow) {
            Minecraft.getInstance().font.drawShadow(poseStack, String.valueOf(o), 0, 0, color);
        } else {
            Minecraft.getInstance().font.draw(poseStack, String.valueOf(o), 0, 0, color);
        }
        poseStack.popPose();
    }

    public int getStringWidth(String string) {
        int width = Minecraft.getInstance().font.width(string);
        return (int)(isSmall ? width * SCALING : width);
    }

    private void doTransform(PoseStack poseStack, int x, int y) {
        poseStack.pushPose();
        poseStack.translate(x, y, 0);
        if (isSmall) {
            poseStack.scale(SCALING, SCALING, 1);
        }
    }
}
