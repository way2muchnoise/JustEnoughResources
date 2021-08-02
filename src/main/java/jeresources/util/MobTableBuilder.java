package jeresources.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class MobTableBuilder {
    private final Map<ResourceLocation, LivingEntity> mobTables = new HashMap<>();
    private final World world;

    public MobTableBuilder(World world) {
        this.world = world;
    }

    public void add(ResourceLocation resourceLocation, EntityType<?> entityType) {
        Entity entity = entityType.create(world);
        if (entity instanceof LivingEntity) {
            mobTables.put(resourceLocation, (LivingEntity) entity);
        } else {
            if (entity != null) {
                entity.remove();
            }
        }
    }

    public void addSheep(ResourceLocation resourceLocation, EntityType<SheepEntity> entityType, DyeColor dye) {
        SheepEntity entity = entityType.create(world);
        if (entity != null) {
            entity.setColor(dye);
            mobTables.put(resourceLocation, entity);
        }
    }

    public Map<ResourceLocation, LivingEntity> getMobTables() {
        return mobTables;
    }
}
