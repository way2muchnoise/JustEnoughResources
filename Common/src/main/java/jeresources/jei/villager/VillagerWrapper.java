package jeresources.jei.villager;

import jeresources.collection.TradeList;
import jeresources.entry.AbstractVillagerEntry;
import jeresources.reference.Resources;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VillagerWrapper implements IRecipeCategoryExtension {
    private final AbstractVillagerEntry<?> entry;
    private IFocus<ItemStack> focus;

    public VillagerWrapper(AbstractVillagerEntry<?> entry) {
        this.entry = entry;
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

    public List<ItemStack> getPois() {
        return entry.getPois();
    }

    public boolean hasPois() {
        return entry.hasPois();
    }

    public boolean hasLevels() {
        return entry.hasLevels();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // TODO: Fix scissoring
        // RenderHelper.scissor(poseStack,7, 43, 59, 79);
        AbstractVillager villager = entry.getVillagerEntity();
        RenderHelper.renderEntity(
            guiGraphics,
            37, 118, 36.0F,
            38 - mouseX,
            80 - mouseY,
            villager
        );
        //RenderHelper.stopScissor();

        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - getPossibleLevels(focus).size()) / 2;
        int i;
        for (i = 0; i < getPossibleLevels(focus).size(); i++) {
            RenderHelper.drawTexture(guiGraphics, 130, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_FIRST_ITEM, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_FIRST_ITEM + VillagerCategory.X_ITEM_DISTANCE, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
            RenderHelper.drawTexture(guiGraphics, VillagerCategory.X_ITEM_RESULT, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
        }
        if (entry.hasLevels()) {
            i = 0;
            for (int level : getPossibleLevels(focus)) {
                Font.normal.print(guiGraphics, "lv. " + (level + 1), 72, y + i++ * VillagerCategory.Y_ITEM_DISTANCE + 6);
            }
        }

        Font.normal.print(guiGraphics, TranslationHelper.translateAndFormat(entry.getDisplayName()), 5, 5);
        if (entry.hasPois()) {
            Font.normal.splitPrint(guiGraphics, TranslationHelper.translateAndFormat("jer.villager.poi"), 5, 18, 45);
            RenderHelper.drawTexture(guiGraphics, 49, 18, 22, 120, 18, 18, Resources.Gui.Jei.VILLAGER.getResource());
        }
    }
}
