package jeresources.jei.mob;

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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

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
    public void drawInfo(int recipeWidth, int recipeHeight, double mouseX, double mouseY) {
        EntityLivingBase entityLivingBase = this.mob.getEntity();
        RenderHelper.scissor(7, 43, 59, 79);
        this.scale = getScale(this.mob.getEntity());
        this.offsetY = getOffsetY(this.mob.getEntity());
        RenderHelper.renderEntity(
            37, 105 - offsetY, scale,
            38 - mouseX,
            70 - offsetY - mouseY,
            entityLivingBase
        );
        RenderHelper.stopScissor();

        String mobName = this.mob.getMobName();
        if (Settings.showDevData) {
            String entityString = entityLivingBase.getEntityString();
            if (entityString != null) {
                mobName += " (" + entityString + ")";
            }
        }
        Font.normal.print(mobName, 7, 2);
        Font.normal.print(this.mob.getBiomes().length > 1 ? TranslationHelper.translateAndFormat("jer.mob.biome") : TranslationHelper.translateAndFormat("jer.mob.spawn") + " " + this.mob.getBiomes()[0], 7, 12);
        Font.normal.print(this.mob.getLightLevel(), 7, 22);
        Font.normal.print(TranslationHelper.translateAndFormat("jer.mob.exp") + ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    public List<String> getTooltipStrings(double mouseX, double mouseY) {
        if (this.mob.getBiomes().length > 1 && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(this.mob.getBiomes());
        return Collections.emptyList();
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        tooltip.add(this.mob.getDrops()[slotIndex].toString());
        List<String> list = getToolTip(ingredient);
        if (list != null)
            tooltip.addAll(list);
    }

    public EntityLivingBase getMob() {
        return this.mob.getEntity();
    }

    public List<String> getToolTip(ItemStack stack) {
        for (LootDrop item : this.mob.getDrops()) {
            if (stack.isItemEqual(item.item))
                return item.getTooltipText();
            if (item.canBeCooked() && stack.isItemEqual(item.smeltedItem))
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

    private float getScale(EntityLivingBase entityLivingBase) {
        float width = entityLivingBase.width;
        float height = entityLivingBase.height;
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

    private int getOffsetY(EntityLivingBase entityLivingBase) {
        int offsetY = 0;
        if (entityLivingBase instanceof EntitySquid) offsetY = 20;
        else if (entityLivingBase instanceof EntityWitch) offsetY = -10;
        else if (entityLivingBase instanceof EntityGhast) offsetY = 15;
        else if (entityLivingBase instanceof EntityWither) offsetY = -15;
        else if (entityLivingBase instanceof EntityDragon) offsetY = 15;
        else if (entityLivingBase instanceof EntityEnderman) offsetY = -10;
        else if (entityLivingBase instanceof EntityGolem) offsetY = -10;
        else if (entityLivingBase instanceof EntityAnimal) offsetY = -20;
        else if (entityLivingBase instanceof EntityVillager) offsetY = -15;
        else if (entityLivingBase instanceof EntityVindicator) offsetY = -15;
        else if (entityLivingBase instanceof EntityEvoker) offsetY = -10;
        else if (entityLivingBase instanceof EntityBlaze) offsetY = -10;
        else if (entityLivingBase instanceof EntityCreeper) offsetY = -15;
        return offsetY;
    }
}
