package jeresources.jei.mob;

import jeresources.api.drop.LootDrop;
import jeresources.entry.MobEntry;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotRichTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

// No more crashes due to invalid indices. index >= 0 && index < drops.size()
//
// A reliable approach is to use the item ID. Searching by slot number, on the other hand, 
// frequently leads to errors and—depending on the third-party mods being used—requires corresponding adjustments. 
// Item IDs, by contrast, are always a safe choice.
//
// Future-Feature:
// Using `instanceof ItemStack stack` also prevents a potential `NullPointerException` or `ClassCastException` in the event that JEI displays a different type of ingredient (e.g., a liquid).

// That's an custom code. Please test it before release.

public class MobTooltip implements IRecipeSlotRichTooltipCallback {
    private final MobEntry entry;

    public MobTooltip(MobEntry entry) {
        this.entry = entry;
    }

    @Override
    public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
        if (recipeSlotView.isEmpty()) return;

        Optional<ITypedIngredient<?>> displayed = recipeSlotView.getDisplayedIngredient();
        if (displayed.isEmpty() || !(displayed.get().getIngredient() instanceof ItemStack stack)) {
            return;
        }

        LootDrop lootDrop = findMatchingDrop(recipeSlotView, stack);
        if (lootDrop != null) {
            tooltip.add(lootDrop.toStringTextComponent());
        }

        List<Component> list = getToolTip(stack);
        if (list != null) {
            tooltip.addAll(list);
        }
    }

    private LootDrop findMatchingDrop(IRecipeSlotView recipeSlotView, ItemStack stack) {
        List<LootDrop> drops = this.entry.getDrops();

        // 1st attempt: Directly via the slot index, provided it is valid and the item ID matches.
        try {
            int index = Integer.parseInt(recipeSlotView.getSlotName().orElse("-1"));
            if (index >= 0 && index < drops.size()) {
                LootDrop candidate = drops.get(index);
                if (matchesStack(candidate, stack)) {
                    return candidate;
                }
            }
        } catch (NumberFormatException ignored) {
            // If slotName is not a number, proceed directly to the item search.
        }

        // 2nd attempt (fallback): Search via item ID (including processed variants).
        for (LootDrop drop : drops) {
            if (matchesStack(drop, stack)) {
                return drop;
            }
        }

        return null;
    }

    private boolean matchesStack(LootDrop drop, ItemStack stack) {
        if (stack.is(drop.item.getItem())) {
            return true;
        }
        return drop.canBeCooked() && stack.is(drop.smeltedItem.getItem());
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

