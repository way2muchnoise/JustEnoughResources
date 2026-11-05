package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.util.CollectionHelper;
import jeresources.util.Font;
import jeresources.util.EntityPreviewRendering;
import jeresources.util.TranslationHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MobWrapper implements IRecipeCategoryExtension<MobEntry> {
    @Override
    public void drawInfo(MobEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        LivingEntity livingEntity = recipe.getEntity();
        EntityPreviewRendering.render(guiGraphics, livingEntity, mouseX, mouseY);

        String mobName = recipe.getMobName();
        if (Settings.showDevData) {
            String entityString = livingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(guiGraphics, mobName, 7, 2);

        final String biomesLine;
        if (recipe.hasMultipleBiomes())
            biomesLine = TranslationHelper.translateAndFormat("jer.mob.biome");
        else
            biomesLine = recipe.getTranslatedBiomes()
                .findFirst()
                .map(firstBiome -> TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + firstBiome)
                .orElse("");
        Font.normal.print(guiGraphics, biomesLine, 7, 12);

        Font.normal.print(guiGraphics, recipe.getLightLevel().toString(), 7, 22);
        Font.normal.print(guiGraphics, TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + recipe.getExp(), 7, 32);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(MobEntry recipe, double mouseX, double mouseY) {
        if (recipe.hasMultipleBiomes() && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(Component::literal, recipe.getTranslatedBiomes());
        return Collections.emptyList();
    }

    private boolean isOnBiome(double mouseX, double mouseY) {
        return 2 <= mouseX
            && mouseX < 165
            && 12 <= mouseY
            && mouseY < 12 + 10;
    }

}
