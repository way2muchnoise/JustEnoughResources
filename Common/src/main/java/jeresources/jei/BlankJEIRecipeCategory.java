package jeresources.jei;

import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class BlankJEIRecipeCategory<T> implements IRecipeCategory<T> {
    private final IDrawable icon;
    protected final IRecipeCategoryExtension<T> recipeCategoryExtension;

    protected BlankJEIRecipeCategory(IDrawable icon, IRecipeCategoryExtension<T> recipeCategoryExtension) {
        this.icon = icon;
        this.recipeCategoryExtension = recipeCategoryExtension;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void draw(T recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        recipeCategoryExtension.drawInfo(recipe, getBackground().getWidth(), getBackground().getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(T recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipeCategoryExtension.getTooltipStrings(recipe, mouseX, mouseY);
    }
}
