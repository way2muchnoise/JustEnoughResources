package jeresources.platform;

import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

import java.util.Arrays;
import java.util.List;

public interface ILootTableHelper {
    default List<LootPool> getPools(LootTable table) {
        return table.pools;
    }

    default List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        return pool.entries;
    }

    default List<LootItemCondition> getLootConditions(LootPool pool) {
        return pool.conditions;
    }

    default NumberProvider getRolls(LootPool pool) {
        return pool.rolls;
    }

    default NumberProvider getBonusRolls(LootPool pool) {
        return pool.bonusRolls;
    }
}
