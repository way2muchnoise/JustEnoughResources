package jeresources.jei.worldgen;

import jeresources.api.render.ColourHelper;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;

public class WorldGenCategory extends BlankRecipeCategory
{
    protected static final int X_ITEM = 5;
    protected static final int Y_ITEM = 21;
    protected static final int X_DROP_ITEM = 5;
    protected static final int Y_DROP_ITEM = 66;
    private static final int DROP_ITEM_COUNT = 8;

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.WORLD_GEN;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.worldgen.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.WORLD_GEN;
    }

    @Override
    public void drawExtras(@Nonnull Minecraft minecraft)
    {
        RenderHelper.drawLine(WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET + WorldGenWrapper.X_AXIS_SIZE, WorldGenWrapper.Y_OFFSET, ColourHelper.GRAY);
        RenderHelper.drawLine(WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET - WorldGenWrapper.Y_AXIS_SIZE, ColourHelper.GRAY);
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, false, X_ITEM, Y_ITEM);

        for (int i = 0; i < DROP_ITEM_COUNT; i++)
            recipeLayout.getItemStacks().init(i + 1, false, X_DROP_ITEM + i * 18, Y_DROP_ITEM);

        if (recipeWrapper instanceof WorldGenWrapper)
        {
            WorldGenWrapper worldGenWrapper = (WorldGenWrapper) recipeWrapper;
            recipeLayout.getItemStacks().addTooltipCallback(worldGenWrapper);
            recipeLayout.getItemStacks().set(0, worldGenWrapper.getBlock());
            for (int i = 0; i < Math.min(DROP_ITEM_COUNT, worldGenWrapper.getDrops().size()); i++)
                recipeLayout.getItemStacks().set(i + 1, worldGenWrapper.getDrops().get(i));
        }
    }

}