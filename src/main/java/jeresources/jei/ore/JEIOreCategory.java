package jeresources.jei.ore;

import jeresources.api.utils.ColorHelper;
import jeresources.api.utils.conditionals.Conditional;
import jeresources.config.Settings;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;

public class JEIOreCategory implements IRecipeCategory
{
    public static final int X_OFFSPRING = 59;
    public static final int Y_OFFSPRING = 52;
    public static final int X_AXIS_SIZE = 90;
    public static final int Y_AXIS_SIZE = 40;
    public static final int X_ITEM = 7;
    public static final int Y_ITEM = 5;

    private static int CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);

    public static void reloadSettings()
    {
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
    }

    /*
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
    {
        currenttip = super.handleTooltip(gui, currenttip, recipe);

        if (GuiContainerManager.shouldShowTooltip(gui) && currenttip.size() == 0)
        {
            Point offset = gui.getRecipePosition(recipe);
            Point pos = GuiDraw.getMousePosition();
            Point relMouse = new Point(pos.x - gui.guiLeft - offset.x, pos.y - gui.guiTop - offset.y);
            // Check if we are inside the coordinate system
            if (relMouse.x > X_OFFSPRING && relMouse.x < X_OFFSPRING + X_AXIS_SIZE &&
                relMouse.y > Y_OFFSPRING - Y_AXIS_SIZE && relMouse.y < Y_OFFSPRING)
            {
                OreWrapper cachedOre = (OreWrapper) arecipes.get(recipe);
                float[] chances = cachedOre.oreMatchEntry.getChances();
                double space = X_AXIS_SIZE / (chances.length * 1D);
                // Calculate the hovered over y value
                int index = (int) ((relMouse.x - X_OFFSPRING) / space);
                int yValue = Math.max(0, index + cachedOre.oreMatchEntry.getMinY() - Settings.EXTRA_RANGE + 1);
                if (index >= 0 && index < chances.length)
                    currenttip.add("Y: " + yValue + String.format(" (%.2f%%)", chances[index] * 100));
            }
        }
        return currenttip;
    }

    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> currenttip, int recipe)
    {
        OreWrapper cachedOre = (OreWrapper) arecipes.get(recipe);
        if (stack != null && cachedOre.contains(stack))
        {
            if (cachedOre.oreMatchEntry.isSilkTouchNeeded(stack))
                currenttip.add(Conditional.silkTouch.toString());
            if (gui.isMouseOver(cachedOre.getResult(), recipe))
                currenttip.addAll(cachedOre.getRestrictions());
        }
        return currenttip;
    }
    */

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
        return Resources.Gui.JeiBackground.ORE;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {
        RenderHelper.drawArrow(X_OFFSPRING, Y_OFFSPRING, X_OFFSPRING + X_AXIS_SIZE, Y_OFFSPRING, ColorHelper.GRAY);
        RenderHelper.drawArrow(X_OFFSPRING, Y_OFFSPRING, X_OFFSPRING, Y_OFFSPRING - Y_AXIS_SIZE, ColorHelper.GRAY);
    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, false, X_ITEM, Y_ITEM);

        if (recipeWrapper instanceof OreWrapper)
        {
            OreWrapper oreWrapper = (OreWrapper)recipeWrapper;
            recipeLayout.getItemStacks().set(0, oreWrapper.getOresAndDrops());
        }
    }

}