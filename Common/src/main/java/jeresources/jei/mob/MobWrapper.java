package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.util.CollectionHelper;
import jeresources.util.EntityPreviewRendering;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class MobWrapper implements IRecipeCategoryExtension<MobEntry> {
    @Override
    public void drawInfo(MobEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        LivingEntity livingEntity = recipe.getEntity();
        if (livingEntity == null) return; // added null check to prevent crashes when entity is not created correctly
        EntityPreviewRendering.render(guiGraphics, livingEntity, mouseX, mouseY);
    }

    @Override
    public void createRecipeExtras(MobEntry recipe, IRecipeExtrasBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        LivingEntity livingEntity = recipe.getEntity();
        String mobName = recipe.getMobName();
        if (Settings.showDevData && livingEntity != null) {
            String entityString = livingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        builder.addText(FormattedText.of(mobName), 100, 100).setPosition(7, 2);

        final String biomesLine;
        if (recipe.hasMultipleBiomes())
            biomesLine = TranslationHelper.translateAndFormat("jer.mob.biome");
        else
            biomesLine = recipe.getTranslatedBiomes()
                    .findFirst()
                    .map(firstBiome -> TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + firstBiome)
                    .orElse("");
        builder.addText(FormattedText.of(biomesLine), 100, 100).setPosition(7, 12);

        builder
                .addText(FormattedText.of(recipe.getLightLevel().toString()), 100, 100)
                .setPosition(7, 22);

        builder
                .addText(FormattedText.of(TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + recipe.getExp()), 100, 100)
                .setPosition(7, 32);

    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, MobEntry recipe, double mouseX, double mouseY) {
        if (recipe.hasMultipleBiomes() && isOnBiome(mouseX, mouseY))
            tooltip.addAll(CollectionHelper.create(Component::literal, recipe.getTranslatedBiomes()));
    }

    private boolean isOnBiome(double mouseX, double mouseY) {
        return 2 <= mouseX
            && mouseX < 165
            && 12 <= mouseY
            && mouseY < 12 + 10;
    }

}
