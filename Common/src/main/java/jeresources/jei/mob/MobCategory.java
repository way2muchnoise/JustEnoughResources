package jeresources.jei.mob;

import jeresources.api.drop.LootDrop;
import jeresources.config.Settings;
import jeresources.entry.MobEntry;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MobCategory extends BlankJEIRecipeCategory<MobEntry> {
    protected static final int X_FIRST_ITEM = 97;
    protected static final int Y_FIRST_ITEM = 43;

    public MobCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 16, 16, 16), new MobWrapper());
    }

    @Override
    public @NotNull Component getTitle() {
        return Component.translatable("jer.mob.title");
    }

    @Override
    public @NotNull IDrawable getJERBackground() {
        return Resources.Gui.Jei.MOB;
    }

    @Override
    public @NotNull IRecipeType<MobEntry> getRecipeType() {
        return JEIConfig.MOB_TYPE;
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MobEntry recipe, @NotNull IFocusGroup focuses) {
        int xOffset = 0;;
        List<LootDrop> drops = recipe.getDrops();
        int dropCount = Math.min(drops.size(), Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN);
        for (int i = 0; i < Settings.ITEMS_PER_ROW; i++) {
            int yOffset = 0;
            for (int ii = 0; ii < Settings.ITEMS_PER_COLUMN; ii++) {
                int slotNumber = i + ii * Settings.ITEMS_PER_ROW;
                IRecipeSlotBuilder slotBuilder = builder
                    .addSlot(RecipeIngredientRole.OUTPUT, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset)
                    .setSlotName(String.valueOf(slotNumber))
                    .addRichTooltipCallback(new MobTooltip(recipe));
                if (slotNumber < dropCount) {
                    slotBuilder.addItemStacks(drops.get(slotNumber).getDrops());
                }
                yOffset += 80 / Settings.ITEMS_PER_COLUMN;
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW;
        }
        if (recipe.hasSpawnEgg()) {
            builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 151, 22).add(recipe.getSpawnEgg());
        }
    }
}