package jeresources.jei.dungeon;

import jeresources.entries.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.utils.CollectionHelper;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class DungeonWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack>
{
    public DungeonEntry chest;

    public DungeonWrapper(DungeonEntry chest)
    {
        this.chest = chest;
    }

    @Override
    public List getInputs()
    {
        return null;
    }

    @Override
    public List getOutputs()
    {
        return CollectionHelper.create(this.chest.getItemStacks());
    }

    public int amountOfItems()
    {
        return this.chest.getItemStacks().length;
    }

    public List<ItemStack> getItems(int slot, int slots)
    {
        List<ItemStack> list = CollectionHelper.create(this.chest.getItemStacks()[slot]);
        for (int n = 1; n < (amountOfItems() / slots) + 1; n++)
            list.add(this.amountOfItems() <= slot + slots * n ? null : this.chest.getItemStacks()[slot + slots * n]);
        return list;
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
        Font.normal.print(TranslationHelper.translateToLocal(this.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(this.chest), 60, 20);
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {

    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        RenderHelper.renderChest(15, 20, -40, 20, getLidAngle());
    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY)
    {
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
        tooltip.add(getChanceString(ingredient));
    }

    private boolean done;
    private int lidStart;

    private float getLidAngle()
    {

        float angle = (((int) System.currentTimeMillis() / 100) - lidStart) % 80;
        if (angle > 50 || done)
        {
            done = true;
            angle = 50;
        }

        return angle;
    }

    public void resetLid()
    {
        lidStart = (int) System.currentTimeMillis() / 100;
    }

    public String getChanceString(ItemStack itemStack)
    {
        Float chance = this.chest.getChange(itemStack);
        if (chance == null) return null;
        chance *= 100;
        String format = chance < 100 ? "%2.1f" : "%2.0f";
        return String.format(format, chance).replace(',', '.') + "%";
    }
}
