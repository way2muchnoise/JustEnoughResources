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

    public BackgroundDrawable(String resource, int width, int height)
    {
        this.resource = new ResourceLocation(Reference.ID, resource);
        this.width = width;
        this.height = height;
    }

    public BackgroundDrawable(String resource)
    {
        this(resource, 166, 65);
    }

    @Override
    public int getWidth()
    {
        return this.width;
    }

    @Override
    public int getHeight()
    {
        return this.height;
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
        GuiUtils.drawTexturedModalRect(xOffset, yOffset, 5, 11, this.width, this.height, 0);
    }

    public ResourceLocation getResource()
    {
        return this.resource;
    }
}
