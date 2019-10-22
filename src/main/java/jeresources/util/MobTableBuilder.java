package jeresources.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class MobTableBuilder {
    private final Map<ResourceLocation, LivingEntity> mobTables = new HashMap<>();
    private final World world;

    public MobTableBuilder(World world) {
        this.world = world;
    }

    public <T extends LivingEntity> void add(ResourceLocation resourceLocation, EntityType<T> entityType) {
        add(resourceLocation, entityType, null);
    }

    public <T extends LivingEntity> void add(ResourceLocation resourceLocation, EntityType<T> entityType, @Nullable EntityPropertySetter<T> entityPropertySetter) {
        T LivingEntity = entityType.create(world);
        if (LivingEntity != null) {
            if (entityPropertySetter != null) {
                entityPropertySetter.setProperties(LivingEntity);
            }
            mobTables.put(resourceLocation, LivingEntity);
        }
    }

    public Map<ResourceLocation, LivingEntity> getMobTables() {
        return mobTables;
    }

    public interface EntityPropertySetter<T extends LivingEntity> {
        void setProperties(T entity);
    }
}
