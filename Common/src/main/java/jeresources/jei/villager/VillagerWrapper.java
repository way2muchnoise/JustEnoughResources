package jeresources.jei.villager;

import jeresources.entry.AbstractVillagerEntry;
import jeresources.reference.Resources;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;


public class VillagerWrapper implements IRecipeCategoryExtension<AbstractVillagerEntry> {
    private IFocus<ItemStack> focus;

    public void setFocus(IFocus<ItemStack> focus) {
        this.focus = focus;
    }

    @Override
    public void drawInfo(AbstractVillagerEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        AbstractVillager villager = recipe.getVillagerEntity();
        RenderHelper.renderEntity(
            guiGraphics,
            7, 43, 66, 122,
            36.0F,
            38 - mouseX,
            80 - mouseY,
            villager
        );

        // Draw slots on the background
        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - recipe.getPossibleLevels(focus).size()) / 2;
        int i;
        for (i = 0; i < recipe.getPossibleLevels(focus).size(); i++) {
            RenderHelper.drawTexture(guiGraphics, Resources.Gui.Jei.VILLAGER.getResource(), 130, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20);
            RenderHelper.drawTexture(guiGraphics, Resources.Gui.Jei.VILLAGER.getResource(), VillagerCategory.X_FIRST_ITEM, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18);
            RenderHelper.drawTexture(guiGraphics, Resources.Gui.Jei.VILLAGER.getResource(), VillagerCategory.X_FIRST_ITEM + VillagerCategory.X_ITEM_DISTANCE, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18);
            RenderHelper.drawTexture(guiGraphics, Resources.Gui.Jei.VILLAGER.getResource(), VillagerCategory.X_ITEM_RESULT, y + i * VillagerCategory.Y_ITEM_DISTANCE, 22, 120, 18, 18);
        }

        if (recipe.hasPois()) {
            RenderHelper.drawTexture(guiGraphics, Resources.Gui.Jei.VILLAGER.getResource(), 49, 18, 22, 120, 18, 18);
        }
    }

    @Override
    public void createRecipeExtras(AbstractVillagerEntry recipe, IRecipeExtrasBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        // Renders all the text
        int i = 0;
        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - recipe.getPossibleLevels(focus).size()) / 2;
        if (recipe.hasLevels()) {
            for (Object oLevel : recipe.getPossibleLevels(focus)) {
                Integer level = (Integer) oLevel;
                builder
                        .addText(FormattedText.of("lv. " + (level + 1)), 100,100)
                        .setPosition(72, y + i++ * VillagerCategory.Y_ITEM_DISTANCE + 6);
            }
        }

        builder.addText(recipe.getDisplayName(), 100, 100).setPosition(5, 5);
        if (recipe.hasPois()) {
            builder
                    .addText(FormattedText.of(TranslationHelper.translateAndFormat("jer.villager.poi")), 45, 100)
                    .setPosition(5, 18);
        }
    }
}
