package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.entries.MobEntry;
import jeresources.gui.GuiContainerHook;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.registry.MobRegistry;
import jeresources.utils.Font;
import jeresources.utils.MobHelper;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class JEIMobCategory implements IRecipeCategory
{
    private static final int X_FIRST_ITEM = 90;
    private static final int Y_FIRST_ITEM = 42;

    private static int SPACING_Y = 90 / Settings.ITEMS_PER_COLUMN;
    private static int CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);

    public static void reloadSettings()
    {
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
    }

    private static int lastRecipe = -1;

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (MobEntry entry : MobRegistry.getInstance().getMobsThatDropItem(result))
            arecipes.add(new MobWrapper(entry));
        lastRecipe = -1;
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);

        EntityLivingBase entityLivingBase = ((MobWrapper) arecipes.get(recipe)).getMob();
        boolean normal = entityLivingBase.width <= entityLivingBase.height;
        float scale = getScale(entityLivingBase);
        float offsetX = normal ? (scale > 34.0F ? (75 - scale/2) : (75 - scale)) : 72 ;
        if (ModList.thaumcraft.isLoaded() && entityLivingBase instanceof EntityTaintacle) offsetX = 115;
        RenderHelper.renderEntity(30, 165 - (int)offsetX, scale, 150 - GuiDraw.getMousePosition().x, 150 - GuiDraw.getMousePosition().y, entityLivingBase);
    }

    private float getScale(EntityLivingBase entityLivingBase)
    {
        float width = entityLivingBase.width;
        float height = entityLivingBase.height;
        if (width <= height)
        {
            if (height < 0.8) return 70.0F;
            else if (height < 1) return 35.0F;
            else if (height < 2) return 32.0F;
            else if (height < 3) return 26.0F;
            else if (height < 4) return 20.0F;
            else return 10.0F;
        } else
        {
            if (width < 1) return 38.0F;
            else if (width < 2) return 27.0F;
            else if (width < 3) return 13.0F;
            else return 9.0F;
        }
    }

    @Override
    public void drawExtras(int recipe)
    {
        MobWrapper cachedMob = (MobWrapper) arecipes.get(recipe);

        Font.normal.print(cachedMob.mob.getMobName(), 2, 2);
        Font.normal.print(TranslationHelper.translateToLocal("ner.mob.biome"), 2, 12);
        Font.normal.print(cachedMob.mob.getLightLevel(), 2, 22);
        Font.normal.print(TranslationHelper.translateToLocal("ner.mob.exp") + ": " + MobHelper.getExpDrop(cachedMob.mob), 2, 32);

        int y = Y_FIRST_ITEM + 4;
        for (int i = cachedMob.set * Settings.ITEMS_PER_COLUMN; i < cachedMob.set * Settings.ITEMS_PER_COLUMN + Settings.ITEMS_PER_COLUMN; i++)
        {
            if (i >= cachedMob.mob.getDrops().length) break;
            Font.normal.print(cachedMob.mob.getDrops()[i].toString(), X_FIRST_ITEM + 18, y);
            y += SPACING_Y;
        }

        if (cachedMob.lastSet > 0)
            Font.normal.print(TranslationHelper.getLocalPageInfo(cachedMob.set, cachedMob.lastSet), X_FIRST_ITEM, 120);

        cachedMob.cycleOutputs(cycleticks, recipe);
    }

    @Override
    public List<String> handleTooltip(GuiRecipe gui, List<String> currenttip, int recipe)
    {
        currenttip = super.handleTooltip(gui, currenttip, recipe);
        if (isOnBiome(GuiDraw.getMousePosition(), gui, recipe))
        {
            MobWrapper cachedMob = (MobWrapper) arecipes.get(recipe);
            Collections.addAll(currenttip, cachedMob.mob.getBiomes());
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> toolTip, int recipe)
    {
        if (stack != null)
        {
            toolTip.addAll(((MobWrapper) arecipes.get(recipe)).getToolTip(stack));
        }
        return toolTip;
    }

    private boolean isOnBiome(Point mousePosition, GuiRecipe gui, int recipe)
    {
        GuiContainerHook guiContainerHook = new GuiContainerHook(gui, gui.width, gui.height);
        Point offset = gui.getRecipePosition(recipe);
        Point relMouse = new Point(mousePosition.x - guiContainerHook.getGuiLeft() - offset.x, mousePosition.y - guiContainerHook.getGuiTop() - offset.y);
        return 2 <= relMouse.x && relMouse.x < 165 && 12 <= relMouse.y && relMouse.y < 12 + 10;
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.MOB;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("ner.mob.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.JeiBackground.MOB;
    }

    @Override
    public void drawExtras(Minecraft minecraft)
    {

    }

    @Override
    public void drawAnimations(Minecraft minecraft)
    {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper)
    {

    }

}
