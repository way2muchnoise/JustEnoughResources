package jeresources.jei;

import jeresources.config.Settings;
import jeresources.entries.DungeonEntry;
import jeresources.reference.Resources;
import jeresources.registry.DungeonRegistry;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class JEIDungeonCategory implements IRecipeCategory
{
    private static final int X_FIRST_ITEM = 5;
    private static final int Y_FIRST_ITEM = 48;

    private static int ITEMS_PER_PAGE;
    private static int SPACING_X;
    private static int SPACING_Y;
    private static int CYCLE_TIME;

    public static void reloadSettings()
    {
        ITEMS_PER_PAGE = Settings.ITEMS_PER_COLUMN * Settings.ITEMS_PER_ROW;
        SPACING_X = 166 / Settings.ITEMS_PER_ROW;
        SPACING_Y = 80 / Settings.ITEMS_PER_COLUMN;
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
    }

    private static int lidStart = -1;
    private static int lastRecipe = -1;
    private static boolean done;

    @Override
    public String getGuiTexture()
    {
        return Resources.Gui.Jei.DUNGEON.toString();
    }

    @Override
    public String getRecipeName()
    {
        return TranslationHelper.translateToLocal("ner.dungeon.title");
    }

    @Override
    public int recipiesPerPage()
    {
        return 1;
    }

    @Override
    public void loadTransferRects()
    {
        transferRects.add(new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(5, 5, 40, 40), JEIConfig.DUNGEON, new Object()));
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results)
    {
        if (outputId.equals(JEIConfig.DUNGEON))
        {
            for (DungeonEntry entry : DungeonRegistry.getInstance().getDungeons())
                arecipes.add(new CachedDungeonChest(entry));
            lastRecipe = -1;
        } else super.loadCraftingRecipes(outputId, results);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result)
    {
        for (DungeonEntry entry : DungeonRegistry.getInstance().getDungeons(result))
            arecipes.add(new CachedDungeonChest(entry, result));
        lastRecipe = -1;
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(this.getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);

        CachedDungeonChest cachedChest = (CachedDungeonChest) arecipes.get(recipe);

        int x = X_FIRST_ITEM;
        int y = Y_FIRST_ITEM;
        for (int i = ITEMS_PER_PAGE * cachedChest.set; i < ITEMS_PER_PAGE * cachedChest.set + ITEMS_PER_PAGE; i++)
        {
            if (i >= cachedChest.getContents().length) break;
            if (cachedChest.resultIndex == i)
                GuiDraw.drawTexturedModalRect(x - 1, y - 1, 18, 238, 18, 18);
            else
                GuiDraw.drawTexturedModalRect(x - 1, y - 1, 0, 238, 18, 18);
            y += SPACING_Y;
            if (y >= Y_FIRST_ITEM + SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = Y_FIRST_ITEM;
                x += SPACING_X;
            }
        }

        RenderHelper.renderChest(15, 20, -40, 20, getLidAngle(recipe));
    }

    private float getLidAngle(int recipe)
    {
        if (recipe != lastRecipe)
        {
            done = false;
            lastRecipe = -1;
            lidStart = -1;
        }

        if (lidStart == -1) lidStart = cycleticks;

        float angle = (cycleticks - lidStart) % 80;
        if (angle > 50 || done)
        {
            done = true;
            angle = 50;
        }

        return angle;
    }

    @Override
    public void drawExtras(int recipe)
    {
        CachedDungeonChest cachedChest = (CachedDungeonChest) arecipes.get(recipe);

        Font.normal.print(TranslationHelper.translateToLocal(cachedChest.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(cachedChest.chest), 60, 20);
        if (cachedChest.lastSet > 0)
            Font.small.print(TranslationHelper.getLocalPageInfo(cachedChest.set, cachedChest.lastSet), 60, 36);

        int x = X_FIRST_ITEM + 18;
        int y = Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
        for (int i = ITEMS_PER_PAGE * cachedChest.set; i < ITEMS_PER_PAGE * cachedChest.set + ITEMS_PER_PAGE; i++)
        {
            if (i >= cachedChest.getContents().length) break;
            double chance = cachedChest.getChances()[i] * 100;
            String format = chance < 100 ? "%2.1f" : "%2.0f";
            String toPrint = String.format(format, chance).replace(',', '.') + "%";
            Font.small.print(toPrint, x, y);
            y += SPACING_Y;
            if (y >= Y_FIRST_ITEM + SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = Y_FIRST_ITEM + (8 - Settings.ITEMS_PER_COLUMN);
                x += SPACING_X;
            }
        }

        cachedChest.cycleOutputs(cycleticks, recipe);
    }

    public class CachedDungeonChest extends TemplateRecipeHandler.CachedRecipe
    {
        public int resultIndex = -1;
        public DungeonEntry chest;
        public int set, lastSet;
        private long cycleAt;

        public CachedDungeonChest(DungeonEntry chest)
        {
            this.chest = chest;
            set = 0;
            cycleAt = -1;
            lastSet = (this.getContents().length / (ITEMS_PER_PAGE + 1));
        }

        public CachedDungeonChest(DungeonEntry chest, ItemStack result)
        {
            this(chest);

            ItemStack[] content = chest.getItemStacks();
            for (int i = 0; i < content.length; i++)
            {
                if (content[i].isItemEqual(result))
                {
                    resultIndex = i;
                    set = (int) (resultIndex / ITEMS_PER_PAGE);
                }
            }
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
        public PositionedStack getResult()
        {
            return new PositionedStack(this.getContents()[set * ITEMS_PER_PAGE], X_FIRST_ITEM, Y_FIRST_ITEM);
        }

        @Override
        public List<PositionedStack> getOtherStacks()
        {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            int x = X_FIRST_ITEM;
            int y = Y_FIRST_ITEM;
            for (int i = ITEMS_PER_PAGE * set; i < ITEMS_PER_PAGE * set + ITEMS_PER_PAGE; i++)
            {
                if (i >= this.getContents().length) break;
                list.add(new PositionedStack(this.getContents()[i], x, y));
                y += SPACING_Y;
                if (y >= Y_FIRST_ITEM + SPACING_Y * Settings.ITEMS_PER_COLUMN)
                {
                    y = Y_FIRST_ITEM;
                    x += SPACING_X;
                }
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
                cycle();
                cycleAt += CYCLE_TIME;
            }
        }

        public void cycle()
        {
            if (++set > lastSet) set = 0;
        }

        public void cycleBack()
        {
            if (--set < 0) set = lastSet;
        }
    }
}
