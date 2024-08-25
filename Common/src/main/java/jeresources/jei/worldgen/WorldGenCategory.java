package jeresources.jei.worldgen;

import jeresources.api.render.ColorHelper;
import jeresources.entry.WorldGenEntry;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import jeresources.util.RenderHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class WorldGenCategory extends BlankJEIRecipeCategory<WorldGenEntry> {
    protected static final int X_ITEM = 6;
    protected static final int Y_ITEM = 22;
    protected static final int X_DROP_ITEM = 6;
    protected static final int Y_DROP_ITEM = 67;
    private static final int DROP_ITEM_COUNT = 8;

    public WorldGenCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 32, 16, 16, 16), new WorldGenWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.worldgen.title");
    }

    @Override
    public @NotNull IDrawable getBackground() {
        return Resources.Gui.Jei.WORLD_GEN;
    }

    @Override
    public @NotNull RecipeType<WorldGenEntry> getRecipeType() {
        return JEIConfig.WORLD_GEN_TYPE;
    }

    @Override
    public void draw(@NotNull WorldGenEntry recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        RenderHelper.drawLine(guiGraphics, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET + WorldGenWrapper.X_AXIS_SIZE, WorldGenWrapper.Y_OFFSET, ColorHelper.GRAY);
        RenderHelper.drawLine(guiGraphics, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET, WorldGenWrapper.X_OFFSET, WorldGenWrapper.Y_OFFSET - WorldGenWrapper.Y_AXIS_SIZE, ColorHelper.GRAY);
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull WorldGenEntry recipe, @NotNull IFocusGroup focuses) {
        WorldGenTooltip worldGenTooltip = new WorldGenTooltip(recipe);
        builder.addSlot(RecipeIngredientRole.OUTPUT, X_ITEM, Y_ITEM)
            .addItemStacks(recipe.getBlocks())
            .setSlotName(WorldGenWrapper.ORE_SLOT_NAME)
            .addTooltipCallback(worldGenTooltip);

        for (int i = 0; i < Math.min(DROP_ITEM_COUNT, recipe.getDrops().size()); i++)
            builder.addSlot(RecipeIngredientRole.OUTPUT, X_DROP_ITEM + i * 18, Y_DROP_ITEM)
                .addItemStack(recipe.getDrops().get(i))
                .addTooltipCallback(worldGenTooltip);
    }
}