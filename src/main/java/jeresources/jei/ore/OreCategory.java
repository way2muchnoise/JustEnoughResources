package jeresources.jei.ore;

import jeresources.api.utils.ColorHelper;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class OreCategory implements IRecipeCategory
{
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
        recipeLayout.getItemStacks().init(0, false, OreWrapper.X_ITEM, OreWrapper.Y_ITEM);

        if (recipeWrapper instanceof OreWrapper)
        {
            OreWrapper oreWrapper = (OreWrapper)recipeWrapper;
            recipeLayout.getItemStacks().addTooltipCallback(oreWrapper);
            recipeLayout.getItemStacks().setFromRecipe(0, oreWrapper.getOresAndDrops());
        }
    }

}