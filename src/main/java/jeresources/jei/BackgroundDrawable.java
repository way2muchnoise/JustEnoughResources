package jeresources.jei;

import jeresources.reference.Reference;
import mezz.jei.api.gui.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import javax.annotation.Nonnull;

public class BackgroundDrawable implements IDrawable
{
    private final int width, height;
    private final ResourceLocation resource;
    private static final int PADDING = 5;

    public BackgroundDrawable(String resource, int width, int height)
    {
        this.resource = new ResourceLocation(Reference.ID, resource);
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth()
    {
        return this.width + PADDING * 2;
    }

    @Override
    public int getHeight()
    {
        return this.height + PADDING * 2;
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft)
    {
        draw(minecraft, 0, 0);
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset)
    {
        GlStateManager.resetColor();
        minecraft.getTextureManager().bindTexture(this.resource);
        GuiUtils.drawTexturedModalRect(xOffset + PADDING, yOffset + PADDING, 0, 0, this.width, this.height, 0);
    }

    public ResourceLocation getResource()
    {
        return resource;
    }
}
