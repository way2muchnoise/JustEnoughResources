package jeresources.jei;

import jeresources.api.utils.DropItem;
import jeresources.config.Settings;
import jeresources.entries.MobEntry;
import jeresources.gui.GuiContainerHook;
import jeresources.reference.Resources;
import jeresources.registry.MobRegistry;
import jeresources.utils.Font;
import jeresources.utils.MobHelper;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
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
    public String getGuiTexture()
    {
        return Resources.Gui.Jei.MOB.toString();
    }

    @Override
    public String getRecipeName()
    {
        return TranslationHelper.translateToLocal("ner.mob.title");
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(62, 72, 28, 18), JEIConfig.MOB, new Object()));
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        if (ingredient.getItem() instanceof ItemSword)
        {
            for (MobEntry entry : MobRegistry.getInstance().getMobs())
                arecipes.add(new CachedMob(entry));
            lastRecipe = -1;
        }
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals(JEIConfig.MOB))
        {
            for (MobEntry entry : MobRegistry.getInstance().getMobs())
                arecipes.add(new CachedMob(entry));
            lastRecipe = -1;
        } else super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (MobEntry entry : MobRegistry.getInstance().getMobsThatDropItem(result))
            arecipes.add(new CachedMob(entry));
        lastRecipe = -1;
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);

        EntityLivingBase entityLivingBase = ((CachedMob) arecipes.get(recipe)).getMob();
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
        CachedMob cachedMob = (CachedMob) arecipes.get(recipe);

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
            CachedMob cachedMob = (CachedMob) arecipes.get(recipe);
            Collections.addAll(currenttip, cachedMob.mob.getBiomes());
        }
        return currenttip;
    }

    @Override
    public List<String> handleItemTooltip(GuiRecipe gui, ItemStack stack, List<String> toolTip, int recipe)
    {
        if (stack != null)
        {
            toolTip.addAll(((CachedMob) arecipes.get(recipe)).getToolTip(stack));
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

    public class CachedMob extends TemplateRecipeHandler.CachedRecipe
    {
        public MobEntry mob;
        public int set, lastSet;
        private long cycleAt;

        public CachedMob(MobEntry mob)
        {
            this.mob = mob;
            this.set = 0;
            this.lastSet = (mob.getDrops().length / (Settings.ITEMS_PER_COLUMN + 1));
            cycleAt = -1;
        }

        public EntityLivingBase getMob()
        {
            return this.mob.getEntity();
        }

        @Override
        public PositionedStack getResult()
        {
            if (mob.getDrops().length == 0) return null;
            return new PositionedStack(mob.getDrops()[set * Settings.ITEMS_PER_COLUMN].item, X_FIRST_ITEM, Y_FIRST_ITEM);
        }

        @Override
        public List<PositionedStack> getOtherStacks()
        {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            int y = Y_FIRST_ITEM;
            for (int i = set * Settings.ITEMS_PER_COLUMN; i < set * Settings.ITEMS_PER_COLUMN + Settings.ITEMS_PER_COLUMN; i++)
            {
                if (i >= mob.getDrops().length) break;
                list.add(new PositionedStack(mob.getDrops()[i].item, X_FIRST_ITEM, y));
                y += SPACING_Y;
            }
            if (list.size() > 0) list.remove(0);
            return list;
        }

        public void cycleOutputs(long tick, int recipe)
        {
            if (cycleAt == -1 || recipe != lastRecipe)
            {
                lastRecipe = recipe;
                cycleAt = tick + CYCLE_TIME;
                return;
            }

            if (tick >= cycleAt)
            {
                if (++set > lastSet) set = 0;
                cycleAt += CYCLE_TIME;
            }
        }

        public List<String> getToolTip(ItemStack stack)
        {
            for (DropItem item : mob.getDrops())
            {
                if (item.item.isItemEqual(stack)) return item.conditionals;
            }
            return new ArrayList<String>();
        }
    }
}
