package jeresources.jei.enchantment;

import jeresources.entry.EnchantmentEntry;
import jeresources.registry.EnchantmentRegistry;
import jeresources.util.Font;
import jeresources.util.TranslationHelper;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class EnchantmentWrapper implements IRecipeCategoryExtension {
    private static final int ENTRIES_PER_PAGE = 11;
    private static final int ENCHANT_X = 35;
    private static final int FIRST_ENCHANT_Y = 7;
    private static final int SPACING_Y = 10;
    private static final int PAGE_X = 55;
    private static final int PAGE_Y = 120;
    private static final int CYCLE_TIME = 2;

    protected final ItemStack itemStack;
    private final List<EnchantmentEntry> enchantments;
    private final int lastSet;
    private int set, nextCycle;

    public static EnchantmentWrapper create(@NotNull ItemStack itemStack) {
        List<EnchantmentEntry> enchantments = new LinkedList<>(EnchantmentRegistry.getInstance().getEnchantments(itemStack));
        if (enchantments.isEmpty())
            return null;
        return new EnchantmentWrapper(itemStack, enchantments);
    }

    private EnchantmentWrapper(@NotNull ItemStack itemStack, @NotNull List<EnchantmentEntry> enchantments) {
        this.itemStack = itemStack;
        this.enchantments = enchantments;
        this.set = 0;
        this.lastSet = this.enchantments.size() / (ENTRIES_PER_PAGE + 1);
        this.nextCycle = ((int) System.currentTimeMillis() / 1000) + CYCLE_TIME;
    }

    public List<EnchantmentEntry> getEnchantments() {
        doCycle();
        int last = set * ENTRIES_PER_PAGE + ENTRIES_PER_PAGE;
        if (last >= this.enchantments.size()) last = this.enchantments.size();
        return this.enchantments.subList(set * ENTRIES_PER_PAGE, last);
    }

    private void doCycle() {
        if (((int) System.currentTimeMillis() / 1000) > nextCycle) {
            if (!Screen.hasShiftDown()) // Don't cycle when holding shift
                this.set = this.set == lastSet ? 0 : this.set + 1;
            this.nextCycle = ((int) System.currentTimeMillis() / 1000) + CYCLE_TIME;
        }
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int y = FIRST_ENCHANT_Y;
        for (EnchantmentEntry enchantment : getEnchantments()) {
            Font.normal.print(guiGraphics, enchantment.getTranslatedWithLevels(), ENCHANT_X, y);
            y += SPACING_Y;
        }
        if (this.lastSet > 0) {
            String toPrint = TranslationHelper.getLocalPageInfo(this.set, this.lastSet);
            Font.normal.print(guiGraphics, toPrint, PAGE_X, PAGE_Y);
        }
    }
}
