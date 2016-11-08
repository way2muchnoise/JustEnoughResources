package jeresources.jei.enchantment;

import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EnchantmentCategory extends BlankRecipeCategory<EnchantmentWrapper>
{
    private static final int ITEM_X = 12;
    private static final int ITEM_Y = 11;

    private IDrawable icon;

    public EnchantmentCategory() {
        icon = JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 0, 16, 16);
    }

    @Nonnull
    @Override
    public String getUid()
    {
        return JEIConfig.ENCHANTMENT;
    }

    @Nonnull
    @Override
    public String getTitle()
    {
        return TranslationHelper.translateToLocal("jer.enchantments.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground()
    {
        return Resources.Gui.Jei.ENCHANTMENT;
    }

    @Nullable
    @Override
    public IDrawable getIcon()
    {
        return icon;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull EnchantmentWrapper recipeWrapper)
    {
        recipeLayout.getItemStacks().init(0, true, ITEM_X, ITEM_Y);

        recipeLayout.getItemStacks().setFromRecipe(0, recipeWrapper.getInputs());
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull EnchantmentWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, ITEM_X, ITEM_Y);

        recipeLayout.getItemStacks().set(0, ingredients.getInputs(ItemStack.class).get(0));
    }
}
