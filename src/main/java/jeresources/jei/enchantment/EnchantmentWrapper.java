package jeresources.jei.enchantment;

import jeresources.entries.EnchantmentEntry;
import jeresources.jei.enchantment.JEIEnchantmentCategory;
import jeresources.registry.EnchantmentRegistry;
import net.minecraft.item.ItemStack;

import java.util.LinkedList;
import java.util.List;

public class EnchantmentWrapper extends TemplateRecipeHandler.CachedRecipe
{

    private ItemStack itemStack;
    private List<EnchantmentEntry> enchantments;
    public int set, lastSet;
    private long cycleAt;

    public EnchantmentWrapper(ItemStack itemStack)
    {
        this.itemStack = itemStack;
        this.enchantments = new LinkedList<EnchantmentEntry>(EnchantmentRegistry.getInstance().getEnchantments(itemStack));
        this.set = 0;
        this.lastSet = this.enchantments.size() / (JEIEnchantmentCategory.ENTRYS_PER_PAGE + 1);
        this.cycleAt = -1;
    }

    @Override
    public PositionedStack getResult()
    {
        return new PositionedStack(this.itemStack, JEIEnchantmentCategory.ITEM_X, JEIEnchantmentCategory.ITEM_Y);
    }

    public List<EnchantmentEntry> getEnchantments()
    {
        int last = set * JEIEnchantmentCategory.ENTRYS_PER_PAGE + JEIEnchantmentCategory.ENTRYS_PER_PAGE;
        if (last >= this.enchantments.size()) last = this.enchantments.size() - 1;
        return this.enchantments.subList(set * JEIEnchantmentCategory.ENTRYS_PER_PAGE, last);
    }

    public void cycleOutput(long tick)
    {
        if (cycleAt == -1)
            cycleAt = tick + JEIEnchantmentCategory.CYCLE_TIME;

        if (tick >= cycleAt)
        {
            if (++set > lastSet) set = 0;
            cycleAt += JEIEnchantmentCategory.CYCLE_TIME;
        }
    }
}
