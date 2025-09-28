package jeresources.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class Font {
    public final static Font small = new Font(true);
    public final static Font normal = new Font(false);

    private final boolean isSmall;
    private static final float SCALING = 0.75F;

    private Font(boolean small) {
        this.isSmall = small;
    }

    /**
     * Renders a string onto the specified graphics context at the given position.
     *
     * @param guiGraphics the graphics context used for rendering the string
     * @param line the text to render
     * @param x the x-coordinate where the string will be drawn
     * @param y the y-coordinate where the string will be drawn
     */
    public void print(GuiGraphics guiGraphics, String line, int x, int y) {
        doTransform(guiGraphics, x, y);
        guiGraphics.drawString(getMCFont(), line, x, y, 8, false);
        guiGraphics.pose().popMatrix();
    }

    public void print(GuiGraphics guiGraphics, Component line, int x, int y) {
        print(guiGraphics, line.getVisualOrderText(), x, y);
    }

    public void print(GuiGraphics guiGraphics, FormattedCharSequence line, int x, int y) {
        doTransform(guiGraphics, x, y);
        guiGraphics.drawString(getMCFont(), line, 0, 0, 8, false);
        guiGraphics.pose().popMatrix();
    }

    public void print(GuiGraphics guiGraphics, int number, int x, int y) {
        print(guiGraphics, String.valueOf(number), x, y);
    }

    public void splitPrint(GuiGraphics guiGraphics, String line, int x, int y, int maxWidth) {
        doTransform(guiGraphics, x, y);
        int scaledWidth = (int) (maxWidth * (isSmall ? SCALING : 1));
        List<FormattedCharSequence> lines = Minecraft.getInstance().font.split(Component.literal(line), scaledWidth);
        int scaledLineHeight = (int) (Minecraft.getInstance().font.lineHeight * (isSmall ? SCALING : 1));
        for (int i = 0; i < lines.size(); i++) {
            guiGraphics.drawString(getMCFont(), lines.get(i), 0, i * scaledLineHeight, 8, false);

        }
        guiGraphics.pose().popMatrix();
    }

    public void splitPrint(GuiGraphics guiGraphics, FormattedCharSequence line, int x, int y, int maxWidth) {
        splitPrint(guiGraphics, line.toString(), x, y, maxWidth);
    }

    public void print(GuiGraphics guiGraphics, FormattedCharSequence line, int x, int y, int color) {
        print(guiGraphics, line, x, y, color, false);
    }

    public void print(GuiGraphics guiGraphics, FormattedCharSequence line, int x, int y, int color, boolean shadow) {
        doTransform(guiGraphics, x, y);
        guiGraphics.drawString(getMCFont(), line, 0, 0, color, shadow);
        guiGraphics.pose().popMatrix();
    }

    public int getStringWidth(FormattedCharSequence line) {
        int width = Minecraft.getInstance().font.width(line);
        return (int)(isSmall ? width * SCALING : width);
    }

    public int getStringWidth(String line) {
        int width = Minecraft.getInstance().font.width(line);
        return (int)(isSmall ? width * SCALING : width);
    }

    private void doTransform(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        if (isSmall) {
            guiGraphics.pose().scale(SCALING, SCALING);
        }
    }

    public static net.minecraft.client.gui.Font getMCFont() {
        return Minecraft.getInstance().font;
    }
}
