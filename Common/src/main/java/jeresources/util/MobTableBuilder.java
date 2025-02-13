package jeresources.util;

import jeresources.compatibility.CompatBase;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.data.loot.EntityLootSubProvider.SPECIAL_LOOT_TABLE_TYPES;


public class MobTableBuilder {
    private final Map<ResourceKey<LootTable>, Supplier<LivingEntity>> mobTables = new HashMap<>();

    public void add(ResourceKey<LootTable> resourceLocation, EntityType<?> entityType) {
        if (isNonLiving(entityType) || !entityType.isEnabled(CompatBase.getLevel().enabledFeatures())) {
            return;
        }
        mobTables.put(resourceLocation, () -> (LivingEntity) entityType.create(CompatBase.getLevel()));
    }

    public void addSheep(ResourceKey<LootTable> resourceLocation, EntityType<Sheep> entityType, DyeColor dye) {
        mobTables.put(resourceLocation, () -> {
            Sheep sheep = entityType.create(CompatBase.getLevel());
            assert sheep != null;
            sheep.setColor(dye);
            return sheep;
        });
    }

    public Map<ResourceKey<LootTable>, Supplier<LivingEntity>> getMobTables() {
        return mobTables;
    }

    private static boolean isNonLiving(@NotNull EntityType<?> entityType) {
        return !SPECIAL_LOOT_TABLE_TYPES.contains(entityType) && entityType.getCategory() == MobCategory.MISC;
    }
}
