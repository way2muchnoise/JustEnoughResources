package jeresources.jei.enchantment;

import mezz.jei.api.IItemRegistry;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EnchantmentMaker {
	public static List<EnchantmentWrapper> createRecipes(IItemRegistry itemRegistry)
	{
		List<EnchantmentWrapper> recipes = new ArrayList<>();
		for (ItemStack itemStack : itemRegistry.getItemList())
		{
			EnchantmentWrapper recipe = EnchantmentWrapper.create(itemStack);
			if (recipe != null)
				recipes.add(recipe);
		}
		return recipes;
	}
}
