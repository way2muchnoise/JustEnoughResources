package jeresources.jei.villager;

import jeresources.collection.TradeList;
import jeresources.entry.VillagerEntry;
import jeresources.reference.Resources;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class VillagerWrapper implements IRecipeWrapper {
    private final VillagerEntry entry;
    private IFocus<ItemStack> focus;

    public VillagerWrapper(VillagerEntry entry) {
        this.entry = entry;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInputs(VanillaTypes.ITEM, entry.getInputs());
        ingredients.setOutputs(VanillaTypes.ITEM, entry.getOutputs());
    }

    public TradeList getTrades(int level) {
        return entry.getVillagerTrades(level);
    }

    public int getMaxLevel() {
        return entry.getMaxLevel();
    }

    public List<Integer> getPossibleLevels(IFocus<ItemStack> focus) {
        return entry.getPossibleLevels(focus);
    }

    public void setFocus(IFocus<ItemStack> focus) {
        this.focus = focus;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        RenderHelper.scissor(minecraft, 7, 43, 59, 79);
        EntityVillager entityVillager;
        try {
            entityVillager = new EntityVillager(minecraft.world, entry.getProfession());
        } catch (RuntimeException e) { // thrown when profession doesn't exist, this shouldn't happen
            entityVillager = new EntityVillager(minecraft.world);
        }
        RenderHelper.renderEntity(
            37, 118, 36.0F,
            38 - mouseX,
            80 - mouseY,
            entityVillager
        );
        RenderHelper.stopScissor();

        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - getPossibleLevels(focus).size()) / 2;
        int i;
        for (i = 0; i < getPossibleLevels(focus).size(); i++)
            RenderHelper.drawTexture(130, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20, Resources.Gui.Jei.VILLAGER.getResource());
        i = 0;
        for (int level : getPossibleLevels(focus))
            Font.normal.print("lv. " + level, 72, y + i++ * VillagerCategory.Y_ITEM_DISTANCE + 6);

        Font.normal.print(TranslationHelper.translateToLocal(entry.getDisplayName()), 10, 25);
    }
}
