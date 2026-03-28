package jeresources.jei;

import jeresources.reference.Reference;
import jeresources.util.RenderHelper;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class BackgroundDrawable implements IDrawable {
    private final int width, height, u, v;
    private final Identifier resource;
    private static final int PADDING = 5;

    public BackgroundDrawable(String resource, int width, int height) {
        this(resource, 0, 0, width, height);
    }

    public BackgroundDrawable(String resource, int u, int v, int width, int height) {
        this.resource = Identifier.fromNamespaceAndPath(Reference.ID, resource);
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
    }

    @Override
    public int getWidth() {
        return this.width + PADDING * 2;
    }

    @Override
    public int getHeight() {
        return this.height + PADDING * 2;
    }

    @Override
    public void draw(@NotNull GuiGraphics guiGraphics, int xOffset, int yOffset) {
        RenderHelper.drawTexturedModalRect(guiGraphics, this.resource, xOffset + PADDING, yOffset + PADDING, u, v, this.width, this.height);
    }

    public Identifier getResource() {
        return resource;
    }
}
