package jeresources.jei.category;

import jeresources.config.Settings;
import jeresources.entries.EnchantmentEntry;
import jeresources.reference.Resources;
import jeresources.registry.EnchantmentRegistry;
import jeresources.utils.Font;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import java.util.LinkedList;
import java.util.List;

public class JEIEnchantmentCategory implements IRecipeCategory
{
    private static final int ITEM_X = 8;
    private static final int ITEM_Y = 6;
    private static final int ENTRYS_PER_PAGE = 11;
    private static final int ENCHANT_X = 30;
    private static final int FIRST_ENCHANT_Y = 5;
    private static final int SPACING_Y = 10;
    private static final int PAGE_X = 55;
    private static final int PAGE_Y = 120;

    private static int CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);

    public static void reloadSettings()
    {
        CYCLE_TIME = (int) (20 * Settings.CYCLE_TIME);
    }

    @Override
    public String getGuiTexture()
    {
        return Resources.Gui.Jei.ENCHANTMENT.toString();
    }

    @Override
    public String getRecipeName()
    {
        return TranslationHelper.translateToLocal("ner.enchantments.title");
    }

    @Override
    public void drawBackground(int recipe)
    {
        GL11.glColor4f(1, 1, 1, 1);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(0, 0, 5, 11, 166, 130);
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient)
    {
        if (EnchantmentRegistry.getInstance().getEnchantments(ingredient).size() > 0)
            arecipes.add(new CachedEnchantment(ingredient));
    }

    @Override
    public void drawExtras(int recipe)
    {
        CachedEnchantment cachedEnchantment = (CachedEnchantment) arecipes.get(recipe);
        int y = FIRST_ENCHANT_Y;
        for (EnchantmentEntry enchantment : cachedEnchantment.getEnchantments())
        {
            Font.normal.print(enchantment.getTranslatedWithLevels(), ENCHANT_X, y);
            y += SPACING_Y;
        }
        if (cachedEnchantment.lastSet > 0)
        {
            String toPrint = TranslationHelper.getLocalPageInfo(cachedEnchantment.set, cachedEnchantment.lastSet);
            Font.normal.print(toPrint, PAGE_X, PAGE_Y);
        }

        cachedEnchantment.cycleOutput(cycleticks);
    }

    public class CachedEnchantment extends TemplateRecipeHandler.CachedRecipe
    {

        private ItemStack itemStack;
        private List<EnchantmentEntry> enchantments;
        public int set, lastSet;
        private long cycleAt;

        public CachedEnchantment(ItemStack itemStack)
        {
            this.itemStack = itemStack;
            this.enchantments = new LinkedList<EnchantmentEntry>(EnchantmentRegistry.getInstance().getEnchantments(itemStack));
            this.set = 0;
            this.lastSet = this.enchantments.size() / (ENTRYS_PER_PAGE + 1);
            this.cycleAt = -1;
        }

        @Override
        public PositionedStack getResult()
        {
            return new PositionedStack(this.itemStack, ITEM_X, ITEM_Y);
        }

        public List<EnchantmentEntry> getEnchantments()
        {
            int last = set * ENTRYS_PER_PAGE + ENTRYS_PER_PAGE;
            if (last >= this.enchantments.size()) last = this.enchantments.size() - 1;
            return this.enchantments.subList(set * ENTRYS_PER_PAGE, last);
        }

        public void cycleOutput(long tick)
        {
            if (cycleAt == -1)
                cycleAt = tick + CYCLE_TIME;

            if (tick >= cycleAt)
            {
                if (++set > lastSet) set = 0;
                cycleAt += CYCLE_TIME;
            }
        }
    }
}
