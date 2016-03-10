package jeresources.jei.villager;

import jeresources.entries.VillagerEntry;
import jeresources.reference.Resources;
import jeresources.utils.Font;
import jeresources.utils.RenderHelper;
import jeresources.utils.TranslationHelper;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.passive.EntityVillager;

import javax.annotation.Nonnull;
import java.util.List;

public class VillagerWrapper extends BlankRecipeWrapper
{
    private final VillagerEntry entry;

    public VillagerWrapper(VillagerEntry entry)
    {
        this.entry = entry;
    }

    @Nonnull
    @Override
    public List getInputs()
    {
        return entry.getInputs();
    }

    @Nonnull
    @Override
    public List getOutputs()
    {
        return entry.getOutputs();
    }

    public VillagerEntry.TradeList getTrades(int level)
    {
        return entry.getVillagerTrades(level);
    }

    public int getMaxLevel()
    {
        return entry.getMaxLevel();
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY)
    {
        RenderHelper.scissor(minecraft, recipeWidth, recipeHeight, 7.2F, 57.8F, 59.0F, 79.0F);
        RenderHelper.renderEntity(
                37, 120, 36.0F,
                38 - mouseX,
                80 - mouseY,
                new EntityVillager(null, entry.getProfession())
        );
        RenderHelper.stopScissor();

        int y = VillagerCategory.Y_ITEM_DISTANCE * (6 - getMaxLevel()) / 2;
        for (int i = 0; i < getMaxLevel(); i++)
            RenderHelper.drawTexture(125, y + i * VillagerCategory.Y_ITEM_DISTANCE, 0, 120, 20, 20, Resources.Gui.Jei.VILLAGER.getResource());

        Font.normal.print(TranslationHelper.translateToLocal(entry.getName()), 10, 20);
        Font.normal.print(TranslationHelper.translateToLocal("jer.villager.buys"), VillagerCategory.X_FIRST_ITEM + 2, VillagerCategory.Y_ITEM_DISTANCE * ((6 - getMaxLevel()) / 2) - 10);
        Font.normal.print(TranslationHelper.translateToLocal("jer.villager.sells"), VillagerCategory.X_ITEM_RESULT, VillagerCategory.Y_ITEM_DISTANCE * ((6 - getMaxLevel()) / 2) -10);
    }
}
