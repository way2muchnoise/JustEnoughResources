package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DungeonTooltip implements IRecipeSlotTooltipCallback {
    private final DungeonEntry entry;

    public DungeonTooltip(DungeonEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        tooltip.add(entry.getChestDrop((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()).toStringTextComponent());
    }
}
