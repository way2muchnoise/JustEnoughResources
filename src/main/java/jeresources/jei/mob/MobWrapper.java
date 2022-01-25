package jeresources.jei.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import jeresources.api.drop.LootDrop;
import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.util.CollectionHelper;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.ingredient.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MobWrapper implements IRecipeCategoryExtension, ITooltipCallback<ItemStack> {
    private final MobEntry mob;

    public MobWrapper(MobEntry mob) {
        this.mob = mob;
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.mob.getDropsItemStacks());
    }

    public List<LootDrop> getDrops() {
        return this.mob.getDrops();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @Nonnull PoseStack poseStack, double mouseX, double mouseY) {
        LivingEntity livingEntity = this.mob.getEntity();
        RenderHelper.scissor(poseStack,7, 43, 59, 79);
        float scale = getScale(livingEntity);
        int offsetY = getOffsetY(livingEntity);
        RenderHelper.renderEntity(
            poseStack,
            37, 105 - offsetY, scale,
            38 - mouseX,
            70 - offsetY - mouseY,
            livingEntity
        );
        RenderHelper.stopScissor();

        String mobName = this.mob.getMobName();
        if (Settings.showDevData) {
            String entityString = livingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(poseStack, mobName, 7, 2);

        final String biomesLine;
        if (this.mob.hasMultipleBiomes())
            biomesLine = TranslationHelper.translateAndFormat("jer.mob.biome");
        else
            biomesLine = this.mob.getTranslatedBiomes()
                .findFirst()
                .map(firstBiome -> TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + firstBiome)
                .orElse("");
        Font.normal.print(poseStack, biomesLine, 7, 12);

        Font.normal.print(poseStack, this.mob.getLightLevel().toString(), 7, 22);
        Font.normal.print(poseStack, TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    @Nonnull
    public List<Component> getTooltipStrings(double mouseX, double mouseY) {
        if (this.mob.hasMultipleBiomes() && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(TextComponent::new, this.mob.getTranslatedBiomes());
        return Collections.emptyList();
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, List<Component> tooltip) {
        LootDrop lootDrop = this.mob.getDrops().get(slotIndex);
        tooltip.add(lootDrop.toStringTextComponent());
        List<Component> list = getToolTip(ingredient);
        if (list != null)
            tooltip.addAll(list);
    }

    public List<Component> getToolTip(ItemStack stack) {
        for (LootDrop item : this.mob.getDrops()) {
            if (stack.sameItem(item.item))
                return item.getTooltipText();
            if (item.canBeCooked() && stack.sameItem(item.smeltedItem))
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

    private float getScale(LivingEntity LivingEntity) {
        float width = LivingEntity.getBbWidth();
        float height = LivingEntity.getBbHeight();
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
