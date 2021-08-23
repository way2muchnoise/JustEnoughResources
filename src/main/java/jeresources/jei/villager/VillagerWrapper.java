package jeresources.jei.villager;

import com.mojang.blaze3d.vertex.PoseStack;
import jeresources.collection.TradeList;
import jeresources.entry.VillagerEntry;
import jeresources.reference.Resources;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class VillagerWrapper implements IRecipeCategoryExtension {
    private final VillagerEntry entry;
    private IFocus<ItemStack> focus;

    public VillagerWrapper(VillagerEntry entry) {
        this.entry = entry;
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
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
    public void drawInfo(int recipeWidth, int recipeHeight, PoseStack poseStack, double mouseX, double mouseY) {
        RenderHelper.scissor(poseStack,7, 43, 59, 79);
        Villager villager = entry.getVillagerEntity();
        RenderHelper.renderEntity(
            poseStack,
            37, 118, 36.0F,
            38 - mouseX,
            80 - mouseY,
            villager
        );
        RenderHelper.stopScissor();

        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - getPossibleLevels(focus).size()) / 2;
        int i;
        for (i = 0; i < getPossibleLevels(focus).size(); i++) {
            RenderHelper.drawTexture(poseStack, 130, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20, Resources.Gui.Jei.VILLAGER.getResource());
        }
        i = 0;
        for (int level : getPossibleLevels(focus)) {
            Font.normal.print(poseStack, "lv. " + (level + 1), 72, y + i++ * VillagerCategory.Y_ITEM_DISTANCE + 6);
        }

        Font.normal.print(poseStack, TranslationHelper.translateAndFormat(entry.getDisplayName()), 10, 25);
    }
}
