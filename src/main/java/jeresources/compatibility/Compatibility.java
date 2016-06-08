package jeresources.compatibility;

import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.config.Settings;
import jeresources.json.WorldGenAdapter;

public class Compatibility
{
    public static void init()
    {
        boolean initWorldGen = true;
        if (Settings.useDIYdata)
        {
            if (WorldGenAdapter.hasWorldGenDIYData())
            {
                WorldGenAdapter.readDIYData();
                initWorldGen = false;
            }
        }
        new MinecraftCompat().init(initWorldGen);
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
    }
}
