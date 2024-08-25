package jeresources.jei.plant;

import jeresources.compatibility.CompatBase;
import jeresources.entry.PlantEntry;
import jeresources.util.PlantHelper;
import jeresources.util.RenderHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

public class PlantWrapper implements IRecipeCategoryExtension<PlantEntry> {
    @Override
    public void drawInfo(PlantEntry recipe, int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.renderBlock(guiGraphics, getFarmland(recipe), 30, 30, -10, 20F, 20F);
        RenderHelper.renderBlock(guiGraphics, getBlockState(recipe), 30, 12, 10, 20F, 20F);
    }

    private BlockState state;
    private Property<?> ageProperty;
    private long timer = -1;
    private static final int TICKS = 500; // .5s

    private BlockState getBlockState(PlantEntry recipe) {
        if (this.state == null) {
            if (recipe.getPlantState() != null) this.state = recipe.getPlantState();
            else if (recipe.getPlant() != null) this.state = PlantHelper.getPlant(recipe.getPlant(),CompatBase.getLevel(), BlockPos.ZERO);
            else this.state = Block.byItem(recipe.getPlantItemStack().getItem()).defaultBlockState();

            if (recipe.getAgeProperty() != null) this.ageProperty = recipe.getAgeProperty();
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

    private BlockState getFarmland(PlantEntry recipe) {
        if (recipe.getSoil() != null) {
            return recipe.getSoil();
        }

        return Blocks.FARMLAND.defaultBlockState();
    }
}
