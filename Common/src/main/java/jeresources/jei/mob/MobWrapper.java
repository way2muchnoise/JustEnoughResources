package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.util.CollectionHelper;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.ingredient.ICraftingGridHelper;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MobWrapper implements IRecipeCategoryExtension<MobEntry> {
    @Override
    public void drawInfo(MobEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        LivingEntity livingEntity = recipe.getEntity();
        float scale = getScale(livingEntity);
        int offsetY = getOffsetY(livingEntity);
        RenderHelper.renderEntity(
            guiGraphics,
            7, 43, 66, 122,
            scale,
            38 - mouseX,
            70 - offsetY - mouseY,
            livingEntity
        );
    }

    @Override
    public void createRecipeExtras(MobEntry recipe, IRecipeExtrasBuilder builder, ICraftingGridHelper craftingGridHelper, IFocusGroup focuses) {
        LivingEntity livingEntity = recipe.getEntity();
        String mobName = recipe.getMobName();
        if (Settings.showDevData) {
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

    private float getScale(LivingEntity livingEntity) {
        float width = livingEntity.getBbWidth();
        float height = livingEntity.getBbHeight();
        if (width <= height) {
            if (height < 0.9) return 50.0F;
            else if (height < 1) return 35.0F;
            else if (height < 1.8) return 33.0F;
            else if (height < 2) return 32.0F;
            else if (height < 3) return 24.0F;
            else if (height < 4) return 20.0F;
            else return 10.0F;
        } else {
            if (width < 1) return 38.0F;
            else if (width < 2) return 27.0F;
            else if (width < 3) return 13.0F;
            else return 9.0F;
        }
    }

    private int getOffsetY(LivingEntity livingEntity) {
        int offsetY = 0;
        if (livingEntity instanceof Squid) offsetY = 20;
        else if (livingEntity instanceof Turtle) offsetY = 10;
        else if (livingEntity instanceof Witch) offsetY = -5;
        else if (livingEntity instanceof Ghast) offsetY = 15;
        else if (livingEntity instanceof WitherBoss) offsetY = -15;
        else if (livingEntity instanceof EnderDragon) offsetY = 15;
        else if (livingEntity instanceof EnderMan) offsetY = -10;
        else if (livingEntity instanceof AbstractGolem) offsetY = -10;
        else if (livingEntity instanceof Animal) offsetY = -20;
        else if (livingEntity instanceof Villager) offsetY = -15;
        else if (livingEntity instanceof Husk) offsetY = -15;
        else if (livingEntity instanceof AbstractIllager) offsetY = -15;
        else if (livingEntity instanceof WanderingTrader) offsetY = -15;
        else if (livingEntity instanceof Blaze) offsetY = -10;
        else if (livingEntity instanceof Creeper) offsetY = -15;
        else if (livingEntity instanceof AbstractPiglin) offsetY = -15;
        return offsetY;
    }
}