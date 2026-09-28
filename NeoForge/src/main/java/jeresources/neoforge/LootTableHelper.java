package jeresources.neoforge;

import jeresources.platform.ILootTableHelper;
import net.minecraft.core.Holder;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;


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

    @Override
    public Holder<ContextIntProvider> getRolls(LootPool pool) {
        return pool.getRolls();
    }

    @Override
    public Holder<ContextFloatProvider> getBonusRolls(LootPool pool) {
        return pool.getBonusRolls();
    }
}
