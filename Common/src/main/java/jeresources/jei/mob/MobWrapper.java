package jeresources.jei.mob;

import jeresources.api.drop.LootDrop;
import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.util.CollectionHelper;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
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
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class MobWrapper implements IRecipeCategoryExtension, IRecipeSlotTooltipCallback {
    private final MobEntry mob;

    public MobWrapper(MobEntry mob) {
        this.mob = mob;
    }

    public List<LootDrop> getDrops() {
        return this.mob.getDrops();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        LivingEntity livingEntity = this.mob.getEntity();
        // TODO: Fix scissoring
        //RenderHelper.scissor(poseStack,7, 43, 59, 79);
        float scale = getScale(livingEntity);
        int offsetY = getOffsetY(livingEntity);
        RenderHelper.renderEntity(
            guiGraphics,
            37, 105 - offsetY, scale,
            38 - mouseX,
            70 - offsetY - mouseY,
            livingEntity
        );
        //RenderHelper.stopScissor();

        String mobName = this.mob.getMobName();
        if (Settings.showDevData) {
            String entityString = livingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(guiGraphics, mobName, 7, 2);

        final String biomesLine;
        if (this.mob.hasMultipleBiomes())
            biomesLine = TranslationHelper.translateAndFormat("jer.mob.biome");
        else
            biomesLine = this.mob.getTranslatedBiomes()
                .findFirst()
                .map(firstBiome -> TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + firstBiome)
                .orElse("");
        Font.normal.print(guiGraphics, biomesLine, 7, 12);

        Font.normal.print(guiGraphics, this.mob.getLightLevel().toString(), 7, 22);
        Font.normal.print(guiGraphics, TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    public @NotNull List<Component> getTooltipStrings(double mouseX, double mouseY) {
        if (this.mob.hasMultipleBiomes() && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(Component::literal, this.mob.getTranslatedBiomes());
        return Collections.emptyList();
    }

    @Override
    public void onTooltip(@NotNull IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        LootDrop lootDrop = this.mob.getDrops().get(Integer.parseInt(recipeSlotView.getSlotName().orElse("0")));
        tooltip.add(lootDrop.toStringTextComponent());
        List<Component> list = getToolTip((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient());
        if (list != null)
            tooltip.addAll(list);
    }

    public List<Component> getToolTip(ItemStack stack) {
        for (LootDrop item : this.mob.getDrops()) {
            if (stack.is(item.item.getItem()))
                return item.getTooltipText();
            if (item.canBeCooked() && stack.is(item.smeltedItem.getItem()))
                return item.getTooltipText(true);
        }
        return null;
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
    
    public boolean hasSpawnEgg() {
        return getSpawnEgg() != null;
    }
    
    public ItemStack getSpawnEgg() {
        return this.mob.getEntity().getPickResult();
    }
}