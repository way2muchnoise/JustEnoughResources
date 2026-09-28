package jeresources.platform;

import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;

import java.util.List;

public interface ILootTableHelper {
    default List<LootPool> getPools(LootTable table) {
        return table.pools;
    }

    default List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        return pool.entries;
    }

    default List<LootItemCondition> getLootConditions(LootPool pool) {
        return pool.condition.map(lootItemConditionHolder -> List.of(lootItemConditionHolder.value())).orElseGet(List::of);
    }

    default Holder<ContextIntProvider> getRolls(LootPool pool) {
        return pool.rolls;
    }

    default Holder<ContextFloatProvider> getBonusRolls(LootPool pool) {
        return pool.bonusRolls;
    }
}
