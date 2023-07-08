package jeresources.jei.worldgen;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import jeresources.config.Settings;
import jeresources.entry.WorldGenEntry;
import jeresources.util.Font;
import jeresources.util.RenderHelper;
import jeresources.util.TranslationHelper;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.recipe.category.extensions.IRecipeCategoryExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldGenWrapper implements IRecipeCategoryExtension, IRecipeSlotTooltipCallback {
    protected static final int X_OFFSET = 49 - 20;
    protected static final int Y_OFFSET = 52;
    protected static final int X_AXIS_SIZE = 100 + 20 + 8;
    protected static final int Y_AXIS_SIZE = 40;
    protected static final String ORE_SLOT_NAME = "oreSlot";

    private final WorldGenEntry worldGenEntry;

    public WorldGenWrapper(WorldGenEntry worldGenEntry) {
        this.worldGenEntry = worldGenEntry;
    }

    public int getLineColor() {
        return this.worldGenEntry.getColour();
    }

    public List<ItemStack> getBlocks() {
        List<ItemStack> blocks = new LinkedList<>();
        blocks.add(this.worldGenEntry.getBlock());
        if (this.worldGenEntry.hasDeepSlateVariant()) {
            blocks.add(this.worldGenEntry.getDeepSlateBlock());
        }
        return blocks;
    }

    public List<ItemStack> getDrops() {
        return this.worldGenEntry.getDrops();
    }

    @Override
    public void drawInfo(int recipeWidth, int recipeHeight, @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        float[] array = this.worldGenEntry.getChances();
        double max = 0;
        for (double d : array)
            if (d > max) max = d;
        double xPrev = X_OFFSET;
        double yPrev = Y_OFFSET;
        double space = X_AXIS_SIZE / ((array.length - 1) * 1D);
        for (int i = 0; i < array.length; i++) {
            double value = array[i];
            double y = Y_OFFSET - ((value / max) * Y_AXIS_SIZE);
            if (i > 0) // Only draw a line after the first element (cannot draw line with only one point)
            {
                double x = xPrev + space;
                RenderHelper.drawLine(guiGraphics, (int)xPrev, (int)yPrev, (int)x, (int)y, getLineColor());
                xPrev = x;
            }
            yPrev = y;
        }

        final int xPercents = X_OFFSET - 2;
        final int yPercents = Y_OFFSET - 4;

        final String minPercent = "0%";
        final int minPercentWidth = Font.small.getStringWidth(minPercent);
        Font.small.print(guiGraphics, minPercent, xPercents - minPercentWidth, yPercents);
        final String maxPercent = String.format("%.2f", max * 100) + "%";
        final int maxPercentWidth = Font.small.getStringWidth(maxPercent);
        Font.small.print(guiGraphics, maxPercent, xPercents - maxPercentWidth, yPercents - Y_AXIS_SIZE);

        final int yLabels = Y_OFFSET + 2;
        final int xLabels = X_OFFSET;

        final int minLabel = this.worldGenEntry.getMinY();
        final int minLabelWidth = Font.small.getStringWidth(String.valueOf(minLabel));
        final int minLabelOffset = xLabels - (minLabelWidth / 2);
        Font.small.print(guiGraphics, minLabel, minLabelOffset, yLabels);

        final int maxLabel = this.worldGenEntry.getMaxY();
        final int maxLabelWidth = Font.small.getStringWidth(String.valueOf(maxLabel));
        final int maxLabelOffset = xLabels + X_AXIS_SIZE - (maxLabelWidth / 2);
        Font.small.print(guiGraphics, maxLabel, maxLabelOffset, yLabels);

        final int midLabel = (maxLabel + minLabel) / 2;
        final int midLabelWidth = Font.small.getStringWidth(String.valueOf(midLabel));
        final int midLabelOffset = xLabels + (X_AXIS_SIZE / 2) - (midLabelWidth / 2);
        Font.small.print(guiGraphics, midLabel, midLabelOffset, yLabels);

        Font.small.print(guiGraphics, TranslationHelper.translateAndFormat("jer.worldgen.drops"), WorldGenCategory.X_DROP_ITEM, WorldGenCategory.Y_DROP_ITEM - 8);

        String dimension = TranslationHelper.tryDimensionTranslate(worldGenEntry.getDimension());
        int x = (recipeWidth - Font.normal.getStringWidth(dimension)) / 2;
        Font.normal.print(guiGraphics, dimension, x, 0);
    }

    @Override
    public List<Component> getTooltipStrings(double mouseX, double mouseY) {
        List<Component> tooltip = new LinkedList<>();
        if (onGraph(mouseX, mouseY))
            tooltip = getLineTooltip(mouseX, tooltip);
        return tooltip;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        tooltip.addAll(getItemStackTooltip(recipeSlotView.getSlotName().orElse(null), (ItemStack) recipeSlotView.getDisplayedIngredient().get().getIngredient()));
    }

    private List<Component> getItemStackTooltip(String slotName, ItemStack itemStack) {
        List<String> tooltip = new LinkedList<>();
        if (itemStack != null && slotName != null && slotName.equals(ORE_SLOT_NAME)) {
            if (this.worldGenEntry.isSilkTouchNeeded())
                tooltip.add(Conditional.silkTouch.toString());

            List<String> biomes = this.worldGenEntry.getBiomeRestrictions();
            if (biomes.size() > 0) {
                tooltip.add(TranslationHelper.translateAndFormat("jer.worldgen.biomes") + ":");
                tooltip.addAll(biomes);
            }

            if (Settings.showDevData) {
                tooltip.add(TranslationHelper.translateAndFormat("jer.worldgen.averageChunk") + ":");
                tooltip.add("" + this.worldGenEntry.getAverageBlockCountPerChunk());
            }

        } else {
            tooltip.add(TranslationHelper.translateAndFormat("jer.worldgen.average"));
            for (LootDrop dropItem : this.worldGenEntry.getLootDrops(itemStack)) {
                String line = " - ";

                if (dropItem.fortuneLevel > 0) {
                    line += Enchantments.BLOCK_FORTUNE.getFullname(dropItem.fortuneLevel).getString(); // Avoid crash by using Minecraft ObjectHolder
                } else {
                    line += TranslationHelper.translateAndFormat("jer.worldgen.base");
                }

                if (dropItem.chance < 1f) {
                    line += " " + TranslationHelper.translateAndFormat("jer.worldgen.chance", dropItem.formatChance() + "%");
                }

                if (dropItem.minDrop == dropItem.maxDrop) {
                    line += ": " + dropItem.minDrop;
                } else {
                    line += ": " + dropItem.minDrop + " - " + dropItem.maxDrop;
                }

                if (dropItem.isAffectedBy(Conditional.affectedByFortune)) {
                    line += " " + TranslationHelper.translateAndFormat("jer.worldgen.affectedByFortune");
                }

                tooltip.add(line);
            }
        }
        return tooltip.stream().map(Component::literal).collect(Collectors.toList());
    }

    private List<Component> getLineTooltip(double mouseX, List<Component> tooltip) {
        final double exactMouseX = getExactMouseX(mouseX);
        final float[] chances = this.worldGenEntry.getChances();
        final double space = X_AXIS_SIZE / (chances.length * 1D);
        // Calculate the hovered over y value
        final int index = (int) ((exactMouseX - X_OFFSET + 1) / space);
        final int yValue = index + this.worldGenEntry.getMinY();
        if (index >= 0 && index < chances.length) {
            float chance = chances[index] * 100;
            String percent = chance > 0.01f || chance == 0 ? String.format(" (%.2f%%)", chance) : " <0.01%";
            tooltip.add(Component.literal("Y: " + yValue + percent));
        }

        return tooltip;
    }

    private static double getExactMouseX(final double mouseX) {
        Minecraft mc = Minecraft.getInstance();
        final int scaledWidth = mc.getWindow().getGuiScaledWidth();
        final double mouseXExact = mc.mouseHandler.xpos() * scaledWidth / (double) mc.getWindow().getWidth();
        final double mouseXFraction = mouseXExact - Math.floor(mouseXExact);
        return mouseX + mouseXFraction;
    }

    private boolean onGraph(double mouseX, double mouseY) {
        return mouseX >= X_OFFSET - 1
            && mouseX < X_OFFSET + X_AXIS_SIZE
            && mouseY >= Y_OFFSET - Y_AXIS_SIZE - 1
            && mouseY < Y_OFFSET;
    }

}
