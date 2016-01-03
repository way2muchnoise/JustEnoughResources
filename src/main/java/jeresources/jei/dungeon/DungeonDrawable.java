package jeresources.jei.dungeon;

import jeresources.config.Settings;
import jeresources.entries.DungeonEntry;
import jeresources.jei.BackgroundDrawable;
import jeresources.reference.Textures;
import jeresources.utils.CollectionHelper;
import jeresources.utils.RenderHelper;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.List;

public class DungeonDrawable extends BackgroundDrawable
{
    private int slotCount, currentPage, current, last;
    private boolean animate;

    public DungeonDrawable()
    {
        super(Textures.Gui.Jei.DUNGEON, 166, 130);
        this.slotCount = 0;
        this.currentPage = 0;
        this.current = 0;
    }

    public void setSlots(int count)
    {
        this.slotCount = count;
    }

    public void setCurrentPage(int page, int current, int last)
    {
        this.currentPage = page;
        this.last = last;
        this.current = current;
    }

    @Override
    public void draw(@Nonnull Minecraft minecraft, int xOffset, int yOffset)
    {
        super.draw(minecraft, xOffset, yOffset);

        int x = JEIDungeonCategory.X_FIRST_ITEM;
        int y = JEIDungeonCategory.Y_FIRST_ITEM;
        for (int i = JEIDungeonCategory.ITEMS_PER_PAGE * this.currentPage; i < JEIDungeonCategory.ITEMS_PER_PAGE * this.currentPage + JEIDungeonCategory.ITEMS_PER_PAGE; i++)
        {
            if (i >= last) break;
            if (current == i)
                minecraft.currentScreen.drawTexturedModalRect(x - 1, y - 1, 18, 238, 18, 18);
            else
                minecraft.currentScreen.drawTexturedModalRect(x - 1, y - 1, 18, 238, 18, 18);
            y += JEIDungeonCategory.SPACING_Y;
            if (y >= JEIDungeonCategory.Y_FIRST_ITEM + JEIDungeonCategory.SPACING_Y * Settings.ITEMS_PER_COLUMN)
            {
                y = JEIDungeonCategory.Y_FIRST_ITEM;
                x += JEIDungeonCategory.SPACING_X;
            }
        }

        //RenderHelper.renderChest(15, 20, -40, 20, getLidAngle(recipe));
    }

    private static int lidStart = -1;
    private static int lastRecipe = -1;
    private static boolean done;

    /*private float getLidAngle(int recipe)
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
    }*/

    public void doAnimation(boolean animate)
    {

    }

    public static class DungeonWrapper implements IRecipeWrapper
    {
        public int resultIndex = -1;
        public DungeonEntry chest;
        public int set, lastSet;
        private long cycleAt;

        public DungeonWrapper(DungeonEntry chest)
        {
            this.chest = chest;
            set = 0;
            cycleAt = -1;
            lastSet = (this.getContents().length / (JEIDungeonCategory.ITEMS_PER_PAGE + 1));
        }

        public DungeonWrapper(DungeonEntry chest, ItemStack result)
        {
            this(chest);

            ItemStack[] content = chest.getItemStacks();
            for (int i = 0; i < content.length; i++)
            {
                if (content[i].isItemEqual(result))
                {
                    resultIndex = i;
                    set = (resultIndex / JEIDungeonCategory.ITEMS_PER_PAGE);
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
            return new PositionedStack(this.getContents()[set * JEIDungeonCategory.ITEMS_PER_PAGE], JEIDungeonCategory.X_FIRST_ITEM, JEIDungeonCategory.Y_FIRST_ITEM);
        }

        @Override
        public List<PositionedStack> getOtherStacks()
        {
            List<PositionedStack> list = new ArrayList<PositionedStack>();
            int x = JEIDungeonCategory.X_FIRST_ITEM;
            int y = JEIDungeonCategory.Y_FIRST_ITEM;
            for (int i = JEIDungeonCategory.ITEMS_PER_PAGE * set; i < JEIDungeonCategory.ITEMS_PER_PAGE * set + JEIDungeonCategory.ITEMS_PER_PAGE; i++)
            {
                if (i >= this.getContents().length) break;
                list.add(new PositionedStack(this.getContents()[i], x, y));
                y += JEIDungeonCategory.SPACING_Y;
                if (y >= JEIDungeonCategory.Y_FIRST_ITEM + JEIDungeonCategory.SPACING_Y * Settings.ITEMS_PER_COLUMN)
                {
                    y = JEIDungeonCategory.Y_FIRST_ITEM;
                    x += JEIDungeonCategory.SPACING_X;
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
                cycleAt = tick + JEIDungeonCategory.CYCLE_TIME;
                return;
            }

            if (tick >= cycleAt)
            {
                cycle();
                cycleAt += JEIDungeonCategory.CYCLE_TIME;
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

        }

        @Override
        public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
        {
            RenderHelper.renderChest(15, 20, -40, 20, getLidAngle());
        }

        private static int lidStart = -1;
        private static boolean done;

        private float getLidAngle()
        {
            if (lidStart == -1) lidStart = cycleticks;

            float angle = (cycleticks - lidStart) % 80;
            if (angle > 50 || done)
            {
                done = true;
                angle = 50;
            }

            return angle;
        }
    }
}
