package jeresources.jei.drops;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;

import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.BlankRecipeWrapper;

public class DropsWrapper extends BlankRecipeWrapper implements ITooltipCallback<ItemStack> {

	private static final String averageDropsString = TranslationHelper.translateToLocal("jer.drops.tooltip.average");
	private final List<ItemStack> input;
	private final Map<ItemStack, Float> outputs;
	private final List<ItemStack> outputList;

	public DropsWrapper(ItemStack input, Map<ItemStack, Float> outputs) {
		this.input = Collections.singletonList(input);
		this.outputs = outputs;

		Comparator<Map.Entry<ItemStack, Float>> comparator = new Comparator<Map.Entry<ItemStack, Float>>() {
			@Override
			public int compare(Map.Entry<ItemStack, Float> o1, Map.Entry<ItemStack, Float> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		};

		List<Map.Entry<ItemStack, Float>> sortedOutputs = new ArrayList<>();
		sortedOutputs.addAll(outputs.entrySet());
		Collections.sort(sortedOutputs, comparator);
		this.outputList = new ArrayList<>();
		for (Map.Entry<ItemStack, Float> sortedOutput : sortedOutputs) {
			ItemStack stack = sortedOutput.getKey();
			stack.stackSize = Math.max(1, Math.round(sortedOutput.getValue()));
			this.outputList.add(stack);
		}
	}

	@Override
	public List<ItemStack> getInputs() {
		return input;
	}

	@Override
	public List<ItemStack> getOutputs() {
		return outputList;
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
		Float chance = outputs.get(ingredient);
		if (chance != null && chance > 0) {
			String chanceString;
			if (chance >= 0.995f) {
				chanceString = String.format("%.2f", chance);
			} else {
				chanceString = String.format("%.2f%%", chance * 100f);
			}
			tooltip.add(String.format(averageDropsString, chanceString));
		}
	}

	public List<ItemStack> getItems(int slot, int slotCount)
	{
		List<ItemStack> items = new ArrayList<>();
		for (int i = slot; i < outputList.size(); i += slotCount) {
			items.add(outputList.get(i));
		}
		return items;
	}
}
