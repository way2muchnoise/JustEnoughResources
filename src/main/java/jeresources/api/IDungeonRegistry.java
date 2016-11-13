package jeresources.api;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

public interface IDungeonRegistry {
    /**
     * Add a new category
     * <code>IDungeonRegistry.registerCategory("chest/chest_type", "localization.string");</code>
     *
     * @param category the category name
     * @param localization the translation key
     */
    void registerCategory(String category, String localization);

    /**
     * @param category can be a localization string directly or a registered category
     * @param tableLocation the {@link ResourceLocation} of the loot table
     */
    void registerChest(String category, ResourceLocation tableLocation);

    /**
     * @param category can be a localization string directly or a registered category
     * @param lootTable the actual {@link LootTable}
     */
    void registerChest(String category, LootTable lootTable);
}
