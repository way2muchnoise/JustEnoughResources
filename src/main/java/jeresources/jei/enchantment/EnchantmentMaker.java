package jeresources.jei.enchantment;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class EnchantmentMaker {
    public static List<EnchantmentWrapper> createRecipes(Collection<ItemStack> itemStacks) {
        List<EnchantmentWrapper> recipes = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            EnchantmentWrapper recipe = EnchantmentWrapper.create(itemStack);
            if (recipe != null)
                recipes.add(recipe);
        }
        return recipes;
    }
}
