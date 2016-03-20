package jeresources.jei.dungeon;

import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class DungeonWrapper extends BlankRecipeWrapper implements ITooltipCallback<ItemStack>
{
    public DungeonEntry chest;

    public DungeonWrapper(DungeonEntry chest)
    {
        this.chest = chest;
    }

    @Nonnull
    @Override
    public List getOutputs()
    {
        return this.chest.getItemStacks();
    }

    public int amountOfItems()
    {
        return this.chest.getItemStacks().size();
    }

    public List<ItemStack> getItems(int slot, int slots)
    {
        List<ItemStack> list = this.chest.getItemStacks().subList(slot, slot+1);
        for (int n = 1; n < (amountOfItems() / slots) + 1; n++)
            list.add(this.amountOfItems() <= slot + slots * n ? null : this.chest.getItemStacks().get(slot + slots * n));
        return list;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        Font.normal.print(TranslationHelper.translateToLocal(this.chest.getName()), 60, 7);
        Font.small.print(DungeonRegistry.getInstance().getNumStacks(this.chest), 60, 20);
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight)
    {
        RenderHelper.renderChest(15, 20, -40, 20, getLidAngle());
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip)
    {
        tooltip.add(this.chest.getChestDrop(ingredient).toString());
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
}
