package jeresources.jei.enchantment;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class EnchantmentCategory extends BlankJEIRecipeCategory<EnchantmentWrapper> {
    private static final int ITEM_X = 13;
    private static final int ITEM_Y = 12;

    public EnchantmentCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 0, 16, 16), null);
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.enchantments.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.ENCHANTMENT;
    }

    @Override
    public @NotNull RecipeType<EnchantmentWrapper> getRecipeType() {
        return JEIConfig.ENCHANTMENT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull EnchantmentWrapper recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, ITEM_X, ITEM_Y).addItemStack(recipe.itemStack);
    }

    @Override
    public void draw(EnchantmentWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        recipe.drawInfo(recipe, getBackground().getWidth(), getBackground().getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(EnchantmentWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipe.getTooltipStrings(recipe, mouseX, mouseY);
    }
}
