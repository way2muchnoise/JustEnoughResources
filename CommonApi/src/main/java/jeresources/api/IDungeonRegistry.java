package jeresources.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

public interface IDungeonRegistry {
    /**
     * Add a new category
     * <code>IDungeonRegistry.registerCategory("chest/chest_type", "localization.string");</code>
     *
     * @param category     the category name
     * @param localization the translation key
     */
    void registerCategory(@NotNull String category, @NotNull String localization);

    /**
     * @param category      can be a localization string directly or a registered category
     * @param tableLocation the {@link ResourceLocation} of the loot table
     */
    void registerChest(@NotNull String category, @NotNull ResourceLocation tableLocation);

    /**
     * @param category  can be a localization string directly or a registered category
     * @param lootTable the actual {@link LootTable}
     */
    void registerChest(@NotNull String category, @NotNull LootTable lootTable);
}
