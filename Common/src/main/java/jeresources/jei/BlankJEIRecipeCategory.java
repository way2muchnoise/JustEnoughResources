package jeresources.jei;

import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class BlankJEIRecipeCategory<T> implements IRecipeCategory<T> {
    private final IDrawable icon;
    protected final IRecipeCategoryExtension<T> recipeCategoryExtension;

    protected BlankJEIRecipeCategory(IDrawable icon, IRecipeCategoryExtension<T> recipeCategoryExtension) {
        this.icon = icon;
        this.recipeCategoryExtension = recipeCategoryExtension;
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    public abstract @NotNull IDrawable getJERBackground();

    @Override
    public int getHeight() {
        return getJERBackground().getHeight();
    }

    @Override
    public int getWidth() {
        return getJERBackground().getWidth();
    }

    @Override
    public void draw(T recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        getJERBackground().draw(guiGraphics);
        recipeCategoryExtension.drawInfo(recipe, getWidth(), getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, T recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        recipeCategoryExtension.getTooltip(tooltip, recipe, mouseX, mouseY);
    }
}
