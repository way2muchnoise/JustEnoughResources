package jeresources.entry;

import jeresources.util.TranslationHelper;
import net.minecraft.enchantment.Enchantment;

public class EnchantmentEntry {
    private Enchantment enchantment;

    public EnchantmentEntry(Enchantment enchantment) {
        this.enchantment = enchantment;
    }

    public String getTranslatedWithLevels() {
        String s = this.enchantment.getDisplayName(1).getString();
        if (this.enchantment.getMinLevel() != this.enchantment.getMaxLevel())
            s += "-" + TranslationHelper.translateAndFormat("enchantment.level." + this.enchantment.getMaxLevel());
        return s;
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }
}
