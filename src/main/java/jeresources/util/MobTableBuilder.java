package jeresources.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

public class MobTableBuilder {
    private final Map<ResourceLocation, EntityLivingBase> mobTables = new HashMap<>();
    private final World world;

    public MobTableBuilder(World world) {
        this.world = world;
    }

    public <T extends EntityLivingBase> void add(ResourceLocation resourceLocation, Class<T> entityClass) {
        add(resourceLocation, entityClass, null);
    }

    public <T extends EntityLivingBase> void add(ResourceLocation resourceLocation, Class<T> entityClass, @Nullable EntityPropertySetter<T> entityPropertySetter) {
        T entityLivingBase = construct(world, entityClass);
        if (entityLivingBase != null) {
            if (entityPropertySetter != null) {
                entityPropertySetter.setProperties(entityLivingBase);
            }
            mobTables.put(resourceLocation, entityLivingBase);
        }
    }

    @Nullable
    private static <T extends EntityLivingBase> T construct(World world, Class<T> entityClass) {
        Constructor<T> constructor = getConstructor(entityClass);
        if (constructor != null) {
            try {
                return constructor.newInstance(world);
            } catch (ReflectiveOperationException | RuntimeException e) {
                LogHelper.warn("Could not create entity " + entityClass, e);
                return null;
            }
        }
        return null;
    }

    @Nullable
    private static <T extends EntityLivingBase> Constructor<T> getConstructor(Class<T> entityClass) {
        try {
            return entityClass.getConstructor(World.class);
        } catch (NoSuchMethodException e) {
            LogHelper.warn("Could not find constructor for entity " + entityClass);
            return null;
        }
    }

    public Map<ResourceLocation, EntityLivingBase> getMobTables() {
        return mobTables;
    }

    public interface EntityPropertySetter<T extends EntityLivingBase> {
        void setProperties(T entity);
    }
}
