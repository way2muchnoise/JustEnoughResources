package jeresources.jei.plant;

import jeresources.api.drop.PlantDrop;
import jeresources.compatibility.CompatBase;
import jeresources.entry.PlantEntry;
import jeresources.util.PlantHelper;
import jeresources.util.RenderHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlantWrapper implements IRecipeCategoryExtension, IRecipeSlotTooltipCallback {
    protected final PlantEntry plantEntry;

    public PlantWrapper(PlantEntry entry) {
        plantEntry = entry;
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.renderBlock(guiGraphics, getFarmland(), 30, 30, -10, 20F, 20F);
        RenderHelper.renderBlock(guiGraphics, getBlockState(), 30, 12, 10, 20F, 20F);
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, @NotNull List<Component> tooltip) {
        if (recipeSlotView.getRole() != RecipeIngredientRole.INPUT) tooltip.add(getChanceString((ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()));
    }

    public float getChance(ItemStack itemStack) {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        return switch (drop.getDropKind()) {
            case chance -> drop.getChance();
            case weight -> (float) drop.getWeight() / this.plantEntry.getTotalWeight();
            case minMax -> Float.NaN;
            default -> 0;
        };
    }

    public int[] getMinMax(ItemStack itemStack) {
        PlantDrop drop = this.plantEntry.getDrop(itemStack);
        return new int[]{drop.getMinDrop(), drop.getMaxDrop()};
    }

    private Component getChanceString(ItemStack itemStack) {
        float chance = getChance(itemStack);
        String toPrint;
        if (Float.isNaN(chance)) {
            int[] minMax = this.getMinMax(itemStack);
            toPrint = minMax[0] + (minMax[0] == minMax[1] ? "" : " - " + minMax[1]);
        } else {
            toPrint = String.format("%2.2f", chance * 100).replace(",", ".") + "%";
        }
        return Component.literal(toPrint);
    }

    private BlockState state;
    private Property<?> ageProperty;
    private long timer = -1;
    private static final int TICKS = 500; // .5s

    private BlockState getBlockState() {
        if (this.state == null) {
            if (this.plantEntry.getPlantState() != null) this.state = this.plantEntry.getPlantState();
            else if (this.plantEntry.getPlant() != null) this.state = PlantHelper.getPlant(this.plantEntry.getPlant(),CompatBase.getLevel(), BlockPos.ZERO);
            else this.state = Block.byItem(this.plantEntry.getPlantItemStack().getItem()).defaultBlockState();

            if (this.plantEntry.getAgeProperty() != null) this.ageProperty = this.plantEntry.getAgeProperty();
            else this.state.getProperties().stream().filter(p -> p.getName().equals("age")).findAny().ifPresent(property -> this.ageProperty = property);
        }

        if (ageProperty != null) {
            if (timer == -1) timer = System.currentTimeMillis() + TICKS;
            else if (System.currentTimeMillis() > timer) {
                this.state = this.state.cycle(ageProperty);
                this.timer = System.currentTimeMillis() + TICKS;
            }
        }

        return state;
    }

    private BlockState getFarmland() {
        if (plantEntry.getSoil() != null) {
            return plantEntry.getSoil();
        }

        return Blocks.FARMLAND.defaultBlockState();
    }
}
