package jeresources.jei.mob;

import com.mojang.blaze3d.matrix.MatrixStack;
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MobWrapper implements IRecipeCategoryExtension, ITooltipCallback<ItemStack> {
    private final MobEntry mob;
    private float scale;
    private int offsetY;

    public MobWrapper(MobEntry mob) {
        this.mob = mob;
        this.scale = getScale(mob.getEntity());
        this.offsetY = getOffsetY(mob.getEntity());
    }

    @Override
    public void setIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setOutputs(VanillaTypes.ITEM, this.mob.getDropsItemStacks());
    }

    public LootDrop[] getDrops() {
        return this.mob.getDrops();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, MatrixStack matrixStack, double mouseX, double mouseY) {
        LivingEntity LivingEntity = this.mob.getEntity();
        RenderHelper.scissor(matrixStack,7, 43, 59, 79);
        this.scale = getScale(this.mob.getEntity());
        this.offsetY = getOffsetY(this.mob.getEntity());
        RenderHelper.renderEntity(
            matrixStack,
            37, 105 - offsetY, scale,
            38 - mouseX,
            70 - offsetY - mouseY,
            LivingEntity
        );
        RenderHelper.stopScissor();

        String mobName = this.mob.getMobName();
        if (Settings.showDevData) {
            String entityString = LivingEntity.getStringUUID();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(matrixStack, mobName, 7, 2);
        Font.normal.print(matrixStack, this.mob.getBiomes().length > 1 ? TranslationHelper.translateAndFormat("jer.mob.biome") : TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + this.mob.getBiomes()[0], 7, 12);
        Font.normal.print(matrixStack, this.mob.getLightLevel(), 7, 22);
        Font.normal.print(matrixStack, TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(double mouseX, double mouseY) {
        if (this.mob.getBiomes().length > 1 && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(StringTextComponent::new, this.mob.getBiomes());
        return Collections.emptyList();
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<ITextComponent> tooltip) {
        tooltip.add(this.mob.getDrops()[slotIndex].toStringTextComponent());
        List<ITextComponent> list = getToolTip(ingredient);
        if (list != null)
            tooltip.addAll(list);
    }

    public LivingEntity getMob() {
        return this.mob.getEntity();
    }

    public List<ITextComponent> getToolTip(ItemStack stack) {
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
        if (livingEntity instanceof SquidEntity) offsetY = 20;
        else if (livingEntity instanceof TurtleEntity) offsetY = 10;
        else if (livingEntity instanceof WitchEntity) offsetY = -5;
        else if (livingEntity instanceof GhastEntity) offsetY = 15;
        else if (livingEntity instanceof WitherEntity) offsetY = -15;
        else if (livingEntity instanceof EnderDragonEntity) offsetY = 15;
        else if (livingEntity instanceof EndermanEntity) offsetY = -10;
        else if (livingEntity instanceof GolemEntity) offsetY = -10;
        else if (livingEntity instanceof AnimalEntity) offsetY = -20;
        else if (livingEntity instanceof VillagerEntity) offsetY = -15;
        else if (livingEntity instanceof WanderingTraderEntity) offsetY = -15;
        else if (livingEntity instanceof BlazeEntity) offsetY = -10;
        else if (livingEntity instanceof CreeperEntity) offsetY = -15;
        return offsetY;
    }
}
