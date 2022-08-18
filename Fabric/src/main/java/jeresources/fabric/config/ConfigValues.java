package jeresources.fabric.config;

import jeresources.config.Settings;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.gui.entries.BooleanListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import me.shedaniel.clothconfig2.gui.entries.IntegerListListEntry;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ConfigValues {

    public static IntegerListEntry itemsPerColumn;
    public static IntegerListEntry itemsPerRow;

    public static BooleanListEntry useDIYdata;

    public static StringListListEntry excludedEnchants, hiddenCategories;
    public static BooleanListEntry showDevData;
    public static BooleanListEntry disableLootManagerReloading;
    public static IntegerListListEntry excludedDimensions;
    public static void build(ConfigCategory category, ConfigEntryBuilder builder) {
        itemsPerColumn = builder.startIntField(Component.literal("itemsPerColumn"), Settings.ITEMS_PER_ROW)
            .setDefaultValue(4).setMin(1).setMax(4)
            .setSaveConsumer(newValue -> Settings.ITEMS_PER_ROW = newValue)
            .build();
        category.addEntry(itemsPerColumn);
        itemsPerRow = builder.startIntField(Component.literal("itemsPerRow"), Settings.ITEMS_PER_COLUMN)
            .setDefaultValue(4).setMin(1).setMax(4)
            .setSaveConsumer(newValue -> Settings.ITEMS_PER_COLUMN = newValue)
            .build();
        category.addEntry(itemsPerRow);
        useDIYdata = builder.startBooleanToggle(Component.literal("diyData"), Settings.useDIYdata)
            .setDefaultValue(true)
            .setSaveConsumer(newValue -> Settings.useDIYdata = newValue)
            .build();
        category.addEntry(useDIYdata);
        showDevData = builder.startBooleanToggle(Component.literal("showDevData"), Settings.showDevData)
            .setDefaultValue(false)
            .setSaveConsumer(newValue -> Settings.showDevData = newValue)
            .build();
        category.addEntry(showDevData);
        excludedEnchants = builder.startStrList(Component.literal("enchantsBlacklist"), List.of("flimflam", "soulBound"))
            .setSaveConsumer(newValue -> Settings.excludedEnchants = newValue.toArray(new String[0]))
            .build();
        category.addEntry(excludedEnchants);
        hiddenCategories = builder.startStrList(Component.literal("hiddenTabs"), List.of())
            .setSaveConsumer(newValue -> Settings.hiddenCategories = newValue.toArray(new String[0]))
            .build();
        category.addEntry(hiddenCategories);
        excludedDimensions = builder.startIntList(Component.literal("dimensionsBlacklist"), List.of(-11))
            .setSaveConsumer(newValue -> Settings.excludedDimensions = List.copyOf(newValue))
            .build();
        category.addEntry(excludedDimensions);
        disableLootManagerReloading = builder.startBooleanToggle(Component.literal("disableLootManagerReloading"), Settings.disableLootManagerReloading)
            .setDefaultValue(false)
            .setSaveConsumer(newValue -> Settings.disableLootManagerReloading = newValue)
            .build();
        category.addEntry(disableLootManagerReloading);
    }
}
