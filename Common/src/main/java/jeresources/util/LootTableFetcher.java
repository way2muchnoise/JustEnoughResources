package jeresources.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.world.level.storage.loot.LootTable;

public class LootTableFetcher {
    private final ReloadableServerRegistries.Holder reloadableServerRegistries;

    public LootTableFetcher() {
        this.reloadableServerRegistries = null;
    }

    public LootTableFetcher(ReloadableServerRegistries.Holder reloadableServerRegistries) {
        this.reloadableServerRegistries = reloadableServerRegistries;
    }

    public LootTable getLootTable(ResourceKey<LootTable> lootTableKey) {
        if (reloadableServerRegistries == null) {
            return LootTable.EMPTY;
        }
        return reloadableServerRegistries.getLootTable(lootTableKey);
    }
}
