package jeresources.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class DimensionHelper {

    public static String getDimensionName(RegistryKey<World> worldRegistryKey) {
        return worldRegistryKey.location().getPath();
    }

    public static DimensionType getType(RegistryKey<DimensionType> dimensionTypeRegistryKey) {
        return DynamicRegistries.builtin().dimensionTypes().get(dimensionTypeRegistryKey);
    }
}
