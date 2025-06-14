package jeresources.jei.plant;

import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class PlantTooltip implements IRecipeSlotRichTooltipCallback {
    private final PlantEntry entry;

    public PlantTooltip(PlantEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        if (recipeSlotView.getRole() != RecipeIngredientRole.INPUT) {
            tooltip.add(getChanceString((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()));
        }
    }

    public float getChance(ItemStack itemStack) {
        PlantDrop drop = entry.getDrop(itemStack);
        return switch (drop.getDropKind()) {
            case chance -> drop.getChance();
            case weight -> (float) drop.getWeight() / entry.getTotalWeight();
            case minMax -> Float.NaN;
            default -> 0;
        };
    }

    public int[] getMinMax(ItemStack itemStack) {
        PlantDrop drop = entry.getDrop(itemStack);
        return new int[]{drop.getMinDrop(), drop.getMaxDrop()};
    }

    private Component getChanceString(ItemStack itemStack) {
        float chance = getChance(itemStack);
        String toPrint;
        if (Float.isNaN(chance)) {
            int[] minMax = this.getMinMax(itemStack);
            toPrint = minMax[0] + (minMax[0] == minMax[1] ? "" : " - " + minMax[1]);
        } else {
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";
        }
        return Component.literal(toPrint);
    }
}
