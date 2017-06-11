package jeresources.jei;

import jeresources.reference.Reference;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

public abstract class BlankJEIRecipeCategory<T extends IRecipeWrapper> extends BlankRecipeCategory<T> {
    @Override
    public String getModName() {
        return Reference.NAME;
    }
}
