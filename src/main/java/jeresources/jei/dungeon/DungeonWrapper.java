package jeresources.jei.dungeon;

import jeresources.config.Settings;
import jeresources.entries.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.utils.CollectionHelper;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DungeonWrapper implements IRecipeWrapper
{
    public DungeonEntry chest;
    public int set, lastSet;

    public DungeonWrapper(DungeonEntry chest)
    {
        this.chest = chest;
        set = 0;
        lastSet = (this.getContents().length / (DungeonCategory.ITEMS_PER_PAGE + 1));
    }

    public ItemStack[] getContents()
    {
        return chest.getItemStacks();
    }

    public Float[] getChances()
    {
        return chest.getChances();
    }


    @Override
    public List getInputs()
    {
        return null;
    }

    @Override
    public List getOutputs()
    {
        return CollectionHelper.create(this.chest.getItemStacks());
    }

    public List<ItemStack> getItems()
    {
        return CollectionHelper.create(this.chest.getItemStacks());
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        Font.normal.print(TranslationHelper.translateToLocal(this.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(this.chest), 60, 20);
        if (this.lastSet > 0)
            Font.small.print(TranslationHelper.getLocalPageInfo(this.set, this.lastSet), 60, 36);

        int x = DungeonCategory.X_FIRST_ITEM + 18;
        int y = DungeonCategory.Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
        for (int i = DungeonCategory.ITEMS_PER_PAGE * this.set; i < DungeonCategory.ITEMS_PER_PAGE * this.set + DungeonCategory.ITEMS_PER_PAGE; i++)
        {
            if (i >= this.getContents().length) break;
            double chance = this.getChances()[i] * 100;
            String format = chance < 100 ? "%2.1f" : "%2.0f";
            String toPrint = String.format(format, chance).replace(',', '.') + "%";
            Font.small.print(toPrint, x, y);
            y += DungeonCategory.SPACING_Y;
            if (y >= DungeonCategory.Y_FIRST_ITEM + DungeonCategory.SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = DungeonCategory.Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
                x += DungeonCategory.SPACING_X;
            }
        }
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        RenderHelper.renderChest(15, 20, -40, 20, getLidAngle());
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        return null;
    }

    private boolean done;
    private int lidStart;

    private float getLidAngle()
    {

        float angle = (((int) System.currentTimeMillis()/100) - lidStart) % 80;
        if (angle > 50 || done)
        {
            done = true;
            angle = 50;
        }

        return angle;
    }

    public void resetLid()
    {
        lidStart = (int) System.currentTimeMillis()/100;
    }
}
