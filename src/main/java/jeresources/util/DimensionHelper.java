package jeresources.util;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionHelper {

    public static String getDimensionName(ResourceKey<Level> worldRegistryKey) {
        return worldRegistryKey.location().getPath();
    }

    public static DimensionType getType(ResourceKey<DimensionType> dimensionTypeRegistryKey) {
        return RegistryAccess.m_123086_()
            .registry(Registry.DIMENSION_TYPE_REGISTRY).get()
            .get(dimensionTypeRegistryKey);
    }
}
