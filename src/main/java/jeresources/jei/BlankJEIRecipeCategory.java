package jeresources.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlankJEIRecipeCategory<T extends IRecipeCategoryExtension> implements IRecipeCategory<T> {
    private final IDrawable icon;

    protected BlankJEIRecipeCategory(IDrawable icon) {
        this.icon = icon;
    }

    @Nullable
    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(T recipe, IIngredients ingredients) {
        recipe.setIngredients(ingredients);
    }

    @Override
    public void draw(T recipe, PoseStack poseStack, double mouseX, double mouseY) {
        recipe.drawInfo(getBackground().getWidth(), getBackground().getHeight(), poseStack, mouseX, mouseY);
    }

    @Override
    public List<Component> getTooltipStrings(T recipe, double mouseX, double mouseY) {
        return recipe.getTooltipStrings(mouseX, mouseY);
    }
}
