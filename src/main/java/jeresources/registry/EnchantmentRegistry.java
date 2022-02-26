package jeresources.registry;

import jeresources.entry.EnchantmentEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class EnchantmentRegistry {
    private Set<EnchantmentEntry> enchantments;
    private static EnchantmentRegistry instance;

    public static EnchantmentRegistry getInstance() {
        if (instance == null)
            return instance = new EnchantmentRegistry();
        return instance;
    }

    public EnchantmentRegistry() {
        enchantments = new HashSet<>();
        for (Enchantment enchantment : getEnchants())
            if (enchantment != null) enchantments.add(new EnchantmentEntry(enchantment));
    }

    public Set<EnchantmentEntry> getEnchantments(ItemStack itemStack) {
        Set<EnchantmentEntry> set = new HashSet<>();
        for (EnchantmentEntry enchantmentEntry : enchantments) {
            Enchantment enchantment = enchantmentEntry.getEnchantment();
            if (itemStack.getItem() == Items.BOOK && enchantment.isAllowedOnBooks())
                set.add(enchantmentEntry);
            else if (enchantment.canEnchant(itemStack) && EnchantmentHelper.isEnchantmentCompatible(EnchantmentHelper.getEnchantments(itemStack).keySet(), enchantment))
                set.add(enchantmentEntry);
        }
        return set;
    }

    private void excludeFormRegistry(Enchantment enchantment) {
        enchantments.removeIf(enchantmentEntry -> enchantmentEntry.getEnchantment().getRegistryName().toString().equals(enchantment.getRegistryName().toString()));
    }

    private void excludeFormRegistry(String sEnchantment) {
        for (Enchantment enchantment : getEnchants())
            if (enchantment != null && enchantment.getRegistryName().toString().toLowerCase().contains(sEnchantment.toLowerCase()))
                excludeFormRegistry(enchantment);
    }

    public void removeAll(String[] excludedEnchants) {
        for (String enchant : excludedEnchants)
            excludeFormRegistry(enchant);
    }

    private static Iterable<Enchantment> getEnchants() {
        return ForgeRegistries.ENCHANTMENTS;
    }
}
