package jeresources.jei.mob;

import jeresources.config.Settings;
import jeresources.jei.BlankJEIRecipeCategory;
import jeresources.jei.JEIConfig;
import jeresources.reference.Resources;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

public class MobCategory extends BlankJEIRecipeCategory<MobWrapper> {
    protected static final int X_FIRST_ITEM = 97;
    protected static final int Y_FIRST_ITEM = 43;

    public MobCategory() {
        super(JEIConfig.getJeiHelpers().getGuiHelper().createDrawable(Resources.Gui.Jei.TABS, 16, 16, 16, 16));
    }

    @Nonnull
    @Override
    public ResourceLocation getUid() {
        return JEIConfig.MOB;
    }

    @Nonnull
    @Override
    public Component getTitle() {
        return new TranslatableComponent("jer.mob.title");
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return Resources.Gui.Jei.MOB;
    }

    @Override
    public Class<? extends MobWrapper> getRecipeClass() {
        return MobWrapper.class;
    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull MobWrapper recipeWrapper, @Nonnull IIngredients ingredients) {
        int xOffset = 0;
        int slot = 0;
        for (int i = 0; i < Settings.ITEMS_PER_ROW; i++) {
            int yOffset = 0;
            for (int ii = 0; ii < Settings.ITEMS_PER_COLUMN; ii++) {
                recipeLayout.getItemStacks().init(slot++, false, X_FIRST_ITEM + xOffset, Y_FIRST_ITEM + yOffset);
                yOffset += 80 / Settings.ITEMS_PER_COLUMN;
            }
            xOffset += 72 / Settings.ITEMS_PER_ROW;
        }
        recipeLayout.getItemStacks().addTooltipCallback(recipeWrapper);
        int slotIndex = Math.min(recipeWrapper.getDrops().length, Settings.ITEMS_PER_ROW * Settings.ITEMS_PER_COLUMN);
        for (int i = 0; i < slotIndex; i++)
            recipeLayout.getItemStacks().set(i, recipeWrapper.getDrops()[i].getDrops());
        if (recipeWrapper.hasSpawnEgg()) {
            recipeLayout.getItemStacks().init(slotIndex, true, 150, 21);
            recipeLayout.getItemStacks().set(slotIndex, recipeWrapper.getSpawnEgg());
        }
    }
}
