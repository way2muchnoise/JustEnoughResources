package jeresources.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import jeresources.jei.drops.DropsWrapper;

public class DropsRegistry
{
	private static final List<DropsWrapper> drops = new ArrayList<>();

	public static void registerDrops(ItemStack itemStack, Map<ItemStack, Float> outputs) {
		if (outputs.size() == 0)
		{
			return;
		}

		// skip blocks that only drop themselves
		if (outputs.size() == 1)
		{
			Map.Entry<ItemStack, Float> outputEntry = outputs.entrySet().iterator().next();
			if (outputEntry.getKey().isItemEqual(itemStack)) {
				Float chanceOfSelf = outputEntry.getValue();
				if (chanceOfSelf != null && Math.abs(chanceOfSelf - 1.0f) < 0.01f) {
					return;
				}
			}
		}

		DropsWrapper dropsWrapper = new DropsWrapper(itemStack, outputs);
		drops.add(dropsWrapper);
	}

	public static List<DropsWrapper> getDrops() {
		return drops;
	}
}
