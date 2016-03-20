package jeresources.entry;

import jeresources.util.TranslationHelper;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentEntry
{
    private Enchantment enchantment;

    public EnchantmentEntry(Enchantment enchantment)
    {
        this.enchantment = enchantment;
    }

    public String getTranslatedWithLevels()
    {
        String s = this.enchantment.getTranslatedName(1);
        if (this.enchantment.getMinLevel() != this.enchantment.getMaxLevel())
            s += "-" + TranslationHelper.translateToLocal("enchantment.level." + this.enchantment.getMaxLevel());
        return s;
    }

    public Enchantment getEnchantment()
    {
        return enchantment;
    }
}
