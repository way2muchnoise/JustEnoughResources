package jeresources.jei.mob;

import jeresources.api.utils.DropItem;
import jeresources.config.Settings;
import jeresources.entries.MobEntry;
import jeresources.utils.*;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MobWrapper implements IRecipeWrapper
{
    public MobEntry mob;
    public int set, lastSet;

    public MobWrapper(MobEntry mob)
    {
        this.mob = mob;
        this.set = 0;
        this.lastSet = (mob.getDrops().length / (Settings.ITEMS_PER_COLUMN + 1));
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
        boolean normal = entityLivingBase.width <= entityLivingBase.height;
        float scale = getScale(entityLivingBase);
        float offsetX = normal ? (scale > 34.0F ? (75 - scale/2) : (75 - scale)) : 72 ;
        RenderHelper.renderEntity(30, 160 - (int)offsetX, scale, 150 - RenderHelper.getMousePosition().x, 150 - RenderHelper.getMousePosition().y, entityLivingBase);

        Font.normal.print(this.mob.getMobName(), 2, 2);
        Font.normal.print(TranslationHelper.translateToLocal("jer.mob.biome"), 2, 12);
        Font.normal.print(this.mob.getLightLevel(), 2, 22);
        Font.normal.print(TranslationHelper.translateToLocal("jer.mob.exp") + ": " + MobHelper.getExpDrop(this.mob), 2, 32);

        int y = MobCategory.Y_FIRST_ITEM + 4;
        for (int i = this.set * Settings.ITEMS_PER_COLUMN; i < this.set * Settings.ITEMS_PER_COLUMN + Settings.ITEMS_PER_COLUMN; i++)
        {
            if (i >= this.mob.getDrops().length) break;
            Font.normal.print(this.mob.getDrops()[i].toString(), MobCategory.X_FIRST_ITEM + 18, y);
            y += MobCategory.SPACING_Y;
        }

        if (this.lastSet > 0)
            Font.normal.print(TranslationHelper.getLocalPageInfo(this.set, this.lastSet), MobCategory.X_FIRST_ITEM, 120);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
        List<String> tooltip = null;
        if (isOnBiome(mouseX, mouseY))
            tooltip = CollectionHelper.create(this.mob.getBiomes());
        else if (false)
            tooltip = getToolTip(null);
        return tooltip;
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
