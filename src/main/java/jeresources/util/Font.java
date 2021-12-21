package jeresources.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class Font {
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private boolean isSmall;
    private static final float SCALING = 0.75F;

    private Font(boolean small) {
        this.isSmall = small;
    }

    public void print(PoseStack poseStack, String line, int x, int y) {
        doTransform(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, line, 0, 0, 8);
        poseStack.popPose();
    }

    public void print(PoseStack poseStack, FormattedCharSequence line, int x, int y) {
        doTransform(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, line, 0, 0, 8);
        poseStack.popPose();
    }

    public void print(PoseStack poseStack, int number, int x, int y) {
        print(poseStack, String.valueOf(number), x, y);
    }

    public void splitPrint(PoseStack poseStack, String line, int x, int y, int maxWidth) {
        doTransform(poseStack, x, y);
        int scaledWidth = (int) (maxWidth * (isSmall ? SCALING : 1));
        List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(new TextComponent(line), scaledWidth);
        int scaledLineHeight = (int) (Minecraft.getInstance().font.lineHeight * (isSmall ? SCALING : 1));
        for (int i = 0; i < lines.size(); i++) {
            Minecraft.getInstance().font.draw(poseStack, lines.get(i), 0, i * scaledLineHeight, 8);

        }
        poseStack.popPose();
    }

    public void splitPrint(PoseStack poseStack, FormattedCharSequence line, int x, int y, int maxWidth) {
        splitPrint(poseStack, line.toString(), x, y, maxWidth);
    }

    public void print(PoseStack poseStack, FormattedCharSequence line, int x, int y, int color) {
        doTransform(poseStack, x, y);
        Minecraft.getInstance().font.draw(poseStack, line, 0, 0, color);
        poseStack.popPose();
    }

    public void print(PoseStack poseStack, FormattedCharSequence line, int x, int y, int color, boolean shadow) {
        doTransform(poseStack, x, y);
        if (shadow) {
            Minecraft.getInstance().font.drawShadow(poseStack, line, 0, 0, color);
        } else {
            Minecraft.getInstance().font.draw(poseStack, line, 0, 0, color);
        }
        poseStack.popPose();
    }

    public int getStringWidth(FormattedCharSequence line) {
        int width = Minecraft.getInstance().font.width(line);
        return (int)(isSmall ? width * SCALING : width);
    }

    public int getStringWidth(String line) {
        int width = Minecraft.getInstance().font.width(line);
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
