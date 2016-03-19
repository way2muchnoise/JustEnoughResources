package jeresources.utils;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraft.world.storage.loot.LootTableManager;

import java.util.ArrayList;
import java.util.List;

public class LootHelper
{
    public static List<LootTable> getAllChestLootTables(LootTableManager manager)
    {
        return getAllChestLootTables(manager, false);
    }

    public static List<LootTable> getAllChestLootTables(LootTableManager manager, boolean withBonusChest)
    {
        ArrayList<LootTable> chestTables = new ArrayList<>();

        if (withBonusChest)
            chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_SPAWN_BONUS_CHEST));

        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_END_CITY_TREASURE));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_SIMPLE_DUNGEON));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_VILLAGE_BLACKSMITH));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_ABANDONED_MINESHAFT));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_NETHER_BRIDGE));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_STRONGHOLD_LIBRARY));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_STRONGHOLD_CROSSING));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_STRONGHOLD_CORRIDOR));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_DESERT_PYRAMID));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_JUNGLE_TEMPLE));
        chestTables.add(manager.getLootTableFromLocation(LootTableList.CHESTS_IGLOO_CHEST));

        return chestTables;
    }

    public static List<ResourceLocation> getAllChestLootTablesResourceLocations()
    {
        ArrayList<ResourceLocation> chestTables = new ArrayList<>();

        chestTables.add(LootTableList.CHESTS_END_CITY_TREASURE);
        chestTables.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
        chestTables.add(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
        chestTables.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
        chestTables.add(LootTableList.CHESTS_NETHER_BRIDGE);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_CROSSING);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
        chestTables.add(LootTableList.CHESTS_DESERT_PYRAMID);
        chestTables.add(LootTableList.CHESTS_JUNGLE_TEMPLE);
        chestTables.add(LootTableList.CHESTS_IGLOO_CHEST);

        return chestTables;
    }
}
