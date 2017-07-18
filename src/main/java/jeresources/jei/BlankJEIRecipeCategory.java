package jeresources.jei;

import jeresources.reference.Reference;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nullable;

public abstract class BlankJEIRecipeCategory<T extends IRecipeWrapper> implements IRecipeCategory<T> {
    private final IDrawable icon;

    protected BlankJEIRecipeCategory(IDrawable icon) {
        this.icon = icon;
    }

    @Override
    public String getModName() {
        return Reference.NAME;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
