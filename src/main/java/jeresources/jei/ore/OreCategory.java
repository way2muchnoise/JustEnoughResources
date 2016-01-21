package jeresources.jei.ore;

import jeresources.api.utils.ColorHelper;
import jeresources.api.utils.DropItem;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class OreCategory implements IRecipeCategory
{
    protected static final int X_ITEM = 17;
    protected static final int Y_ITEM = 21;
    protected static final int X_DROP_ITEM = 17;
    protected static final int Y_DROP_ITEM = 66;
    private static final int DROP_ITEM_COUNT = 8;

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.ORE;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.ore.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.ORE;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        RenderHelper.drawArrow(OreWrapper.X_OFFSPRING, OreWrapper.Y_OFFSPRING, OreWrapper.X_OFFSPRING + OreWrapper.X_AXIS_SIZE, OreWrapper.Y_OFFSPRING, ColorHelper.GRAY);
        RenderHelper.drawArrow(OreWrapper.X_OFFSPRING, OreWrapper.Y_OFFSPRING, OreWrapper.X_OFFSPRING, OreWrapper.Y_OFFSPRING - OreWrapper.Y_AXIS_SIZE, ColorHelper.GRAY);
    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, false, X_ITEM, Y_ITEM);

        for (int i = 0; i < DROP_ITEM_COUNT; i++)
            recipeLayout.getItemStacks().init(i + 1, false, X_DROP_ITEM + i * 18, Y_DROP_ITEM);

        if (recipeWrapper instanceof OreWrapper)
        {
            OreWrapper oreWrapper = (OreWrapper) recipeWrapper;
            recipeLayout.getItemStacks().addTooltipCallback(oreWrapper);
            recipeLayout.getItemStacks().setFromRecipe(0, oreWrapper.getOres());
            for (int i = 0 ; i < Math.min(DROP_ITEM_COUNT, oreWrapper.getDrops().size()); i++)
            {
                DropItem drop = oreWrapper.getDrops().get(i);
                ItemStack itemStack = drop.item.copy();
                itemStack.stackSize = Math.max(1, (int)Math.floor(drop.chance));
                recipeLayout.getItemStacks().set(i + 1, itemStack);
            }
        }
    }

}