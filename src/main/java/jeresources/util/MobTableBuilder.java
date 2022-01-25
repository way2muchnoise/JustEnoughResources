package jeresources.util;

import net.minecraft.data.loot.EntityLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public class MobTableBuilder {
    private final Map<ResourceLocation, Supplier<LivingEntity>> mobTables = new ConcurrentHashMap<>();
    private final MyEntityLoot entityLootHelper = new MyEntityLoot();
    private final Level level;

    public MobTableBuilder(Level level) {
        this.level = level;
    }

    public void add(ResourceLocation resourceLocation, EntityType<?> entityType) {
        if (entityLootHelper.isNonLiving(entityType)) {
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

    /** Helper class to allow public access to EntityLoot.isNonLiving */
    private static class MyEntityLoot extends EntityLoot {
        @Override
        public boolean isNonLiving(@NotNull EntityType<?> entitytype) {
            return super.isNonLiving(entitytype);
        }
    }
}
