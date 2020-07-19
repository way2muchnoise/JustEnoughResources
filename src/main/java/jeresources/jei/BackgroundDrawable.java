package jeresources.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import jeresources.reference.Reference;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.gui.GuiUtils;

public class BackgroundDrawable implements IDrawable {
    private final int width, height;
    private final ResourceLocation resource;
    private static final int PADDING = 5;

    public BackgroundDrawable(String resource, int width, int height) {
        this.resource = new ResourceLocation(Reference.ID, resource);
        this.width = width;
        this.height = height;
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
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        RenderSystem.clearCurrentColor();
        Minecraft.getInstance().getTextureManager().bindTexture(this.resource);
        RenderSystem.pushMatrix();
        RenderSystem.multMatrix(matrixStack.getLast().getMatrix());
        GuiUtils.drawTexturedModalRect(xOffset + PADDING, yOffset + PADDING, 0, 0, this.width, this.height, 0);
        RenderSystem.popMatrix();
    }

    public ResourceLocation getResource() {
        return resource;
    }
}
