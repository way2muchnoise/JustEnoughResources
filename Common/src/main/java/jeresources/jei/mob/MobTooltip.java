package jeresources.jei.mob;

import jeresources.api.drop.LootDrop;
import jeresources.entry.MobEntry;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MobTooltip implements IRecipeSlotTooltipCallback {
    private final MobEntry entry;

    public MobTooltip(MobEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onTooltip(@NotNull IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        LootDrop lootDrop = this.entry.getDrops().get(Integer.parseInt(recipeSlotView.getSlotName().orElse("0")));
        tooltip.add(lootDrop.toStringTextComponent());
        List<Component> list = getToolTip((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient());
        if (list != null)
            tooltip.addAll(list);
    }

    public List<Component> getToolTip(ItemStack stack) {
        for (LootDrop item : this.entry.getDrops()) {
            if (stack.is(item.item.getItem()))
                return item.getTooltipText();
            if (item.canBeCooked() && stack.is(item.smeltedItem.getItem()))
                return item.getTooltipText(true);
        }
        return null;
    }
}
