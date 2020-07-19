package jeresources.util;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;

public class DimensionHelper {

    public static String getWorldName(RegistryKey<World> worldRegistryKey) {
        return worldRegistryKey.func_240901_a_().getPath();
    }
}
