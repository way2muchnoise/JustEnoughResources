package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import org.jetbrains.annotations.NotNull;

public class DungeonWrapper implements IRecipeCategoryExtension<DungeonEntry> {
    @Override
    public void drawInfo(DungeonEntry entry, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.renderChest(guiGraphics, 15, 20, -40, 20, getLidAngle());
    }

    @Override
    public void createRecipeExtras(DungeonEntry entry, IRecipeExtrasBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        builder.addText(FormattedText.of(TranslationHelper.translateAndFormat(entry.getName())), 200, 100).setPosition(50, 7);
        builder.addText(FormattedText.of(DungeonRegistry.getInstance().getNumStacks(entry)), 200, 100).setPosition(50, 20);
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
