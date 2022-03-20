package jeresources.jei.enchantment;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.jei.dungeon.DungeonWrapper;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EnchantmentCategory extends BlankJEIRecipeCategory<EnchantmentWrapper> {
    private static final int ITEM_X = 13;
    private static final int ITEM_Y = 12;

    public EnchantmentCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 0, 16, 16));
    }

    @Override
    public @NotNull ResourceLocation getUid() {
        return JEIConfig.ENCHANTMENT;
    }

    @Override
    public @NotNull Component getTitle() {
        return new TranslatableComponent("jer.enchantments.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.ENCHANTMENT;
    }

    @Override
    public @NotNull Class<? extends EnchantmentWrapper> getRecipeClass() {
        return EnchantmentWrapper.class;
    }

    @Override
    public @NotNull RecipeType<EnchantmentWrapper> getRecipeType() {
        return JEIConfig.ENCHANTMENT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull EnchantmentWrapper recipeWrapper, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, ITEM_X, ITEM_Y).addItemStack(recipeWrapper.itemStack);
    }
}
