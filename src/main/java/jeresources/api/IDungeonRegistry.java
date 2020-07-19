package jeresources.api;

import net.minecraft.loot.LootTable;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public interface IDungeonRegistry {
    /**
     * Add a new category
     * <code>IDungeonRegistry.registerCategory("chest/chest_type", "localization.string");</code>
     *
     * @param category     the category name
     * @param localization the translation key
     */
    void registerCategory(@Nonnull String category, @Nonnull String localization);

    /**
     * @param category      can be a localization string directly or a registered category
     * @param tableLocation the {@link ResourceLocation} of the loot table
     */
    void registerChest(@Nonnull String category, @Nonnull ResourceLocation tableLocation);

    /**
     * @param category  can be a localization string directly or a registered category
     * @param lootTable the actual {@link LootTable}
     */
    void registerChest(@Nonnull String category, @Nonnull LootTable lootTable);
}
