package jeresources.compatibility;

import jeresources.api.IDungeonRegistry;
import jeresources.entry.DungeonEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.util.LootTableHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableManager;

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
    public void registerCategory(String category, String localization) {
        categoryMapping.add(new Tuple<>(category, localization));
    }

    @Override
    public void registerChest(String category, ResourceLocation tableLocation) {
        rawRegisters.put(category, tableLocation);
    }

    @Override
    public void registerChest(String category, LootTable lootTable) {
        preppedRegisters.add(new DungeonEntry(category, lootTable));
    }

    protected static void commit() {
        categoryMapping.forEach(t -> DungeonRegistry.addCategoryMapping(t.getFirst(), t.getSecond()));
        preppedRegisters.forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
        World world = CompatBase.getWorld();
        LootTableManager manager = LootTableHelper.getManager(world);
        LootTableHelper.getAllChestLootTablesResourceLocations().stream()
            .map(resourceLocation -> new DungeonEntry(resourceLocation.getResourcePath(), manager.getLootTableFromLocation(resourceLocation)))
            .forEach(entry -> DungeonRegistry.getInstance().registerDungeonEntry(entry));
    }
}
