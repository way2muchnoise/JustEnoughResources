package jeresources.util;

import jeresources.compatibility.CompatBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static net.minecraft.data.loot.EntityLootSubProvider.SPECIAL_LOOT_TABLE_TYPES;


public class MobTableBuilder {
    private final Map<ResourceLocation, Supplier<LivingEntity>> mobTables = new HashMap<>();
    /**
     * level should be a client level.
     * Passing in a ServerLevel can allow modded mobs to load all kinds of things,
     * like in the `VillagerTrades.TreasureMapForEmeralds` which loads chunks!
     */
    private final Level level;

    public MobTableBuilder() {
        this.level = CompatBase.getLevel();
    }

    public void add(ResourceLocation resourceLocation, EntityType<?> entityType) {
        if (isNonLiving(entityType) || !entityType.isEnabled(level.enabledFeatures())) {
            return;
        }
        mobTables.put(resourceLocation, () -> (LivingEntity) entityType.create(level));
    }

    public void addSheep(ResourceLocation resourceLocation, EntityType<Sheep> entityType, DyeColor dye) {
        mobTables.put(resourceLocation, () -> {
            Sheep sheep = entityType.create(level);
            assert sheep != null;
            sheep.setColor(dye);
            return sheep;
        });
    }

    public Map<ResourceLocation, Supplier<LivingEntity>> getMobTables() {
        return mobTables;
    }

    private static boolean isNonLiving(@Nonnull EntityType<?> entityType) {
        return !SPECIAL_LOOT_TABLE_TYPES.contains(entityType) && entityType.getCategory() == MobCategory.MISC;
    }
}
