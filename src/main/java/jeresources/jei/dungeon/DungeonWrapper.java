package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class DungeonWrapper implements IRecipeCategoryExtension, ITooltipCallback<ItemStack> {
    public final DungeonEntry chest;

    public DungeonWrapper(DungeonEntry chest) {
        this.chest = chest;
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.chest.getItemStacks());
    }

    public int amountOfItems(IFocus<ItemStack> focus) {
        return this.chest.getItemStacks(focus).size();
    }

    public List<ItemStack> getItems(IFocus<ItemStack> focus, int slot, int slots) {
        List<ItemStack> list = this.chest.getItemStacks(focus).subList(slot, slot + 1);
        for (int n = 1; n < (amountOfItems(focus) / slots) + 1; n++)
            list.add(this.amountOfItems(focus) <= slot + slots * n ? null : this.chest.getItemStacks(focus).get(slot + slots * n));
        list.removeIf(Objects::isNull);
        return list;
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, double mouseX, double mouseY) {
        RenderHelper.renderChest(15, 20, -40, 20, getLidAngle());
        Font.normal.print(TranslationHelper.translateAndFormat(this.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(this.chest), 60, 20);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
        tooltip.add(this.chest.getChestDrop(ingredient).toString());
    }

    private boolean done;
    private int lidStart;

    private float getLidAngle() {

        float angle = (((int) System.currentTimeMillis() / 100) - lidStart) % 80;
        if (angle > 50 || done) {
            done = true;
            angle = 50;
        }

        return angle;
    }

    public void resetLid() {
        lidStart = (int) System.currentTimeMillis() / 100;
    }
}
