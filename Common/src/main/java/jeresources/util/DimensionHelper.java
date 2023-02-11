package jeresources.util;

import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

public class DimensionHelper {

    public static String getDimensionName(ResourceKey<Level> worldRegistryKey) {
        return worldRegistryKey.location().getPath();
    }

    public static Holder<DimensionType> getType(ResourceKey<DimensionType> dimensionTypeRegistryKey) {
        return Minecraft.getInstance().level.registryAccess()
            .registryOrThrow(Registries.DIMENSION_TYPE)
            .getHolder(dimensionTypeRegistryKey)
            .orElseThrow();
    }
}
