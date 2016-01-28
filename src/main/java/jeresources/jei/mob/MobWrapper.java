package jeresources.jei.mob;

import jeresources.api.drop.DropItem;
import jeresources.compatibility.ModList;
import jeresources.entries.MobEntry;
import jeresources.utils.CollectionHelper;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.common.entities.monster.cult.EntityCultist;
import thaumcraft.common.entities.monster.tainted.EntityTaintVillager;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MobWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack>
{
    private MobEntry mob;
    private float scale;
    private int offsetY;

    public MobWrapper(MobEntry mob)
    {
        this.mob = mob;
        this.scale = getScale(mob.getEntity());
        this.offsetY = getOffsetY(mob.getEntity());
    }

    @Override
    public List getInputs()
    {
        return null;
    }

    @Override
    public List getOutputs()
    {
        return this.mob.getDropsItemStacks();
    }

    public List<ItemStack> getDrops()
    {
        return this.mob.getDropsItemStacks();
    }

    @Override
    public List<FluidStack> getFluidInputs()
    {
        return null;
    }

    @Override
    public List<FluidStack> getFluidOutputs()
    {
        return null;
    }

    @Override
    @Deprecated
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        EntityLivingBase entityLivingBase = this.mob.getEntity();
        RenderHelper.scissor(minecraft, recipeWidth, recipeHeight, 7.2F, 57.8F, 59.0F, 79.0F);
        RenderHelper.renderEntity(
                37, 110 - offsetY, scale,
                38 - mouseX,
                70 - offsetY - mouseY,
                entityLivingBase
        );
        RenderHelper.stopScissor();

        Font.normal.print(this.mob.getMobName(), 7, 2);
        Font.normal.print(this.mob.getBiomes().length > 1 ? TranslationHelper.translateToLocal("jer.mob.biome") : TranslationHelper.translateToLocal("jer.mob.spawn") + " " + this.mob.getBiomes()[0], 7, 12);
        Font.normal.print(this.mob.getLightLevel(), 7, 22);
        Font.normal.print(TranslationHelper.translateToLocal("jer.mob.exp") + ": " + this.mob.getExp(), 7, 32);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        if (this.mob.getBiomes().length > 1 && isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(this.mob.getBiomes());
        return null;
    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton)
    {
        return false;
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip)
    {
        tooltip.add(this.mob.getDrops()[slotIndex].toString());
        List<String> list = getToolTip(ingredient);
        if (list != null)
            tooltip.addAll(list);
    }

    public EntityLivingBase getMob()
    {
        return this.mob.getEntity();
    }

    public List<String> getToolTip(ItemStack stack)
    {
        for (DropItem item : mob.getDrops())
            if (item.item.isItemEqual(stack))
                return item.conditionals;
        return null;
    }

    private boolean isOnBiome(int mouseX, int mouseY)
    {
        return 2 <= mouseX
                && mouseX < 165
                && 12 <= mouseY
                && mouseY < 12 + 10;
    }

    private float getScale(EntityLivingBase entityLivingBase)
    {
        float width = entityLivingBase.width;
        float height = entityLivingBase.height;
        if (width <= height)
        {
            if (height < 0.8) return 50.0F;
            else if (height < 1) return 35.0F;
            else if (height < 1.8) return 33.0F;
            else if (height < 2) return 32.0F;
            else if (height < 3) return 24.0F;
            else if (height < 4) return 20.0F;
            else return 10.0F;
        } else
        {
            if (width < 1) return 38.0F;
            else if (width < 2) return 27.0F;
            else if (width < 3) return 13.0F;
            else return 9.0F;
        }
    }

    private int getOffsetY(EntityLivingBase entityLivingBase)
    {
        int offsetY = 0;
        if (entityLivingBase instanceof EntitySquid) offsetY = 20;
        else if (entityLivingBase instanceof EntityWitch) offsetY = -10;
        else if (entityLivingBase instanceof EntityGhast) offsetY = 15;
        else if (entityLivingBase instanceof EntityWither) offsetY = -15;
        else if (entityLivingBase instanceof EntityDragon) offsetY = 15;
        else if (entityLivingBase instanceof EntityEnderman) offsetY = -10;
        else if (entityLivingBase instanceof EntityGolem) offsetY = -10;
        else if (ModList.thaumcraft.isLoaded())
        {
            if (entityLivingBase instanceof EntityTaintacle) offsetY = 50;
            else if (entityLivingBase instanceof EntityTaintVillager) offsetY = -10;
            else if (entityLivingBase instanceof EntityCultist) offsetY = -10;
        }
        return offsetY;
    }
}
