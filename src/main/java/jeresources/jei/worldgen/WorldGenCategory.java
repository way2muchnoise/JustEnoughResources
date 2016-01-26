package jeresources.jei.worldgen;

import jeresources.api.drop.DropItem;
import jeresources.api.render.ColourHelper;
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

public class WorldGenCategory implements IRecipeCategory
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
        return JEIConfig.WORLDGEN;
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
    public void drawExtras(Minecraft minecraft)
    {
        RenderHelper.drawLine(WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET + WorldGenWrapper.X_AXIS_SIZE, WorldGenWrapper.Y_OFFSET, ColourHelper.GRAY);
        RenderHelper.drawLine(WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET - WorldGenWrapper.Y_AXIS_SIZE, ColourHelper.GRAY);
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

        if (recipeWrapper instanceof WorldGenWrapper)
        {
            WorldGenWrapper worldGenWrapper = (WorldGenWrapper) recipeWrapper;
            recipeLayout.getItemStacks().addTooltipCallback(worldGenWrapper);
            recipeLayout.getItemStacks().set(0, worldGenWrapper.getBlock());
            for (int i = 0; i < Math.min(DROP_ITEM_COUNT, worldGenWrapper.getDrops().size()); i++)
            {
                DropItem drop = worldGenWrapper.getDrops().get(i);
                ItemStack itemStack = drop.item.copy();
                itemStack.stackSize = Math.max(1, (int)Math.floor(drop.chance));
                recipeLayout.getItemStacks().set(i + 1, itemStack);
            }
        }
    }

}