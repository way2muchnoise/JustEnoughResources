package jeresources.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class MobTableBuilder {
    private final Map<ResourceLocation, LivingEntity> mobTables = new HashMap<>();
    private final Level level;

    public MobTableBuilder(Level level) {
        this.level = level;
    }

    public void add(ResourceLocation resourceLocation, EntityType<?> entityType) {
        Entity entity = entityType.create(level);
        if (entity instanceof LivingEntity) {
            mobTables.put(resourceLocation, (LivingEntity) entity);
        } else {
            if (entity != null) {
                entity.remove(Entity.RemovalReason.DISCARDED);
            }
        }
    }

    public void addSheep(ResourceLocation resourceLocation, EntityType<Sheep> entityType, DyeColor dye) {
        Sheep sheep = entityType.create(level);
        if (sheep != null) {
            sheep.setColor(dye);
            mobTables.put(resourceLocation, sheep);
        }
    }

    public Map<ResourceLocation, LivingEntity> getMobTables() {
        return mobTables;
    }
}
