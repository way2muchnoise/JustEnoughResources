package jeresources.jei.mob;

import jeresources.api.utils.DropItem;
import jeresources.entries.MobEntry;
import jeresources.utils.*;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.common.entities.monster.tainted.EntityTaintacle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MobWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack>
{
    public MobEntry mob;

    public MobWrapper(MobEntry mob)
    {
        this.mob = mob;
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
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        EntityLivingBase entityLivingBase = this.mob.getEntity();
        float scale = getScale(entityLivingBase);
        float offsetX = entityLivingBase.height / scale;
        if (ModList.thaumcraft.isLoaded() && entityLivingBase instanceof EntityTaintacle) offsetX = 50;
        RenderHelper.renderEntity(37, 110 - (int)offsetX, scale, 150 - RenderHelper.getMousePosition().x, 150 - RenderHelper.getMousePosition().y, entityLivingBase);

        Font.normal.print(this.mob.getMobName(), 7, 2);
        Font.normal.print(TranslationHelper.translateToLocal("jer.mob.biome"), 7, 12);
        Font.normal.print(this.mob.getLightLevel(), 7, 22);
        Font.normal.print(TranslationHelper.translateToLocal("jer.mob.exp") + ": " + MobHelper.getExpDrop(this.mob), 7, 32);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        if (isOnBiome(mouseX, mouseY))
            return CollectionHelper.create(this.mob.getBiomes());
        return null;
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
            if (height < 0.8) return 70.0F;
            else if (height < 1) return 35.0F;
            else if (height < 2) return 32.0F;
            else if (height < 3) return 26.0F;
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
}
