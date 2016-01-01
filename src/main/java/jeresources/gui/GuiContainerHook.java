package jeresources.gui;

import net.minecraft.client.gui.inventory.GuiContainer;

public class GuiContainerHook extends GuiContainer
{
    public GuiContainerHook(GuiContainer container, int width, int height)
    {
        super(container.inventorySlots);
        this.guiLeft = (width - this.xSize) / 2;
        this.guiTop = (height - this.ySize) / 2;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {

    }

    public int getGuiTop()
    {
        return this.guiTop;
    }

    public int getGuiLeft()
    {
        return this.guiLeft;
    }
}
