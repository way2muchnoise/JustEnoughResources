package jeresources.jei.plant;

import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.util.RenderHelper;
import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class PlantWrapper implements IRecipeWrapper, ITooltipCallback<ItemStack> {
    private final PlantEntry plantEntry;

    public PlantWrapper(PlantEntry entry) {
        plantEntry = entry;
    }

    @Override
    public void getIngredients(@Nonnull IIngredients ingredients) {
        ingredients.setInput(VanillaTypes.ITEM, plantEntry.getPlantItemStack());
        ingredients.setOutputs(VanillaTypes.ITEM, plantEntry.getLootDropStacks());
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        RenderHelper.renderBlock(getFarmland(), 26, 50, -10, 20F, 0.4F);
        RenderHelper.renderBlock(getBlockState(), 26, 32, 10, 20F, 0.4F);
    }

    @Override
    public void onTooltip(int slotIndex, boolean input, ItemStack ingredient, List<String> tooltip) {
        if (!input)
            tooltip.add(getChanceString(ingredient));
    }

    public float getChance(ItemStack itemStack) {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        switch (drop.getDropKind()) {
            case chance:
                return drop.getChance();
            case weight:
                return (float) drop.getWeight() / this.plantEntry.getTotalWeight();
            case minMax:
                return Float.NaN;
            default:
                return 0;
        }
    }

    public int[] getMinMax(ItemStack itemStack) {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        return new int[]{drop.getMinDrop(), drop.getMaxDrop()};
    }

    private String getChanceString(ItemStack itemStack) {
        float chance = getChance(itemStack);
        String toPrint;
        if (Float.isNaN(chance)) {
            int[] minMax = this.getMinMax(itemStack);
            toPrint = minMax[0] + (minMax[0] == minMax[1] ? "" : " - " + minMax[1]);
        } else
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";
        return toPrint;
    }

    private IBlockState state;
    private long timer = -1;
    private static final int TICKS = 1000; // 1s

    private IBlockState getBlockState() {
        if (this.plantEntry.getPlant() != null) {
            if (timer == -1) timer = System.currentTimeMillis() + TICKS;
            if (this.state == null)
                this.state = this.plantEntry.getPlant().getPlant(null, null);
            if (System.currentTimeMillis() > timer) {
                this.state = this.state.cycleProperty(BlockCrops.AGE);
                this.timer = System.currentTimeMillis() + TICKS;
            }
            return this.state;
        } else
            return Block.getBlockFromItem(this.plantEntry.getPlantItemStack().getItem()).getStateFromMeta(this.plantEntry.getPlantItemStack().getItemDamage());
    }

    private IBlockState getFarmland() {
        if (plantEntry.getSoil() != null) {
            return plantEntry.getSoil();
        }

        return Blocks.FARMLAND.getDefaultState();
    }
}
