package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class DungeonWrapper implements IRecipeCategoryExtension<DungeonEntry> {
    @Override
    public void drawInfo(DungeonEntry entry, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.renderChest(guiGraphics, 15, 20, -40, 20, getLidAngle());
        Font.normal.print(guiGraphics, TranslationHelper.translateAndFormat(entry.getName()), 60, 7);
        Font.small.print(guiGraphics, DungeonRegistry.getInstance().getNumStacks(entry), 60, 20);
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
