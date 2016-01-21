package jeresources.jei.drops;

import javax.annotation.Nonnull;

import jeresources.jei.JEIConfig;
import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class DropsHandler implements IRecipeHandler<DropsWrapper> {
	@Nonnull
	@Override
	public Class<DropsWrapper> getRecipeClass() {
		return DropsWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid() {
		return JEIConfig.DROPS;
	}

	@Nonnull
	@Override
	public IRecipeWrapper getRecipeWrapper(@Nonnull DropsWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(@Nonnull DropsWrapper recipe) {
		return true;
	}
}
