package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.world.item.ItemStack;


public class DungeonTooltip implements IRecipeSlotRichTooltipCallback {
    private final DungeonEntry entry;

    public DungeonTooltip(DungeonEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        tooltip.add(entry.getChestDrop((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()).toStringTextComponent());
    }
}
