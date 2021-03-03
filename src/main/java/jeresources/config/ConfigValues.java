package jeresources.config;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ConfigValues {

    public static ForgeConfigSpec.IntValue itemsPerColumn;
    public static ForgeConfigSpec.IntValue itemsPerRow;
    public static ForgeConfigSpec.BooleanValue diyData;
    public static ForgeConfigSpec.BooleanValue showDevData;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> enchantsBlacklist;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> hiddenTabs;
    public static ForgeConfigSpec.ConfigValue<List<? extends Integer>> dimensionsBlacklist;
    public static ForgeConfigSpec.BooleanValue disableLootManagerReloading;

    public static ForgeConfigSpec build() {
        ForgeConfigSpec.Builder builder =  new ForgeConfigSpec.Builder();

        itemsPerColumn = builder.defineInRange("itemsPerColumn", 4, 1, 4);
        itemsPerRow = builder.defineInRange("itemsPerRow", 4, 1, 4);
        diyData = builder.worldRestart().define("diyData", true);
        showDevData = builder.worldRestart().define("showDevData", false);
        enchantsBlacklist = builder.worldRestart().defineList("enchantsBlacklist", Arrays.asList("flimflam", "soulBound"), new TypePredicate(String.class));
        hiddenTabs = builder.worldRestart().defineList("hiddenTabs", new ArrayList<>(), new TypePredicate(String.class));
        dimensionsBlacklist = builder.worldRestart().defineList("dimensionsBlacklist", Arrays.asList(-11), new TypePredicate(Integer.class));
        disableLootManagerReloading = builder.worldRestart().define("disableLootManagerReloading", false);

        return builder.build();
    }

    public static void pushChanges() {
        Settings.ITEMS_PER_COLUMN = itemsPerColumn.get();
        Settings.ITEMS_PER_ROW = itemsPerRow.get();
        Settings.useDIYdata = diyData.get();
        Settings.showDevData = showDevData.get();
        Settings.excludedEnchants = enchantsBlacklist.get().toArray(new String[0]);
        Settings.hiddenCategories = hiddenTabs.get().toArray(new String[0]);
        Settings.excludedDimensions = new ArrayList<>(dimensionsBlacklist.get());
        Settings.reload();
    }

    private static class TypePredicate implements Predicate<Object> {

        private Class type;

        public TypePredicate(Class type) {
            this.type = type;
        }

        @Override
        public boolean test(Object o) {
            return type.isInstance(o);
        }
    }
}
