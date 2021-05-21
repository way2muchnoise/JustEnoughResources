package jeresources.jei.enchantment;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.TranslationHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class EnchantmentCategory extends BlankJEIRecipeCategory<EnchantmentWrapper> {
    private static final int ITEM_X = 12;
    private static final int ITEM_Y = 11;

    public EnchantmentCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 0, 16, 16));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return JEIConfig.ENCHANTMENT;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return TranslationHelper.translateAndFormat("jer.enchantments.title");
    }

    @Nonnull
    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("jer.enchantments.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return Resources.Gui.Jei.ENCHANTMENT;
    }

    @Override
    public Class<? extends EnchantmentWrapper> getRecipeClass() {
        return EnchantmentWrapper.class;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull EnchantmentWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, ITEM_X, ITEM_Y);

        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
    }
}
