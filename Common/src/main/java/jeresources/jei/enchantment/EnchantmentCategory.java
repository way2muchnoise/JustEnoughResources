package jeresources.jei.enchantment;

import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

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
    public @NotNull IDrawable getJERBackground() {
        return Resources.Gui.Jei.ENCHANTMENT;
    }

    @Override
    public @NotNull IRecipeType<EnchantmentWrapper> getRecipeType() {
        return JEIConfig.ENCHANTMENT_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull EnchantmentWrapper recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, ITEM_X, ITEM_Y).add(recipe.itemStack);
    }

    @Override
    public void draw(EnchantmentWrapper recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        getJERBackground().draw(guiGraphics);
        recipe.drawInfo(recipe, getWidth(), getHeight(), guiGraphics, mouseX, mouseY);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, EnchantmentWrapper recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        recipe.getTooltip(tooltip, recipe, mouseX, mouseY);
    }
}
