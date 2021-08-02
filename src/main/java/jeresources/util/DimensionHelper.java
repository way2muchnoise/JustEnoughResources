package jeresources.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class DimensionHelper {

    public static String getWorldName(RegistryKey<World> worldRegistryKey) {
        return worldRegistryKey.getRegistryName().getPath();
    }

    public static DimensionType getType(RegistryKey<DimensionType> dimensionTypeRegistryKey) {
        return DynamicRegistries.builtin().dimensionTypes().get(dimensionTypeRegistryKey);
    }
}
