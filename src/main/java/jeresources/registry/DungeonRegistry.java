package jeresources.registry;

import jeresources.entry.DungeonEntry;
import jeresources.util.TranslationHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DungeonRegistry {
    private Map<String, DungeonEntry> registry;
    public static Map<String, String> categoryToLocalKeyMap = new LinkedHashMap<>();
    private static DungeonRegistry instance;

    public static DungeonRegistry getInstance() {
        if (instance == null)
            return instance = new DungeonRegistry();
        return instance;
    }

    public DungeonRegistry() {
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
        addCategoryMapping("chests/woodland_mansion", "jer.dungeon.woodlandMansion");
        addCategoryMapping("chests/village/village_weaponsmith", "jer.dungeon.village_weaponsmith");
        addCategoryMapping("chests/village/village_toolsmith", "jer.dungeon.village_toolsmith");
        addCategoryMapping("chests/village/village_armorer", "jer.dungeon.village_armorer");
        addCategoryMapping("chests/village/village_cartographer", "jer.dungeon.village_cartographer");
        addCategoryMapping("chests/village/village_mason", "jer.dungeon.village_mason");
        addCategoryMapping("chests/village/village_shepherd", "jer.dungeon.village_shepherd");
        addCategoryMapping("chests/village/village_butcher", "jer.dungeon.village_butcher");
        addCategoryMapping("chests/village/village_fletcher", "jer.dungeon.village_fletcher");
        addCategoryMapping("chests/village/village_fisher", "jer.dungeon.village_fisher");
        addCategoryMapping("chests/village/village_tannery", "jer.dungeon.village_tannery");
        addCategoryMapping("chests/village/village_temple", "jer.dungeon.village_temple");
        addCategoryMapping("chests/village/village_desert_house", "jer.dungeon.village_desert_house");
        addCategoryMapping("chests/village/village_plains_house", "jer.dungeon.village_plains_house");
        addCategoryMapping("chests/village/village_taiga_house", "jer.dungeon.village_taiga_house");
        addCategoryMapping("chests/village/village_snowy_house", "jer.dungeon.village_snowy_house");
        addCategoryMapping("chests/village/village_savanna_house", "jer.dungeon.village_savanna_house");
        addCategoryMapping("chests/underwater_ruin_small", "jer.dungeon.underwater_ruin_small");
        addCategoryMapping("chests/underwater_ruin_big", "jer.dungeon.underwater_ruin_big");
        addCategoryMapping("chests/buried_treasure", "jer.dungeon.buried_treasure");
        addCategoryMapping("chests/shipwreck_map", "jer.dungeon.shipwreck_map");
        addCategoryMapping("chests/shipwreck_supply", "jer.dungeon.shipwreck_supply");
        addCategoryMapping("chests/shipwreck_treasure", "jer.dungeon.shipwreck_treasure");
        addCategoryMapping("chests/pillager_outpost", "jer.dungeon.pillager_outpost");
    }

    public static boolean addCategoryMapping(String category, String name) {
        if (!categoryToLocalKeyMap.containsKey(category)) {
            categoryToLocalKeyMap.put(category, name);
            return true;
        }
        return false;
    }

    public void registerDungeonEntry(DungeonEntry entry) {
        if (entry == null) return;
        String name = entry.getName();
        if (registry.containsKey(name)) return;
        registry.put(name, entry);
    }

    public List<DungeonEntry> getDungeons() {
        return new ArrayList<>(registry.values());
    }

    public String getNumStacks(DungeonEntry entry) {
        int max = entry.getMaxStacks();
        int min = entry.getMinStacks();
        if (min == max) return TranslationHelper.translateAndFormat("jer.stacks", max);
        return TranslationHelper.translateAndFormat("jer.stacks", min + " - " + max);
    }

    public void clear() {
        registry.clear();
    }
}
