package jeresources.jei.drops;

import javax.annotation.Nonnull;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public class DropsCategory implements IRecipeCategory {

	@Nonnull
	@Override
	public String getUid() {
		return JEIConfig.DROPS;
	}

	@Nonnull
	@Override
	public String getTitle() {
		return TranslationHelper.translateToLocal("jer.drops.title");
	}

	@Nonnull
	@Override
	public IDrawable getBackground() {
		return Resources.Gui.Jei.DROPS;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {

	}

	@Override
	public void drawAnimations(Minecraft minecraft) {

	}

	@Override
	public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();
		itemStacks.init(0, true, 5, 14);

		final int spacing = 16 + 1;
		final int maxSlotCount = 10;

		if (recipeWrapper instanceof DropsWrapper) {
			DropsWrapper dropsWrapper = (DropsWrapper) recipeWrapper;
			itemStacks.addTooltipCallback(dropsWrapper);

			itemStacks.set(0, dropsWrapper.getInputs());

			List<ItemStack> outputs = dropsWrapper.getOutputs();
			int slotCount = Math.min(maxSlotCount, outputs.size());
			int slotIndex = 1;
			final int xStart = 54;
			int x = xStart;
			int y = 6;
			for (int i = 0; i < slotCount; i++) {
				itemStacks.init(slotIndex, false, x, y);
				itemStacks.set(slotIndex, dropsWrapper.getItems(i, maxSlotCount));
				slotIndex++;
				if (i == 4) {
					x = xStart;
					y += spacing;
				} else {
					x += spacing;
				}
			}
		}
	}
}
