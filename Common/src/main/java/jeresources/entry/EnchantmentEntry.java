package jeresources.entry;

import jeresources.util.TranslationHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentEntry {
    private Holder<Enchantment> enchantment;

    public EnchantmentEntry(Holder<Enchantment> enchantment) {
        this.enchantment = enchantment;
    }

    public String getTranslatedWithLevels() {
        String s = Enchantment.getFullname(this.enchantment, 1).getString();
        if (this.enchantment.value().getMinLevel() != this.enchantment.value().getMaxLevel())
            s += "-" + TranslationHelper.translateAndFormat("enchantment.level." + this.enchantment.value().getMaxLevel());
        return s;
    }

    public Enchantment getEnchantment() {
        return enchantment.value();
    }

    public Holder<Enchantment> getEnchantmentHolder() {
        return enchantment;
    }

    public HolderSet<Item> getSupportedItems() {
        return enchantment.value().getSupportedItems();
    }
}
