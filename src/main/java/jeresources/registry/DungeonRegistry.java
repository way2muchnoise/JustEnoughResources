package jeresources.registry;

import jeresources.entry.DungeonEntry;
import jeresources.util.TranslationHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DungeonRegistry
{
    private Map<String, DungeonEntry> registry;
    public static Map<String, String> categoryToLocalKeyMap = new LinkedHashMap<>();
    private static DungeonRegistry instance;

    public static DungeonRegistry getInstance()
    {
        if (instance == null)
            return instance = new DungeonRegistry();
        return instance;
    }

    public DungeonRegistry()
    {
        registry = new LinkedHashMap<>();
        addCategoryMapping("chests/abandoned_mineshaft", "jer.dungeon.abandonedMineshaftChest");
        addCategoryMapping("chests/desert_pyramid", "jer.dungeon.desertPyramidChest");
        addCategoryMapping("chests/jungle_temple", "jer.dungeon.pyramidJungleChest");
        addCategoryMapping("chests/igloo_chest", "jer.dungeon.iglooChest");
        addCategoryMapping("chests/stronghold_corridor", "jer.dungeon.strongholdCorridorChest");
        addCategoryMapping("chests/stronghold_library", "jer.dungeon.strongholdLibraryChest");
        addCategoryMapping("chests/stronghold_crossing", "jer.dungeon.strongholdCrossingChest");
        addCategoryMapping("chests/village_blacksmith", "jer.dungeon.villageBlacksmithChest");
        addCategoryMapping("chests/spawn_bonus_chest", "jer.dungeon.spawnBonusChest");
        addCategoryMapping("chests/simple_dungeon", "jer.dungeon.simpleDungeonChest");
        addCategoryMapping("chests/nether_bridge", "jer.dungeon.netherBridgeChest");
        addCategoryMapping("chests/end_city_treasure", "jer.dungeon.endCityTreasureChest");
    }

    public static boolean addCategoryMapping(String category, String name)
    {
        if (!categoryToLocalKeyMap.containsKey(category))
        {
            categoryToLocalKeyMap.put(category, name);
            return true;
        }
        return false;
    }

    public void registerDungeonEntry(DungeonEntry entry)
    {
        String name = entry.getName();
        if (registry.containsKey(name)) return;
        registry.put(name, entry);
    }

    public List<DungeonEntry> getDungeons()
    {
        return new ArrayList<>(registry.values());
    }

    public String getNumStacks(DungeonEntry entry)
    {
        int max = entry.getMaxStacks();
        int min = entry.getMinStacks();
        if (min == max) return String.format(TranslationHelper.translateToLocal("jer.stacks"), max);
        return String.format(TranslationHelper.translateToLocal("jer.stacks"), min + " - " + max);
    }
}
