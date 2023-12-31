package jeresources.forge;

import jeresources.platform.ILootTableHelper;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LootTableHelper implements ILootTableHelper {

    private static LootTableHelper instance;

    public static ILootTableHelper instance() {
        if (instance == null) {
            instance = new LootTableHelper();
        }
        return instance;
    }

    private LootTableHelper() {

    }

    public List<LootPool> getPools(LootTable table) {
        // public net.minecraft.world.level.storage.loot.LootTable f_79109_ # pools
        return ObfuscationReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
    }

    public List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        // public net.minecraft.world.level.storage.loot.LootPool f_79023_ # entries
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "f_79023_");
    }

    public List<LootItemCondition> getLootConditions(LootPool pool) {
        // public net.minecraft.world.level.storage.loot.LootPool f_79024_ # conditions
        return ObfuscationReflectionHelper.getPrivateValue(LootPool.class, pool, "f_79024_");
    }

    @Override
    public NumberProvider getRolls(LootPool pool) {
        return pool.getRolls();
    }

    @Override
    public NumberProvider getBonusRolls(LootPool pool) {
        return pool.getBonusRolls();
    }
}
