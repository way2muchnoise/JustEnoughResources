package jeresources.compatibility;

import jeresources.api.IDungeonRegistry;
import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.LogHelper;
import jeresources.util.LootTableHelper;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTableManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DungeonRegistryImpl implements IDungeonRegistry {
    private static List<Tuple<String, String>> categoryMapping = new LinkedList<>();
    private static Map<String, ResourceLocation> rawRegisters = new HashMap<>();
    private static List<DungeonEntry> preppedRegisters = new LinkedList<>();

    protected DungeonRegistryImpl() {

    }

    @Override
    public void registerCategory(@Nonnull String category, @Nonnull String localization) {
        categoryMapping.add(new Tuple<>(category, localization));
    }

    @Override
    public void registerChest(@Nonnull String category, @Nonnull ResourceLocation tableLocation) {
        rawRegisters.put(category, tableLocation);
    }

    @Override
    public void registerChest(@Nonnull String category, @Nonnull LootTable lootTable) {
        try {
            preppedRegisters.add(new DungeonEntry(category, lootTable));
        } catch (Exception e) {
            LogHelper.debug("Bad dungeon chest registry for category %s", category);
        }
    }

    protected static void commit() {
        categoryMapping.forEach(t -> DungeonRegistry.addCategoryMapping(t.getA(), t.getB()));
        preppedRegisters.forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
        World world = CompatBase.getWorld();
        LootTableManager manager = LootTableHelper.getManager(world);
        rawRegisters.entrySet().stream()
            .map(entry -> {
                try {
                    return new DungeonEntry(entry.getKey(), manager.get(entry.getValue()));
                } catch (Exception e) {
                    LogHelper.debug("Bad dungeon chest registry for category %s", entry.getKey());
                    return null;
                }
            }).forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
    }
}
