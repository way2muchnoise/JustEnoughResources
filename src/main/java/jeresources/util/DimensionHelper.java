package jeresources.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

public class DimensionHelper {

    public static String getWorldName(RegistryKey<World> worldRegistryKey) {
        return worldRegistryKey.func_240901_a_().getPath();
    }

    public static DimensionType getType(RegistryKey<DimensionType> dimensionTypeRegistryKey) {
        return DynamicRegistries.func_239770_b_().func_230520_a_().func_243576_d(dimensionTypeRegistryKey);
    }
}
